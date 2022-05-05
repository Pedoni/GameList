package com.example.mygamelist.Recyclers.Notifications;

public final class NotificationCard {

    private final String imageResource;
    private final String type;
    private final String text;
    private final String friends;

    public NotificationCard(final String imageResource, final String type, final String text, final String friends){
        this.imageResource = imageResource;
        this.type = type;
        this.text = text;
        this.friends = friends;
    }

    public String getImageResource() {
        return this.imageResource;
    }

    public String getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }

    public String getFriends() {
        return this.friends;
    }

}
