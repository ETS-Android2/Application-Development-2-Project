package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodplanet.Model.JournalEntry;
import com.example.moodplanet.Model.MoodEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements MoodRecyclerViewAdapter.MoodEntriesOnClickListener, Serializable {
    RecyclerView moodRecyclerView;
    DatabaseReference databaseReference;
    MoodRecyclerViewAdapter moodRecyclerViewAdapter;
    public static List<MoodEntry> moodEntries;
    Toolbar mToolbar;
    long count =  0;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = findViewById(R.id.hometoolbar);
        mToolbar.setTitle("My Moods");

        textView = findViewById(R.id.textView8);
        // toolbar depended on theme color
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(selectedColor);
        getWindow().setStatusBarColor(selectedColor);

        moodEntries = new ArrayList<>();
        moodRecyclerView = findViewById(R.id.moodRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Mood_Entries");
        moodRecyclerViewAdapter = new MoodRecyclerViewAdapter(this, moodEntries, this);
        moodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = databaseReference.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getUid());

        moodRecyclerView.setAdapter(moodRecyclerViewAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moodEntries.clear();

                // Retrieves all children of MoodEntry class
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MoodEntry moodEntry = dataSnapshot.getValue(MoodEntry.class);
                    count = snapshot.getChildrenCount();
                    moodEntries.add(moodEntry);
                    // reverse the list so the newest data will be displayed on top
                    Collections.reverse(moodEntries);
                    moodRecyclerViewAdapter.notifyDataSetChanged();


                    }
                if (count == 0) {
                    textView.setText("You have no mood entries so far! Please tell us how you feel xD");
                }
                else {
                    textView.setText("");
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /**
         * itemTouchHelper is for swiping either left or right to delete (u can set it in the param)
         * when the user swipe either left or right, there will be a dialog appears asking are you sure
         */
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                MoodEntry entry = moodEntries.get(position);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            /**
                             * if yes, implement the code to delete the specific data here
                             */
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        databaseReference.child(entry.getKey()).removeValue();
                                        moodEntries.remove(position);
                                        moodRecyclerViewAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("Remove Error", "onCancelled", error.toException());
                                        // TESTING THIS LATER
//                                        // used snackbar instead to display message
//                                        View view = snack.getView();
//                                        snack = Snackbar.make(view, "Please complete the required fields",
//                                                Snackbar.LENGTH_INDEFINITE);
//
//                                        snack.setAction("Close", new View.OnClickListener() {
//
//                                            @Override
//                                            public void onClick(View view) {
//                                                snack.dismiss();
//                                            }
//                                        }).setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark)).show();         //
                                    }
                                });

                                /**
                                 * Undo snackbar
                                 */
                                Snackbar.make(moodRecyclerView, "Mood: " + entry.getChosenMood(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        databaseReference.push().setValue(entry)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        moodEntries.clear();
                                        moodRecyclerViewAdapter.notifyDataSetChanged();
                                    }
                                }).show();
                                break;

                            /**
                             * if no, first, notify the recyclerview again to fetch the data again
                             * cuz after user swipe, the row will disappear even when user clicks no
                             */
                            case DialogInterface.BUTTON_NEGATIVE:
                                moodRecyclerViewAdapter.notifyDataSetChanged();
                                dialog.cancel();
                                break;
                        }
                    }
                };

                // build the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Delete Mood Entry")
                        .setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        };

        // link the itemtouchelper to the recycler view
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(moodRecyclerView);
    }

    @Override
    public void onMoodEntryClick(int position) {
        MoodEntry moodEntry = moodEntries.get(position);
        Intent intent = new Intent(HomeActivity.this,
                SpecificMoodEntryActivity.class);
        intent.putExtra("entry", moodEntry);
        startActivity(intent);
    }
}