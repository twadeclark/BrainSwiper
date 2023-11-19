package com.twadeclark.brainswiper;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.InvalidationTracker;
import androidx.room.Room;

import com.twadeclark.brainswiper.database.Deck;
import com.twadeclark.brainswiper.database.DeckViewModel;
import com.twadeclark.brainswiper.databinding.ActivityDeckEditorBinding;


import com.twadeclark.brainswiper.database.DeckDao;
import com.twadeclark.brainswiper.database.AppDatabase;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeckEditor extends AppCompatActivity {

    private ActivityDeckEditorBinding binding;
    private static final Executor IO_EXECUTOR = Executors.newSingleThreadExecutor();
    private DeckViewModel deckViewModel;
    private int currentDeckId = -1; // Default to -1 to indicate no deck loaded


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeckEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        currentDeckId = getIntent().getIntExtra("DECK_ID", -1);

        initUI();
    }

    public void onSaveClicked(View view) {
        String deckName = binding.editTextTextDeckName.getText().toString().trim();
        String deckContents = binding.editTextTextMultiLine.getText().toString();

        if (currentDeckId == -1) { // new deck
            Deck newDeck = new Deck(deckName, deckContents);
            deckViewModel.insertDeck(newDeck);
        } else {
            Deck existingDeck = new Deck(deckName, deckContents, currentDeckId);
            deckViewModel.updateDeck(existingDeck);
        }

        finish();
    }

    public void onCancelClicked(View view) {
        finish();
    }

    private void initUI() {
        if (getIntent().hasExtra("deckId")) {
            // This means we are editing an existing deck
            String deckName = getIntent().getStringExtra("deckName");
            String deckContents = getIntent().getStringExtra("deckContents");

            // Set these values in your EditTexts or other views
            binding.editTextTextDeckName.setText(deckName);
            binding.editTextTextMultiLine.setText(deckContents);
        }

    }

}