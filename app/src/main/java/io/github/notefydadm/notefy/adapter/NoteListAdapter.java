package io.github.notefydadm.notefy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;
import java.util.List;

import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.R;
import io.github.notefydadm.notefy.view.activities.MainActivity;
import io.github.notefydadm.notefy.viewModel.NoteViewModel;

public class NoteListAdapter extends Adapter<NoteListAdapter.NoteListViewHolder> {

    private ArrayList<Note> notes = new ArrayList<>();
    private Context fragmentContext;
    private NoteViewModel viewModel;

    private PositionClickedListener positionListener;

    public NoteListAdapter(Activity activity, Context context) {
        this.viewModel = new ViewModelProvider((MainActivity)activity).get(NoteViewModel.class);
        this.fragmentContext = context;
        this.positionListener = new PositionClickedListener() {
            @Override
            public void itemClicked(int position) {
                Note note = notes.get(position);
                viewModel.postSelectedNote(note);
            }
        };
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

        Note note = notes.get(position);
        holder.bind(note, fragmentContext);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void addNotes(List<Note> repos) {
        for(Note actNote : repos){
            if(!notes.contains(actNote)){
                notes.add(actNote);
                notifyDataSetChanged();
            }
        }
    }

    interface PositionClickedListener {
        void itemClicked(int position);
    }

    class NoteListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title, content;
        private PositionClickedListener listener;

        public NoteListViewHolder(@NonNull View itemView, PositionClickedListener listener) {
            super(itemView);

            this.title = itemView.findViewById(R.id.card_title);
            this.content = itemView.findViewById(R.id.card_content);

            itemView.setOnClickListener(this);

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
    }
}
