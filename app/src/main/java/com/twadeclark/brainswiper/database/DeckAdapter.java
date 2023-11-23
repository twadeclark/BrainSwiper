package com.twadeclark.brainswiper.database;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    private OnDeckClickListener clickListener;

    public void setOnDeckLongClickListener(OnDeckLongClickListener listener) {
    }

    public void setOnDeckClickListener(OnDeckClickListener listener) {
        this.clickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
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
        return decks == null ? 0 : decks.size();
    }

    public static class DeckViewHolder extends RecyclerView.ViewHolder {
        final androidx.appcompat.widget.AppCompatButton button;

        public void bindDeck(Deck deck) {
        }

        public DeckViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.deck_name);

        }
    }

}
