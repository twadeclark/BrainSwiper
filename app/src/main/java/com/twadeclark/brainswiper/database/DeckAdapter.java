package com.twadeclark.brainswiper.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twadeclark.brainswiper.R;

import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {
    private List<Deck> decks;

    public interface OnDeckLongClickListener {
        void onDeckLongClick(Deck deck);
    }

    private OnDeckLongClickListener longClickListener;

    public void setOnDeckLongClickListener(OnDeckLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_item_layout, parent, false);
//        return new DeckViewHolder(v);
        return new DeckViewHolder(v, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Deck currentDeck = decks.get(position);
        holder.textViewDeckName.setText(currentDeck.getDeckName());
        holder.bindDeck(currentDeck);
    }

    @Override
    public int getItemCount() {
        int retVal = decks == null  ? 0 : decks.size();
        return retVal;
    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewDeckName;

//        public DeckViewHolder(View itemView, OnDeckLongClickListener longClickListener) {
//            super(itemView);
//            // Initialize other views...
//
//            itemView.setOnLongClickListener(v -> {
//                if (longClickListener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        longClickListener.onDeckLongClick(/* Get your Deck object here */);
//                        return true;
//                    }
//                }
//                return false;
//            });

        private Deck currentDeck;

        public void bindDeck(Deck deck) {
            this.currentDeck = deck;
            // Set other views based on the deck information
        }

        public DeckViewHolder(View itemView, OnDeckLongClickListener longClickListener) {
            super(itemView);
            textViewDeckName = itemView.findViewById(R.id.deck_name);

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        longClickListener.onDeckLongClick(currentDeck);
                        return true;
                    }
                }
                return false;
            });
        }
    }

}
