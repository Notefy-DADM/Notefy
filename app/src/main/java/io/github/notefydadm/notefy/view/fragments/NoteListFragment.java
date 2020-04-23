package io.github.notefydadm.notefy.view.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.adapter.NoteListAdapter;
import io.github.notefydadm.notefy.databinding.FragmentNoteListBinding;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.view.SetNoteNameDialog;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class NoteListFragment extends Fragment {
    private FragmentNoteListBinding binding;
    private NoteViewModel noteViewModel;
    private ChangeToTextEditor listener;

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
                askNoteTittle();
            }
        });

        // Get view model for notes
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        // When a note is selected, open it
        noteViewModel.getSelectedNote().observe(getViewLifecycleOwner(), new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                if (note != null) {
                    //  Check orientation
                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        // landscape
                        System.out.println("Selected note landscape: " + note+ " Content: "+note.getContent());
                        // TODO: Open text editor on landscape mode
                    }
                    else{
                        // portrait
                        System.out.println("Selected note portrait: " + note+ " Content: "+note.getContent());
                        listener.changeToTextEditor();
                    }
                }
            }
        });

        //  You can't cast a Fragment to a Context.
        binding.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));

        //RecyclerView.ItemDecoration separator = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        //myRecyclerView.addItemDecoration(separator);

        // We need to have an Adapter for the RecyclerView
        binding.setAdapter(new NoteListAdapter(requireActivity(), getContext()));

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface ChangeToTextEditor{
        void changeToTextEditor();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChangeToTextEditor) context;
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
    }

}