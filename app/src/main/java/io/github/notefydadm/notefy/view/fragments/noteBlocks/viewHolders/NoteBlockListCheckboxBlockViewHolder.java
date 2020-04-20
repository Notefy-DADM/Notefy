package io.github.notefydadm.notefy.view.fragments.noteBlocks.viewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.github.notefydadm.notefy.R;

public class NoteBlockListCheckboxBlockViewHolder extends RecyclerView.ViewHolder {

    public final CheckBox checkBox;
    public final EditText editText;
    public final ImageButton button;


    public NoteBlockListCheckboxBlockViewHolder(@NonNull View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkBox_block);
        editText = itemView.findViewById(R.id.editText_checkboxBlock);
        button = itemView.findViewById(R.id.deleteCheckboxButton);
    }
}
