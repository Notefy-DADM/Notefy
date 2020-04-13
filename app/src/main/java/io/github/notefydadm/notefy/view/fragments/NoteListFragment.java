package io.github.notefydadm.notefy.view.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteListFragment extends Fragment {

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

        // Get view model for notes
        //final NoteViewModel noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        //noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        System.out.println("Created!");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        public void changeToTextEditor();
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