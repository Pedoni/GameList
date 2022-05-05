package com.example.mygamelist.Recyclers.Top;

public final class TopCard {

    private final String imageResource;
    private final String name;

    public TopCard(final String imageResource, final String name, final String console){
        this.imageResource = imageResource;
        this.name = name;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getPlace() {
        return name;
    }


}
