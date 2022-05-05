package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.UserGame;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.Score.CardAdapter;
import com.example.mygamelist.Recyclers.Score.ScoreCard;
import com.example.mygamelist.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GameListFragment extends Fragment {

    private static final String LOG = "GameListFragment";
    private AppDatabase db;
    private List<UserGame> games;
    private List<Game> searchedGames;
    private RecyclerView recyclerViewGames;
    private CardAdapter cardAdapterGames;
    private Button all;
    private Button prog;
    private Button playing;
    private Button played;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            setButtons(activity);
            setList(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setButtons(final Activity activity) {
        all = getView().findViewById(R.id.allGameList);
        prog = getView().findViewById(R.id.programmedGameList);
        playing = getView().findViewById(R.id.playedGameList);
        played = getView().findViewById(R.id.playingGameList);
        all.setOnClickListener(v -> setListType("all", activity));
        prog.setOnClickListener(v -> setListType("prog", activity));
        playing.setOnClickListener(v -> setListType("playing", activity));
        played.setOnClickListener(v -> setListType("played", activity));
    }

    private void setListType(final String type, final Activity activity) {
        ProfileFragment.LIST = type;
        Utilities.pushFragment((AppCompatActivity)activity, new RefreshFragment(), "RefreshFragment");
    }

    private void setList(final Activity activity) {
        recyclerViewGames = getView().findViewById(R.id.scoreListRecycle);
        recyclerViewGames.setHasFixedSize(true);
        Log.e("SL", ProfileFragment.LIST);
        if(ProfileFragment.LIST.equals("all") || ProfileFragment.LIST.equals("played") ||
                ProfileFragment.LIST.equals("playing") || ProfileFragment.LIST.equals("prog")) {
            setView(activity, ProfileFragment.LIST);
        } else if(ProfileFragment.LIST.equals("fav")){
            getView().findViewById(R.id.linearLayoutSearch).setVisibility(View.GONE);
            List<ScoreCard> favGames = new ArrayList<>();
            games = db.generalDao().getFavGamesFromUser(HomeActivity.currentUser);
            if(!games.isEmpty()){
                searchedGames = new ArrayList<>();
                for(final UserGame u: games) {
                    Game g = db.generalDao().getGame(u.gameId);
                    searchedGames.add(g);
                    favGames.add(new ScoreCard(g.gameImage, g.gameName, g.platform, u.state, u.rating, activity));
                }
                Collections.sort(favGames, (v, w)-> v.getName().compareTo(w.getName()));
                Collections.sort(searchedGames, (v, w)-> v.gameName.compareTo(w.gameName));
                cardAdapterGames = new CardAdapter(favGames, activity, (position)->{
                    AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                    if(appCompatActivity != null) {
                        HomeActivity.currentGame = searchedGames.get(position).gameId;
                        Utilities.pushFragment(appCompatActivity, new VideogameFragment(), "VideogameFragment");
                    }
                });
                recyclerViewGames.setAdapter(cardAdapterGames);
            }
        }
    }

    private void setView(final Activity activity, final String type) {
        setColors();
        switch (type) {
            case "all":
                all.setTextColor(Color.WHITE);
                break;
            case "prog":
                prog.setTextColor(Color.WHITE);
                break;
            case "played":
                played.setTextColor(Color.WHITE);
                break;
            case "playing":
                playing.setTextColor(Color.WHITE);
                break;
        }
        List<ScoreCard> listGames = new ArrayList<>();
        games = db.generalDao().getAllUserGamesFromUser(HomeActivity.currentUser);
        if(!games.isEmpty()){
            searchedGames = new ArrayList<>();
            for(final UserGame u: games) {
                Game g = db.generalDao().getGame(u.gameId);
                if(type.equals("all") || (type.equals("prog") && u.state==0) || (type.equals("playing") && u.state==2) || (type.equals("played") && u.state==1)){
                    searchedGames.add(g);
                    listGames.add(new ScoreCard(g.gameImage, g.gameName, g.platform, u.state, u.rating, activity));
                }
            }
            Collections.sort(listGames, (v, w)-> v.getName().compareTo(w.getName()));
            Collections.sort(searchedGames, (v, w)-> v.gameName.compareTo(w.gameName));
            cardAdapterGames = new CardAdapter(listGames, activity, position -> {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                if(appCompatActivity != null) {
                    HomeActivity.currentGame = searchedGames.get(position).gameId;
                    Utilities.pushFragment(appCompatActivity, new VideogameFragment(), "VideogameFragment");
                }
            });
            recyclerViewGames.setAdapter(cardAdapterGames);
        }
    }

    private void setColors() {
        all.setTextColor(Color.parseColor("#AEAAAA"));
        prog.setTextColor(Color.parseColor("#AEAAAA"));
        played.setTextColor(Color.parseColor("#AEAAAA"));
        playing.setTextColor(Color.parseColor("#AEAAAA"));
    }
}
