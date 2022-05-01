package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.moodplanet.Model.MoodEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class AddEntryActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton sad, happy, calm, inlove, sleepy, cheerful, scared, optimistic, pensive, angry;
    MoodEntry moodEntry;
    Intent editIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        moodEntry = null;
        // For the intent object
        if (getIntent().getExtras() != null) {
            moodEntry = (MoodEntry) getIntent().getSerializableExtra("entry");
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
        Intent intent = new Intent(getApplicationContext(), MoodOfTheDayActivity.class);
        String key = "mood";

        switch (view.getId()) {
            case R.id.sadIB:
                intent.putExtra(key, "sad");
                moodEntry.setChosenMood("sad");
                break;
            case R.id.heheIB:
                intent.putExtra(key, "happy");
                moodEntry.setChosenMood("happy");
                break;
            case R.id.zzzIB:
                intent.putExtra(key, "sleepy");
                moodEntry.setChosenMood("sleepy");
                break;
            case R.id.calmIB:
                intent.putExtra(key, "calm");
                moodEntry.setChosenMood("calm");
                break;
            case R.id.ohnoIB:
                intent.putExtra(key, "scared");
                moodEntry.setChosenMood("scared");
                break;
            case R.id.loveIB:
                intent.putExtra(key, "inlove");
                moodEntry.setChosenMood("inlove");
                break;
            case R.id.yayIB:
                intent.putExtra(key, "cheerful");
                moodEntry.setChosenMood("cheerful");
                break;
            case R.id.okIB:
                intent.putExtra(key, "optimistic");
                moodEntry.setChosenMood("optimistic");
                break;
            case R.id.hmmIB:
                intent.putExtra(key, "pensive");
                moodEntry.setChosenMood("pensive");
                break;
            default:
                intent.putExtra(key, "angry");
                moodEntry.setChosenMood("angry");
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