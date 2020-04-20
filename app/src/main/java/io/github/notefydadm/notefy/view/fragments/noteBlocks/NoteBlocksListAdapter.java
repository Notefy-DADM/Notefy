package io.github.notefydadm.notefy.view.fragments.noteBlocks;

import android.media.AudioTrack;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.model.Block;
import io.github.notefydadm.notefy.model.CheckBoxBlock;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.TextBlock;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders.NoteBlockListCheckboxBlockViewHolder;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders.NoteBlockListTextBlockViewHolder;

public class NoteBlocksListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemModifiedCallback itemModifiedCallback;
    private ArrayList<Block> blockList;

    public NoteBlocksListAdapter(ArrayList<Block> blocks, OnItemModifiedCallback callback) {
        blockList = new ArrayList<>();
        if(blocks!=null){
            blockList = blocks;
        }
        this.itemModifiedCallback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        Block block = blockList.get(position);
        if(block.getClass() == TextBlock.class){
            return NoteBlocksViewTypes.TEXT_BLOCK;
        }else if(block.getClass() == CheckBoxBlock.class){
            return NoteBlocksViewTypes.CHECK_BOX_BLOCK;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case NoteBlocksViewTypes.TEXT_BLOCK:
                View viewTextBlock = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.note_text_block,parent,false);
                return new NoteBlockListTextBlockViewHolder(viewTextBlock);
            case NoteBlocksViewTypes.CHECK_BOX_BLOCK:
                View viewCheckboxBlock = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.note_checkbox_block,parent,false);
                return new NoteBlockListCheckboxBlockViewHolder(viewCheckboxBlock);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getClass() == NoteBlockListTextBlockViewHolder.class){
            bindTextBlock((NoteBlockListTextBlockViewHolder)holder,position);
        }else if(holder.getClass() == NoteBlockListCheckboxBlockViewHolder.class){

            bindCheckboxBlock((NoteBlockListCheckboxBlockViewHolder)holder,position);
        }
    }

    private void bindTextBlock(NoteBlockListTextBlockViewHolder viewHolder, final int position){
        final TextBlock block = (TextBlock) blockList.get(position);
        viewHolder.editText.setText(block.getText());
        viewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemModifiedCallback.onItemModified();
                block.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBlockAtPosition(position);
            }
        });
    }

    private void bindCheckboxBlock(NoteBlockListCheckboxBlockViewHolder viewHolder, final int position){
        final CheckBoxBlock block = (CheckBoxBlock) blockList.get(position);
        viewHolder.checkBox.setChecked(block.isChecked());
        viewHolder.editText.setText(block.getText());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemModifiedCallback.onItemModified();
                block.setChecked(isChecked);
            }
        });

        viewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemModifiedCallback.onItemModified();
                block.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBlockAtPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blockList.size();
    }

    public ArrayList<Block> getBlocks(){
        return blockList;
    }

    public void deleteBlockAtPosition(int position){
        blockList.remove(position);
        notifyDataSetChanged();
    }

    public void addBlock(Block block){
        blockList.add(block);
        notifyDataSetChanged();
    }
}
