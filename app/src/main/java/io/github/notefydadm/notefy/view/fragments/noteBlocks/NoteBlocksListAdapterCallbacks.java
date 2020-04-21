package io.github.notefydadm.notefy.view.fragments.noteBlocks;

import java.util.function.Consumer;

public class NoteBlocksListAdapterCallbacks {
    private OnItemModifiedCallback onItemModified;
    private Runnable deleteBlock;

    public NoteBlocksListAdapterCallbacks(OnItemModifiedCallback onItemModified, Runnable deleteBlock) {
        this.onItemModified = onItemModified;
        this.deleteBlock = deleteBlock;
    }

    public OnItemModifiedCallback getOnItemModified() {
        return onItemModified;
    }

    public Runnable getDeleteBlock() {
        return deleteBlock;
    }
}
