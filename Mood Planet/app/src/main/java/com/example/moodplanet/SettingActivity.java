package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
import java.util.Locale;


public class SettingActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Button edit, logout;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private TextView fname, lname, timeTextView;
    private ImageView facebookIV, twitterIV, instagramIV;
    private Switch notifSwitch;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;
    private int hour;
    private int minute;
    FirebaseAuth mAuth;

    Boolean checked;

    Toolbar mToolbar;
    ImageButton mRedColor, mGreenColor, mYellowColor, mPurpleColor, mBlueColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        checked = SharedPref.loadCheckedFromPref(this);
        hour = SharedPref.loadHourFromPref(this);
        minute = SharedPref.loadMinuteFromPref(this);

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

        if (hour != -1 && minute != -1)
            timeTextView.setText("Notification Time: " + String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        else
            timeTextView.setText("");

        // theme color
        mToolbar = findViewById(R.id.tbar);
        mRedColor = findViewById(R.id.themeRed);
        mGreenColor = findViewById(R.id.themeGreen);
        mYellowColor = findViewById(R.id.themeYellow);
        mBlueColor = findViewById(R.id.themeBlue);
        mPurpleColor = findViewById(R.id.themePurple);
        mToolbar.setTitle("Setting");

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

        // edit button fucntionality
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, EditProfile.class));
            }
        });


        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
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
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        notifSwitch.setChecked(checked);

        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPref.saveChekedInPref(getApplicationContext(), isChecked);

                if (isChecked) {
                    // if no time is set
                    if (hour == -1 && minute == -1) {   // hour and minute will depend on shared pref   // WE ONLY NEEDED TO DO THE ALARM MANAGER ONCE
                        Toast.makeText(getApplicationContext(), "Notification on!", Toast.LENGTH_SHORT).show();

                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                hour = selectedHour;
                                minute = selectedMinute;

                                SharedPref.saveMinuteInPref(getApplicationContext(), minute);
                                SharedPref.saveHourInPref(getApplicationContext(), hour);

                                timeTextView.setText("Notification Time: " + String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));

                                calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                notifSwitch.setChecked(true);

                                // need this condition to prevent 00:00 from notifying
                                if (Calendar.getInstance().after(calendar)) {
                                    calendar.add(Calendar.DAY_OF_MONTH, 1); // means to add another day
                                }

                                notificationChannel();
                                Intent intent = new Intent(getApplicationContext(), Notification_receiver.class);
                                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                            calendar.getTimeInMillis(), pendingIntent);
                                }
                            }

                        };
                        int style = AlertDialog.THEME_HOLO_DARK;
                        TimePickerDialog timePickerDialog = new TimePickerDialog(SettingActivity.this, style, onTimeSetListener, hour, minute, true);
                        timePickerDialog.setTitle("Select Time");
                        timePickerDialog.show();
                    }

                } else {
//                    sh.edit().putBoolean("checked", false).apply();
                    timeTextView.setText("");
                    hour = -1; // set it to -1 again
                    minute = -1; // set it to -1 again
                    SharedPref.saveMinuteInPref(getApplicationContext(), minute);
                    SharedPref.saveHourInPref(getApplicationContext(), hour);
                    Toast.makeText(getApplicationContext(), "Notification off!", Toast.LENGTH_SHORT).show();
                    if (alarmManager != null && pendingIntent != null) {
                        alarmManager.cancel(pendingIntent);
                    }
                    notifSwitch.setChecked(false);
                }
            }
        });

        // theme color buttons

        if (getColor() != getResources().getColor(R.color.colorPrimary)) {
            mToolbar.setBackgroundColor(getColor());
            getWindow().setStatusBarColor(getColor());
        }
        mRedColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorRed));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorRed));

                storeColor(getResources().getColor(R.color.colorRed));
            }
        });

        mGreenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorGreen));

                storeColor(getResources().getColor(R.color.colorGreen));
            }
        });

        mYellowColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorYellow));

                storeColor(getResources().getColor(R.color.colorYellow));
            }
        });

        mBlueColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue));

                storeColor(getResources().getColor(R.color.colorBlue));
            }
        });

        mPurpleColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPurple));

                storeColor(getResources().getColor(R.color.colorPurple));
            }
        });



    }// onCreate()


    // store the theme color to sharedPreferences
    private void storeColor(int color) {
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("color", color);
        mEditor.apply();
    }

    //get the color from sharedPreferences
    private int getColor() {
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));

        return selectedColor;
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

        } catch (ActivityNotFoundException e) {
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

    public void resetPreferences(View view) {
        SharedPref.removeDataFromPref(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPref.registerPref(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPref.unregisterPref(this, this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        Boolean isChecked = sh.getBoolean("isChecked", false);
//
//        notifSwitch.setChecked(isChecked);
//        hour = sh.getInt("hour", -1);
//        minute = sh.getInt("minute", -1);
//
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        SharedPreferences.Editor myEdit = sharedPreferences.edit();
//
//        myEdit.putBoolean("isChecked", false);
//        myEdit.putInt("hour", hour);
//        myEdit.putInt("minute", minute);
//        myEdit.apply();
//
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(SharedPref.PREF_CHECKED_KEY)) {
            checked = SharedPref.loadCheckedFromPref(this);
            notifSwitch.setChecked(checked);
        }
        if (s.equals(SharedPref.PREF_HOUR_KEY)) {
            hour = SharedPref.loadHourFromPref(this);
        }
        if (s.equals(SharedPref.PREF_MINUTE_KEY)) {
            minute = SharedPref.loadMinuteFromPref(this);
        }
    }
}