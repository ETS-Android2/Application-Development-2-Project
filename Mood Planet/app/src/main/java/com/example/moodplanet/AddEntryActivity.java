package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.moodplanet.Model.MoodEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class AddEntryActivity extends AppCompatActivity implements View.OnClickListener, Serializable {
    ImageButton sad, happy, calm, inlove, sleepy, cheerful, scared, optimistic, pensive, angry;
    MoodEntry moodEntry;
    Intent editIntent, intent;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        mToolbar = findViewById(R.id.addeTb);
        mToolbar.setTitle("Add Entry");
        // toolbar depended on theme color
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(selectedColor);
        getWindow().setStatusBarColor(selectedColor);

        moodEntry = null;
        // For the intent object
        if (getIntent().getExtras() != null) {
            moodEntry = (MoodEntry) getIntent().getSerializableExtra("entryKey");
            editIntent = new Intent (AddEntryActivity.this, EditMoodEntryActivity.class);
        }

        sad = findViewById(R.id.sadIB);
        sad.setOnClickListener(this);

        happy = findViewById(R.id.heheIB);
        happy.setOnClickListener(this);

        sleepy = findViewById(R.id.zzzIB);
        sleepy.setOnClickListener(this);

        calm = findViewById(R.id.calmIB);
        calm.setOnClickListener(this);

        inlove = findViewById(R.id.loveIB);
        inlove.setOnClickListener(this);

        cheerful = findViewById(R.id.yayIB);
        cheerful.setOnClickListener(this);

        scared = findViewById(R.id.ohnoIB);
        scared.setOnClickListener(this);

        optimistic = findViewById(R.id.okIB);
        optimistic.setOnClickListener(this);

        pensive = findViewById(R.id.hmmIB);
        pensive.setOnClickListener(this);

        angry = findViewById(R.id.angryB);
        angry.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        intent = new Intent(getApplicationContext(), MoodOfTheDayActivity.class);
        String key = "mood";

        switch (view.getId()) {
            case R.id.sadIB:
                intent.putExtra(key, "sad");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("sad");
                }
                break;
            case R.id.heheIB:
                intent.putExtra(key, "happy");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("happy");
                }
                break;
            case R.id.zzzIB:
                intent.putExtra(key, "sleepy");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("sleepy");
                }
                break;
            case R.id.calmIB:
                intent.putExtra(key, "calm");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("calm");
                }
                break;
            case R.id.ohnoIB:
                intent.putExtra(key, "scared");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("scared");
                }
                break;
            case R.id.loveIB:
                intent.putExtra(key, "inlove");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("inlove");
                }
                break;
            case R.id.yayIB:
                intent.putExtra(key, "cheerful");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("cheerful");
                }
                break;
            case R.id.okIB:
                intent.putExtra(key, "optimistic");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("optimistic");
                }
                break;
            case R.id.hmmIB:
                intent.putExtra(key, "pensive");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("pensive");
                }
                break;
            default:
                intent.putExtra(key, "angry");
                if (moodEntry != null) {
                    moodEntry.setChosenMood("angry");
                }
        }

        if (moodEntry == null) {
            startActivity(intent);
        }
        else {
            editIntent.putExtra("entry", moodEntry);
            startActivity(editIntent); // will go to edit page
        }
    }
}