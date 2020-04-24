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
    private MenuItem item;
    private Context context;
    private NoteViewModel viewModel;
    private NoteListAdapter mAdapter;

    DeleteDialogListener listener;

    public DeleteDialog(MenuItem item, DeleteDialogListener listener, NoteListAdapter mAdapter, Context context) {
        this.item = item;
        this.listener = listener;
        this.context = context;
        this.mAdapter = mAdapter;
    }

    public interface DeleteDialogListener{
        void onDeleteDialogPositiveClick(DeleteDialog dialog, MenuItem item);
        void onDeleteDialogNeutralClick(DeleteDialog dialog, MenuItem item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setMessage(R.string.Dmessage_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.Ddelete_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler.removeNote(viewModel.getSelectedNote().getValue(), new DatabaseHandler.removeNoteCallback() {
                            @Override
                            public void onSuccessfulRemoved() {
                                Toast.makeText(context,R.string.DSuccesful,Toast.LENGTH_SHORT).show();
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailureRemoved() {
                                Toast.makeText(context,R.string.DFailure,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void noteNotHaveId() {
                                Toast.makeText(context,R.string.DFailure,Toast.LENGTH_SHORT).show();
                            }
                        });
                        listener.onDeleteDialogPositiveClick(DeleteDialog.this,item);
                    }
                })
                .setNeutralButton(R.string.Dcancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDeleteDialogNeutralClick(DeleteDialog.this,item);
                    }
                });

        return builder.create();
    }
}
