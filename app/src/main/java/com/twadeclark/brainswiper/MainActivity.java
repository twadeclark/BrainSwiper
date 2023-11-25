package com.twadeclark.brainswiper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
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
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DeckAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.twadeclark.brainswiper.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        loadDeckNames();

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

        DeckViewModel mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        Log.d("MainActivity", "+ mDeckViewModel.toString(): " + mDeckViewModel.toString());

        Log.d("MainActivity", "+ mDeckViewModel.getAllDecks().toString(): " + mDeckViewModel.getAllDecks().toString());

        LiveData<List<Deck>> liveTemp = mDeckViewModel.getAllDecks();
        Object o1 = liveTemp.getValue();
        Object o2 = liveTemp.isInitialized();
//        Log.d("MainActivity", "+ o.toString(): " + o1.toString());
//        Log.d("MainActivity", "+ o.toString(): " + o2.toString());
//        Log.d("MainActivity", "+ here: ");


        mDeckViewModel.getAllDecks().observe(this, decks -> mAdapter.setDecks(decks));
    }

}

