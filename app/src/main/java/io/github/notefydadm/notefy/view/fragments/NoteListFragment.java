package io.github.notefydadm.notefy.view.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.adapter.NoteListAdapter;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.view.dialogs.ShareDialog;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteListFragment extends Fragment implements ShareDialog.ShareDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View myView;
    private RecyclerView myRecyclerView;
    private NoteListAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    private NoteViewModel noteViewModel;

    private ChangeToTextEditor listener;

    private ShareDialog shareDialog;

    public NoteListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteListFragment newInstance(String param1, String param2) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        System.out.println("Created!");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Deselect any previously selected note
        if (noteViewModel != null) noteViewModel.setSelectedNote(null);

        try {
            // Inflate the layout for this fragment
            myView = inflater.inflate(R.layout.fragment_note_list, container, false);
            myRecyclerView = myView.findViewById(R.id.myRecycler);

            // Get view model for notes
            noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

            // We need to have an Adapter for the RecyclerView
            myAdapter = new NoteListAdapter(this.getActivity(), getContext());

            // Update the adapter whenever the ViewModel gets new notes
            noteViewModel.getNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    myAdapter.addNotes(notes);
                }
            });

            // When a note is selected, open it
            noteViewModel.getSelectedNote().observe(getViewLifecycleOwner(), new Observer<Note>() {
                @Override
                public void onChanged(Note note) {
                    if (note != null) {
                        //  Check orientation
                        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            // landscape
                            System.out.println("Selected note landscape: " + note+ " Content: "+note.getContent());
                            //noteViewModel.setText(note.getContent());
                        }
                        else{
                            // portrait
                            System.out.println("Selected note portrait: " + note+ " Content: "+note.getContent());
                            //noteViewModel.setText(note.getContent());
                        /*Intent intent = new Intent(getActivity(), TextEditorPortraitActivity.class);
                        intent.putExtra("selectedNote", note);
                        intent.putExtra("selectedNoteContent",note.getContent());
                        startActivity(intent);*/
                            listener.changeToTextEditor();
                        }
                    }
                }
            });

            initRecyclerView();

        }catch(Exception e){
            e.printStackTrace();
        }

        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public interface ChangeToTextEditor{
        void changeToTextEditor();
        void changeToTextEditor(boolean isEditing);
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChangeToTextEditor) context;
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    private void initRecyclerView(){
        //  You can't cast a Fragment to a Context.
        myLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        myRecyclerView.setLayoutManager(myLayoutManager);
        //RecyclerView.ItemDecoration separator = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        //myRecyclerView.addItemDecoration(separator);

        //  We need to have an Adapter for the RecyclerView
        myRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 121:
                //deleteDialog(item);
                //openDeleteDialog(item);
                return true;

            case 122:
                //shareDialog(item);
                openShareDialog(item);
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    public void displayContextMenuMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void openShareDialog(MenuItem item){
        shareDialog = new ShareDialog(item);
        shareDialog.setListener(this);
        shareDialog.show(NoteListFragment.this.getChildFragmentManager(), "shareDialog");
    }

    @Override
    public void onShareDialogPositiveClick(ShareDialog dialog, MenuItem item) {
        myAdapter.removeNote(item.getGroupId());
        displayContextMenuMessage(getString(R.string.remove_notelist_context));
    }

    @Override
    public void onShareDialogNegativeClick(ShareDialog dialog, MenuItem item) {
        //  Cancels dialog
    }

    /*private void deleteDialog(final MenuItem item){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setMessage(R.string.Dmessage_notelist_dialog)
                .setPositiveButton(R.string.Ddelete_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myAdapter.removeNote(item.getGroupId());
                        displayContextMenuMessage(getString(R.string.remove_notelist_context));
                    }
                })
                .setNegativeButton(R.string.Dcancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

            AlertDialog dialog = builder.create();
            dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void shareDialog(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.share_dialog,null))
            .setTitle(R.string.Stitle_notelist_dialog)
            .setPositiveButton(R.string.Sshare_button_notelist_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayContextMenuMessage(getString(R.string.share_notelist_context));
                }
            })
            .setNegativeButton(R.string.Scancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    */
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