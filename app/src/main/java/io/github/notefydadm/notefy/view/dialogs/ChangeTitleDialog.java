package io.github.notefydadm.notefy.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.databinding.ChangeTitleDialogBinding;
import io.github.notefydadm.notefy.model.Note;

public class ChangeTitleDialog extends DialogFragment {
    private ChangeTitleDialogBinding binding;

    private ChangeTitleDialogCallback listener;
    private Note note;

    public ChangeTitleDialog(Note note, ChangeTitleDialogCallback listener) {
        this.listener = listener;
        this.note = note;
    }

    public interface ChangeTitleDialogCallback {
        void onChangeTitleClick(String title);
        void onCancelClick();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.change_title_dialog,null,false);
        String nameButtonChangeName;
        if(addNewNote()) {
            nameButtonChangeName = getString(R.string.add_note);
        }else{
            nameButtonChangeName = getString(R.string.change_name);
            binding.setTitle(note.getTitle());
        }
        builder.setView(binding.getRoot())
                .setTitle(R.string.Ctitle_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(nameButtonChangeName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Send the event back to the host
                        listener.onChangeTitleClick(binding.getTitle());
                    }
                })
                .setNegativeButton(R.string.Ccancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancelClick();
                    }
                });

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_title_dialog, container, false);
    }

    private boolean addNewNote() {
        return note == null || note.getNoteId() == null || note.getNoteId().isEmpty();
    }

}
