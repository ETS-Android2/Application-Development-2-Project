package com.example.moodplanet;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodplanet.Model.User;
import com.example.moodplanet.Model.Quotes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuoteActivity extends AppCompatActivity {
    private Button logout;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private  List<Quotes> quotesList;
    private FirebaseAuth mAuth;
    private ImageButton play, stop, pause;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        quotesList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        logout = (Button) findViewById(R.id.logoutButton);
        play = findViewById(R.id.playButton);
        stop = findViewById(R.id.stopButton);
        pause = findViewById(R.id.pauseButton);
        mediaPlayer = MediaPlayer.create(this, R.raw.song);

        mediaPlayer.start();
        isPlaying = true;
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
                    mediaPlayer.start();
                    isPlaying = true;
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                isPlaying = false;
            }
        });



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(QuoteActivity.this, MainActivity.class));
                }

        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = firebaseUser.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.userGreetingTextView);

        // get specific value of this child
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    String firstName = user.firstName;
                    greetingTextView.setText("Welcome, " + firstName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuoteActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getQuotes() {
        // create a retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL_QUOTE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create an instance of class Api
        Api api = retrofit.create(Api.class);

        Call<List<Quotes>> call = api.getAllQuotes();
            call.enqueue(new Callback<List<Quotes>>() {
                @Override
                public void onResponse(Call<List<Quotes>> call, Response<List<Quotes>> response) {
                    if (response.code() != 200) {
                        // handling the error & display it
                        return;
                    }
                    List<Quotes> quotes = response.body();

                    for (Quotes quote : quotes) {
                        quotesList.add(quote);
                    }
                    if (response.isSuccessful())
                        putDataIntoFragment();
                }

                @Override
                public void onFailure(Call<List<Quotes>> call, Throwable t) {
                    Log.w("MyTag", "requestFailed", t);
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 800);
    }
    private  void putDataIntoFragment() {
        TextView quotes = (TextView) findViewById(R.id.quoteEditText);

        Random rand = new Random(); //instance of random class
        int upperbound = quotesList.size();
        int int_random = rand.nextInt(upperbound);

        quotes.setText(quotesList.get(int_random).getText());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getQuotes();
    }
}