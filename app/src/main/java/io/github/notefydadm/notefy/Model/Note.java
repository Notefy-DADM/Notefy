package io.github.notefydadm.notefy.Model;

import android.graphics.Color;
import android.location.Location;

import androidx.annotation.ColorInt;

import java.time.LocalDateTime;

public class Note {
    private String title;

    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;

    private String userID;

    private boolean isFavorite;

    @ColorInt
    private int color;

    private NoteState state;

    public enum NoteState {DRAFT, PUBLISHED, HIDDEN, ARCHIVED, DELETED}

    // TODO: Add location system
    private Location createdLocation;

    public Note(String title, String userID) {
        this.title = title;
        this.userID = userID;

        state = NoteState.DRAFT;
        isFavorite = false;
        color = Color.WHITE;

        // This requires minimum API 26
        LocalDateTime currentDateTime = LocalDateTime.now();
        creationDate = lastModifiedDate = currentDateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getUserID() {
        return userID;
    }

    public NoteState getState() {
        return state;
    }

    public Location getCreatedLocation() {
        return createdLocation;
    }
}
