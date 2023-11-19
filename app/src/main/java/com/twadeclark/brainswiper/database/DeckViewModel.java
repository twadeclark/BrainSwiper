package com.twadeclark.brainswiper.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {

    private DeckRepository mRepository;

    public DeckViewModel(Application application) {
        super(application);
        mRepository = new DeckRepository(application);
    }

    public LiveData<List<Deck>> getAllDecks() {
        return mRepository.getAllDecks();
    }

    public void insertDeck(Deck deck) {
        mRepository.insertDeck(deck);
    }

    public void updateDeck(Deck deck) {
        mRepository.updateDeck(deck);
    }

}
