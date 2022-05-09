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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChartMainActivity extends AppCompatActivity {

    HashMap<String, List<MoodEntry>> moodRateHm = new HashMap<>();
    HashMap<String, List<MoodEntry>> moodHashMap = new HashMap<>();
    TextView suggestion;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_main);
        suggestion = findViewById(R.id.suggestionTv);
        moodRateHm = HomeActivity.moodRateHm;
        moodHashMap = HomeActivity.moodHashMap;

        mToolbar = findViewById(R.id.chartToolbar);
        mToolbar.setTitle("Mood Report");
        // toolbar depended on theme color
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(selectedColor);
        getWindow().setStatusBarColor(selectedColor);

        setSuggestion();
    }

    private void setSuggestion() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                suggestion.setText("It's Monday tomorrow! Click to get cat power ~!");
                break;
            case Calendar.MONDAY:
                suggestion.setText("asd");
                break;
            case Calendar.TUESDAY:
                // etc.
                break;
            case Calendar.WEDNESDAY:
                // etc.
                break;
            case Calendar.THURSDAY:
                // etc.
                break;
            case Calendar.FRIDAY:
                // etc.
                break;
            case Calendar.SATURDAY:
                // etc.
                break;

        }
    }
}