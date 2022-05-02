package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moodplanet.Model.MoodEntry;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class SpecificMoodEntryActivity extends AppCompatActivity implements Serializable {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView moodRate, description, moodDate;
    Button editBtn, deleteBtn;
    ImageView moodImage;
    MoodEntry entry;
    Snackbar snack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_mood_entry_activity);

        // database setup
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Mood_Entries");

        // connect xml views
        entry = null;
        moodRate = findViewById(R.id.moodrate_text_view);
        description = findViewById(R.id.descriptionContentTV);
        moodImage = findViewById(R.id.edit_moodimageview);
        editBtn = findViewById(R.id.editButton);
        deleteBtn = findViewById(R.id.deleteButton);
        moodDate = findViewById(R.id.edit_date_time_textview);

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
                Intent intent = new Intent(SpecificMoodEntryActivity.this,
                        AddEntryActivity.class);
                intent.putExtra("entryKey", entry);
                startActivity(intent);
            }
        });

        // delete functionality
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference.child(entry.getKey()).removeValue();
                snack = Snackbar.make(view, "Mood entry deleted",
                        Snackbar.LENGTH_INDEFINITE);

                snack.setAction("Close", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        snack.dismiss();
                    }
                }).setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark)).show();         //
            }

        });
    }
}