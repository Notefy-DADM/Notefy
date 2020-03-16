package io.github.notefydadm.notefy.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.github.notefydadm.notefy.Model.Note;

public class NoteViewModel extends ViewModel {
    private MutableLiveData<List<Note>> notes;

    public LiveData<List<Note>> getNotes() {
        if (notes == null) {
            notes = new MutableLiveData<>();
            loadNotes();
        }
        return notes;
    }

    private void loadNotes() {
        // Do an asynchronous operation to fetch notes from database
    }
}
