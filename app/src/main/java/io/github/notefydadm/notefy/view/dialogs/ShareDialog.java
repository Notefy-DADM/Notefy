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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.zip.Inflater;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.databinding.ShareDialogBinding;
import io.github.notefydadm.notefy.view.fragments.NoteListFragment;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class ShareDialog extends DialogFragment {
    private MenuItem item;
    private ShareDialogBinding binding;
    private NoteViewModel viewModel;

    private ShareDialogListener listener;

    public ShareDialog(MenuItem item, ShareDialogListener listener) {
        this.item = item;
        this.listener = listener;
    }

    public interface ShareDialogListener {
        void onShareDialogPositiveClick(ShareDialog dialog, MenuItem item);
        void onShareDialogNeutralClick(ShareDialog dialog, MenuItem item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        viewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.share_dialog,null,false);

        builder.setView(binding.getRoot())
                .setTitle(R.string.Stitle_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.Sshare_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Send the event back to the host
                        listener.onShareDialogPositiveClick(ShareDialog.this, item);
                        DatabaseHandler.shareNoteWithUser(viewModel.getSelectedNote().getValue(), binding.getUsername(), new DatabaseHandler.shareNoteWithUserCallback() {
                            @Override
                            public void onSuccessfulShared() {
                                //Toast.makeText(requireContext(),"Shared note",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailureShared() {

                            }

                            @Override
                            public void onUserToShareNotExists() {

                            }
                        });
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
