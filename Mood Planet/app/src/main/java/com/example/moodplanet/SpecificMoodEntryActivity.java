package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moodplanet.Model.MoodEntry;

import java.io.Serializable;

public class SpecificMoodEntryActivity extends AppCompatActivity implements Serializable {
    TextView moodRate, description, moodDate;
    Button editBtn, deleteBtn;
    ImageView moodImage;
    MoodEntry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_mood_entry_activity);
        entry = null;
        // connect xml views
        moodRate = findViewById(R.id.moodrate_text_view);
        description = findViewById(R.id.description_text_view);
        moodImage = findViewById(R.id.mood_image_view);
        editBtn = findViewById(R.id.editButton);
        deleteBtn = findViewById(R.id.deleteButton);
        moodDate = findViewById(R.id.date_time_textview);

        // For the intent object
        if (getIntent().getExtras() != null) {
            entry = (MoodEntry) getIntent().getSerializableExtra("entry");
        }

        moodRate.setText("Mood Rate(1-5): " + String.valueOf(entry.getMoodRate()));
        description.setText(entry.getDescription());
        moodDate.setText(entry.getDayOfWeek() + " " + entry.getLocalDateTime());
        // for mood image
        switch (entry.getChosenMood()) {
            case "sad":
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

            default:
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.angry));
                break;

        }
        // edit functionality
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // delete functionality
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}