package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Journal MainActivty
 */
public class JournalActivity extends AppCompatActivity {

    ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        add = findViewById(R.id.addJournalIB);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JournalActivity.this, AddJournalActivity.class);
                startActivity(intent);
            }
        });
    }
}