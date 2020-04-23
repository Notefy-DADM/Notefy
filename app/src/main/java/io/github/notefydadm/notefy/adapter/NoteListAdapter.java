package io.github.notefydadm.notefy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.List;

import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.database.DatabaseHandler;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.view.activities.MainActivity;
import io.github.notefydadm.notefy.view.fragments.NoteListFragment;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class NoteListAdapter extends Adapter<NoteListAdapter.NoteListViewHolder> {

    private LiveData<List<Note>> notes;
    private Context fragmentContext;
    private NoteViewModel viewModel;

    private PositionClickedListener positionListener;

    private NoteListFragment.ChangeToTextEditor listener;


    public NoteListAdapter(@NonNull Activity activity, Context context, final NoteListFragment.ChangeToTextEditor listener) {
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
                    listener.changeToTextEditor();
                }
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_row, parent, false);
        return new NoteListViewHolder(v, positionListener);
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

    public void removeNote(int position){
        DatabaseHandler.removeNote(notes.getValue().get(position), new DatabaseHandler.removeNoteCallback() {
            @Override
            public void onSuccessfulRemoved() {
                notifyDataSetChanged();
            }

            @Override
            public void onFailureRemoved() {

            }

            @Override
            public void noteNotHaveId() {

            }
        });
    }

    interface PositionClickedListener {
        void itemClicked(int position);
    }

    class NoteListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView title, content;
        private CardView cardView;
        private PositionClickedListener listener;

        public NoteListViewHolder(@NonNull View itemView, PositionClickedListener listener) {
            super(itemView);

            this.title = itemView.findViewById(R.id.card_title);
            this.content = itemView.findViewById(R.id.card_content);
            this.cardView = itemView.findViewById(R.id.note);

            itemView.setOnClickListener(this);
            cardView.setOnCreateContextMenuListener(this);

            this.listener = listener;
        }

        public void bind(Note note, Context context) {
            title.setText(note.getTitle());
            content.setText(note.getContent());
        }

        @Override
        public void onClick(View view) {
            listener.itemClicked(getAdapterPosition());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            viewModel.postSelectedNote(notes.getValue().get(getAdapterPosition()));
            menu.add(this.getAdapterPosition(), 121,0,R.string.remove_adapter_context);
            menu.add(this.getAdapterPosition(),122,1,R.string.share_adapter_context);
            menu.add(this.getAdapterPosition(),123,2,R.string.change_adapter_context);
        }
    }
}
