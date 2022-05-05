package com.example.mygamelist.Recyclers.Forum;

public final class ForumCard {

    private final String imageResource;
    private final String title;
    private final String forumGame;
    private final String forumUser;
    private final String comments;

    public ForumCard(final String imageResource, final String title, final String forumGame, final String forumUser, final String comments){
        this.imageResource = imageResource;
        this.title = title;
        this.forumGame = forumGame;
        this.forumUser = forumUser;
        this.comments = comments;
    }

    public String getImageResource() {
        return this.imageResource;
    }

    public String getTitle() {
        return this.title;
    }

    public String getGame() {
        return this.forumGame;
    }

    public String getUser() {
        return this.forumUser;
    }

    public String getComments() {
        return this.comments;
    }

}
