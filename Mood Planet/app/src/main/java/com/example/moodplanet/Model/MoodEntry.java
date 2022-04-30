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
    private String dayOfWeek;
    private String localDateTime;
    private String userID;
    private int moodRate;
    // to get key of mood entry
    private String key;

    public MoodEntry() {
    }

    public MoodEntry(String key, String chosenMood, String description, String userID, int moodRate, String localDateTime, String dayOfWeek) {
        this.chosenMood = chosenMood;
        this.dayOfWeek = dayOfWeek;
        this.description = description;
        this.localDateTime = localDateTime;
        this.moodRate = moodRate;
        this.userID = userID;
        this.key = key;

    }

    public int getMoodRate() {
        return moodRate;
    }

    public void setMoodRate(int moodRate) {
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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}