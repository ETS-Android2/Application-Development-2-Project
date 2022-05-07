package com.example.moodplanet;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.moodplanet.Model.MoodEntry;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment {
    private PieChart pieChart;
    DatabaseReference databaseReference;
    ArrayList<PieEntry> entries = new ArrayList<>();
    HashMap<String, List<MoodEntry>> moodHashMap = new HashMap<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PieChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PieChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PieChartFragment newInstance(String param1, String param2) {
        PieChartFragment fragment = new PieChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        // Inflate the layout for this fragment
        pieChart = v.findViewById(R.id.activity_main_piechart);
        setupPieChart();
        loadPieChartData();
        return v;
    }
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Mood chart this week");
        pieChart.setCenterTextSize(18);

        pieChart.getDescription().setEnabled(false);

        // get details of labels included
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {

        moodHashMap = HomeActivity.moodHashMap;

        //calculate the percentage of the mood
        float angry = moodHashMap.get("angry").size();
        float pensive = moodHashMap.get("pensive").size();
        float optimistic = moodHashMap.get("optimistic").size();
        float cheerful = moodHashMap.get("cheerful").size();
        float inlove = moodHashMap.get("inlove").size();
        float scared = moodHashMap.get("scared").size();
        float calm = moodHashMap.get("calm").size();
        float sleepy = moodHashMap.get("sleepy").size();
        float happy = moodHashMap.get("happy").size();
        float sad = moodHashMap.get("sad").size();

        if (angry > 0)
            entries.add(new PieEntry(angry, "angry"));
        if (pensive > 0)
            entries.add(new PieEntry(pensive, "pensive"));
        if (optimistic > 0)
            entries.add(new PieEntry(optimistic, "optimistic"));
        if (cheerful > 0)
            entries.add(new PieEntry(cheerful, "cheerful"));
        if (inlove > 0)
            entries.add(new PieEntry(inlove, "in love"));
        if (scared > 0)
            entries.add(new PieEntry(scared, "scared"));
        if (calm > 0)
            entries.add(new PieEntry(calm, "calm"));
        if (sleepy > 0)
            entries.add(new PieEntry(sleepy, "sleepy"));
        if (sad > 0)
            entries.add(new PieEntry(sad, "sad"));
        if (happy > 0)
            entries.add(new PieEntry(happy, "happy"));

        ArrayList<Integer> colors;
        colors = new ArrayList<>();
        for (int color: ColorTemplate.JOYFUL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(2000, Easing.EaseInOutQuad);
    }
}