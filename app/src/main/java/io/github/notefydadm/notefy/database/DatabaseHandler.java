package io.github.notefydadm.notefy.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.notefydadm.notefy.model.Block;
import io.github.notefydadm.notefy.model.CheckBoxBlock;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.TextBlock;

import static io.github.notefydadm.notefy.model.Note.NoteState;

public class DatabaseHandler {

    public interface userGetNoteListListenerCallback{
        void onNoteAdded(Note note);
        void onNoteModified(Note note);
        void onNoteRemoved(Note note);
        void onFailureOnListener(Exception exception);
    }

    public interface addNoteToUserCallback{
        void onSuccessfulAdded();
        void onFailureAdded();

    }

    public interface removeNoteCallback{
        void onSuccessfulRemoved();
        void onFailureRemoved();
        void noteNotHaveId();

    }

    public interface createUserCallback {
        void onSuccessfulAdded();
        void onFailureAdded();
    }

    public interface shareNoteWithUserCallback{
        void onSuccessfulShared();
        void onFailureShared();
        void onUserToShareNotExists();
    }


    public static void createUser(String userId, String userName,final createUserCallback callback){
        Map<String, Object> noteMap = new HashMap<>();
        noteMap.put("user_name", userName);
        FirebaseFirestore.getInstance().collection("users").document(userId)
                .set(noteMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Add_Notes", "DocumentSnapshot successfully updated!");
                callback.onSuccessfulAdded();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Add_Notes", "Error updating document", e);
                callback.onFailureAdded();
            }
        });

    }
    
    public static void addNoteToUser(String userId, Note note, final addNoteToUserCallback callback) {
        Timestamp timestampCreationDate = new Timestamp(note.getCreationDate().getSecond(),note.getCreationDate().getNano());
        Timestamp timestampLastModifiedDate = new Timestamp(note.getLastModifiedDate().getSecond(),note.getLastModifiedDate().getNano());

        Map<String, Object> noteMap = new HashMap<>();
        noteMap.put("title", note.getTitle());
        noteMap.put("user_id", userId);
        noteMap.put("state", getStringFromState(note.getState()));
        noteMap.put("is_favourite", note.isFavorite());
        noteMap.put("colour", note.getColor());
        noteMap.put("creation_date", timestampCreationDate);
        noteMap.put("last_modified_date", timestampLastModifiedDate);
        noteMap.put("blocks",note.getBlocks());

        if (note.getNoteId() == null || note.getNoteId().isEmpty()) {
            FirebaseFirestore.getInstance().collection("notes")
                    .add(noteMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("Add_Notes", "DocumentSnapshot successfully written!");
                    callback.onSuccessfulAdded();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Add_Notes", "Error writing document", e);
                    callback.onFailureAdded();
                }
            });
        } else {
            FirebaseFirestore.getInstance().collection("notes").document(note.getNoteId())
                    .set(noteMap, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Add_Notes", "DocumentSnapshot successfully updated!");
                    callback.onSuccessfulAdded();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("Add_Notes", "Error updating document", e);
                    callback.onFailureAdded();
                }
            });
        }
    }

    public static void removeNote(Note note, final removeNoteCallback callback){
        if(!note.getNoteId().isEmpty()){
            FirebaseFirestore.getInstance().collection("notes").document(note.getNoteId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    callback.onSuccessfulRemoved();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailureRemoved();
                }
            });
        }else{
            callback.noteNotHaveId();
        }
    }

    public static void shareNoteWithUser(final Note note, final String userName, final shareNoteWithUserCallback callback){
        FirebaseFirestore.getInstance().collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean userFound = false;
                for(DocumentSnapshot documentSnapshot :queryDocumentSnapshots.getDocuments()){
                    if(documentSnapshot.get("user_name").equals(userName)){
                        userFound = true;
                        String userToShareId = documentSnapshot.getId();
                        FirebaseFirestore.getInstance().collection("notes").document(note.getNoteId())
                                .update("users_shared", FieldValue.arrayUnion(userToShareId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                callback.onSuccessfulShared();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callback.onFailureShared();
                            }
                        });
                    }
                }
                if(!userFound){
                    callback.onUserToShareNotExists();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailureShared();
            }
        });
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
        String title = snapshot.getString("title");
        String userId = snapshot.getString("user_id");
        NoteState state = getStateFromString(snapshot.getString("state"));
        boolean isFavourite = snapshot.getBoolean("is_favourite");
        int colour = Math.toIntExact(snapshot.getLong("colour"));
        LocalDateTime creationDate = LocalDateTime.ofEpochSecond(snapshot.getTimestamp("creation_date").getSeconds(),0, ZoneOffset.UTC);
        LocalDateTime lastModifiedDate = LocalDateTime.ofEpochSecond(snapshot.getTimestamp("last_modified_date").getSeconds(),0, ZoneOffset.UTC);
        ArrayList<HashMap<String, Object>> blocksArrayMap = (ArrayList<HashMap<String, Object>>) snapshot.get("blocks");
        ArrayList<Block> blocks = getBlocksFromArrayListHashMap(blocksArrayMap);

        Note item = new Note(id,title,userId,state,isFavourite,colour,creationDate,lastModifiedDate,blocks);
        return item;
    }

    private static NoteState getStateFromString(String stateInString){
        return NoteState.valueOf(stateInString);
    }

    private static String getStringFromState(NoteState state){
        return state.toString();
    }

    private static ArrayList<Block> getBlocksFromArrayListHashMap(ArrayList<HashMap<String, Object>> list){
        ArrayList<Block> blocks = new ArrayList<>();
        for(HashMap<String,Object> actMap: list){
            String fontFamily = (String) actMap.get("fontFamily");
            double fontSize = (double) actMap.get("fontSize");
            String text = (String) actMap.get("text");
            String textStyle = (String) actMap.get("textStyle");
            String content = (String) actMap.get("content");
            if(actMap.get("checked")==null){
                //Text typ
                TextBlock textBlock = new TextBlock(text,fontFamily,fontSize,textStyle);
                blocks.add(textBlock);
            }else{
                //Checkbox type
                boolean checked = (boolean) actMap.get("checked");
                CheckBoxBlock checkBoxBlock = new CheckBoxBlock(text,checked,fontFamily,fontSize,textStyle);
                blocks.add(checkBoxBlock);

            }
        }
        return blocks;
    }

}
