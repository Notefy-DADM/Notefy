package io.github.notefydadm.notefy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.model.Block;
import io.github.notefydadm.notefy.model.CheckBoxBlock;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.TextBlock;
import io.github.notefydadm.notefy.view.activities.MainActivity;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.NoteBlocksListAdapter;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.OnItemModifiedCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {
    NoteBlocksListAdapter adapter;
    Note noteToShow;

    public NoteFragment(Note note) {
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(new TextBlock("Texto1"));
        blocks.add(new TextBlock("Texto2"));
        blocks.add(new CheckBoxBlock("CheckboxTrue",true));
        blocks.add(new CheckBoxBlock("CheckboxTrue",false));
        blocks.add(new TextBlock("Texto3"));
        Note exampleNote = new Note("Id","Titulo note","idUser", Note.NoteState.PUBLISHED,false,2, LocalDateTime.MAX,LocalDateTime.MIN,blocks);

        this.noteToShow = note;
    }

    public Note getNoteToSave(){
        noteToShow.setBlocks(adapter.getBlocks());
        return noteToShow;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideButtonSave();

        View view =inflater.inflate(R.layout.fragment_note,container,false);



        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBlocks);
        OnItemModifiedCallback onItemModifiedCallback = new OnItemModifiedCallback() {
            @Override
            public void onItemModified() {
                showButtonSave();
            }
        };
        adapter = new NoteBlocksListAdapter(getCopyOfBlocks(noteToShow.getBlocks()),onItemModifiedCallback);
        LinearLayoutManager layoutManager = new LinearLayoutManager(inflater.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        initFab(view);


        return view;
    }

    private void initFab(View view){
        FloatingActionsMenu menuMultipleActions = view.findViewById(R.id.multiple_actions);

        final FloatingActionButton buttonAddText = view.findViewById(R.id.buttonAddText);
        buttonAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showButtonSave();
                addNewTextBlock();
            }
        });
        final FloatingActionButton buttonAddCheckbox = view.findViewById(R.id.buttonAddCheckbox);
        buttonAddCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showButtonSave();
                addNewCheckboxBlock();
            }
        });
    }

    private void addNewTextBlock(){
        TextBlock block = new TextBlock("Text");
        adapter.addBlock(block);
    }

    private void addNewCheckboxBlock(){
        CheckBoxBlock block = new CheckBoxBlock("Text",false);
        adapter.addBlock(block);
    }

    private void showButtonSave(){
        MainActivity activity = (MainActivity) getActivity();
        activity.toolbarMenu.findItem(R.id.save_toolbar).setVisible(true);
        activity.toolbarMenu.findItem(R.id.save_toolbar).setEnabled(true);
    }

    private void hideButtonSave(){
        MainActivity activity = (MainActivity) getActivity();
        activity.toolbarMenu.findItem(R.id.save_toolbar).setVisible(false);
        activity.toolbarMenu.findItem(R.id.save_toolbar).setEnabled(false);
    }

    private ArrayList<Block> getCopyOfBlocks(ArrayList<Block> blocks){
        ArrayList<Block> blockArrayListReturn = new ArrayList<>();
        for(Block actBlock: blocks){
            if(actBlock.getClass() == TextBlock.class){
                TextBlock oldTextBlock = (TextBlock) actBlock;
                TextBlock textBlock = new TextBlock(oldTextBlock.getText(),oldTextBlock.getFontFamily(),oldTextBlock.getFontSize(),oldTextBlock.getTextStyle());

                blockArrayListReturn.add(textBlock);
            }else{
                CheckBoxBlock oldCheckboxBlock = (CheckBoxBlock) actBlock;
                CheckBoxBlock checkBoxBlock = new CheckBoxBlock(oldCheckboxBlock.getText(),oldCheckboxBlock.isChecked(),oldCheckboxBlock.getFontFamily(),oldCheckboxBlock.getFontSize(),oldCheckboxBlock.getTextStyle());
                blockArrayListReturn.add(checkBoxBlock);
            }
        }
        return blockArrayListReturn;
    }


}
