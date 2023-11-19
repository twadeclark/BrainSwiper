package com.twadeclark.brainswiper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;

import com.twadeclark.brainswiper.database.Deck;
import com.twadeclark.brainswiper.database.DeckDao;
import com.twadeclark.brainswiper.database.AppDatabase;
import com.twadeclark.brainswiper.database.DeckViewModel;
import com.twadeclark.brainswiper.database.MyAdapter;
import com.twadeclark.brainswiper.databinding.ActivityDeckEditorBinding;
import com.twadeclark.brainswiper.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DeckViewModel mDeckViewModel;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        loadDeckNames();
    }

    public void createNewDeck(View view) {
        Intent intent = new Intent(MainActivity.this, DeckEditor.class);
        startActivity(intent);
    }

    public void loadDeckNames() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
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

