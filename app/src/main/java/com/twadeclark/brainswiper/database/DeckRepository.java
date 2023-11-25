package com.twadeclark.brainswiper.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DeckRepository {
    private final DeckDao deckDao;
    private static final Executor IO_EXECUTOR = Executors.newSingleThreadExecutor();

    public DeckRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        deckDao = db.deckDao();
    }

    public void deleteDeck(Deck deck) {
        IO_EXECUTOR.execute(() -> deckDao.delete(deck));
    }

    public void updateDeck(Deck deck) {
        IO_EXECUTOR.execute(() -> deckDao.update(deck));
    }

    public void insertDeck(Deck deck) {
        IO_EXECUTOR.execute(() -> deckDao.insert(deck));
    }

    public void updateLastAccessed(long deckId, long lastAccessed) {
        IO_EXECUTOR.execute(() -> deckDao.updateLastAccessed(deckId, lastAccessed));
    }

    public LiveData<List<Deck>> getAllDecks() {
        return deckDao.getAllDecks();
    }

    public LiveData<Deck> getDeckById(int deckId) {
        return deckDao.getDeckById(deckId);
    }

}
