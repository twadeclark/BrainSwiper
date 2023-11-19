package com.twadeclark.brainswiper.database;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twadeclark.brainswiper.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.DeckViewHolder> {
    private List<Deck> decks;

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_item_layout, parent, false);
        return new DeckViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Deck currentDeck = decks.get(position);
        holder.textViewDeckName.setText(currentDeck.getDeckName());
    }

    @Override
    public int getItemCount() {
        int retVal = decks == null  ? 0 : decks.size();
        return retVal;
    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewDeckName;

        public DeckViewHolder(View itemView) {
            super(itemView);
            textViewDeckName = itemView.findViewById(R.id.deck_name);
        }
    }

}
