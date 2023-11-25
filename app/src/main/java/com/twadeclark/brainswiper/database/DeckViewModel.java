package com.twadeclark.brainswiper.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
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


    // maintain state on screen rotate
//    private MutableLiveData<List<Flashcard>> flashcardList = new MutableLiveData<>();
//    private MutableLiveData<Integer> currentCardIndex = new MutableLiveData<>();
//    private MutableLiveData<Boolean> isFrontOfCardShown = new MutableLiveData<>();
//    private MutableLiveData<Integer> deckLength = new MutableLiveData<>();
//    private MutableLiveData<Integer> deckIdFromIntent = new MutableLiveData<>();
//    private MutableLiveData<Deck> thisDeck = new MutableLiveData<>();
    private List<Flashcard> flashcardList = new ArrayList<Flashcard>();
    private Integer currentCardIndex = 0;
    private Boolean isFrontOfCardShown = true;

    public Integer incrementCurrentCardIndex() {
        return currentCardIndex++;
    }

    public List<Flashcard> getFlashcardList() {
        return flashcardList;
    }

    public void setFlashcardList(List<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }

    public Integer getCurrentCardIndex() {
        return currentCardIndex;
    }

    public void setCurrentCardIndex(Integer currentCardIndex) {
        this.currentCardIndex = currentCardIndex;
    }

    public Boolean getFrontOfCardShown() {
        return isFrontOfCardShown;
    }

    public void setFrontOfCardShown(Boolean frontOfCardShown) {
        isFrontOfCardShown = frontOfCardShown;
    }



}
