package com.twadeclark.brainswiper;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.twadeclark.brainswiper.database.Deck;
import com.twadeclark.brainswiper.database.DeckViewModel;
import com.twadeclark.brainswiper.databinding.ActivityDeckEditorBinding;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeckEditor extends AppCompatActivity {

    private ActivityDeckEditorBinding binding;
    private static final Executor IO_EXECUTOR = Executors.newSingleThreadExecutor();
    private DeckViewModel deckViewModel;
    private int currentDeckId = -1; // Default to -1 to indicate no deck loaded


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) { getSupportActionBar().setTitle("Edit Deck"); }
        super.onCreate(savedInstanceState);

        binding = ActivityDeckEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        currentDeckId = getIntent().getIntExtra("deckId", -1);

        initUI();
    }

    public void onSaveClicked(View view) {
        String deckName = binding.editTextTextDeckName.getText().toString().trim();
        String deckContents = binding.editTextTextMultiLine.getText().toString().trim();

        if (deckName.isEmpty() ) {
            Date d = new Date();
            deckName = "Created " + d;
        }

        if (currentDeckId == -1) { // new deck
            if (deckContents.isEmpty() ) {
                // forget it and close
            } else {
                Deck newDeck = new Deck(deckName, deckContents);
                deckViewModel.insertDeck(newDeck);
            }
        } else { // existing deck
            Deck existingDeck = new Deck(deckName, deckContents, currentDeckId);

            if (deckContents.isEmpty()) {
                deckViewModel.deleteDeck(existingDeck);
            } else {
                deckViewModel.updateDeck(existingDeck);
            }
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