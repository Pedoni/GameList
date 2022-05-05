package com.example.mygamelist.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mygamelist.Database.Entities.CommentLike;
import com.example.mygamelist.Database.Entities.Forum;
import com.example.mygamelist.Database.Entities.ForumComment;
import com.example.mygamelist.Database.Entities.Friendship;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.Notification;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.Database.Entities.UserGame;

import java.util.List;

@Dao
public interface generalDAO {

    //users
    @Query("SELECT * FROM user")
    List<User> getAllUsers();
    @Query("SELECT * FROM user WHERE userId = :id")
    User getUser(String id);
    @Query("SELECT userId FROM user WHERE userEmail = :email")
    Long getUserIdFromMail(String email);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);
    @Delete
    void deleteUser(User user);
    @Insert
    void insertAllUsers(User... user);

    //videogames
    @Query("SELECT * FROM game")
    List<Game> getAllGames();
    @Query("SELECT * FROM game ORDER BY gameName")
    List<Game> getAllGamesSorted();
    @Query("SELECT * FROM game WHERE gameId!=:id AND series!=:series")
    List<Game> getAllOtherGames(String id, String series);
    @Insert
    void insertAllGames(Game... game);
    @Query("SELECT * FROM game WHERE gameId=:id")
    Game getGame(long id);
    @Query("SELECT * FROM game WHERE gameName=:name")
    Game getGameId(String name);
    @Query("SELECT * FROM game WHERE series=:series AND gameId!=:gameId ORDER BY publishYear DESC LIMIT 10")
    List<Game> getSameSeriesGame(String series, long gameId);
    @Query("SELECT * FROM game WHERE publisher=:publisher AND gameId!=:gameId ORDER BY publishYear DESC LIMIT 10")
    List<Game> getSamePublisherGame(String publisher, long gameId);

    //usergames
    @Query("SELECT * FROM usergame")
    List<UserGame> getAllUserGames();
    @Query("SELECT * FROM usergame")
    UserGame getUserGame();
    @Query("SELECT * FROM usergame WHERE userId = :id")
    List<UserGame> getAllUserGamesFromUser(String id);
    @Query("SELECT * FROM usergame WHERE userId = :id AND favourite = 1")
    List<UserGame> getFavGamesFromUser(String id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserGame(UserGame usergame);
    @Insert
    void insertAllUserGames(UserGame... usergame);
    @Delete
    void deleteUserGame(UserGame usergame);
    @Query("UPDATE usergame SET favourite=:f WHERE userId = :userid AND gameId=:gameid")
    void setFavourite(int f, String userid, long gameid);
    @Query("UPDATE usergame SET rating=:rating WHERE userId = :userid AND gameId=:gameid")
    void setRating(int rating, String userid, long gameid);
    @Query("SELECT rating FROM usergame WHERE userId = :userid AND gameId=:gameid")
    int getRating(String userid, long gameid);
    @Query("SELECT comment FROM usergame WHERE userId = :userid AND gameId=:gameid")
    String getComment(String userid, long gameid);
    @Query("UPDATE usergame SET comment=:comment WHERE userId = :userid AND gameId=:gameid")
    void setComment(String comment, String userid, long gameid);
    @Query("SELECT AVG(rating) FROM usergame WHERE gameId = :gameid AND rating>0")
    float getAverageScore(long gameid);

    //miste
    @Query("SELECT gameId FROM usergame WHERE rating>0 GROUP BY gameId ORDER BY AVG(rating) DESC LIMIT 10")
    List<Long> getBestGamesAllTime();
    @Query("SELECT gameId FROM usergame GROUP BY gameId ORDER BY AVG(rating) DESC")
    List<Long> getBestGamesAllTimeNoLim();
    @Query("SELECT gameId FROM usergame GROUP BY gameId ORDER BY COUNT(gameId) DESC")
    List<Long> getGamesOfTheMoment();

    //friendship
    @Insert
    void insertAllFriendships(Friendship... friendship);
    @Query("SELECT * FROM friendship WHERE (firstUserId=:id OR secondUserId=:id) AND state=1")
    List<Friendship> getFriendUsers(String id);
    @Query("SELECT * FROM friendship WHERE (firstUserId=:id OR secondUserId=:id)")
    List<Friendship> getFriendUsersAll(String id);
    @Delete
    void deleteFriendship(Friendship f);
    @Insert
    void addFriendship(Friendship f);

    //comment likes
    @Insert
    void insertLike(CommentLike commentlike);
    @Delete
    void removeLike(CommentLike commentlike);
    @Query("SELECT * FROM commentlike")
    List<CommentLike> getAllCommentLikes();
    @Query("SELECT COUNT(*) FROM commentlike WHERE commentUserId=:commentUserId AND gameId=:gameId")
    int getNumOfCommentLikesOfOneComment(long commentUserId, long gameId);

    //notifications
    @Query("SELECT * FROM notification WHERE userId=:userId ORDER BY timestamp DESC")
    List<Notification> getAllUserNotifications(String userId);
    @Insert
    void insertNotification(Notification notification);
    @Query("UPDATE notification SET read = 1 WHERE userId = :userId")
    void readAllNotification(String userId);
    @Query("SELECT COUNT(*) FROM notification WHERE userId=:userId AND read=0")
    int getNumberOfNotifications(String userId);
    @Delete
    void removeNotification(Notification notification);

    //forum
    @Insert
    void insertAllForums(Forum... forum);
    @Insert
    void insertForum(Forum forum);
    @Delete
    void deleteForum(Forum forum);
    @Insert
    void insertAllForumComments(ForumComment... forumcomment);
    @Insert
    void insertForumComment(ForumComment forumcomment);
    @Query("SELECT * FROM forum WHERE forumId=:forumId")
    Forum getForum(long forumId);
    @Query("SELECT * FROM forum ORDER BY forumCreationDate DESC")
    List<Forum> getAllForumsOrdered();
    @Query("SELECT * FROM forum WHERE userId=:userId ORDER BY forumCreationDate DESC LIMIT 1")
    Forum getLastUserForum(long userId);
    @Query("SELECT * FROM forumcomment WHERE forumId=:forumId ORDER BY timestamp ASC")
    List<ForumComment> getAllForumComments(long forumId);
    @Query("SELECT COUNT(*) FROM forumcomment WHERE forumId=:forumId")
    int getNumberOfForumComments(long forumId);

    //stats
    @Query("SELECT COUNT(*) FROM usergame WHERE userId=:userId")
    int statsAdded(long userId);
    @Query("SELECT COUNT(*) FROM usergame WHERE userId=:userId AND favourite=1")
    int statsFav(long userId);
    @Query("SELECT COUNT(*) FROM usergame WHERE userId=:userId AND comment!=''")
    int statsCom(long userId);
    @Query("SELECT COUNT(*) FROM commentlike WHERE commentUserId=:userId")
    int statsComLike(long userId);
    @Query("SELECT COUNT(*) FROM forum WHERE userId=:userId")
    int statsPosts(long userId);
    @Query("SELECT AVG(rating) AS score FROM usergame WHERE userId=:userId")
    float statsMediumScore(long userId);

    //settings
    @Query("SELECT userPassword FROM user WHERE userId=:userId")
    String getActualPsw(long userId);
    @Query("UPDATE user SET userPassword=:psw WHERE userId = :userId")
    void setNewPsw(long userId, String psw);
    @Query("SELECT username FROM user")
    List<String> getAllUsernames();

    @Query("UPDATE user SET username=:username WHERE userId = :userId")
    void updateUsername(long userId, String username);
}
