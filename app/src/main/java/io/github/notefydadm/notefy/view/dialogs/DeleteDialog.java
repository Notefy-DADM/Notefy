package io.github.notefydadm.notefy.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import io.github.notefydadm.notefy.R;

public class DeleteDialog extends DialogFragment {
    private MenuItem item;

    DeleteDialogListener listener;

    public DeleteDialog(MenuItem item, DeleteDialogListener listener) {
        this.item = item;
        this.listener = listener;
    }

    public interface DeleteDialogListener{
        void onDeleteDialogPositiveClick(DeleteDialog dialog, MenuItem item);
        void onDeleteDialogNegativeClick(DeleteDialog dialog, MenuItem item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setMessage(R.string.Dmessage_notelist_dialog)
                .setPositiveButton(R.string.Ddelete_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDeleteDialogPositiveClick(DeleteDialog.this,item);
                    }
                })
                .setNegativeButton(R.string.Dcancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDeleteDialogNegativeClick(DeleteDialog.this,item);
                    }
                });

        return builder.create();
    }
}
