package com.twadeclark.brainswiper.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DeckDao {
    @Query("UPDATE decks SET lastAccessed = :lastAccessed WHERE id = :deckId")
    void updateLastAccessed(long deckId, long lastAccessed);

    @Query("SELECT * FROM decks ORDER BY lastAccessed DESC")
    LiveData<List<Deck>> getAllDecks();

    @Query("SELECT * FROM decks WHERE id = :deckId")
    LiveData<Deck> getDeckById(int deckId);

    @Insert
    void insert(Deck deck);

    @Insert
    void insertAll(Deck... decks);

    @Delete
    void delete(Deck deck);

    @Update
    void update(Deck deck);

}
