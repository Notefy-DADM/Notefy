package io.github.notefydadm.notefy.Model;

import android.location.Location;

import java.time.LocalDateTime;

public class Reminder extends Note {
    private LocalDateTime reminderDate;

    // TODO: Add location system for reminders
    private Location reminderLocation;

    public Reminder(String title, String userID, LocalDateTime reminderDate) {
        super(title, userID);
        this.reminderDate = reminderDate;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    public Location getReminderLocation() {
        return reminderLocation;
    }

    public void setReminderLocation(Location reminderLocation) {
        this.reminderLocation = reminderLocation;
    }
}
