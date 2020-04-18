package io.github.notefydadm.notefy.model;

import android.graphics.Color;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;

public class Note implements Parcelable {

    private String noteId;
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

    public Note() {
        state = NoteState.DRAFT;
        isFavorite = false;
        color = Color.WHITE;

        blocks = new ArrayList<>();

        // This requires minimum API 26
        LocalDateTime currentDateTime = LocalDateTime.now();
        creationDate = lastModifiedDate = currentDateTime;
    }

    public Note(String title, String userID) {
        this();
        this.title = title;
        this.userID = userID;
    }

    public Note(String noteId, String title, String userID, NoteState state, boolean isFavorite, @ColorInt int color, LocalDateTime creationDate, LocalDateTime lastModifiedDate, ArrayList<Block> blocks) {
        this.noteId = noteId;
        this.title = title;
        this.userID = userID;

        this.state = state;
        this.isFavorite = isFavorite;
        this.color = color;

        this.creationDate = creationDate;
        this.lastModifiedDate = lastModifiedDate;

        if (blocks == null) {
            this.blocks = new ArrayList<>();
        } else {
            this.blocks = blocks;
        }
    }

    public String getNoteId() {
        return noteId;
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

    public TextBlock getTitleBlock() {
        return new TextBlock(getTitle(), "sans-serif", 20, "bold");
    }

    public String getContent() {
        StringBuilder content = new StringBuilder();
        final Iterator<Block> blockIterator = this.blocks.iterator();
        while (blockIterator.hasNext()) {
            content.append(blockIterator.next().getContent());
            if (blockIterator.hasNext()) content.append('\n');
        }
        return content.toString();
    }

    public String getTitleAndContent() {
        return title == null ? getContent() : title + '\n' + getContent();
    }

    public void setContent(String text) {
        blocks.clear();
        addContent(text);
    }

    public void addContent(String text) {
        try (Scanner sc = new Scanner(text)) {
            StringBuilder builder = new StringBuilder();

            String firstLine = sc.nextLine();
            setTitle(firstLine);

            while (sc.hasNext()) {
                String line = sc.nextLine();

                if (CheckBoxBlock.matches(line)) {
                    if (builder.length() > 0) {
                        blocks.add(new TextBlock(builder.toString()));
                        builder = new StringBuilder();
                    }
                    blocks.add(CheckBoxBlock.fromLine(line));
                } else {
                    if (sc.hasNextLine() && builder.length() > 0) {
                        builder.append('\n');
                    }
                    builder.append(line);
                }
            }
            if (builder.length() > 0) {
                blocks.add(new TextBlock(builder.toString()));
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }

    // Parcelable
    protected Note(Parcel in) {
        title = in.readString();
        userID = in.readString();
        isFavorite = in.readByte() != 0;
        color = in.readInt();
        createdLocation = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(userID);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeInt(color);
        dest.writeParcelable(createdLocation, flags);
    }

}
