package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moodplanet.Model.JournalEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    EditText content;
    Button save;

    String journalContent;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    JournalEntry journalEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        content = findViewById(R.id.editJournalEditTv);
        save = findViewById(R.id.cancelEditJournalBtn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("journals");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    journalContent = content.getText().toString();
                    if (journalContent.length() == 0) {
                        content.setError("You forgot to write something...");
                        content.requestFocus();
                        return;
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
                    String dayOfWeek = new SimpleDateFormat("EEEE").format(new Date());


                    String currentTime = LocalDateTime.now().format(formatter);
                    journalEntry = new JournalEntry(currentTime, dayOfWeek, journalContent, MainActivity.userID);


                    databaseReference.push().setValue(journalEntry)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddJournalActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddJournalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    content.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } catch (Exception e) {
                    Toast.makeText(AddJournalActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}