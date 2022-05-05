package com.example.mygamelist.Database.Entities;

import androidx.room.Entity;

@Entity(tableName = "commentlike", primaryKeys = {"userId", "commentUserId", "gameId"})
public final class CommentLike {
    public long userId;
    public long commentUserId;
    public long gameId;

    public CommentLike(final long userId, final long commentUserId, final long gameId){
        this.userId = userId;
        this.commentUserId = commentUserId;
        this.gameId = gameId;
    }

}