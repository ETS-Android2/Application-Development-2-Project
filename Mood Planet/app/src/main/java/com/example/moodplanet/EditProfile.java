package com.example.moodplanet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moodplanet.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    Toolbar mToolbar;
    Button updatebtn, settingsbtn;
    EditText fname, lname, useremail, newpassword, confirmpassword;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;
    User dataUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        updatebtn = findViewById(R.id.updateProfilebutton);
        settingsbtn = findViewById(R.id.settingsButton);
        fname = findViewById(R.id.editfname);
        lname = findViewById(R.id.editlname);
        useremail = findViewById(R.id.editemail);
        newpassword = findViewById(R.id.editpassword);
        confirmpassword = findViewById(R.id.editconfirmpassword);
        mToolbar = findViewById(R.id.editPtoolbar);
        mToolbar.setTitle("Edit Profile");
        // toolbar depended on theme color
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(selectedColor);
        getWindow().setStatusBarColor(selectedColor);


        // Database setup
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");


        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataUser = snapshot.getValue(User.class);
                fname.setText(dataUser.firstName);
                lname.setText(dataUser.lastName);
                useremail.setText(dataUser.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfile.this, SettingActivity.class));
            }
        });
        // update button functionality
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = fname.getText().toString().trim();
                String lastName = lname.getText().toString().trim();
                String newEmail = useremail.getText().toString().trim();
                String newPassword = newpassword.getText().toString().trim();
                String confirmpass = confirmpassword.getText().toString().trim();

                // if email is empty, just use the old email
                if (newEmail.isEmpty())
                    useremail.setText(dataUser.email);

                // if first name is empty, then use the old first name
                if (firstName.isEmpty())
                    fname.setText(dataUser.firstName);

                // if last name is empty, then use the old last name
                if (lastName.isEmpty())
                    lname.setText(dataUser.lastName);

                // if new email doesnt match the pattern then error, stop method
                if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    useremail.setError("Please enter a valid email!");
                    useremail.requestFocus();
                    return;
                }

                // if password is below 6 then error, stop method
                if (newPassword.length() < 6) {
                    newpassword.setError("Minimum password length is 6 characters!");
                    newpassword.requestFocus();
                    return;
                }

                // if confirm pass is below 6, then error, stop method
                if (confirmpass.length() < 6) {
                    confirmpassword.setError("Minimum password length is 6 characters!");
                    confirmpassword.requestFocus();
                    return;
                }

                // if passwords doesnt match then throw error
                if (!newPassword.equals(confirmpass)) {
                    newpassword.setError("Password doesn't match");
                    confirmpassword.setError("Password doesn't match");
                    confirmpassword.requestFocus();
                    newpassword.requestFocus();
                    return;
                }

                // good password SO change password in firebase authentication
                else {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                }
                            }
                        });
                }

                // update only when the user has a different email
                if (!dataUser.email.equals(newEmail)) {
                    user.updateEmail(newEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                    }
                                }
                            });
                }
                User updateUser = new User(firstName, lastName, newEmail);
                databaseReference.child(user.getUid()).setValue(updateUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "User update", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfile.this, SettingActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "User update failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

}