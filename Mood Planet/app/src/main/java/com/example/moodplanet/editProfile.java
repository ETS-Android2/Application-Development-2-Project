package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class editProfile extends AppCompatActivity {
    Toolbar mToolbar;
    Button updatebtn;
    EditText fname, lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        updatebtn = findViewById(R.id.updateProfilebutton);
        fname = findViewById(R.id.editfname);
        lname = findViewById(R.id.editlname);
        mToolbar = findViewById(R.id.editPtoolbar);
        mToolbar.setTitle("Edit Profile");
        // toolbar depended on theme color
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(selectedColor);
        getWindow().setStatusBarColor(selectedColor);


    }
}