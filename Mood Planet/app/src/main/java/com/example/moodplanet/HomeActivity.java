package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.moodplanet.Model.MoodEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    RecyclerView moodRecyclerView;
    DatabaseReference databaseReference;
    MoodRecyclerViewAdapter moodRecyclerViewAdapter;
    List<MoodEntry> moodEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        moodEntries = new ArrayList<>();
        moodRecyclerView = findViewById(R.id.moodRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Mood_Entries");
        moodRecyclerViewAdapter = new MoodRecyclerViewAdapter(this, moodEntries);
        moodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = databaseReference.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                moodEntries.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MoodEntry moodEntry = dataSnapshot.getValue(MoodEntry.class);
                    moodEntries.add(moodEntry);
                }
                moodRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}