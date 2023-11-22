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

    public interface OnDeckClickListener {
        void onDeckClick(Deck deck);
    }


    private OnDeckLongClickListener longClickListener;
    private OnDeckClickListener clickListener;

    public void setOnDeckLongClickListener(OnDeckLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setOnDeckClickListener(OnDeckClickListener listener) {
        this.clickListener = listener;
    }

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
        holder.button.setText(currentDeck.getDeckName());

        holder.button.setOnClickListener(v -> {
            if (clickListener != null) {
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onDeckClick(currentDeck);
                }
            }
        });

        holder.bindDeck(currentDeck);
    }

    @Override
    public int getItemCount() {
        int retVal = decks == null ? 0 : decks.size();
        return retVal;
    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        androidx.appcompat.widget.AppCompatButton button;
        private Deck currentDeck;

        public void bindDeck(Deck deck) {
            this.currentDeck = deck;
        }

        public DeckViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.deck_name);

        }
    }

}
