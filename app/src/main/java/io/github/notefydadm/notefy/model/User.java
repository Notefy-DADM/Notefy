package io.github.notefydadm.notefy.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class User {
    private String userId;
    private String userName;
    private String email;

    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public User(String userId, String userName, String email) {
        this(userId, userName);
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return getUserName();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof User && userId.equals(((User) obj).userId);
    }
}
