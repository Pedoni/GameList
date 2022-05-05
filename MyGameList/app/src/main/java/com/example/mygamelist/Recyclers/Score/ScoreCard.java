package com.example.mygamelist.Recyclers.Score;

import android.app.Activity;

import com.example.mygamelist.R;

public final class ScoreCard {

    private final String imageResource;
    private final String name;
    private final String console;
    private final String state;
    private final int rating;

    public ScoreCard(final String imageResource, final String name, final String console, final int state, final int rating, final Activity activity){
        this.imageResource = imageResource;
        this.name = name;
        this.console = console;
        this.state = state==0 ? activity.getString(R.string.programmed) : state==1 ? activity.getString(R.string.playing) : activity.getString(R.string.played);
        this.rating = rating;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getConsole() {
        return console;
    }

    public String getRating() {
        return String.valueOf(rating);
    }

    public String getState() {
        return String.valueOf(state);
    }
}
