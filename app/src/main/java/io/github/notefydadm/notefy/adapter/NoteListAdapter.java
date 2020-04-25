package io.github.notefydadm.notefy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.List;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.databinding.CardListRowBinding;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.view.activities.MainActivity;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class NoteListAdapter extends Adapter<NoteListAdapter.NoteListViewHolder> {

    private LiveData<List<Note>> notes;
    private Context fragmentContext;
    private NoteViewModel viewModel;

    private PositionClickedListener positionListener;

    private AdapterNoteListCallbacks listener;

    public interface AdapterNoteListCallbacks {
        void changeNameNote(Note note);
        void shareNote(Note note);
        void deleteNote(Note note);
    }

    public NoteListAdapter(@NonNull Activity activity, Context context, final AdapterNoteListCallbacks listener) {
        this.viewModel = new ViewModelProvider((MainActivity)activity).get(NoteViewModel.class);
        this.fragmentContext = context;
        this.listener = listener;
        positionListener = new PositionClickedListener() {
            @Override
            public void itemClicked(int position) {
                List<Note> noteList = notes.getValue();
                if (noteList != null) {
                    Note note = noteList.get(position);
                    viewModel.postSelectedNote(note);
                }
            }

            @Override
            public void optionsClicked(final int position, View view) {

                final Note note = notes.getValue().get(position);
                PopupMenu popup = new PopupMenu(fragmentContext, view);
                //inflating menu from xml resource
                popup.inflate(R.menu.note_click_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.changeTitleItem:
                                listener.changeNameNote(note);
                                return true;
                            case R.id.removeItem:
                                listener.deleteNote(note);
                                return true;
                            case R.id.shareItem:
                                listener.shareNote(note);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        };
        this.notes = viewModel.getNotes();
        this.notes.observe((LifecycleOwner) activity, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                NoteListAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardListRowBinding binding =
                CardListRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteListViewHolder(binding, positionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListViewHolder holder, int position) {
        List<Note> noteList = notes.getValue();
        if (noteList != null) {
            Note note = noteList.get(position);
            holder.bind(note, fragmentContext);
        }
    }

    @Override
    public int getItemCount() {
        List<Note> noteList = notes.getValue();
        return noteList == null ? 0 : noteList.size();
    }

    interface PositionClickedListener {
        void itemClicked(int position);
        void optionsClicked(int position, View view);
    }

    static class NoteListViewHolder extends RecyclerView.ViewHolder {
        private CardListRowBinding binding;

        NoteListViewHolder(@NonNull CardListRowBinding itemBinding, final PositionClickedListener listener) {
            super(itemBinding.getRoot());

            this.binding = itemBinding;

            binding.setOnClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClicked(getAdapterPosition());
                }
            });

            binding.setOnOptionsClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.optionsClicked(getAdapterPosition(),v);
                }
            });
        }

        void bind(Note note, Context context) {
            binding.setNote(note);
        }
    }
}
