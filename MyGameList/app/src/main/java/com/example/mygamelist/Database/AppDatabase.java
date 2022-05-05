package com.example.mygamelist.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mygamelist.Database.DAOs.generalDAO;
import com.example.mygamelist.Database.Entities.CommentLike;
import com.example.mygamelist.Database.Entities.Forum;
import com.example.mygamelist.Database.Entities.ForumComment;
import com.example.mygamelist.Database.Entities.Friendship;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.Notification;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.Database.Entities.UserGame;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Game.class, Forum.class, CommentLike.class, Friendship.class, UserGame.class, Notification.class,
        ForumComment.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract generalDAO generalDao();

    public synchronized static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static AppDatabase buildDatabase(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class,
                "my-database")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            getInstance(context).generalDao().insertAllUsers(User.populateData());
                            getInstance(context).generalDao().insertAllGames(Game.populateData());
                            getInstance(context).generalDao().insertAllUserGames(UserGame.populateData());
                            getInstance(context).generalDao().insertAllFriendships(Friendship.populateData());
                            getInstance(context).generalDao().insertAllForums(Forum.populateData());
                            getInstance(context).generalDao().insertAllForumComments(ForumComment.populateData());
                        });
                    }
                })
                .allowMainThreadQueries()
                .build();
    }

}