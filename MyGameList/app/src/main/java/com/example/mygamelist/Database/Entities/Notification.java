package com.example.mygamelist.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification", primaryKeys = {"userId", "type", "involvedUser"})
public final class Notification {
    public final long userId;
    public final int type;    //0 friend coming friend requests, 1 accepted friend requests
    public final boolean read;    //read or not by the user
    public final long involvedUser; //if is a friend request, the id of the other user
    public final String text;
    public final String timestamp;

    public Notification(final long userId, final int type, final boolean read, final long involvedUser, final String text, final String timestamp){
        this.userId = userId;
        this.type = type;
        this.read = read;
        this.involvedUser = involvedUser;
        this.text = text;
        this.timestamp = timestamp;
    }

}