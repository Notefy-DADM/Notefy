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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.databinding.ShareDialogBinding;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class ShareDialog extends DialogFragment {
    private MenuItem item;
    private ShareDialogBinding binding;
    private NoteViewModel viewModel;
    private Context context;

    private ShareDialogListener listener;

    public ShareDialog(MenuItem item, ShareDialogListener listener, Context context) {
        this.item = item;
        this.listener = listener;
        this.context = context;
    }

    public interface ShareDialogListener {
        void onShareDialogPositiveClick(ShareDialog dialog, MenuItem item);
        void onShareDialogNeutralClick(ShareDialog dialog, MenuItem item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.share_dialog,null,false);

        builder.setView(binding.getRoot())
                .setTitle(R.string.Stitle_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.Sshare_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler.shareNoteWithUser(viewModel.getSelectedNote().getValue(), binding.getUsername(), new DatabaseHandler.shareNoteWithUserCallback() {
                            @Override
                            public void onSuccessfulShared() {
                                Toast.makeText(context,R.string.SSuccessful,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailureShared() {
                                Toast.makeText(context,R.string.SFailure, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onUserToShareNotExists() {
                                Toast.makeText(context,R.string.SUser_no_exists,Toast.LENGTH_SHORT).show();
                            }
                        });
                        //  Send the event back to the host
                        listener.onShareDialogPositiveClick(ShareDialog.this, item);
                    }
                })
                .setNeutralButton(R.string.Scancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onShareDialogNeutralClick(ShareDialog.this,item);
                    }
                });
        
        return builder.create();
    }

}
