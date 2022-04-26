package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MoodOfTheDayActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String chosenMood;
    private
    ImageView moodImage;
    Button saveButton;
    EditText description;
    SeekBar moodRateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_of_the_day);

        moodImage = findViewById(R.id.chosenMoodImage);
        chosenMood = getIntent().getStringExtra("mood");

        switch (chosenMood) {
            case "sad" :
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.sad));
                break;

            case "happy":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.hehe));
                break;

            case "sleepy":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.zzz));
                break;

            case "calm":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.calm));
                break;

            case "scared":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.ohno));
                break;

            case "inlove":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.love));
                break;

            case "cheerful":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.yay));
                break;

            case "optimistic":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.ok));
                break;

            case "pensive":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.hmm));
                break;

            default :
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.angry));
                break;
        }
    }
}