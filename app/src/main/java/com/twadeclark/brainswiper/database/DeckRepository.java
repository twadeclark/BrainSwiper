package com.twadeclark.brainswiper.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DeckRepository {
    private DeckDao deckDao;

    public DeckRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        deckDao = db.deckDao();
    }


    public LiveData<List<Deck>> getAllDecks() {
        LiveData<List<Deck>> allDecks = deckDao.getAllDecks();
        return allDecks;
    }

    public void insertNewDeck(Deck deck) {

    }

}
