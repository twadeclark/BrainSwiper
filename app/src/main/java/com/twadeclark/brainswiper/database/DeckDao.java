package com.twadeclark.brainswiper.database;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DeckDao {
    @Query("SELECT * FROM decks")
    LiveData<List<Deck>> getAllDecks();

    @Insert
    void insert(Deck deck);

    @Delete
    void delete(Deck deck);

//    @Query("SELECT deckName FROM decks")
//    LiveData<List<String>> getAllDeckNames();

}
