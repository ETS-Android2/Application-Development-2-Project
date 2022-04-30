package com.example.moodplanet.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

/**
 * Created by Chilka Castro on 4/25/2022.
 */
public class MoodEntry {
    private String chosenMood;
    private String description;
    private String localDateTime;
    private String userID;
    // to get key of mood entry
    String key;

    public MoodEntry() {
    }

    public MoodEntry(String chosenMood, String description, String userID, String localDateTime) {
        this.chosenMood = chosenMood;
        this.description = description;
        this.localDateTime = localDateTime;
        this.userID = userID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getChosenMood() {
        return chosenMood;
    }

    public void setChosenMood(String chosenMood) {
        this.chosenMood = chosenMood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // need to format this to have pretty date and time
    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}