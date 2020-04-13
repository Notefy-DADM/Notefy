package io.github.notefydadm.notefy.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.TextBlock;

public class NoteViewModel extends ViewModel {
    private MutableLiveData<List<Note>> notes;
    private MutableLiveData<Note> selectedNote = new MutableLiveData<>();

    public LiveData<List<Note>> getNotes() {
        if (notes == null) {
            notes = new MutableLiveData<>();
            loadNotes();
        }
        return notes;
    }

    public LiveData<List<Note>> getAllNotes(){
        if(notes == null){
            notes = new MutableLiveData<>();
            this.notes.setValue(loadAllNotes());
        }
        return notes;
    }

    public void setNotes(ArrayList<Note> notes){
        this.notes.setValue(notes);
    }
    public void setSelectedNote(Note note){
        this.selectedNote.setValue(note);
    }

    public void loadNotes() {
        ArrayList<Note> loadedNotes = new ArrayList<>();
        // TODO: Do an asynchronous operation to fetch notes from database
        // For now, we will use mock notes
        getMockNotes(loadedNotes);
        notes.postValue(loadedNotes);
    }

    public ArrayList<Note> loadAllNotes(){
        ArrayList<Note> loadedNotes = new ArrayList<>();

        loadedNotes.add(new Note("Test", "1"));
        loadedNotes.add(new Note("Very very very long test", "1"));

        Note note = new Note("Important notes to remember for studying", "3");
        note.getBlocks().add(new TextBlock("DADM is cool!"));
        loadedNotes.add(note);

        loadedNotes.add(new Note("Some notes about my games", "3"));

        return loadedNotes;
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

    public void setContent(CharSequence input){
       Note selectedNote = getSelectedNote().getValue();
       if (selectedNote != null) {
           selectedNote.getBlocks().add(new TextBlock(input.toString()));
       }
    }
}
