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

    ShareDialogListener listener;

    public ShareDialog(MenuItem item) {
        this.item = item;
    }

    public interface ShareDialogListener{
        public void onShareDialogPositiveClick(ShareDialog dialog, MenuItem item);
        public void onShareDialogNegativeClick(ShareDialog dialog, MenuItem item);
    }

    public void setListener(ShareDialogListener listener){
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (ShareDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement ShareDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.share_dialog, null);

        builder.setView(view)
                .setTitle(R.string.Stitle_notelist_dialog)
                .setPositiveButton(R.string.Sshare_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Send the event back to the host
                        listener.onShareDialogPositiveClick(ShareDialog.this, item);
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
        View view = inflater.inflate(R.layout.share_dialog, container, false);

        return view;

    }
}
