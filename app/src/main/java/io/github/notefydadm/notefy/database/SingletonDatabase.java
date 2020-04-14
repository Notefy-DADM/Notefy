package io.github.notefydadm.notefy.database;


import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.User;

public class SingletonDatabase {

    private static SingletonDatabase instance;
    private MutableLiveData<List<Note>> mutableNoteList;
    private User user;

    private SingletonDatabase(){
        mutableNoteList = new MutableLiveData<>();

    }

    public static SingletonDatabase getInstance(){
        if(instance == null){
            instance = new SingletonDatabase();
        }

        return instance;
    }

    public void init(){

        DatabaseHandler.userGetNoteListListenerCallback callback = new DatabaseHandler.userGetNoteListListenerCallback() {
            @Override
            public void onNoteAdded(Note note) {
                addNoteToMutableList(note);
            }

            @Override
            public void onNoteModified(Note note) {
                changeNote(note);
            }

            @Override
            public void onNoteRemoved(Note note) {
                removeNote(note);
            }

            @Override
            public void onFailureOnListener(Exception exception) {

            }
        };
        DatabaseHandler.userGetNoteListListener(callback, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private User getUserFromFirebase(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //User user = new user()
        return null;//TODO implementar
    }

    private void addNoteToMutableList(Note noteToAdd){
        List<Note> list = mutableNoteList.getValue();
        if (list != null) {
            list.add(noteToAdd);
        }
        mutableNoteList.setValue(list);
    }

    private void changeNote(Note noteToChange){
        List<Note> list = mutableNoteList.getValue();
        if (list != null) {
            for (int i = 0; i<list.size();i++) {
                if(list.get(i).getNoteId().equals(noteToChange.getNoteId())){
                    list.set(i,noteToChange);
                    mutableNoteList.setValue(list);
                    return;
                }
            }
        }
    }

    private void removeNote(Note noteToRemove){
        List<Note> list = mutableNoteList.getValue();
        if (list != null) {
            for (int i = 0; i<list.size();i++) {
                if(list.get(i).getNoteId().equals(noteToRemove.getNoteId())){
                    list.remove(i);
                    mutableNoteList.setValue(list);
                    return ;
                }
            }
        }
    }

}
