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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.view.fragments.NoteListFragment;

public class ShareDialog extends DialogFragment {
    private String username;
    private MenuItem item;

    private ShareDialogListener listener;

    public ShareDialog(MenuItem item, ShareDialogListener listener) {
        this.item = item;
        this.listener = listener;
    }

    public interface ShareDialogListener {
        void onShareDialogPositiveClick(ShareDialog dialog, MenuItem item);
        void onShareDialogNegativeClick(ShareDialog dialog, MenuItem item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(R.layout.share_dialog)
                .setTitle(R.string.Stitle_notelist_dialog)
                .setPositiveButton(R.string.Sshare_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Send the event back to the host
                        listener.onShareDialogPositiveClick(ShareDialog.this, item);
                        //  TODO: Binding with username in share_dialog.xml layout
                        //setUsername(username);    username from EditText in share_dialog.xml
                    }
                })
                .setNegativeButton(R.string.Scancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onShareDialogNegativeClick(ShareDialog.this, item);
                    }
                });
        
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_dialog, container, false);
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
}
