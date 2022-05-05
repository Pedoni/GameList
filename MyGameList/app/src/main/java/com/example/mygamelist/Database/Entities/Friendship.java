package com.example.mygamelist.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "friendship", primaryKeys = {"firstUserId", "secondUserId"})
public final class Friendship {
    public final long firstUserId;
    public final long secondUserId;
    public final long sender; //è l'ID dell'utente che ha mandato la richiesta
    public final boolean state; //false è richiesta in corso, true è richiesta accettata

    public Friendship(final long firstUserId, final long secondUserId, final long sender, final boolean state){
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.sender = sender;
        this.state = state;
    }

    public static Friendship[] populateData() {
        return new Friendship[] {
                new Friendship(1, 2, 1, true),
                new Friendship(1, 3, 1, true),
                new Friendship(2, 3, 3, true),
                new Friendship(4, 1, 1, true),
                new Friendship(4, 2, 2, true),
                new Friendship(4, 3, 4, true)
        };
    }

}