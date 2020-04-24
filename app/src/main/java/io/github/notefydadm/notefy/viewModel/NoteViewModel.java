package io.github.notefydadm.notefy.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.github.notefydadm.notefy.database.SingletonDatabase;
import io.github.notefydadm.notefy.model.Note;

public class NoteViewModel extends ViewModel {

    private MutableLiveData<List<Note>> notes, databaseNotes;
    private Observer<List<Note>> databaseNotesObserver;

    private MutableLiveData<Note> selectedNote;

    public enum NoteMode { MINE, SHARED_WITH_ME }

    private NoteMode noteMode;

    public NoteViewModel() {
        notes = new MutableLiveData<>();
        selectedNote = new MutableLiveData<>();
        databaseNotesObserver = new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                NoteViewModel.this.notes.setValue(notes);
            }
        };
        noteMode = NoteMode.MINE;
    }

    public LiveData<List<Note>> getNotes() {
        return notes;
    }

    public void setSelectedNote(Note note){
        this.selectedNote.setValue(note);
    }

    public void postSelectedNote(Note note) { this.selectedNote.postValue(note); }

    public NoteMode getNoteMode() {
        return noteMode;
    }

    public void setNoteMode(NoteMode noteMode) {
        boolean reload = this.noteMode != noteMode;
        this.noteMode = noteMode;
        if (reload) loadNotes();
    }

    public void loadNotes() {
        // TODO: Do an asynchronous operation to fetch notes from database
        if (databaseNotes != null) {
            databaseNotes.removeObserver(databaseNotesObserver);
        }
        switch (noteMode) {
            case MINE:
                databaseNotes = SingletonDatabase.getInstance().getMyNotes();
                break;
            case SHARED_WITH_ME:
                databaseNotes = SingletonDatabase.getInstance().getSharedWithMeNotes();
                break;
        }
        if (databaseNotes != null) {
            notes.setValue(databaseNotes.getValue());
            databaseNotes.observeForever(databaseNotesObserver);
        }
    }

    public LiveData<Note> getSelectedNote() {
        if (selectedNote == null) {
            selectedNote = new MutableLiveData<>();
        }
        return selectedNote;
    }

    @Override
    protected void onCleared() {
        if(databaseNotes != null){
            databaseNotes.removeObserver(databaseNotesObserver);
        }
        super.onCleared();
    }
}
