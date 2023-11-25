package com.twadeclark.brainswiper.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "decks")
public class Deck {
    public Deck(String deckName, String deckContents) {
        this.deckName = deckName;
        this.deckContents = deckContents;
        this.lastAccessed = System.currentTimeMillis();
    }

    @Ignore // so Room doesn't try to use this one
    public Deck(String deckName, String deckContents, int id) {
        this.deckName = deckName;
        this.deckContents = deckContents;
        this.id = id;
        this.lastAccessed = System.currentTimeMillis();
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "deckName")
    public String deckName;
    @ColumnInfo(name = "deckContents")
    public String deckContents;

    public long getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(long lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    @ColumnInfo(name = "lastAccessed")
    private long lastAccessed;

    public int getId() {
        return id;
    }
    public String getDeckName() {
        return deckName;
    }
    public String getDeckContents() {
        return deckContents;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }
    public void setDeckContents(String deckContents) {
        this.deckContents = deckContents;
    }

}
