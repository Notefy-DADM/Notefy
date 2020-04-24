package io.github.notefydadm.notefy.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.databinding.ChangeTitleDialogBinding;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class ChangeTitleDialog extends DialogFragment {
    private ChangeTitleDialogBinding binding;

    ChangeTitleDialogCallback listener;
    Note note;

    public ChangeTitleDialog(Note note, ChangeTitleDialogCallback listener) {
        this.listener = listener;
        this.note = note;
    }

    public interface ChangeTitleDialogCallback {
        void onChangeTittleClick( String tittle);
        void onCancelClick();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.change_title_dialog,null,false);
        builder.setView(binding.getRoot())
                .setTitle(R.string.Ctitle_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.Cchange_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Send the event back to the host
                        listener.onChangeTittleClick(binding.getTitle());
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

}
