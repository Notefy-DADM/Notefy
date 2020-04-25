package io.github.notefydadm.notefy.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import io.github.notefydadm.notefy.model.Block;
import io.github.notefydadm.notefy.model.CheckBoxBlock;
import io.github.notefydadm.notefy.model.Note;
import io.github.notefydadm.notefy.model.TextBlock;
import io.github.notefydadm.notefy.model.User;

import static io.github.notefydadm.notefy.model.Note.NoteState;

public class DatabaseHandler {

    public abstract static class DatabaseListener<T> {
        public abstract void onSnapshot(T snapshot);
        public abstract void onFailureOnListener(Exception exception);
    }

    public abstract static class DatabaseListListener<T> {
        public abstract void onAdded(T item);
        public abstract void onModified(T item);
        public abstract void onRemoved(T item);
        public abstract void onFailureOnListener(Exception exception);
    }

    static <T> DatabaseListListener<T> getListListenerCallback(final MutableLiveData<List<T>> list) {
        return new DatabaseListListener<T>() {
            @Override
            public void onAdded(T item) {
                changeOrAddItem(list, item);
            }

            @Override
            public void onModified(T item) {
                changeOrAddItem(list, item);
            }

            @Override
            public void onRemoved(T item) {
                removeItem(list, item);
            }

            @Override
            public void onFailureOnListener(Exception exception) {

            }
        };
    }

    private static <T> void changeOrAddItem(MutableLiveData<List<T>> listData, T item) {
        List<T> list = listData.getValue();
        if (list != null) {
            final int index = list.indexOf(item);
            if (index < 0) { // Item was not in the list
                list.add(item);
            } else {
                list.set(index, item);
            }
            listData.setValue(list);
        }
    }

    private static <T> void removeItem(MutableLiveData<List<T>> listData, T itemToRemove) {
        List<T> list = listData.getValue();
        if (list != null) {
            boolean removed = list.remove(itemToRemove);
            if (removed) listData.setValue(list);
        }
    }

    public interface AddNoteToUserCallback {
        void onSuccessfulAdded();
        void onFailureAdded();
    }

    public interface RemoveNoteCallback {
        void onSuccessfulRemoved();
        void onFailureRemoved();
        void noteNotHaveId();

    }

    public interface CreateUserCallback {
        void onSuccessfulAdded();
        void onFailureAdded();
    }

    public interface UpdateSharedListNoteCallback {
        void onSuccessfulShared();
        void onFailureShared();
    }


    public static void createUser(String userId, String userName,final CreateUserCallback callback){
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
    
    public static void addNoteToUser(Note note, final AddNoteToUserCallback callback) {
        String userId = note.getUserID();

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

    public static void removeNote(Note note, final RemoveNoteCallback callback){
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

    public static void getUser(final String username, final Consumer<User> callback) {
        FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("user_name", username).limit(1).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot result) {
                    if (result != null) {
                        if (result.isEmpty()) {
                            callback.accept(null);
                        } else {
                            DocumentSnapshot userDocument = result.getDocuments().get(0);
                            callback.accept(getUserFromSnapshot(userDocument));
                        }
                    }
                }
            });
    }

    @Nullable
    public static User getCurrentUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return firebaseUser == null
            ? null
            : new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
    }

    public static void getUsersFromNames(final List<String> userNames, final Consumer<List<User>> callback) {
        FirebaseFirestore.getInstance().collection("users")
            .whereIn("user_name", userNames).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot result) {
                    List<User> users = new ArrayList<>();
                    for (DocumentSnapshot snapshot : result.getDocuments()) {
                        users.add(getUserFromSnapshot(snapshot));
                    }
                    callback.accept(users);
                }
            });
    }

    public static void getUsersFromIds(final HashSet<String> userIds, final Consumer<List<User>> callback) {
        FirebaseFirestore.getInstance().collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot result) {
                        List<User> users = new ArrayList<>();
                        for (DocumentSnapshot snapshot : result.getDocuments()) {
                            if (userIds.contains(snapshot.getId())) {
                                users.add(getUserFromSnapshot(snapshot));
                            }
                        }
                        callback.accept(users);
                    }
                });
    }

    private static User getUserFromSnapshot(DocumentSnapshot snapshot) {
        return new User(snapshot.getId(), snapshot.getString("user_name"));
    }

    public static void updateSharedListNote(final Note note, final List<User> users, final UpdateSharedListNoteCallback callback) {
        List<String> userIds = new ArrayList<>();
        for (User user : users) {
            userIds.add(user.getUserId());
        }
        FirebaseFirestore.getInstance().collection("notes").document(note.getNoteId())
        .update("users_shared", userIds)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public static void getSharedListNoteListener(final DatabaseListener<List<User>> callback, Note note) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("notes").document(note.getNoteId());
        addDocumentListener(reference, new BiConsumer<DocumentSnapshot, Consumer<List<User>>>() {
            @Override
            public void accept(DocumentSnapshot documentSnapshot, Consumer<List<User>> callback) {
                List<String> usersShared = (List<String>) documentSnapshot.get("users_shared");
                if (usersShared != null) {
                    getUsersFromIds(new HashSet<>(usersShared), callback);
                }
            }
        }, callback);
    }

    public static void userGetNoteListListener(final DatabaseListListener<Note> callback, String userId){
        Query query = FirebaseFirestore.getInstance().collection("notes").whereEqualTo("user_id", userId);
        addNoteListListener(query, callback);
    }

    public static void sharedWithUserGetNoteListListener(final DatabaseListListener<Note> callback, String userId) {
        Query query = FirebaseFirestore.getInstance().collection("notes").whereArrayContains("users_shared", userId);
        addNoteListListener(query, callback);
    }

    private static void addNoteListListener(Query query, final DatabaseListListener<Note> callback) {
        DatabaseHandler.addListListener(query, new BiConsumer<QueryDocumentSnapshot, Consumer<Note>>() {
            @Override
            public void accept(QueryDocumentSnapshot snapshot, Consumer<Note> callback) {
                callback.accept(getNoteFromQueryDocumentSnapshot(snapshot));
            }
        }, callback);
    }

    private static <T> void addDocumentListener(DocumentReference reference, final BiConsumer<DocumentSnapshot, Consumer<T>> converter, final DatabaseListener<T> callback) {
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    if (callback != null) callback.onFailureOnListener(e);
                } else if (snapshot != null) {
                    try {
                        converter.accept(snapshot, new Consumer<T>() {
                            @Override
                            public void accept(T item) {
                                if (callback != null) callback.onSnapshot(item);
                            }
                        });
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        });
    }

    private static <T> void addListListener(Query query, final BiConsumer<QueryDocumentSnapshot, Consumer<T>> converter, final DatabaseListListener<T> callback) {
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    callback.onFailureOnListener(e);
                } else if (snapshots != null) {
                    for (final DocumentChange dc : snapshots.getDocumentChanges()) {
                        try {
                            converter.accept(dc.getDocument(), new Consumer<T>() {
                                @Override
                                public void accept(T item) {
                                    switch (dc.getType()) {
                                        case ADDED:
                                            callback.onAdded(item);
                                            break;
                                        case MODIFIED:
                                            callback.onModified(item);
                                            break;
                                        case REMOVED:
                                            callback.onRemoved(item);
                                            break;
                                    }
                                }
                            });
                        } catch (IllegalArgumentException ignored) {
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
