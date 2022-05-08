package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.moodplanet.Model.Notification_receiver;
import com.example.moodplanet.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class SettingActivity extends AppCompatActivity {

    private Button edit, logout;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private TextView fname, lname, timeTextView;
    private ImageView facebookIV, twitterIV, instagramIV;
    private Switch notifSwitch;
    private Calendar calendar;
    private int hour;
    private int minute;
    FirebaseAuth mAuth;
    boolean isCheckedSF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        edit = findViewById(R.id.editAccountBtn);
        fname = findViewById(R.id.firstNameTV);
        lname = findViewById(R.id.lastNameTV);
        mAuth = FirebaseAuth.getInstance();

        // Social media
        facebookIV = findViewById(R.id.facebookImageView);
        twitterIV = findViewById(R.id.twitterImageView);
        instagramIV = findViewById(R.id.instagramImageView);

        // Switch
        notifSwitch = findViewById(R.id.notificationSwitch);
        notifSwitch.setChecked(false);
        Boolean switchState = notifSwitch.isChecked();
        timeTextView = findViewById(R.id.textViewForTime);

//        ---------------------------display the user first name and last name------------------
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = firebaseUser.getUid();



        //-------------logout button------------------
        logout = findViewById(R.id.setringLogoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAuth.signOut();

          startActivity(new Intent(SettingActivity.this, MainActivity.class));
        }
        });
//        ----------------------end logout button---------------------------


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null){
                    String firstname = user.firstName;
                    String lastname = user.lastName;
                    fname.setText(firstname);
                    lname.setText(lastname);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingActivity.this, "Something wrong happened!",
                        Toast.LENGTH_LONG).show();
            }
        });

        //        -------------end display the user first name and last name------------------

//        -------------------------------edit-------------------------------

//
        facebookIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize link and package
                String sAppLink = "fb://page/185150934832623";
                String sPackage = "com.facebook.katana";
                String sWebLink = "https://www.facebook.com/facebookapp";
                // Create method
                openLink(sAppLink, sPackage, sWebLink);
            }


        });

        twitterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize link and package
                String sAppLink = "twitter://user?screen_name=Twitter";
                String sPackage = "com.twitter.android";
                String sWebLink = "https://www.twitter.com/Twitter";

                // call method
                openLink(sAppLink, sPackage, sWebLink);
            }
        });

        instagramIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sAppLink = "https://www.instagram.com/instagram";
                String sPackage = "com.instagram.android";
                // Call method
                openLink(sAppLink, sPackage, sAppLink);
            }
        });
        boolean checked = true;
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sh.edit().putBoolean("checked", true).apply();
                    Toast.makeText(getApplicationContext(), "Notification on!", Toast.LENGTH_SHORT).show();
//                    TimePickerDialog timePickerDialog = new TimePickerDialog(SettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                            // Initialize house and minute
//                            hour = i;
//                            minute = i1;
//                            // Initialize calendar
//                            calendar = Calendar.getInstance();
//                            // Set hour and minute
//                            calendar.set(0, 0, 0, hour, minute);
//
//                            timeTextView.setText(DateFormat.format("hh:mm: aa", calendar));
//                        }
//                    }, 12, 0, false);

                    notificationChannel();
                    notifSwitch.setChecked(true);
                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,17);
                    calendar.set(Calendar.MINUTE, 05);
                    calendar.set(Calendar.SECOND, 00);

                    if (Calendar.getInstance().after(calendar)) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
                    }
                }
                else {
                    sh.edit().putBoolean("checked", false).apply();
                    Toast.makeText(getApplicationContext(), "Notification off!", Toast.LENGTH_SHORT).show();
                }
                isCheckedSF = isChecked;

            }

        });

    }

    private void notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MoodPlanet";
            String description = "Mood Planet Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MOOD_PLANET_NOTIFICATION_CHANNEL",
                    name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
    private void openLink(String sAppLink, String sPackage, String sWebLink) {
        // Use try catch
        try {
            // When application is installed
            // Initialize uri
            Uri uri = Uri.parse(sAppLink);
            // Intialize intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //Set data
            intent.setData(uri);
            //Set package
            intent.setPackage(sPackage);
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // start Actitivy
            startActivity(intent);

        }
        catch (ActivityNotFoundException e) {
            // Open link in browser
            // Initialize uri
            Uri uri = Uri.parse(sWebLink);
            // Initialize intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Set data
            intent.setData(uri);
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Start activity
            startActivity(intent);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        // Setting the fetched data
        // in the EditTexts

        notifSwitch.setChecked(sh.getBoolean("checked", false));
    }
}