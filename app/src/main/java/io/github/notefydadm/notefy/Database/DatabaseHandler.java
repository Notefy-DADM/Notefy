package io.github.notefydadm.notefy.Database;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import io.github.notefydadm.notefy.Model.Note;

public class DatabaseHandler {


    public static ArrayList<Note> getUserNotes(String userId) {
        FirebaseFirestore.getInstance().collection("notes").whereEqualTo("user_id",userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<Note> noteList = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Note item = getNoteFromQueryDocumentSnapshot(snapshot);
                                noteList.add(item);
                            }
                            //callBack.onGettingList(list);
                        }else{
                            //callBack.onGettingEmptyList();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //callBack.onFailureOnGetting(e);
            }
        });
        return null;
    }

    private static Note getNoteFromQueryDocumentSnapshot (QueryDocumentSnapshot snapshot){
        String id = snapshot.getId();
        String tittle = snapshot.getString("tittle");
        LocalDateTime creationDate = LocalDateTime.ofEpochSecond(snapshot.getTimestamp("creation_date").getSeconds(),0, ZoneOffset.UTC);
        LocalDateTime lastModifiedDate = LocalDateTime.ofEpochSecond(snapshot.getTimestamp("last_modified_date").getSeconds(),0, ZoneOffset.UTC);
        String userId = snapshot.getString("user_id");
        boolean isFavourite = snapshot.getBoolean("is_favourite");
        int colour = Math.toIntExact(snapshot.getLong("colour"));

        Note item = new Note(tittle,userId);
        return item;
    }

}
