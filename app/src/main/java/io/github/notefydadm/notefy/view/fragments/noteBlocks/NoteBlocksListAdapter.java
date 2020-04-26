package io.github.notefydadm.notefy.view.fragments.noteBlocks;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.databinding.NoteCheckboxBlockBinding;
import io.github.notefydadm.notefy.databinding.NoteTextBlockBinding;
import io.github.notefydadm.notefy.model.Block;
import io.github.notefydadm.notefy.model.CheckBoxBlock;
import io.github.notefydadm.notefy.model.TextBlock;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders.NoteBlockListCheckboxBlockViewHolder;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders.NoteBlockListTextBlockViewHolder;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders.NoteBlockListViewHolder;

public class NoteBlocksListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemModifiedCallback itemModifiedCallback;
    private ArrayList<Block> blockList;

    public NoteBlocksListAdapter(ArrayList<Block> blocks, OnItemModifiedCallback callback) {
        blockList = blocks == null ? new ArrayList<Block>() : blocks;
        this.itemModifiedCallback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        Block block = blockList.get(position);

        if (block instanceof CheckBoxBlock) {
            return NoteBlocksViewTypes.CHECK_BOX_BLOCK;
        } else if (block instanceof TextBlock) {
            return NoteBlocksViewTypes.TEXT_BLOCK;
        }

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case NoteBlocksViewTypes.TEXT_BLOCK:
                NoteTextBlockBinding viewTextBlock = DataBindingUtil
                    .inflate(inflater, R.layout.note_text_block, parent,false);
                return new NoteBlockListTextBlockViewHolder(viewTextBlock);
            case NoteBlocksViewTypes.CHECK_BOX_BLOCK:
                NoteCheckboxBlockBinding viewCheckboxBlock = DataBindingUtil
                    .inflate(inflater, R.layout.note_checkbox_block, parent,false);
                return new NoteBlockListCheckboxBlockViewHolder(viewCheckboxBlock);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NoteBlockListViewHolder) {
            final Block block = blockList.get(position);
            NoteBlocksListAdapterCallbacks callbacks = new NoteBlocksListAdapterCallbacks(itemModifiedCallback, new Runnable() {
                @Override
                public void run() {
                    NoteBlocksListAdapter.this.deleteBlockAtPosition(position);
                }
            });

            if (block instanceof TextBlock && holder instanceof NoteBlockListTextBlockViewHolder) {
                ((NoteBlockListTextBlockViewHolder)holder).bind((TextBlock)block, callbacks);
            } else if (block instanceof CheckBoxBlock && holder instanceof NoteBlockListCheckboxBlockViewHolder) {
                ((NoteBlockListCheckboxBlockViewHolder)holder).bind((CheckBoxBlock)block, callbacks);
            }
        }
    }

    @Override
    public int getItemCount() {
        return blockList.size();
    }

    public ArrayList<Block> getBlocks() {
        return blockList;
    }

    public void deleteBlockAtPosition(int position) {
        blockList.remove(position);
        notifyDataSetChanged();
    }

    public void addBlock(Block block) {
        blockList.add(block);
        notifyDataSetChanged();
    }

    public void swapBlocks(int positionSource, int positionTarget) {
        Collections.swap(blockList, positionSource, positionTarget);
        notifyItemMoved(positionSource, positionTarget);

        itemModifiedCallback.onItemModified();
    }

    @FunctionalInterface
    public interface TextWatcherOnTextChanged {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    @BindingAdapter("onTextChanged")
    public static void onTextChanged(EditText editText, final TextWatcherOnTextChanged handler) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
