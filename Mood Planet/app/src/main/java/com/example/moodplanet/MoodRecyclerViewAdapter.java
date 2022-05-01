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
    MoodEntriesOnClickListener moodEntryOnClickListener;

    public MoodRecyclerViewAdapter(Context context, List<MoodEntry> moodEntryList,
                                   MoodEntriesOnClickListener moodEntryOnClickListener) {
        this.context = context;
        this.moodEntryList = moodEntryList;
        this.moodEntryOnClickListener = moodEntryOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.mood_child_row, parent,
                false);
        return new ViewHolder(view, moodEntryOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.date.setText(moodEntryList.get(position).getDayOfWeek() +
                " " + moodEntryList.get(position).getLocalDateTime());
        holder.description.setText("Description: " + "Mood Rate: " + moodEntryList.get(position).getMoodRate() + "\n" +
                moodEntryList.get(position).getDescription());
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
            MoodEntriesOnClickListener moodEntriesOnClickListener;

        public ViewHolder(@NonNull View itemView, MoodEntriesOnClickListener moodEntriesOnClickListener) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTimeTextView);
            moodImage = itemView.findViewById(R.id.moodImageView);
            description = itemView.findViewById(R.id.descriptionTV);
            this.moodEntriesOnClickListener = moodEntriesOnClickListener;
            itemView.setOnClickListener(this); // connects to onClick method below
        }

        @Override
        public void onClick(View view) {
            // Home Activity implements the interface(home activity object that implements the interface)
            moodEntriesOnClickListener.onMoodEntryClick(getLayoutPosition());
        }
    }

    /**
     * Takes the position of the clicked entry from the recycler view
     */
    public interface MoodEntriesOnClickListener {
        void onMoodEntryClick(int position);
    }
}