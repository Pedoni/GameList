package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.Game.GameCard;
import com.example.mygamelist.Recyclers.Top.CardAdapter;
import com.example.mygamelist.Recyclers.Top.TopCard;
import com.example.mygamelist.Recyclers.User.UserCard;
import com.example.mygamelist.Utilities;
import com.example.mygamelist.Recyclers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public final class WorldFragment extends Fragment {

    private static final String LOG = "WorldFragment";
    private List<List<Game>> rvGames;
    private SearchView sb;
    private SearchView sb2;
    private AppDatabase db;
    private Button cancelSearch;
    private ScrollView ws;
    private CardView csb;
    private CardView csb2;
    private RecyclerView recyclerViewGames;
    private RecyclerView recyclerViewSearchUsers;
    private List<Game> searchedGames;
    private List<User> searchedUsers;
    private Button switchGames;
    private Button switchUsers;
    private LinearLayout lls;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.world, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        Log.e(LOG, "On view created");
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            Calendar c = Calendar.getInstance();
            String year = String.valueOf(c.get(Calendar.YEAR));
            this.rvGames = Arrays.asList(get10AllTimes(), get10JustAdded(year), get10Upcoming(year), get10OfTheMoment(year));
            setScrollbars(activity);
            setSearch(activity);
            setButtons();
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private List<Game> get10OfTheMoment(final String year) {
        List<Game> games = new ArrayList<>();
        List<Long> gamesOrdered = db.generalDao().getGamesOfTheMoment();
        for(final long i: gamesOrdered){
            Game g = db.generalDao().getGame(i);
            if(g.released && (g.publishYear.equals(year) || (Integer.parseInt(g.publishYear)+1==Integer.parseInt(year)))){
                games.add(g);
            }
        }
        if(games.size()>10){
            games = games.subList(0, 10);
        }
        return games;
    }

    private List<Game> get10Upcoming(final String year) {
        List<Long> topUpc = this.db.generalDao().getBestGamesAllTimeNoLim();
        List<Game> games = new ArrayList<>();
        System.out.println("Current year: " + year);
        for(final long i : topUpc){
            Game g = db.generalDao().getGame(i);
            if((Integer.parseInt(g.publishYear))>=Integer.parseInt(year) && !g.released){
                games.add(g);
            }
        }
        if(games.size()>10){
            games = games.subList(0, 10);
        }
        return games;
    }

    private List<Game> get10JustAdded(final String year) {
        List<Long> topJA = this.db.generalDao().getBestGamesAllTimeNoLim();
        List<Game> games = new ArrayList<>();
        for(final long i: topJA){
            Game g = db.generalDao().getGame(i);
            if(g.publishYear.equals(year) && g.released){
                games.add(g);
            }
        }
        if(games.size()>10){
            games = games.subList(0, 10);
        }
        return games;
    }

    private List<Game> get10AllTimes() {
        List<Long> bests = this.db.generalDao().getBestGamesAllTime();
        List<Game> games = new ArrayList<>();
        for(final long i: bests){
            games.add(db.generalDao().getGame(i));
        }
        return games;
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentState.fromSearch = false;
        if(!FragmentState.searchString.isEmpty()){
            this.sb.setQuery(FragmentState.searchString, false);
        }
        this.sb.setQuery(this.sb.getQuery(), true);
        int id = sb.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = sb.findViewById(id);
        textView.setTextColor(Color.WHITE);
        id = sb2.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        textView = sb2.findViewById(id);
        textView.setTextColor(Color.WHITE);
        switch(FragmentState.typeOfRec){
            case 1: //back in world
                this.recyclerViewSearchUsers.setVisibility(View.GONE);
                this.recyclerViewGames.setVisibility(View.GONE);
                this.ws.setVisibility(View.VISIBLE);
                this.lls.setVisibility(View.GONE);
                this.cancelSearch.setVisibility(View.GONE);
                this.csb2.setVisibility(View.GONE);
                this.csb.setVisibility(View.VISIBLE);
                this.sb.setQuery("", false);
                this.sb.clearFocus();
                break;
            case 2: //back in games
                this.recyclerViewSearchUsers.setVisibility(View.GONE);
                this.recyclerViewGames.setVisibility(View.VISIBLE);
                this.ws.setVisibility(View.GONE);
                this.lls.setVisibility(View.VISIBLE);
                this.switchGames.setTextColor(Color.WHITE);
                this.switchUsers.setTextColor(Color.parseColor("#AEAAAA"));
                break;
            case 3: //back in users
                this.recyclerViewGames.setVisibility(View.GONE);
                this.recyclerViewSearchUsers.setVisibility(View.VISIBLE);
                this.ws.setVisibility(View.GONE);
                this.lls.setVisibility(View.VISIBLE);
                this.switchUsers.setTextColor(Color.WHITE);
                this.switchGames.setTextColor(Color.parseColor("#AEAAAA"));
                break;
            default:
                break;
        }

    }

    private void searchFunc(final Activity activity, final String s){
        if(!sb2.hasFocus()){
            sb2.requestFocus();
            sb2.setQuery(s, false);
        }
        ws.setVisibility(View.GONE);
        cancelSearch.setVisibility(View.VISIBLE);
        recyclerViewGames.setVisibility(View.VISIBLE);
        recyclerViewSearchUsers.setVisibility(View.GONE);
        csb.setVisibility(View.GONE);
        csb2.setVisibility(View.VISIBLE);
        lls.setVisibility(View.VISIBLE);
        ((Button)getView().findViewById(R.id.buttonSwitchGames)).setTextColor(Color.WHITE);
        ((Button)getView().findViewById(R.id.buttonSwitchUsers)).setTextColor(Color.parseColor("#AEAAAA"));
        FragmentState.searchString = s;
        gameSearch(activity, s);
        userSearch(activity, s);
    }

    private void userSearch(final Activity activity, final String s) {
        List<UserCard> listUsers = new ArrayList<>();
        List<User> users = db.generalDao().getAllUsers();
        searchedUsers = new ArrayList<>();
        for(final User v: users) {
            if(v.username.toLowerCase().contains(s.toLowerCase())){
                searchedUsers.add(v);
                listUsers.add(new UserCard(v.userImage, v.username, v.userFirstName + " " + v.userLastName, v.userEmail));
            }
        }
        com.example.mygamelist.Recyclers.User.CardAdapter cardAdapterUsers = new com.example.mygamelist.Recyclers.User.CardAdapter(listUsers, activity, position -> {
            FragmentState.typeOfRec = 3;
            FragmentState.fromSearch = true;
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            if(appCompatActivity != null) {
                HomeActivity.currentUser = Long.toString(searchedUsers.get(position).userId);
                Utilities.pushFragment(appCompatActivity, new ProfileFragment(), "ProfileFragment");
            }
        });
        recyclerViewSearchUsers.setAdapter(cardAdapterUsers);
    }

    private void gameSearch(final Activity activity, final String s) {
        List<GameCard> listGames = new ArrayList<>();
        List<Game> games = db.generalDao().getAllGames();
        searchedGames = new ArrayList<>();
        for(final Game v: games) {
            if(v.gameName.toLowerCase().contains(s.toLowerCase())){
                searchedGames.add(v);
                listGames.add(new GameCard(v.gameImage, v.gameName, v.platform, v.publishYear));
            }
        }
        com.example.mygamelist.Recyclers.Game.CardAdapter cardAdapterGames = new com.example.mygamelist.Recyclers.Game.CardAdapter(listGames, activity, position -> {
            FragmentState.typeOfRec = 2;
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            if (appCompatActivity != null) {
                HomeActivity.currentGame = searchedGames.get(position).gameId;
                Utilities.pushFragment(appCompatActivity, new VideogameFragment(), "VideogameFragment");
            }
        });
        recyclerViewGames.setAdapter(cardAdapterGames);
    }

    private void setSearch(final Activity activity) {
        this.sb = getView().findViewById(R.id.searchBar);
        this.sb2 = getView().findViewById(R.id.searchBar2);
        this.ws = getView().findViewById(R.id.worldScrollview);
        this.csb = getView().findViewById(R.id.cardSearchBar);
        this.lls = getView().findViewById(R.id.linearLayoutSearch);
        this.csb2 = getView().findViewById(R.id.cardSearchBar2);
        this.csb2.setVisibility(View.GONE);
        this.csb.setVisibility(View.VISIBLE);
        recyclerViewGames = getView().findViewById(R.id.searchRecycle);
        recyclerViewGames.setHasFixedSize(true);
        recyclerViewSearchUsers = getView().findViewById(R.id.userSearchRecycle);
        recyclerViewSearchUsers.setHasFixedSize(true);
        this.sb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                if (s.length() > 0) {
                    searchFunc(activity, s);
                }
                return false;
            }
        });

        this.sb2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                if(s.length()>0){
                    searchFunc(activity, s);
                }
                return false;
            }
        });

    }

    private void setScrollbars(final Activity activity) {
        List<RecyclerView> layouts = new ArrayList<>();
        layouts.add(getView().findViewById(R.id.firstTop));
        layouts.add(getView().findViewById(R.id.secondTop));
        layouts.add(getView().findViewById(R.id.thirdTop));
        layouts.add(getView().findViewById(R.id.fourthTop));
        int i = 0;
        for(final RecyclerView r: layouts) {
            List<TopCard> list = new ArrayList<>();
            for(final Game v: this.rvGames.get(i)) {
                list.add(new TopCard(v.gameImage, v.gameName, v.platform));
            }
            int j = i;
            CardAdapter cardAdapter = new CardAdapter(list, activity, (position) -> {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                if (appCompatActivity != null) {
                    HomeActivity.currentGame = this.rvGames.get(j).get(position).gameId;
                    Utilities.pushFragment(appCompatActivity, new VideogameFragment(), "VideogameFragment");
                }
            });
            r.setAdapter(cardAdapter);
            i++;
        }

    }

    private void setButtons() {
        this.switchGames = getView().findViewById(R.id.buttonSwitchGames);
        this.switchUsers = getView().findViewById(R.id.buttonSwitchUsers);
        this.cancelSearch = getView().findViewById(R.id.cancelSearchButton);
        this.switchGames.setOnClickListener(v -> {
            this.recyclerViewSearchUsers.setVisibility(View.GONE);
            this.recyclerViewGames.setVisibility(View.VISIBLE);
            this.switchGames.setTextColor(Color.WHITE);
            this.switchUsers.setTextColor(Color.parseColor("#AEAAAA"));
            FragmentState.typeOfRec = 2;
        });
        this.switchUsers.setOnClickListener(v -> {
            this.recyclerViewGames.setVisibility(View.GONE);
            this.recyclerViewSearchUsers.setVisibility(View.VISIBLE);
            this.switchUsers.setTextColor(Color.WHITE);
            this.switchGames.setTextColor(Color.parseColor("#AEAAAA"));
            FragmentState.typeOfRec = 3;
        });
        this.cancelSearch.setOnClickListener(v -> {
            this.recyclerViewGames.setVisibility(View.GONE);
            this.recyclerViewSearchUsers.setVisibility(View.GONE);
            this.ws.setVisibility(View.VISIBLE);
            lls.setVisibility(View.GONE);
            this.sb.setQuery("", false);
            this.sb.clearFocus();
            this.sb2.setQuery("", false);
            this.sb2.clearFocus();
            cancelSearch.setVisibility(View.GONE);
            this.csb2.setVisibility(View.GONE);
            this.csb.setVisibility(View.VISIBLE);
            FragmentState.typeOfRec = 1;
        });
    }

}
