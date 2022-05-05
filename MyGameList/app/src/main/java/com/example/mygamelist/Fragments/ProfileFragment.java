package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Friendship;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.Notification;
import com.example.mygamelist.Database.Entities.UserGame;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.Top.CardAdapter;
import com.example.mygamelist.Recyclers.Top.TopCard;
import com.example.mygamelist.Utilities;
import com.google.android.material.button.MaterialButton;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ProfileFragment extends Fragment {

    private List<UserGame> usergames = new ArrayList<>();
    private static final String LOG = "ProfileFragment";
    public static String LIST;
    private List<UserGame> favgames = new ArrayList<>();
    private MaterialButton gameListButton;
    private MaterialButton favListButton;
    private AppDatabase db;
    private MaterialButton friendReq;
    private FragmentState fs;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            this.fs = FragmentState.getInstance(activity.getApplicationContext());
            if(fs.isUserBack()){
                HomeActivity.currentGame = fs.getLastUser();
            } else {
                this.fs.setUserGoing(false);
            }
            String name = db.generalDao().getUser(HomeActivity.currentUser).userFirstName;
            String surname = db.generalDao().getUser(HomeActivity.currentUser).userLastName;
            String us = db.generalDao().getUser(HomeActivity.currentUser).username;
            ((TextView)getView().findViewById(R.id.profileName)).setText(name + " " + surname);
            ((TextView)getView().findViewById(R.id.profileUsername)).setText(us);
            setPages(activity);
            setFriendship(activity);
            setScrollbars(activity);
            setStats(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setStats(final Activity activity) {
        CardView statsText = getView().findViewById(R.id.statsText);
        statsText.setOnClickListener(v -> Utilities.pushFragment((AppCompatActivity) activity, new StatsFragment(), "StatsFragment"));
    }

    private void setFriendshipListener(final Activity activity, final Friendship f){
        if(f.state){
            friendReq.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.requestRed)));
            String remf = getString(R.string.remove_friend);
            friendReq.setText(remf);
            friendReq.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_friend_remove_24, 0, 0, 0);
            friendReq.setOnClickListener((v)->{
                this.fs.setVideogameGoing(true);
                db.generalDao().deleteFriendship(f);
                for(int i = 0;i<=1;i++){
                    db.generalDao().removeNotification(new Notification(Long.parseLong(HomeActivity.USER_ID), i, false,
                            Long.parseLong(HomeActivity.currentUser), "", ""));
                    db.generalDao().removeNotification(new Notification(Long.parseLong(HomeActivity.currentUser), i, false,
                            Long.parseLong(HomeActivity.USER_ID), "", ""));
                }
                Utilities.pushFragment((AppCompatActivity) activity, new RefreshFragment(), "RefreshFragment");
            });
        } else if(String.valueOf(f.sender).equals(HomeActivity.USER_ID)) {
            friendReq.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.requestRed)));
            friendReq.setText(getString(R.string.remove_request));
            friendReq.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_friend_remove_24, 0, 0, 0);
            friendReq.setOnClickListener(v -> {
                this.fs.setVideogameGoing(true);
                db.generalDao().deleteFriendship(f);
                db.generalDao().removeNotification(new Notification(Long.parseLong(HomeActivity.currentUser), 0, false,
                        Long.parseLong(HomeActivity.USER_ID), "", ""));
                db.generalDao().removeNotification(new Notification(Long.parseLong(HomeActivity.currentUser), 1, false,
                        Long.parseLong(HomeActivity.USER_ID), "", ""));
                Utilities.pushFragment((AppCompatActivity) activity, new RefreshFragment(), "RefreshFragment");
            });
        } else if(String.valueOf(f.sender).equals(HomeActivity.currentUser)){
            friendReq.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.requestGreen)));
            String accr = getString(R.string.accept_request);
            friendReq.setText(accr);
            friendReq.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_friend_24, 0, 0, 0);
            friendReq.setOnClickListener(v -> {
                this.fs.setVideogameGoing(true);
                String username = db.generalDao().getUser(HomeActivity.USER_ID).username;
                String notText = username + getString(R.string.friend_accepted);
                long tsLong = System.currentTimeMillis()/1000;
                String ts = Long.toString(tsLong);
                db.generalDao().insertNotification(new Notification(Long.parseLong(HomeActivity.currentUser), 1, false,
                        Long.parseLong(HomeActivity.USER_ID), notText, ts));
                db.generalDao().deleteFriendship(f);
                db.generalDao().addFriendship(new Friendship(Long.parseLong(HomeActivity.USER_ID), Long.parseLong(HomeActivity.currentUser),
                        Long.parseLong(HomeActivity.USER_ID), true));
                Utilities.pushFragment((AppCompatActivity) activity, new RefreshFragment(), "RefreshFragment");
            });
        }
    }

    private void setFriendship(final Activity activity) {
        this.friendReq = getView().findViewById(R.id.requestButton);
        if(HomeActivity.USER_ID.equals(HomeActivity.currentUser)){
            friendReq.setVisibility(View.GONE);
        } else {
            List<Friendship> fships = db.generalDao().getFriendUsersAll(HomeActivity.USER_ID);
            for(Friendship f: fships) {
                if(String.valueOf(f.firstUserId).equals(HomeActivity.currentUser) || String.valueOf(f.secondUserId).equals(HomeActivity.currentUser)){
                    setFriendshipListener(activity, f);
                    return;
                }
            }
            createFriendship(activity);
        }
    }

    private void createFriendship(final Activity activity) {
        friendReq.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.requestGreen)));
        friendReq.setOnClickListener(v -> {
            this.fs.setVideogameGoing(true);
            db.generalDao().addFriendship(new Friendship(Long.parseLong(HomeActivity.USER_ID), Long.parseLong(HomeActivity.currentUser),
                    Long.parseLong(HomeActivity.USER_ID), false));
            String username = db.generalDao().getUser(HomeActivity.USER_ID).username;
            String notText = username + getString(R.string.friend_request);
            db.generalDao().insertNotification(new Notification(Long.parseLong(HomeActivity.currentUser), 0, false,
                    Long.parseLong(HomeActivity.USER_ID), notText, HomeActivity.getTimestamp()));
            Utilities.pushFragment((AppCompatActivity) activity, new RefreshFragment(), "RefreshFragment");
        });
    }

    private void setPages(final Activity activity) {
        Button settingsText = getView().findViewById(R.id.settingsText);
        CardView friendsText = getView().findViewById(R.id.friendsText);
        this.gameListButton = getView().findViewById(R.id.buttonUserGames);
        this.favListButton = getView().findViewById(R.id.buttonFavGames);
        settingsText.setOnClickListener(v -> Utilities.pushFragment((AppCompatActivity) activity, new SettingsFragment(), "SettingsFragment"));
        friendsText.setOnClickListener(v -> {
            if(!HomeActivity.currentUser.equals(HomeActivity.USER_ID)){
                fs.addUser(Long.parseLong(HomeActivity.currentUser));
            }
            this.fs.setUserGoing(true);
            Utilities.pushFragment((AppCompatActivity) activity, new FriendsFragment(), "FriendsFragment");
        });
        this.gameListButton.setOnClickListener(v -> {
            LIST = "all";
            Utilities.pushFragment((AppCompatActivity) activity, new GameListFragment(), "GameListFragment");
        });
        this.favListButton.setOnClickListener(v -> {
            LIST = "fav";
            Utilities.pushFragment((AppCompatActivity) activity, new GameListFragment(), "GameListFragment");
        });
        if(!HomeActivity.USER_ID.equals(HomeActivity.currentUser)){
            settingsText.setVisibility(View.GONE);
        }
    }

    private void setScrollbars(final Activity activity) {
        setUsergames(activity);
        setFavourites(activity);
    }

    private void setFavourites(final Activity activity) {
        RecyclerView favGamesView = getView().findViewById(R.id.favGames);
        this.favgames = db.generalDao().getFavGamesFromUser(HomeActivity.currentUser);
        CardAdapter cardAdapter;
        List<TopCard> listfg = new ArrayList<>();
        if(this.favgames.isEmpty()){
            this.favListButton.setEnabled(false);
            listfg.add(new TopCard("ic_videogame_24",getString(R.string.no_games_added),""));
            cardAdapter = new CardAdapter(listfg, activity, position -> {});
        } else {
            List<Game> games = db.generalDao().getAllGames();
            List<Long> gameIds = new ArrayList<>();
            for(final UserGame ug: this.favgames){
                gameIds.add(ug.gameId);
            }
            for(final Game g: games){
                if(gameIds.contains(g.gameId)){
                    listfg.add(new TopCard(g.gameImage, g.gameName,g.platform));
                }
            }
            Collections.reverse(gameIds);
            Collections.reverse(listfg);
            Collections.reverse(this.favgames);
            cardAdapter = new CardAdapter(listfg, activity, position -> {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                if (appCompatActivity != null) {
                    HomeActivity.currentGame = this.favgames.get(position).gameId;
                    Utilities.pushFragment(appCompatActivity, new VideogameFragment(), "VideogameFragment");
                }
            });
        }
        favGamesView.setAdapter(cardAdapter);
    }

    private void setUsergames(final Activity activity) {
        CardAdapter cardAdapter;
        RecyclerView userGamesView = getView().findViewById(R.id.userGames);
        this.usergames = db.generalDao().getAllUserGamesFromUser(HomeActivity.currentUser);
        List<TopCard> listug = new ArrayList<>();
        if(this.usergames.isEmpty()){
            this.gameListButton.setEnabled(false);
            listug.add(new TopCard("ic_videogame_24",getString(R.string.no_games_added),""));
            cardAdapter = new CardAdapter(listug, activity, position -> {});
        } else {
            List<Game> games = db.generalDao().getAllGames();
            List<Long> gameIds = new ArrayList<>();
            for(final UserGame ug: this.usergames){
                gameIds.add(ug.gameId);
            }
            for(final Game g: games){
                if(gameIds.contains(g.gameId)){
                    listug.add(new TopCard(g.gameImage,g.gameName,g.platform));
                }
            }
            Collections.reverse(gameIds);
            Collections.reverse(listug);
            Collections.reverse(this.usergames);
            cardAdapter = new CardAdapter(listug, activity, position -> {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                if(appCompatActivity != null) {
                    HomeActivity.currentGame = this.usergames.get(position).gameId;
                    Utilities.pushFragment(appCompatActivity, new VideogameFragment(), "VideogameFragment");
                }
            });
        }
        userGamesView.setAdapter(cardAdapter);
    }

}
