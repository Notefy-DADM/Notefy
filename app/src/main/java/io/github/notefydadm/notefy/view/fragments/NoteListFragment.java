package io.github.notefydadm.notefy.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.adapter.NoteListAdapter;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.databinding.FragmentNoteListBinding;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.User;
import io.github.notefydadm.notefy.view.dialogs.ChangeTitleDialog;
import io.github.notefydadm.notefy.view.dialogs.DeleteDialog;
import io.github.notefydadm.notefy.view.dialogs.LoadingDialog;
import io.github.notefydadm.notefy.view.dialogs.ShareDialog;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class NoteListFragment extends Fragment{
    private FragmentNoteListBinding binding;
    private NoteViewModel noteViewModel;

    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Deselect any previously selected note
        if (noteViewModel != null) noteViewModel.setSelectedNote(null);

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_list, container, false);
        binding.setAddNewNote(new Runnable() {
            @Override
            public void run() {
                openChangeTitleDialog(new Note());
            }
        });

        // Get view model for notes
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        // Load notes from database
        noteViewModel.loadNotes();

        // Add note mode to binding, and update it when it changes
        LiveData<NoteViewModel.NoteMode> noteModeLiveData = noteViewModel.getNoteMode();
        binding.setNoteMode(noteModeLiveData.getValue());
        noteModeLiveData.observe(this, new Observer<NoteViewModel.NoteMode>() {
            @Override
            public void onChanged(NoteViewModel.NoteMode noteMode) {
                binding.setNoteMode(noteMode);
            }
        });

        //  You can't cast a Fragment to a Context.
        binding.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));

        //RecyclerView.ItemDecoration separator = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        //myRecyclerView.addItemDecoration(separator);

        // We need to have an Adapter for the RecyclerView
        binding.setAdapter(new NoteListAdapter(requireActivity(), getContext(), new NoteListAdapter.AdapterNoteListCallbacks() {
            @Override
            public void changeNameNote(Note note) {
                openChangeTitleDialog(note);
            }

            @Override
            public void shareNote(Note note) {
                openShareDialog(note);
            }

            @Override
            public void deleteNote(Note note) {
                openDeleteDialog(note);
            }
        }));

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    @BindingAdapter("adapter")
    public static void setAdapter(RecyclerView recyclerView, final RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    //  SHARE NOTE DIALOG
    public void openShareDialog(final Note note){
        ShareDialog shareDialog;
        ShareDialog.ShareDialogCallback listener = new ShareDialog.ShareDialogCallback() {
            @Override
            public void onShareDialogPositiveClick(List<User> users) {
                    if (note != null) {
                        DatabaseHandler.updateSharedListNote(note, users , new DatabaseHandler.UpdateSharedListNoteCallback() {
                            @Override
                            public void onSuccessfulShared() {
                                Toast.makeText(getContext(), R.string.SSuccessful, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailureShared() {
                                Toast.makeText(getContext(), R.string.SFailure, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

            }

            @Override
            public void onShareDialogNeutralClick() {

            }
        };

        shareDialog = new ShareDialog(note,listener);
        shareDialog.show(NoteListFragment.this.getChildFragmentManager(), "shareDialog");
    }

    //  DELETE NOTE DIALOG
    public void openDeleteDialog(final Note note){
        DeleteDialog deleteDialog;
        DeleteDialog.DeleteDialogListener listener = new DeleteDialog.DeleteDialogListener() {
            @Override
            public void onClickDelete() {
                final LoadingDialog loadingDialog = new LoadingDialog();
                loadingDialog.show(requireFragmentManager(), null);
                DatabaseHandler.removeNote(note, new DatabaseHandler.RemoveNoteCallback() {
                    @Override
                    public void onSuccessfulRemoved() {
                        loadingDialog.dismiss();
                        Toast.makeText(getContext(),R.string.DSuccessful,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailureRemoved() {
                        loadingDialog.dismiss();
                        Toast.makeText(getContext(),R.string.DFailure,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void noteNotHaveId() {
                        loadingDialog.dismiss();
                        Toast.makeText(getContext(),R.string.DFailure,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onClickCancel() {

            }
        };
        deleteDialog = new DeleteDialog(listener, binding.getAdapter());
        deleteDialog.show(NoteListFragment.this.getChildFragmentManager(), "deleteDialog");
    }

    //  CHANGE NOTE TITLE DIALOG
    public void openChangeTitleDialog(@NonNull final Note note) {
        ChangeTitleDialog changeTitleDialog;
        ChangeTitleDialog.ChangeTitleDialogCallback listener = new ChangeTitleDialog.ChangeTitleDialogCallback() {
            @Override
            public void onChangeTitleClick(String title) {
                if (note.getNoteId() == null || note.getNoteId().isEmpty()) {
                    note.setTitle(title);
                    note.setUserID(FirebaseAuth.getInstance().getUid());
                    noteViewModel.postSelectedNote(note);
                } else {
                    final LoadingDialog loadingDialog = new LoadingDialog();
                    loadingDialog.show(requireFragmentManager(), null);
                    DatabaseHandler.AddNoteToUserCallback callback = new DatabaseHandler.AddNoteToUserCallback() {
                        @Override
                        public void onSuccessfulAdded() {
                            loadingDialog.dismiss();
                            Toast.makeText(getActivity(),"Saved",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailureAdded() {
                            loadingDialog.dismiss();
                            Toast.makeText(getActivity(),"Error saving",Toast.LENGTH_LONG).show();
                        }
                    };

                    note.setTitle(title);
                    DatabaseHandler.addNoteToUser(note,callback);
                }
            }

            @Override
            public void onCancelClick() {

            }
        };
        changeTitleDialog = new ChangeTitleDialog(note,listener);
        changeTitleDialog.show(NoteListFragment.this.getChildFragmentManager(),"changeTitleDialog");
    }

    @Override
    public void onPause() {
        System.out.println("Pause");
        //saved.putParcelableArray("notes", (Parcelable[]) noteViewModel.getNotes().getValue().toArray());
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        //saved.get("notes");
        //noteViewModel.loadNotes();
        System.out.println("Resume");
    }
/*
    private void askNoteTittle(){
        SetNoteNameDialog.SetNoteNameCallback callback = new SetNoteNameDialog.SetNoteNameCallback() {
            @Override
            public void onClickSet(String name) {
                noteViewModel.setSelectedNote(new Note(name, FirebaseAuth.getInstance().getUid()));
            }

            @Override
            public void onClickCancel() {

            }
        };
        final SetNoteNameDialog loadingDialog = new SetNoteNameDialog(callback,"");
        loadingDialog.show(requireFragmentManager(), null);
    }*/



}