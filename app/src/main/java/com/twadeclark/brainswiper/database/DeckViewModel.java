package com.twadeclark.brainswiper.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {

    private final DeckRepository mRepository;

    public DeckViewModel(Application application) {
        super(application);
        mRepository = new DeckRepository(application);
    }

    public void updateLastAccessed(long deckId, long lastAccessed) {
        mRepository.updateLastAccessed(deckId, lastAccessed);
    }

    public LiveData<List<Deck>> getAllDecks() {
        return mRepository.getAllDecks();
    }

    public LiveData<Deck> getDeckById(int deckId) {
        return mRepository.getDeckById(deckId);
    }

    public void insertDeck(Deck deck) {
        mRepository.insertDeck(deck);
    }

    public void updateDeck(Deck deck) {
        mRepository.updateDeck(deck);
    }

    public void deleteDeck(Deck deck) {
        mRepository.deleteDeck(deck);
    }


}
