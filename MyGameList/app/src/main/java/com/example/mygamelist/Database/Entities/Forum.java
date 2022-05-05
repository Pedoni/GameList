package com.example.mygamelist.Database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mygamelist.Activities.HomeActivity;

@Entity(tableName = "forum")
public final class Forum {
    @PrimaryKey(autoGenerate = true)
    public long forumId;
    public long userId;
    public final String forumTitle;
    public final String forumText;
    public final String forumGame;
    public final String forumCreationDate;

    public Forum(final long userId, final String forumTitle, final String forumText, final String forumGame, final String forumCreationDate){
        this.userId = userId;
        this.forumTitle = forumTitle;
        this.forumText = forumText;
        this.forumGame = forumGame;
        this.forumCreationDate = forumCreationDate;
    }

    public static Forum[] populateData() {
        return new Forum[]{
                new Forum(1, "AC3 sopravvalutato?", "Voi pensate che Assassin's Creed 3 sia un gioco sopravvalutato?",
                        "6", HomeActivity.getTimestamp()),
                new Forum(2, "Giocatori di BO2", "Qualcuno gioca ancora a Black Ops 2?",
                        "2", HomeActivity.getTimestamp()),
                new Forum(3, "Meglio del 4?", "Secondo voi far cry 3 è meglio di Far Cry 4?",
                        "3", HomeActivity.getTimestamp()),
                new Forum(4, "Mario Odyssey capolavoro di Nintendo", "Concordate con me?",
                        "4", HomeActivity.getTimestamp()),
                new Forum(5, "Calo costante", "Perché ogni anno Fifa è sempre peggio?",
                        "5", HomeActivity.getTimestamp()),
                new Forum(7, "Ma il prossimo??", "Che fine ha fatto GTA 6?",
                        "7", HomeActivity.getTimestamp()),
                new Forum(11, "Rivelazione della stagione", "Biomutant è la vera rivelazione di questa stagione di videogiochi?",
                        "11", HomeActivity.getTimestamp()),
        };
    }

}