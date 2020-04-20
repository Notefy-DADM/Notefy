package io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.github.notefydadm.notefy.databinding.NoteTextBlockBinding;
import io.github.notefydadm.notefy.model.TextBlock;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.NoteBlocksListAdapterCallbacks;

public class NoteBlockListTextBlockViewHolder extends RecyclerView.ViewHolder implements NoteBlockListViewHolder<TextBlock> {

    private final NoteTextBlockBinding binding;

    public NoteBlockListTextBlockViewHolder(@NonNull NoteTextBlockBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public void bind(TextBlock block, NoteBlocksListAdapterCallbacks callbacks) {
        binding.setTextBlock(block);
        binding.setCallbacks(callbacks);
    }
}
