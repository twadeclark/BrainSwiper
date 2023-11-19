package com.twadeclark.brainswiper.database;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeckDao {
    @Query("SELECT * FROM decks")
    LiveData<List<Deck>> getAllDecks();

    @Insert
    void insert(Deck deck);

    @Delete
    void delete(Deck deck);

    @Update
    void update(Deck deck);

}
