package com.twadeclark.brainswiper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.twadeclark.brainswiper.database.Deck;
import com.twadeclark.brainswiper.database.DeckViewModel;
import com.twadeclark.brainswiper.database.DeckAdapter;
import com.twadeclark.brainswiper.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DeckViewModel mDeckViewModel;
    private DeckAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        loadDeckNames();

        mAdapter.setOnDeckLongClickListener(deck -> {
            Intent intent = new Intent(MainActivity.this, DeckEditor.class);
            intent.putExtra("deckId", deck.getId()); // Make sure your Deck class has an ID field
            intent.putExtra("deckName", deck.getDeckName());
            intent.putExtra("deckContents", deck.getDeckContents());
            startActivity(intent);
        });

        mAdapter.setOnDeckClickListener(deck -> {
            Intent intent = new Intent(MainActivity.this, FlashcardActivity.class);
            intent.putExtra("deckId", deck.getId()); // Make sure your Deck class has an ID field
            intent.putExtra("deckName", deck.getDeckName());
            intent.putExtra("deckContents", deck.getDeckContents());
            startActivity(intent);
        });
    }

    public void createNewDeck(View view) {
        Intent intent = new Intent(MainActivity.this, DeckEditor.class);
        startActivity(intent);
    }

    public void loadDeckNames() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DeckAdapter();
        recyclerView.setAdapter(mAdapter);

        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
//        Log.d("MainActivity", "+ mDeckViewModel.toString(): " + mDeckViewModel.toString());

        mDeckViewModel.getAllDecks().observe(this, new Observer<List<Deck>>() {
            @Override
            public void onChanged(@Nullable final List<Deck> decks) {
                mAdapter.setDecks(decks);
            }
        });
    }

}

