package com.twadeclark.brainswiper.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twadeclark.brainswiper.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> deckNames;

    public MyAdapter(List<String> deckNames) {
        this.deckNames = deckNames;
    }

    // ViewHolder class, onCreateViewHolder, onBindViewHolder, getItemCount methods...

    public void setDeckNames(List<String> deckNames) {
        this.deckNames = deckNames;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String deckName = deckNames.get(position);
        holder.textView.setText(deckName);

    }

    @Override
    public int getItemCount() {
        return deckNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recyclerView);
        }
    }

}
