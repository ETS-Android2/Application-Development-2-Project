package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moodplanet.Model.MoodEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartMainActivity extends AppCompatActivity {

    List<MoodEntry> moodEntries = HomeActivity.moodEntries;
    TextView suggestion;
    Toolbar mToolbar;
    Button catPower;
    DatabaseReference databaseReference;
    ArrayList<HashMap<String, String>> catMemesList;

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

    ProgressDialog progressDialog;      // when connecting to the cloud -> show the progress of the data retrieval from the cloud
    private static String url = "https://cataas.com/api/cats";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_main);
        suggestion = findViewById(R.id.suggestion);
        catMemesList = new ArrayList<>();

        mToolbar = findViewById(R.id.chartToolbar);
        mToolbar.setTitle("Mood Report");
        // toolbar depended on theme color
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(selectedColor);
        getWindow().setStatusBarColor(selectedColor);

        // if there are data inside 2 hashmaps, then trigger getMood()
        if (moodHashMap.size() != 0 && moodRateHm.size() != 0) {
            getMood();
        }

        // to trigger cat memes api
        catPower = findViewById(R.id.catPowerBtn);
        catPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getCatMemes().execute();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Mood_Entries");
        Query query = databaseReference.orderByChild("userID").equalTo(FirebaseAuth.getInstance().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moodHashMap.clear();
                moodRateHm.clear();
                // Retrieves all children of MoodEntry class
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MoodEntry moodEntry = dataSnapshot.getValue(MoodEntry.class);
                    moodEntries.add(moodEntry);
                    String choosenMood = moodEntry.getChosenMood();

                    // get the current weekOfYear and current Year
                    LocalDate date = LocalDate.now();
                    TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                    String weekNumber = "" + date.get(woy);
                    String year = date.getYear() + "";

                    // query only mood entries of the current week to the pie chart
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

                    // query only mood entries of the current week to the bar chart
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

                    moodRateHm.put("sun", sun);
                    moodRateHm.put("mon", mon);
                    moodRateHm.put("tue", tue);
                    moodRateHm.put("wed", wed);
                    moodRateHm.put("thu", thu);
                    moodRateHm.put("fri", fri);
                    moodRateHm.put("sat", sat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * to fetch data from the cat memes api (using AsyncTask)
     */
    private class getCatMemes extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showing progress dialog
            progressDialog = new ProgressDialog(ChartMainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HTTPHandler sh = new HTTPHandler();  // after this do code in HttpHandler

            // making a request
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    // looping through all the context
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = String.valueOf(jsonObject.getString("id"));

                        // create a hashmap -> store the data from the cloud to the local HashMap
                        HashMap<String, String> catMemes_hashMap = new HashMap<>();
                        catMemes_hashMap.put("id", id);
                        catMemesList.add(catMemes_hashMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            buildDialog();
            // dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }


    /**
     * calculate average mood rate and the most regular mood of the current week
     */
    private void getMood() {
        double moodRates = 0;

        int angry = moodHashMap.get("angry").size();
        int pensive = moodHashMap.get("pensive").size();
        int sad = moodHashMap.get("sad").size();
        int optimistic = moodHashMap.get("optimistic").size();
        int cheerful = moodHashMap.get("cheerful").size();
        int inlove = moodHashMap.get("inlove").size();
        int scared = moodHashMap.get("scared").size();
        int calm = moodHashMap.get("calm").size();
        int sleepy = moodHashMap.get("sleepy").size();
        int happy = moodHashMap.get("happy").size();

        int moodsFreq[] = {angry, pensive, sad, optimistic, cheerful, inlove, scared, calm, sleepy, happy};
        String moods[] = {"angry", "pensive", "sad", "optimistic", "cheerful", "inlove", "scared", "calm", "sleepy", "happy"};
        int mainMood = getMainMood(moodsFreq, 10);


        String mostFreqMood = "";
        for (int i = 0; i < 10; i++) {
            if (mainMood == moodHashMap.get(moods[i]).size())
                mostFreqMood = moods[i];
        }

        LocalDate date = LocalDate.now();
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        String weekNumber = "" + date.get(woy);
        String year = date.getYear() + "";
        for (int i = 0; i < moodEntries.size(); i++) {
            MoodEntry moodEntry = moodEntries.get(i);
            if (moodEntry.getWeekOfYear().equals(weekNumber) && moodEntry.getYear().equals(year))
                moodRates += moodEntry.getMoodRate();
        }
        double moodRate = moodRates / (moodEntries.size());

        setSuggestion(moodRate, mostFreqMood);
    }

    /**
    Set suggested captions based on user's entry moods of the current week
     */
    private void setSuggestion(double moodRate, String mostFreqMood) {
        moodRate = Math.ceil(moodRate);
        String moodRateText = "";
        String moodDescText = "";
        if (moodRate == 1 || moodRate == 2 || moodRate == 3) {
            moodRateText = "Don't bundle up your feelings.";
        } else if (moodRate == 4 || moodRate == 5) {
            moodRateText = "Keep it up! You're doing very well ~~";
        }
        String moods[] = {"angry", "pensive", "sad", "optimistic", "cheerful", "inlove", "scared", "calm", "sleepy", "happy"};

        if (mostFreqMood == "angry") {
            moodDescText = "Take some deep breaths might help!";
        } else if (mostFreqMood == "pensive") {
            moodDescText = "Meditating and journaling can help you explore your feelings better";
        } else if (mostFreqMood == "sad") {
            moodDescText = "Don't forget to phone a friend, or us :)";
        } else if (mostFreqMood == "optimistic") {
            moodDescText = "It's a good time to go for a walk";
        } else if (mostFreqMood == "cheerful") {
            moodDescText = "Don't forget to share your happiness to your loved ones :)";
        } else if (mostFreqMood == "inlove") {
            moodDescText = "Are you being in love with life? Or with someone?~";
        } else if (mostFreqMood == "scared") {
            moodDescText = "Your loved ones are always there for you, let them know when you feel struggled :')";
        } else if (mostFreqMood == "calm") {
            moodDescText = "Was it because you've been meditating lately? xD";
        } else if (mostFreqMood == "sleepy") {
            moodDescText = "It's important to allow yourself to rest once in a while :)";
        } else if (mostFreqMood == "happy") {
            moodDescText = "Maybe it's time to try playing sports? xD";
        }

        suggestion.setText("Your average mood rate so far is " + moodRate + " and it looks like you have been feeling " + mostFreqMood + " lately. " + moodRateText + " " + moodDescText);

    }

    /**
     *  get the main mood of the current week
     * @param moodsFreq
     * @param mainMood
     * @return
     */
    private int getMainMood(int[] moodsFreq, int mainMood) {
        int temp;
        for (int i = 0; i < mainMood; i++) {
            for (int j = i + 1; j < mainMood; j++) {
                if (moodsFreq[i] > moodsFreq[j]) {
                    temp = moodsFreq[i];
                    moodsFreq[i] = moodsFreq[j];
                    moodsFreq[j] = temp;
                }
            }
        }
        return moodsFreq[mainMood - 1];
    }

    /**
     * to build a dialog which contains a random image that if fetched from the cat memes api
     */
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChartMainActivity.this);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_dialog, null);
        Random rand = new Random(); //instance of random class
        int upperbound = catMemesList.size();
        int int_random = rand.nextInt(upperbound);
        String url = "https://cataas.com/cat/" + catMemesList.get(int_random).get("id");
        Glide.with(dialogLayout)
                .load(url)
                .into((ImageView) dialogLayout.findViewById(R.id.imageView7));

        builder.setView(dialogLayout);
        builder.show();
    }
}