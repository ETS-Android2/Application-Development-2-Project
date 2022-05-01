package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moodplanet.Model.JournalEntry;
import com.example.moodplanet.Model.MoodEntry;

import java.util.List;

public class MoodRecyclerViewAdapter extends RecyclerView.Adapter<MoodRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<MoodEntry> moodEntryList;
    LayoutInflater inflater;

    public MoodRecyclerViewAdapter(Context context, List<MoodEntry> moodEntryList) {
        this.context = context;
        this.moodEntryList = moodEntryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.mood_child_row, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(moodEntryList.get(position).getLocalDateTime().toString());
        holder.description.setText(moodEntryList.get(position).getDescription().toString());
        String mood = moodEntryList.get(position).getChosenMood();

         if (mood.equalsIgnoreCase("sad"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.sad));

         if (mood.equalsIgnoreCase("happy"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.hehe));

        if (mood.equalsIgnoreCase("sleepy"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.zzz));

        if (mood.equalsIgnoreCase("calm"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.calm));

        if (mood.equalsIgnoreCase("scared"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ohno));

        if (mood.equalsIgnoreCase("inlove"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.love));

        if (mood.equalsIgnoreCase("cheerful"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.yay));

        if (mood.equalsIgnoreCase("optimistic"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ok));

        if (mood.equalsIgnoreCase("pensive"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.hmm));

        if (mood.equalsIgnoreCase("angry"))
            holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.angry));
    }

    @Override
    public int getItemCount() {
        return moodEntryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView date, description;
            ImageView moodImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTextView);
            moodImage = itemView.findViewById(R.id.chosenMoodImage);
            description = itemView.findViewById(R.id.descriptionTV);
        }

        @Override
        public void onClick(View view) {

        }
    }
}