package io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders;

import io.github.notefydadm.notefy.model.Block;
import io.github.notefydadm.notefy.view.fragments.noteBlocks.NoteBlocksListAdapterCallbacks;

public interface NoteBlockListViewHolder<T extends Block> {
    void bind(T block, NoteBlocksListAdapterCallbacks callbacks);
}
