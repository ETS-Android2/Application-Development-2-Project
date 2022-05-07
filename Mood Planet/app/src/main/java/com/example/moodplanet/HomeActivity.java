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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements MoodRecyclerViewAdapter.MoodEntriesOnClickListener, Serializable {
    RecyclerView moodRecyclerView;
    DatabaseReference databaseReference;
    MoodRecyclerViewAdapter moodRecyclerViewAdapter;
    List<MoodEntry> moodEntries;

    // creating lists for pie chart
    List<MoodEntry> angry = new ArrayList<>();
    List<MoodEntry> pensive = new ArrayList<>();
    List<MoodEntry> sad = new ArrayList<>();
    List<MoodEntry> optimistic = new ArrayList<>();
    List<MoodEntry> cheerful = new ArrayList<>();
    List<MoodEntry> inlove = new ArrayList<>();
    List<MoodEntry> scared = new ArrayList<>();
    List<MoodEntry> calm = new ArrayList<>();
    List<MoodEntry> sleepy = new ArrayList<>();
    List<MoodEntry> happy = new ArrayList<>();

    // creating lists for the bar chart
    List<MoodEntry> mon = new ArrayList<>();
    List<MoodEntry> tue = new ArrayList<>();
    List<MoodEntry> wed = new ArrayList<>();
    List<MoodEntry> thu = new ArrayList<>();
    List<MoodEntry> fri = new ArrayList<>();
    List<MoodEntry> sat = new ArrayList<>();
    List<MoodEntry> sun = new ArrayList<>();

    // mood hashmap containing data for the pie chart
    public static HashMap<String, List<MoodEntry>> moodHashMap = new HashMap<>();

    // mood rate hashmap containing data for the bar chart
    public static HashMap<String, List<MoodEntry>> moodRateHm = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        moodEntries = new ArrayList<>();
        moodRecyclerView = findViewById(R.id.moodRecyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Mood_Entries");
        moodRecyclerViewAdapter = new MoodRecyclerViewAdapter(this, moodEntries, this);
        moodRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Query query = databaseReference.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getUid());

        moodRecyclerView.setAdapter(moodRecyclerViewAdapter);

        // get week of year and year
        LocalDate date = LocalDate.now();
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        String weekNumber = "" + date.get(woy);
        String year = date.getYear() + "";

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                moodEntries.clear();
                moodHashMap.clear();
                moodRateHm.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MoodEntry moodEntry = dataSnapshot.getValue(MoodEntry.class);
                    moodEntries.add(moodEntry);
                    String choosenMood = moodEntry.getChosenMood();

                    // get the current weekOfYear and current Year
                    LocalDate date = LocalDate.now();
                    TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                    String weekNumber = "" + date.get(woy);
                    String year = date.getYear() + "";

                    // pie chart
                    if (moodEntry.getWeekOfYear().equals(weekNumber) && moodEntry.getYear().equals(year)) {
                        // angry
                        if (choosenMood.equals("angry")) {
                            angry.add(moodEntry);
                        }
                        // pensive
                        else if (choosenMood.equals("pensive")) {
                            pensive.add(moodEntry);
                        }
                        // sad
                        else if (choosenMood.equals("sad")) {
                            sad.add(moodEntry);
                        }
                        //optimistic
                        else if (choosenMood.equals("optimistic")) {
                            optimistic.add(moodEntry);
                        }
                        //cheerful
                        else if (choosenMood.equals("cheerful")) {
                            cheerful.add(moodEntry);
                        }
                        //inlove
                        else if (choosenMood.equals("inlove")) {
                            inlove.add(moodEntry);
                        }
                        //scared
                        else if (choosenMood.equals("scared")) {
                            scared.add(moodEntry);
                        }
                        //calm
                        else if (choosenMood.equals("calm")) {
                            calm.add(moodEntry);
                        }
                        //sleepy
                        else if (choosenMood.equals("sleepy")) {
                            sleepy.add(moodEntry);
                        }
                        //happy
                        else if (choosenMood.equals("happy")) {
                            happy.add(moodEntry);
                        }
                    }


                        // get dayOfWeek of the current moodEntry
                    String dayOfWeek = moodEntry.getDayOfWeek();

                        //bar chart
                    if (moodEntry.getWeekOfYear().equals(weekNumber) && moodEntry.getYear().equals(year)) {
                        if (dayOfWeek.equals("Monday")) {
                            mon.add(moodEntry);
                        } else if (dayOfWeek.equals("Tuesday")) {
                            tue.add(moodEntry);
                        } else if (dayOfWeek.equals("Wednesday")) {
                            wed.add(moodEntry);
                        } else if (dayOfWeek.equals("Thursday")) {
                            thu.add(moodEntry);
                        } else if (dayOfWeek.equals("Friday")) {
                            fri.add(moodEntry);
                        } else if (dayOfWeek.equals("Saturday")) {
                            sat.add(moodEntry);
                        } else if (dayOfWeek.equals("Sunday")) {
                            sun.add(moodEntry);
                        }
                    }

                    moodHashMap.put("cheerful", cheerful);
                    moodHashMap.put("optimistic", optimistic);
                    moodHashMap.put("sad", sad);
                    moodHashMap.put("angry", angry);
                    moodHashMap.put("pensive", pensive);
                    moodHashMap.put("happy", happy);
                    moodHashMap.put("sleepy", sleepy);
                    moodHashMap.put("calm", calm);
                    moodHashMap.put("scared", scared);
                    moodHashMap.put("inlove", inlove);

                    moodRateHm.put("mon", mon);
                    moodRateHm.put("tue", tue);
                    moodRateHm.put("wed", wed);
                    moodRateHm.put("thu", thu);
                    moodRateHm.put("fri", fri);
                    moodRateHm.put("sat", sat);
                    moodRateHm.put("sun", sun);

                    moodRecyclerViewAdapter.notifyDataSetChanged();
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