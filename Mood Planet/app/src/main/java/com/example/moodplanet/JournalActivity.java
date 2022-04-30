package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.moodplanet.Model.JournalEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Journal MainActivty
 */
public class JournalActivity extends AppCompatActivity implements JournalRecyclerViewAdapter.OnJournalListener {

    ImageButton add;
    JournalRecyclerViewAdapter recyclerViewAdapter;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<JournalEntry> journalEntryList;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        add = findViewById(R.id.addJournalIB);

        recyclerView = findViewById(R.id.journalRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("journals");
        Query query = databaseReference.orderByChild("userID").equalTo(MainActivity.userID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        journalEntryList = new ArrayList<>();
        recyclerViewAdapter = new JournalRecyclerViewAdapter(this, journalEntryList, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|  ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
//                Student std = studentList.get(position);
//                String id = std.getId();
//                myDb.deleteData(id);
//                studentList.remove(position);
//                adapter.notifyDataSetChanged();
                journalEntryList.get(position);


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                journalEntryList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JournalEntry journalEntry = dataSnapshot.getValue(JournalEntry.class);
                    // get key of the current entry
                    String key = dataSnapshot.getKey();
                    journalEntry.setKey(key);
                    journalEntryList.add(journalEntry);
                }

                recyclerViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JournalActivity.this, AddJournalActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onJournalClick(int position) {
        journalEntryList.get(position);
        Intent intent = new Intent(this, EditJournalEntryActivity.class);
        intent.putExtra("content", journalEntryList.get(position).getContent());
        intent.putExtra("position", position);
        intent.putExtra("time", journalEntryList.get(position).getLocalDateTime());
        intent.putExtra("dayOfWeek", journalEntryList.get(position).getDayOfWeek());
        intent.putExtra("key", journalEntryList.get(position).getKey());
        startActivity(intent);
    }


}