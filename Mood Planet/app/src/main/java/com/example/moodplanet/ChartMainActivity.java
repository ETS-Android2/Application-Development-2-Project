package com.example.moodplanet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.moodplanet.Model.MoodEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChartMainActivity extends AppCompatActivity {

    HashMap<String, List<MoodEntry>> moodRateHm = new HashMap<>();
    HashMap<String, List<MoodEntry>> moodHashMap = new HashMap<>();
    List<MoodEntry> moodEntries = HomeActivity.moodEntries;
    TextView suggestion;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_main);
        suggestion = findViewById(R.id.suggestion);
        moodRateHm = HomeActivity.moodRateHm;
        moodHashMap = HomeActivity.moodHashMap;

        mToolbar = findViewById(R.id.chartToolbar);
        mToolbar.setTitle("Mood Report");
        // toolbar depended on theme color
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(selectedColor);
        getWindow().setStatusBarColor(selectedColor);

        getMood();
    }

    /**
     * calculate average mood rate and the most regular mood
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


}