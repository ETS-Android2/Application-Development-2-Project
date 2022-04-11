package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Button returnToLoginBtn, signUpBtn;
    EditText emailTxt, passwordTxt, firstNameTxt, lastNameTxt, confirmPasswordTxt;
    long maxID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set Up firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("User");

        // Connect to xml views
        emailTxt = findViewById(R.id.emailAddressEditText);
        firstNameTxt = findViewById(R.id.firstNameEditText);
        lastNameTxt = findViewById(R.id.lastNameEditText);
        passwordTxt = findViewById(R.id.passwordEditText);
        confirmPasswordTxt = findViewById(R.id.confirmPasswordEditText);
        returnToLoginBtn = findViewById(R.id.returnToLoginButton);
        signUpBtn = findViewById(R.id.registerButton);

        // Do the button functionality
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTxt.getText().toString().trim();
                String firstName = firstNameTxt.getText().toString().trim();
                String lastName = lastNameTxt.getText().toString().trim();
                String password = passwordTxt.getText().toString().trim();
                String confirmPassword = confirmPasswordTxt.getText().toString().trim();

                if (!email.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !password.isEmpty()
                        && !confirmPassword.isEmpty()) {

                    if (password.equals(confirmPassword)) {
                        User user = new User(email, firstName, lastName, password, confirmPassword);
                        //Push data to database
                        databaseReference.child(String.valueOf(maxID + 1)).setValue(user);
                        Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                            password.isEmpty() || confirmPassword.isEmpty()) {
                        emailTxt.setError("Please enter email");
                        firstNameTxt.setError("Please enter first name");
                        lastNameTxt.setError("Please enter last name");
                        passwordTxt.setError("Please enter password");
                        confirmPasswordTxt.setError("Please enter confirm password");
                    }

                    if (!password.equals(confirmPassword)) {
                        passwordTxt.setError("Please enter matching password");
                        confirmPasswordTxt.setError("Please enter matching password");
                    }

                    Toast.makeText(getApplicationContext(), "Please complete all the " +
                            "fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        returnToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // If the data added increased then update maxID using snapshot
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxID = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
