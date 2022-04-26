package com.example.moodplanet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moodplanet.Model.User;
import com.example.moodplanet.Model.JournalEntry;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView Adapter class for the recycler view in Journal MainActivity
 */
public class JournalRecyclerViewAdapter extends RecyclerView.Adapter<JournalRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<JournalEntry> journalEntryList;
    LayoutInflater inflater;

    public JournalRecyclerViewAdapter(Context context, List<JournalEntry> journalEntryList) {
        this.context = context;
        this.journalEntryList = journalEntryList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.from(parent.getContext()).inflate(R.layout.journal_child_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(journalEntryList.get(position).getLocalDateTime());
        holder.entry.setText(journalEntryList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return journalEntryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, entry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.journalDateTv);
            entry = itemView.findViewById(R.id.journalEntryTv);

        }
    }
}
