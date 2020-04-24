package io.github.notefydadm.notefy.database;


import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.User;

public class SingletonDatabase {

    private static SingletonDatabase instance;
    private MutableLiveData<List<Note>> myNotes, sharedWithMeNotes;


    private SingletonDatabase() {
        myNotes = new MutableLiveData<List<Note>>(new ArrayList<Note>());
        sharedWithMeNotes = new MutableLiveData<List<Note>>(new ArrayList<Note>());
    }

    public static SingletonDatabase getInstance() {
        if (instance == null) {
            instance = new SingletonDatabase();
        }
        return instance;
    }

    public void init() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String userUid = user.getUid();
            DatabaseHandler.userGetNoteListListener(DatabaseHandler.getNoteListListenerCallback(myNotes), userUid);
            DatabaseHandler.sharedWithUserGetNoteListListener(DatabaseHandler.getNoteListListenerCallback(sharedWithMeNotes), userUid);
        }
    }

    private static User getUserFromFirebase() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //User user = new user()
        return null; // TODO: Get User object from FirebaseUser
    }

    public MutableLiveData<List<Note>> getMyNotes(){
        return myNotes;
    }
    public MutableLiveData<List<Note>> getSharedWithMeNotes(){
        return sharedWithMeNotes;
    }

    private void addNoteToMutableList(Note noteToAdd){
        List<Note> list = myNotes.getValue();
        if (list != null) {
            list.add(noteToAdd);
        }
        myNotes.setValue(list);
    }
}
