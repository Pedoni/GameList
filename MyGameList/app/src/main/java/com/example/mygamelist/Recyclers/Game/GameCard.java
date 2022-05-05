package com.example.mygamelist.Recyclers.Game;

public final class GameCard {

    private final String imageResource;
    private final String name;
    private final String console;
    private final String year;

    public GameCard(final String imageResource, final String name, final String console, final String year){
        this.imageResource = imageResource;
        this.name = name;
        this.console = console;
        this.year = year;
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

    public String getYear() {
        return year;
    }

}
