package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodplanet.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register, forgotPassword;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    public String userID;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterUserActivity.class));
                break;
            case R.id.signIn:
                userLogin();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void userLogin() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // Commenting this block because it is used for email verification
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {

                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify your account!",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }
                else {
                    Toast.makeText(MainActivity.this,
                            "Failed to login! Please check your credentials",
                            Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetching the stored data
        // from the SharedPreference
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String email = sh.getString("email", "");
        String pwd = sh.getString("password", "");

        // Setting the fetched data
        // in the EditTexts
        editTextEmail.setText(email);
        editTextPassword.setText(String.valueOf(pwd));
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("email", editTextEmail.getText().toString());
        myEdit.putString("password", editTextPassword.getText().toString());
        myEdit.apply();

    }
}