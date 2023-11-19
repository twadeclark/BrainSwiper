package com.twadeclark.brainswiper.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "decks")
public class Deck {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "deckName")
    public String deckName;
    @ColumnInfo(name = "deckContents")
    public String deckContents;

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
