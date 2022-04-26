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
    private LocalDateTime localDateTime;
    private String userID;
    private int moodRate;

    public MoodEntry() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MoodEntry(String chosenMood, String description, String userID, int moodRate) {
        this.chosenMood = chosenMood;
        this.description = description;
        this.localDateTime = LocalDateTime.now();
        this.userID = userID;
        this.moodRate = moodRate;
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
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getMoodRate() {
        return moodRate;
    }

    public void setMoodRate(int moodRate) {
        this.moodRate = moodRate;
    }
}