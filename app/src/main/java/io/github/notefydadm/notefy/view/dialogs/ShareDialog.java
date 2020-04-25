package io.github.notefydadm.notefy.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.adapter.UserListAdapter;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.databinding.ShareDialogBinding;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.User;

public class ShareDialog extends DialogFragment {
    private ShareDialogBinding binding;

    private Note note;
    private ShareDialogCallback listener;

    public ShareDialog(Note note, ShareDialogCallback listener) {
        this.listener = listener;
        this.note = note;
    }

    public interface ShareDialogCallback {
        void onShareDialogPositiveClick(List<User> users);
        void onShareDialogNeutralClick();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.share_dialog,null,false);

        binding.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        binding.setAdapter(new UserListAdapter(this, getContext(), new UserListAdapter.ItemListener() {
            @Override
            public void itemClicked(int position) {
                binding.setSelectedUser(binding.getAdapter().get(position));
            }

            @Override
            public void itemRemoved(User item) {
                binding.setSelectedUser(null);
            }
        }));

        binding.setAddUser(new BiConsumer() {
            @Override
            public void accept(final Object adapter, Object username) {
                DatabaseHandler.getUser(username.toString(), new Consumer<User>() {
                    @Override
                    public void accept(User user) {
                        if (user != null) {
                            try {
                                ((UserListAdapter) adapter).add(user);
                            } catch (IllegalArgumentException ignored) {
                                Toast
                                    .makeText(getContext(), R.string.SUser_already_shared, Toast.LENGTH_SHORT)
                                    .show();
                            }
                        } else {
                            Toast
                                .makeText(getContext(), R.string.SUser_no_exists, Toast.LENGTH_SHORT)
                                .show();
                        }
                    }
                });
            }
        });

        DatabaseHandler.getSharedListNoteListener(new DatabaseHandler.DatabaseListener<List<User>>() {
            @Override
            public void onSnapshot(List<User> users) {
                if (users != null) binding.getAdapter().setUsers(users);
            }

            @Override
            public void onFailureOnListener(Exception exception) {

            }
        },note);

        builder.setView(binding.getRoot())
                .setTitle(R.string.Stitle_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.Sshare_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Send the event back to the host
                        listener.onShareDialogPositiveClick(binding.getAdapter().getUsers().getValue());
                    }
                })
                .setNegativeButton(R.string.Scancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onShareDialogNeutralClick();
                    }
                });
        
        return builder.create();
    }

}
