package com.example.mygamelist.Recyclers.Comment;

public final class CommentCard {

    private final String user_name;
    private final String user_surname;
    private final String comment;
    private final String likes;
    private final long commentUserId;

    public CommentCard(final String user_name, final  String user_surname, final  String comment, final  String likes, final  long commentUserId){
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.comment = comment;
        this.likes = likes;
        this.commentUserId = commentUserId;
    }

    public String getName() {
        return user_name;
    }

    public String getSurname() {
        return user_surname;
    }

    public String getComment() {
        return comment;
    }

    public String getLikes() {
        return likes;
    }

    public long getCommentUserId() { return commentUserId; }

}
