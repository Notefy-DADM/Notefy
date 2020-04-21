package io.github.notefydadm.notefy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.databinding.FragmentNoteBinding;
import io.github.notefydadm.notefy.model.Block;
import io.github.notefydadm.notefy.model.CheckBoxBlock;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.TextBlock;
import io.github.notefydadm.notefy.view.LoadingDialog;
import io.github.notefydadm.notefy.view.activities.MainActivity;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.NoteBlocksListAdapter;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.OnItemModifiedCallback;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {
    private NoteBlocksListAdapter adapter;
    private NoteViewModel noteViewModel;

    public void saveNote() {
        final Note note = noteViewModel.getSelectedNote().getValue();
        if (note != null) {
            note.setBlocks(adapter.getBlocks());

            String userId  = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final LoadingDialog loadingDialog = new LoadingDialog();
            loadingDialog.show(requireFragmentManager(), null);
            DatabaseHandler.addNoteToUserCallback callback = new DatabaseHandler.addNoteToUserCallback() {
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

            DatabaseHandler.addNoteToUser(userId,note,callback);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideButtonSave();
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        final Note note = noteViewModel.getSelectedNote().getValue();
        if (note == null) throw new IllegalStateException("A note needs to be selected");

        FragmentNoteBinding binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_note, container,false);

        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.recyclerViewBlocks);

        adapter = new NoteBlocksListAdapter(getCopyOfBlocks(note.getBlocks()), new OnItemModifiedCallback() {
            @Override
            public void onItemModified() {
                showButtonSave();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(inflater.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        initFab(binding);

        return binding.getRoot();
    }

    private void initFab(FragmentNoteBinding binding) {
        binding.setAddNewTextBlock(new Runnable() {
            @Override
            public void run() {
                showButtonSave();
                addNewTextBlock();
            }
        });
        binding.setAddNewCheckBoxBlock(new Runnable() {
            @Override
            public void run() {
                showButtonSave();
                addNewCheckboxBlock();
            }
        });
    }

    private void addNewTextBlock() {
        TextBlock block = new TextBlock();
        adapter.addBlock(block);
    }

    private void addNewCheckboxBlock(){
        CheckBoxBlock block = new CheckBoxBlock(false);
        adapter.addBlock(block);
    }

    private void showButtonSave(){
        MainActivity activity = (MainActivity) requireActivity();
        final MenuItem saveItem = activity.toolbarMenu.findItem(R.id.save_toolbar);
        saveItem.setVisible(true);
        saveItem.setEnabled(true);
    }

    private void hideButtonSave(){
        MainActivity activity = (MainActivity) requireActivity();
        final MenuItem saveItem = activity.toolbarMenu.findItem(R.id.save_toolbar);
        saveItem.setVisible(false);
        saveItem.setEnabled(false);
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
