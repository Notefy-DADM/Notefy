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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.adapter.UserListAdapter;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.databinding.ShareDialogBinding;
import io.github.notefydadm.notefy.model.Note;
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

        binding.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        binding.setAdapter(new UserListAdapter(this, context, new UserListAdapter.ItemListener() {
            @Override
            public void itemClicked(int position) {
                binding.setSelectedUser(binding.getAdapter().get(position));
            }

            @Override
            public void itemRemoved(String item) {
                binding.setSelectedUser(null);
            }
        }));

        DatabaseHandler.getSharedListNoteListener(new DatabaseHandler.DatabaseListener<List<String>>() {
            @Override
            public void onSnapshot(List<String> users) {
                if (users != null) binding.getAdapter().setUsers(users);
            }

            @Override
            public void onFailureOnListener(Exception exception) {

            }
        }, Objects.requireNonNull(viewModel.getSelectedNote().getValue()));

        builder.setView(binding.getRoot())
                .setTitle(R.string.Stitle_notelist_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.Sshare_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LiveData<Note> selectedNoteData = viewModel.getSelectedNote();
                        if (selectedNoteData != null) {
                            Note selectedNote = selectedNoteData.getValue();
                            if (selectedNote != null) {
                                DatabaseHandler.updateSharedListNote(selectedNote, binding.getAdapter().getUsers().getValue(), new DatabaseHandler.UpdateSharedListNoteCallback() {
                                    @Override
                                    public void onSuccessfulShared() {
                                        Toast.makeText(context, R.string.SSuccessful, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailureShared() {
                                        Toast.makeText(context, R.string.SFailure, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        //  Send the event back to the host
                        listener.onShareDialogPositiveClick(ShareDialog.this, item);
                    }
                })
                .setNegativeButton(R.string.Scancel_button_notelist_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onShareDialogNeutralClick(ShareDialog.this,item);
                    }
                });
        
        return builder.create();
    }

}
