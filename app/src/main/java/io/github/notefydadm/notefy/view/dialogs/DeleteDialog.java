package io.github.notefydadm.notefy.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.adapter.NoteListAdapter;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

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
                .setNeutralButton(R.string.Dcancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClickCancel();
                    }
                });

        return builder.create();
    }
}
