package io.github.notefydadm.notefy.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.databinding.FragmentNoteBinding;
import io.github.notefydadm.notefy.model.Block;
import io.github.notefydadm.notefy.model.CheckBoxBlock;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.TextBlock;
import io.github.notefydadm.notefy.view.activities.MainActivity;
import io.github.notefydadm.notefy.view.dialogs.LoadingDialog;
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

            if (note.getUserId() == null) {
                note.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }

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

            DatabaseHandler.addNoteToUser(note,callback);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideButtonSave();
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);

        final Note note = noteViewModel.getSelectedNote().getValue();
        if (note == null) {
            System.out.println("A NoteFragment with null selected note was being created. Removing from view");
            requireFragmentManager().beginTransaction().remove(this).commit();
            return null;
        }

        FragmentNoteBinding binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_note, container,false);

        adapter = new NoteBlocksListAdapter(getCopyOfBlocks(note.getBlocks()), new OnItemModifiedCallback() {
            @Override
            public void onItemModified() {
                showButtonSave();
            }
        });

        noteViewModel.getSelectedNote().observe(this, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                if (note != null) {
                    adapter.setBlocks(getCopyOfBlocks(note.getBlocks()));
                }
            }
        });

        binding.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        binding.setAdapter(adapter);
        binding.setItemTouchHelper(new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int positionViewHolder = viewHolder.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();

                adapter.swapBlocks(positionViewHolder, positionTarget);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }));

        initFab(binding);

        return binding.getRoot();
    }

    @BindingAdapter("hasFixedSize")
    public static void setHasFixedSize(RecyclerView recyclerView, boolean hasFixedSize) {
        recyclerView.setHasFixedSize(hasFixedSize);
    }

    @BindingAdapter("itemTouchHelper")
    public static void setItemTouchHelper(RecyclerView recyclerView, ItemTouchHelper helper) {
        helper.attachToRecyclerView(recyclerView);
    }

    private void initFab(FragmentNoteBinding binding) {
        binding.setAddNewTextBlock(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showButtonSave();
                addNewTextBlock();
            }
        });
        binding.setAddNewCheckBoxBlock(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        if (activity.toolbarMenu != null) {
            final MenuItem saveItem = activity.toolbarMenu.findItem(R.id.save_toolbar);
            saveItem.setVisible(true);
            saveItem.setEnabled(true);
        }
    }

    private void hideButtonSave(){
        MainActivity activity = (MainActivity) requireActivity();
        if (activity.toolbarMenu != null) {
            final MenuItem saveItem = activity.toolbarMenu.findItem(R.id.save_toolbar);
            saveItem.setVisible(false);
            saveItem.setEnabled(false);
        }
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
