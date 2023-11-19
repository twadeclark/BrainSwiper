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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeckEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        initUI();
    }

    public void onSaveClicked(View view) {
        String deckName = binding.editTextTextDeckName.getText().toString().trim();
        String deckContents = binding.editTextTextMultiLine.getText().toString();

        if (!deckName.isEmpty()) {
            Deck newDeck = new Deck(deckName, deckContents);
            deckViewModel.insertDeck(newDeck);
        }

        finish();
    }
//
//    public static void insertDeck(final DeckDao deckDao, final Deck deck) {
//        IO_EXECUTOR.execute(new Runnable() {
//            @Override
//            public void run() {
//                deckDao.insert(deck);
//            }
//        });
//    }
//
//    private static class InsertDeckAsyncTask extends AsyncTask<Deck, Void, Void> {
//        private DeckDao asyncTaskDao;
//
//        InsertDeckAsyncTask(DeckDao dao) {
//            asyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final Deck... params) {
//            asyncTaskDao.insert(params[0]);
//            return null;
//        }
//    }

    public void onCancelClicked(View view) {
        finish();
    }

    private void initUI() {

    }

}