package com.example.moodplanet.Model;

import java.time.LocalDateTime;

/**
 * Created by Chilka Castro on 4/25/2022.
 */
public class JournalEntry {
    private LocalDateTime localDateTime;
    private String description;
    private String userID;

    public JournalEntry() {
    }

    public JournalEntry(String description, String userID) {
        this.localDateTime = localDateTime;
        this.description = description;
        this.userID = userID;
    }

    // need to format this to have pretty date and time
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}