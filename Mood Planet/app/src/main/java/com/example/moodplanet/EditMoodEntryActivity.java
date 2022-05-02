package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodplanet.Model.MoodEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMoodEntryActivity extends AppCompatActivity {
    TextView moodRate, dateTime;
    Button update;
    EditText description;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SeekBar moodSeekBar;
    ImageView moodImage;

    MoodEntry moodEntry;
    int progressRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood_entry);

        // For the intent object
        if (getIntent().getExtras() != null) {
            moodEntry = (MoodEntry) getIntent().getSerializableExtra("entry");
//            editIntent = new Intent(.this, EditMoodEntryActivity.class);
        }

        description = findViewById(R.id.edit_descriptionEditText);
        moodRate = findViewById(R.id.edit_moodrate_text_view);
        dateTime = findViewById(R.id.edit_date_time_textview);
        moodImage = findViewById(R.id.edit_moodimageview);
        moodSeekBar = findViewById(R.id.edit_moodSeekBar);
        update = findViewById(R.id.updateButton);

        //firebase setup
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Mood_Entries");

        description.setText(moodEntry.getDescription());
        moodSeekBar.setProgress(moodEntry.getMoodRate());  // current
        moodRate.setText("Mood Rate(1-5): " + String.valueOf(moodEntry.getMoodRate()));
        dateTime.setText(moodEntry.getDayOfWeek() + " " + moodEntry.getLocalDateTime());

        // for mood image
        switch (moodEntry.getChosenMood()) {
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


            // mood rate seekbar functionality
        moodSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                moodRate.setText("Mood Rate(1-5): " + String.valueOf(progress)) ;
                progressRate = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // update button functionality
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content = description.getText().toString();
                MoodEntry updateEntry = new MoodEntry(moodEntry.getKey(), moodEntry.getChosenMood()
                                , content, moodEntry.getUserID(), progressRate, moodEntry.getLocalDateTime(), moodEntry.getDayOfWeek());
                databaseReference.child(moodEntry.getKey()).setValue(updateEntry)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Mood Entry Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditMoodEntryActivity.this, HomeActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Mood Entry Update Failed", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
        });

    }
}