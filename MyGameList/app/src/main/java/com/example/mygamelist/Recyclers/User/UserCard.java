package com.example.mygamelist.Recyclers.User;

public final class UserCard {

    private final String imageResource;
    private final String name;
    private final String firstLastName;
    private final String email;

    public UserCard(final String imageResource, final String name, final String firstLastName, final String email){
        this.imageResource = imageResource;
        this.name = name;
        this.firstLastName = firstLastName;
        this.email = email;
    }

    public String getImageResource() {
        return this.imageResource;
    }

    public String getName() {
        return this.name;
    }

    public String getFirstLastName() {
        return this.firstLastName;
    }

    public String getEmail() {
        return this.email;
    }

}
