package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.moodplanet.Model.JournalEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

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
    FirebaseDatabase firebaseDatabase;
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

        /**
         * this will fetch all the data from firebase
         */
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear the previous list whenever the view is being called again
                // avoid add-up from previous list
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

        /**
         * add journal button
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JournalActivity.this, AddJournalActivity.class);
                startActivity(intent);
            }
        });

        /**
         * itemTouchHelper is for swiping either left or right to delete (u can set it in the param)
         * when the user swipe either left or right, there will be a dialog appears asking are you sure
         */
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|  ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                JournalEntry journalEntry = journalEntryList.get(position);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            /**
                             * if yes, implement the code to delete the specific data here
                             */
                            case DialogInterface.BUTTON_POSITIVE:

                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference("journals");
                                String key = journalEntry.getKey();
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        databaseReference.child(key).removeValue();
                                        journalEntryList.remove(position);
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("remove error", "onCancelled", error.toException());
                                    }
                                });

                                /**
                                 * Undo snackbar
                                 */
                                Snackbar.make(recyclerView,"journal on " + journalEntry.getDayOfWeek(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        databaseReference.push().setValue(journalEntry)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(JournalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        journalEntryList.clear();
                                        recyclerViewAdapter.notifyDataSetChanged();
                                    }
                                }).show();
                                break;

                            /**
                             * if no, first, notify the recyclerview again to fetch the data again
                             * cuz after user swipe, the row will disappear even when user clicks no
                             */
                            case DialogInterface.BUTTON_NEGATIVE:
                                recyclerViewAdapter.notifyDataSetChanged();
                                dialog.cancel();
                                break;
                        }
                    }
                };

                // build the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(JournalActivity.this);
                builder.setTitle("Delete Journal Entry")
                        .setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        };

        // link the itemtouchelper to the recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }



    /**
     * implements this method here
     * sending all the needed data to edit journal view
     * @param position
     */
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