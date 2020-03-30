package io.github.notefydadm.notefy.ViewModel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import io.github.notefydadm.notefy.Model.Note;
import io.github.notefydadm.notefy.Model.TextBlock;

public class NoteViewModel extends ViewModel {
    private MutableLiveData<List<Note>> notes;
    private MutableLiveData<Note> selectedNote = new MutableLiveData<>();
    private MutableLiveData<CharSequence> text = new MutableLiveData<>();

    public LiveData<List<Note>> getNotes() {
        if (notes == null) {
            notes = new MutableLiveData<>();
            loadNotes();
        }
        return notes;
    }

    private void loadNotes() {
        ArrayList<Note> loadedNotes = new ArrayList<>();
        // TODO: Do an asynchronous operation to fetch notes from database
        // For now, we will use mock notes
        getMockNotes(loadedNotes);
        notes.postValue(loadedNotes);
    }

    // Used for testing
    private void getMockNotes(ArrayList<Note> noteListToLoadOn) {
        noteListToLoadOn.add(new Note("Test", "1"));
        noteListToLoadOn.add(new Note("Very very very long test", "1"));

        Note note = new Note("Important notes to remember for studying", "3");
        note.getBlocks().add(new TextBlock("DADM is cool!"));
        noteListToLoadOn.add(note);

        noteListToLoadOn.add(new Note("Some notes about my games", "3"));
    }

    public LiveData<Note> getSelectedNote() {
        return selectedNote;
    }

    public void noteSelected(Note note) {
        selectedNote.postValue(note);
    }

    ///////
    public void setText(CharSequence input){
        text.setValue(input);
    }

    public LiveData<CharSequence> getText(){
        return text;
    }
    /*public class NoteListFragment extends Fragment {
        //private noteSelector;
        private NoteViewModel model;
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            model = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);
            .setOnClickistener(item ->
            })
        }
    }*/

}
