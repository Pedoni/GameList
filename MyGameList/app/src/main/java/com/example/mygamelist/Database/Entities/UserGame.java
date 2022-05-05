package com.example.mygamelist.Database.Entities;

import android.util.Pair;

import androidx.room.Entity;

import com.example.mygamelist.Activities.HomeActivity;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "usergame", primaryKeys = {"userId", "gameId"})
public final class UserGame {
    public final long userId;
    public final long gameId;
    public final int rating;
    public final String comment;
    public final int state;
    public final boolean favourite;
    public final String timestamp;
    public static final int GAMES_COUNT = 17;
    public static final int USERS_COUNT = 15;

    public UserGame(final long userId, final long gameId, final int rating, final String comment, final int state, final boolean favourite, final String timestamp) {
        this.userId = userId;
        this.gameId = gameId;
        this.rating = rating;
        this.comment = comment;
        this.state = state;
        this.favourite = favourite;
        this.timestamp = timestamp;
    }

    public static UserGame[] populateData() {
        List<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>() {{
            add(new Pair<>("Spettacolare", 5));
            add(new Pair<>("Niente di che", 3));
            add(new Pair<>("Non c'è male", 3));
            add(new Pair<>("Un gioco molto carino", 4));
            add(new Pair<>("Bellissimo", 5));
            add(new Pair<>("Così così", 3));
            add(new Pair<>("Vale la pena di giocarlo per me", 4));
            add(new Pair<>("Non mi è piaciuto", 2));
            add(new Pair<>("Bello!", 5));
            add(new Pair<>("SpetIl peggior gioco a cui abbia mai giocato...acolare", 1));
            add(new Pair<>("Mah", 3));
            add(new Pair<>("Bruttino", 2));
            add(new Pair<>("Una storia molto piatta", 2));
            add(new Pair<>("Molto bello questo gioco!", 5));
            add(new Pair<>("Non giocatelo, risparmiate i soldi", 1));
        }};
        List<UserGame> ug = new ArrayList<>();
        for(int i=1;i<USERS_COUNT+1;i++){
            for(int j=1;j<GAMES_COUNT+1;j++){
                ug.add(new UserGame(i, j, list.get(rand(USERS_COUNT)).second, list.get(rand(USERS_COUNT)).first, rand(3), false, HomeActivity.getTimestamp()));
            }
        }
        UserGame[] ugg = new UserGame[ug.size()];
        ug.toArray(ugg);
        return ugg;
    }

    private static int rand(final int i) {
        return (int)Math.floor(Math.random()*i);
    }

}


