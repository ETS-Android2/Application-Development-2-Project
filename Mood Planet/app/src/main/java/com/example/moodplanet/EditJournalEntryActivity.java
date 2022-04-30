package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moodplanet.Model.JournalEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditJournalEntryActivity extends AppCompatActivity {

    EditText editContentEditTv;
    Button update, cancel;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    JournalEntry journalEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_journal_entry);

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        String time = intent.getStringExtra("time");
        String dayOfWeek = intent.getStringExtra("dayOfWeek");
        String key = intent.getStringExtra("key");

        editContentEditTv = findViewById(R.id.editJournalEditTv);

        editContentEditTv.setText(content);

        update = findViewById(R.id.updateJournalBtn);
        cancel = findViewById(R.id.cancelEditJournalBtn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("journals");
        Query query = databaseReference.orderByKey().equalTo(key);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String journalContent = editContentEditTv.getText().toString();
                        journalEntry = new JournalEntry(time, dayOfWeek, journalContent, MainActivity.userID);

                        databaseReference.push().setValue(journalEntry);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}