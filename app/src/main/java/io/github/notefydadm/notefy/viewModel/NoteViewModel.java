package io.github.notefydadm.notefy.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.notefydadm.notefy.database.SingletonDatabase;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.TextBlock;

public class NoteViewModel extends ViewModel {
    //private MutableLiveData<List<Note>> notes;
    private MutableLiveData<Note> selectedNote;

    public LiveData<List<Note>> getNotes() {
        return SingletonDatabase.getInstance().getMutableNoteList();
        // TODO: Instead of a singleton with the note list, call the database to get it
        /*if (notes == null) {
            notes = new MutableLiveData<>();
            loadNotes();
        }
        return notes;*/
    }

    public void setSelectedNote(Note note){
        this.selectedNote.setValue(note);
    }
    public void postSelectedNote(Note note) { this.selectedNote.postValue(note); }

    public void loadNotes() {
        ArrayList<Note> loadedNotes = new ArrayList<>();
        // TODO: Do an asynchronous operation to fetch notes from database
    }

    public LiveData<Note> getSelectedNote() {
        if (selectedNote == null) {
            selectedNote = new MutableLiveData<>();
        }
        return selectedNote;
    }
}
