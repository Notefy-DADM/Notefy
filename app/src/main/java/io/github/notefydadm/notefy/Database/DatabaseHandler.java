package io.github.notefydadm.notefy.Database;

import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.notefydadm.notefy.model.Note;

import static io.github.notefydadm.notefy.model.Note.*;
import static io.github.notefydadm.notefy.model.Note.NoteState.*;

public class DatabaseHandler {

    public interface userGetNoteListListenerCallback{
        void onNoteAdded(Note note);
        void onNoteModified(Note note);
        void onNoteRemoved(Note note);
        void onFailureOnListener(Exception exception);
    }
    
    public static void addNoteToUser(String userId, Note note){

        Timestamp timestampCreationDate = new Timestamp(note.getCreationDate().getSecond(),note.getCreationDate().getNano());
        Timestamp timestampLastModifiedDate = new Timestamp(note.getLastModifiedDate().getSecond(),note.getLastModifiedDate().getNano());

        Map<String, Object> noteMap = new HashMap<>();
        noteMap.put("tittle", note.getTitle());
        noteMap.put("userId", userId);
        noteMap.put("state", getStringFromState(note.getState()));
        noteMap.put("is_favourite", note.isFavorite());
        noteMap.put("colour", note.getColor());
        noteMap.put("creation_date", timestampCreationDate);
        noteMap.put("last_modified_date", timestampLastModifiedDate);



        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("notes")
                .add(noteMap).getResult();
        /*
        addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("Add_Notes", "DocumentSnapshot successfully written!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Add_Notes", "Error writing document", e);

            }
        });

         */
    }

    public static List<Note> getUserNotes(String userId) {
        QuerySnapshot queryDocumentSnapshots = FirebaseFirestore.getInstance().collection("notes").whereEqualTo("user_id",userId)
                .get().getResult();
        if(queryDocumentSnapshots!=null) {
            if (!queryDocumentSnapshots.isEmpty()) {
                List<Note> noteList = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Note item = getNoteFromQueryDocumentSnapshot(snapshot);
                    noteList.add(item);
                }
                return noteList;
            } else {
                return null;
            }

        }else{
            return null;
        }

    }

    public static void userGetNoteListListener(final userGetNoteListListenerCallback callback, String userId){
        FirebaseFirestore.getInstance().collection("notes").whereEqualTo("user_id",userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            callback.onFailureOnListener(e);
                            return;
                        }else{
                            for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                Note note = getNoteFromQueryDocumentSnapshot(dc.getDocument());
                                switch (dc.getType()) {
                                    case ADDED:
                                        callback.onNoteAdded(note);
                                        break;
                                    case MODIFIED:
                                        callback.onNoteModified(note);
                                        break;
                                    case REMOVED:
                                        callback.onNoteRemoved(note);
                                        break;
                                }
                            }

                        }

                    }
                });
    }



    private static Note getNoteFromQueryDocumentSnapshot (QueryDocumentSnapshot snapshot){
        String id = snapshot.getId();
        String tittle = snapshot.getString("tittle");
        String userId = snapshot.getString("user_id");
        NoteState state = getStateFromString(snapshot.getString("state"));
        boolean isFavourite = snapshot.getBoolean("is_favourite");
        int colour = Math.toIntExact(snapshot.getLong("colour"));
        LocalDateTime creationDate = LocalDateTime.ofEpochSecond(snapshot.getTimestamp("creation_date").getSeconds(),0, ZoneOffset.UTC);
        LocalDateTime lastModifiedDate = LocalDateTime.ofEpochSecond(snapshot.getTimestamp("last_modified_date").getSeconds(),0, ZoneOffset.UTC);


        Note item = new Note(id,tittle,userId,state,isFavourite,colour,creationDate,lastModifiedDate,null);
        return item;
    }

    private static NoteState getStateFromString(String stateInString){
        switch (stateInString){
            case("DRAFT"):
                return DRAFT;
            case("PUBLISHED"):
                return PUBLISHED;
            case("HIDDEN"):
                return HIDDEN;
            case("ARCHIVED"):
                return ARCHIVED;
            case("DELETED"):
                return DELETED;
            default:
                return null;

        }

    }

    private static String getStringFromState(NoteState state){
        switch (state){
            case DRAFT:
                return "DRAFT";
            case PUBLISHED:
                return "PUBLISHED";
            case HIDDEN:
                return "HIDDEN";
            case ARCHIVED:
                return "ARCHIVED";
            case DELETED:
                return "DELETED";
            default:
                return "";

        }

    }

}
