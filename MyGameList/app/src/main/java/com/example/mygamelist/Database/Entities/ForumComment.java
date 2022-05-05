package com.example.mygamelist.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mygamelist.Activities.HomeActivity;

@Entity(tableName = "forumcomment")
public class ForumComment {
    @PrimaryKey(autoGenerate = true)
    public long commentId;
    public final long forumId;
    public final long userId;
    public final String commentText;
    public final String timestamp;

    public ForumComment(final long forumId, final long userId, final String commentText, final String timestamp){
        this.forumId = forumId;
        this.userId = userId;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public static ForumComment[] populateData() {
        return new ForumComment[]{
                new ForumComment(1, 1, "Voi pensate che Assassin's Creed 3 sia un gioco sopravvalutato?",
                        HomeActivity.getTimestamp()),
                new ForumComment(1, 2, "No, non penso proprio",
                        HomeActivity.getTimestamp()),
                new ForumComment(2, 2, "Qualcuno gioca ancora a Black Ops 2?",
                        HomeActivity.getTimestamp()),
                new ForumComment(3, 3, "Secondo voi far cry 3 è meglio di Far Cry 4?",
                        HomeActivity.getTimestamp()),
                new ForumComment(4, 4, "Concordate con me?",
                        HomeActivity.getTimestamp()),
                new ForumComment(5, 5, "Perché ogni anno Fifa è sempre peggio?",
                        HomeActivity.getTimestamp()),
                new ForumComment(6, 7, "Che fine ha fatto GTA 6?",
                        HomeActivity.getTimestamp()),
                new ForumComment(7, 11, "Biomutant è la vera rivelazione di questa stagione di videogiochi?",
                        HomeActivity.getTimestamp()),
        };
    }

}