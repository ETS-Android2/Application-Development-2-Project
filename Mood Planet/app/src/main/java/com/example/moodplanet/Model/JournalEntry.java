package com.example.moodplanet.Model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * Created by Chilka Castro on 4/25/2022.
 */
public class JournalEntry {
//    private LocalDateTime localDateTime;
//    private Timestamp timestamp;
    private String localDateTime;
    private String content;
    private String userID;

    public JournalEntry() {
    }

    public JournalEntry(String localDateTime, String content, String userID) {
        this.localDateTime = localDateTime;
        this.content = content;
        this.userID = userID;
    }

    public String getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    // get day of week and date
//    public String getDate() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, ''yy");
//        return simpleDateFormat.format(localDateTime);
//    }
//
//    public LocalDateTime getLocalDateTime() {
//        return localDateTime;
//    }
//
//    public void setLocalDateTime(LocalDateTime localDateTime) {
//        this.localDateTime = localDateTime;
//    }


}