package com.twadeclark.brainswiper.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DeckRepository {
    private DeckDao deckDao;
    private LiveData<List<String>> allDecks;

    // Constructor
    public DeckRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        deckDao = db.deckDao();
        allDecks = deckDao.getAllDeckNames();
    }

    // Method to get all deck names
    public LiveData<List<String>> getAllDeckNames() {
        return allDecks;
    }

    // Additional methods for other database operations
    // ...
}
