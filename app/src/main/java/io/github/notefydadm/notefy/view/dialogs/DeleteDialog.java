package io.github.notefydadm.notefy.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.adapter.NoteListAdapter;

public class DeleteDialog extends DialogFragment {
    private NoteListAdapter mAdapter;

    DeleteDialogListener listener;

    public DeleteDialog( DeleteDialogListener listener, NoteListAdapter mAdapter) {
        this.listener = listener;
        this.mAdapter = mAdapter;
    }

    public interface DeleteDialogListener{
        void onClickDelete();
        void onClickCancel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(R.string.Dmessage_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.Ddelete_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        listener.onClickDelete();
                    }
                })
                .setNegativeButton(R.string.Dcancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClickCancel();
                    }
                });

        return builder.create();
    }
}
