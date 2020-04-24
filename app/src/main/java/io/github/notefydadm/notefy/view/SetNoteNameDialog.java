package io.github.notefydadm.notefy.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import io.github.notefydadm.notefy.R;

public class SetNoteNameDialog extends DialogFragment {

    public interface SetNoteNameCallback{
         void onClickSet(String name);
         void onClickCancel();
    }

    SetNoteNameCallback callback;
    boolean addNewNote;
    String noteName;

    public SetNoteNameDialog(SetNoteNameCallback callback,String noteName){
        this.callback = callback;
        this.noteName = noteName;
        if(noteName.isEmpty()){
            addNewNote = true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.set_note_name_dialog,null);
        String nameButtonChangeName;
        if(addNewNote){
            nameButtonChangeName = getString(R.string.add_note);
        }else{
            nameButtonChangeName = getString(R.string.change_name);
        }
        final TextInputLayout editTextName = view.findViewById(R.id.textFieldNoteName);
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(nameButtonChangeName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        callback.onClickSet(editTextName.getEditText().getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SetNoteNameDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}