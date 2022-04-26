package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moodplanet.Model.JournalEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
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

        content = findViewById(R.id.contentEdittv);
        save = findViewById(R.id.saveJournalBtn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("journals");

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    journalContent = content.getText().toString();
                    String currentTime = LocalDateTime.now().toString();
                    journalEntry = new JournalEntry(currentTime, journalContent, MainActivity.userID);
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            databaseReference.push().setValue(journalEntry);
//                            Toast.makeText(AddJournalActivity.this, "inserted", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Toast.makeText(AddJournalActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
//                        }
//                    });

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
                } catch (Exception e) {
                    Toast.makeText(AddJournalActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}