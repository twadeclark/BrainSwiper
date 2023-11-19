package com.twadeclark.brainswiper.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {

    private DeckRepository mRepository;
    private LiveData<List<String>> mAllDecks;

    public DeckViewModel(Application application) {
        super(application);
        mRepository = new DeckRepository(application);
        mAllDecks = mRepository.getAllDeckNames();
    }

    public LiveData<List<String>> getAllDecks() {
        return mAllDecks;
    }

    // Other ViewModel methods...
}
