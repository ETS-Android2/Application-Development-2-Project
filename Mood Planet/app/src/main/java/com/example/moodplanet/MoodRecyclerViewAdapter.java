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
        holder.date.setText(moodEntryList.get(position).getLocalDateTime());
        holder.description.setText(moodEntryList.get(position).getDescription());
        String mood = moodEntryList.get(position).getChosenMood();

        switch (mood) {
            case "sad" :
                holder.moodImage.setImageDrawable(context.getResources().
                        getDrawable(R.drawable.sad));
                break;

            case "happy":
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.hehe));
                break;

            case "sleepy":
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.zzz));
                break;

            case "calm":
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.calm));
                break;

            case "scared":
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ohno));
                break;

            case "inlove":
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.love));
                break;

            case "cheerful":
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.yay));
                break;

            case "optimistic":
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ok));
                break;

            case "pensive":
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.hmm));
                break;

            default :
                holder.moodImage.setImageDrawable(context.getResources().getDrawable(R.drawable.angry));
                break;
        }

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