package io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.github.notefydadm.notefy.R;

public class NoteBlockListTextBlockViewHolder extends RecyclerView.ViewHolder {

    public final EditText editText;
    public final ImageButton button;

    public NoteBlockListTextBlockViewHolder(@NonNull View itemView) {
        super(itemView);
        editText = itemView.findViewById(R.id.editTextBlock);
        button = itemView.findViewById(R.id.deleteTextButton);

    }
}
