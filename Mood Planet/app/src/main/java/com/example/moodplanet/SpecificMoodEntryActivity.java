package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecificMoodEntryActivity extends AppCompatActivity {
    TextView moodRate, description, date;
    Button editBtn, deleteBtn;
    ImageView moodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_mood_entry_activity);

        moodRate = findViewById(R.id.moodrate_text_view);
        description = findViewById(R.id.description_text_view);
        moodImage = findViewById(R.id.mood_image_view);
        editBtn = findViewById(R.id.editButton);
        deleteBtn = findViewById(R.id.deleteButton);

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