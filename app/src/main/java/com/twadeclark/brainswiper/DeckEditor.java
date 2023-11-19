package com.twadeclark.brainswiper;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
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

public class DeckEditor extends AppCompatActivity {

    private ActivityDeckEditorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeckEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
    }

    public void onSaveClicked(View view) {
        // TODO
// Example in DeckEditor.java

// Step 1: Create an instance of the Room Database
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

// Step 2: Access the DAO
        DeckDao deckDao = db.deckDao();

// Step 3: Create a Deck object
        Deck newDeck = new Deck();
//        newDeck.setDeckName("My New Deck");
//        newDeck.setDeckContents("Contents of the new deck");

        String n = String.valueOf(this.binding.editTextTextDeckName.getText()).trim();
        String m = String.valueOf(this.binding.editTextTextMultiLine.getText());


        if (!n.isEmpty()) {
            newDeck.setDeckName(n);
            newDeck.setDeckContents(m);


// Step 4: Execute InsertDeckAsyncTask
//        new DeckDao.InsertDeckAsyncTask(deckDao).execute(newDeck);

            deckDao.insertAll(newDeck);

            finish();
        } else {
            // TODO alert empty name not saved!
        }

    }

    public void onCancelClicked(View view) {
        finish();
    }



    private void initUI() {

    }

}