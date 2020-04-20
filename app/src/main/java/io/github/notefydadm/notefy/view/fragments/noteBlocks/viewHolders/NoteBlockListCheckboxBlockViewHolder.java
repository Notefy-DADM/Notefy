package io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.github.notefydadm.notefy.databinding.NoteCheckboxBlockBinding;
import io.github.notefydadm.notefy.model.CheckBoxBlock;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.NoteBlocksListAdapterCallbacks;

public class NoteBlockListCheckboxBlockViewHolder extends RecyclerView.ViewHolder implements NoteBlockListViewHolder<CheckBoxBlock> {

    private final NoteCheckboxBlockBinding binding;

    public NoteBlockListCheckboxBlockViewHolder(@NonNull NoteCheckboxBlockBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(CheckBoxBlock block, NoteBlocksListAdapterCallbacks callbacks) {
        binding.setCheckBoxBlock(block);
        binding.setCallbacks(callbacks);
    }
}
