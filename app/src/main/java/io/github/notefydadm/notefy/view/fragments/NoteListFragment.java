package io.github.notefydadm.notefy.view.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.adapter.NoteListAdapter;
import io.github.notefydadm.notefy.databinding.FragmentNoteListBinding;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.view.dialogs.ChangeTitleDialog;
import io.github.notefydadm.notefy.view.dialogs.DeleteDialog;
import io.github.notefydadm.notefy.view.dialogs.ShareDialog;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class NoteListFragment extends Fragment implements ShareDialog.ShareDialogListener, DeleteDialog.DeleteDialogListener, ChangeTitleDialog.ChangeTitleDialogListener {
    private FragmentNoteListBinding binding;
    private NoteViewModel noteViewModel;
    private ChangeToTextEditor listener;

    private ShareDialog shareDialog;
    private DeleteDialog deleteDialog;
    private ChangeTitleDialog changeTitleDialog;

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
                noteViewModel.setSelectedNote(new Note());
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
                        //listener.changeToLandTextEditor();
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
        //void changeToLandTextEditor();
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

    //  Context Menu from NoteListAdapter
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            //  Delete option
            case 121:
                openDeleteDialog(item);
                return true;
            //  Share option
            case 122:
                openShareDialog(item);
                return true;
            case 123:
                openChangeTitleDialog(item);
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void displayContextMenuMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //  SHARE NOTE DIALOG
    public void openShareDialog(MenuItem item){
        shareDialog = new ShareDialog(item, this);
        shareDialog.show(NoteListFragment.this.getChildFragmentManager(), "shareDialog");
    }
    @Override
    public void onShareDialogPositiveClick(ShareDialog dialog, MenuItem item) {
        displayContextMenuMessage(getString(R.string.share_notelist_context));
    }
    @Override
    public void onShareDialogNeutralClick(ShareDialog dialog, MenuItem item) {
        //  Dismiss dialog
    }

    //  DELETE NOTE DIALOG
    public void openDeleteDialog(MenuItem item){
        deleteDialog = new DeleteDialog(item, this);
        deleteDialog.show(NoteListFragment.this.getChildFragmentManager(), "deleteDialog");
    }
    @Override
    public void onDeleteDialogPositiveClick(DeleteDialog dialog, MenuItem item) {
        //myAdapter.removeNote(item.getGroupId());
        binding.getAdapter().removeNote(item.getGroupId());
        displayContextMenuMessage(getString(R.string.delete_notelist_context));
    }
    @Override
    public void onDeleteDialogNeutralClick(DeleteDialog dialog, MenuItem item) {
        //  Dismiss dialog
    }

    //  CHANGE NOTE TITLE DIALOG
    public void openChangeTitleDialog(MenuItem item){
        changeTitleDialog = new ChangeTitleDialog(item,this);
        changeTitleDialog.show(NoteListFragment.this.getChildFragmentManager(),"changeTitleDialog");
    }
    @Override
    public void onChangeTitleDialogPositiveClick(ChangeTitleDialog changeTitleDialog, MenuItem item) {
        displayContextMenuMessage(getString(R.string.changeTitle_notelist_context));
        //  TODO: Change note title
    }
    @Override
    public void onChangeTitleDialogNeutralClick(ChangeTitleDialog changeTitleDialog, MenuItem item) {
        //  Dismiss dialog
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

}