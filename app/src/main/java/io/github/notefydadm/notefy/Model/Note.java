package io.github.notefydadm.notefy.Model;

import android.graphics.Color;
import android.location.Location;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Note {
    private String title;

    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;

    private String userID;

    private ArrayList<Block> blocks;

    private boolean isFavorite;

    @ColorInt
    private int color;

    private NoteState state;

    // TODO: Add location system for notes
    private Location createdLocation;

    public Note(String title, String userID) {
        this.title = title;
        this.userID = userID;

        state = NoteState.DRAFT;
        isFavorite = false;
        color = Color.WHITE;

        blocks = new ArrayList<>();

        // This requires minimum API 26
        LocalDateTime currentDateTime = LocalDateTime.now();
        creationDate = lastModifiedDate = currentDateTime;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
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

    public enum NoteState {DRAFT, PUBLISHED, HIDDEN, ARCHIVED, DELETED}

    public String getContent() {
        StringBuilder content = new StringBuilder();
        for (Block block : this.blocks) {
            content.append(block.getContent());
        }
        return content.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }
}
