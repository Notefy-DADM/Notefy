package io.github.notefydadm.notefy.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import io.github.notefydadm.notefy.R;

public class ChangeTitleDialog extends DialogFragment {
    private String title;
    private MenuItem item;

    ChangeTitleDialogListener listener;

    public ChangeTitleDialog(MenuItem item, ChangeTitleDialogListener listener) {
        this.item = item;
        this.listener = listener;
    }

    public interface ChangeTitleDialogListener{
        void onChangeTitleDialogPositiveClick(ChangeTitleDialog changeTitleDialog, MenuItem item);
        void onChangeTitleDialogNeutralClick(ChangeTitleDialog changeTitleDialog, MenuItem item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(R.layout.change_title_dialog)
                .setTitle(R.string.Ctitle_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.Cchange_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Send the event back to the host
                        listener.onChangeTitleDialogPositiveClick(ChangeTitleDialog.this,item);
                        //  TODO: Binding with title in change_title_dialog.xml layout
                        //setTitle(title);    username from EditText in change_title_dialog.xml
                    }
                })
                .setNeutralButton(R.string.Ccancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onChangeTitleDialogNeutralClick(ChangeTitleDialog.this,item);
                    }
                });

        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_title_dialog, container, false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
