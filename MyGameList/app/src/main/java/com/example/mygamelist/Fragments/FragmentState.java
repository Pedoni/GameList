package com.example.mygamelist.Fragments;

import android.content.Context;
import com.example.mygamelist.Activities.HomeActivity;
import java.util.ArrayList;
import java.util.List;

public final class FragmentState {

    private static FragmentState INSTANCE;
    private static List<Long> videogameBackStack;
    private static List<Long> usersBackStack;
    private static boolean isForwardVideogame = true;
    private static boolean isForwardUser = true;
    public static String searchString = "";
    public static boolean fromSearch = false;
    public static int typeOfRec = 1;    //1 world, 2 games, 3 users

    public static FragmentState getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FragmentState();
        }
        return INSTANCE;
    }

    private FragmentState() {
        videogameBackStack = new ArrayList<>();
        usersBackStack = new ArrayList<>();
    }

    public void addVideogame(final long gameId){
        videogameBackStack.add(gameId);
    }

    public void addUser(final long userId){
        usersBackStack.add(userId);
    }

    public long getLastVideogame(){
        long val = videogameBackStack.get(videogameBackStack.size()-1);
        videogameBackStack.remove(videogameBackStack.size()-1);
        if(videogameBackStack.isEmpty()){
            isForwardVideogame = true;
        }
        return val;
    }

    public long getLastUser(){
        return usersBackStack.size()<=1 ? Long.parseLong(HomeActivity.USER_ID) : usersBackStack.get(usersBackStack.size()-1);
    }

    public boolean isVideogameListEmpty(){
        return videogameBackStack.isEmpty();
    }

    public int getUserListSize(){
        return usersBackStack.size();
    }

    public void setVideogameGoing(boolean forward){
        isForwardVideogame = forward;
    }

    public boolean isVideogameBack(){
        return !isForwardVideogame;
    }

    public boolean isUserBack(){
        return !isForwardUser;
    }

    public void cleanVideogameList(){
        videogameBackStack.clear();
    }

    public void cleanUserList() {
        usersBackStack.clear();
        usersBackStack.add(Long.parseLong(HomeActivity.USER_ID));
    }

    public void setUserGoing(final boolean forward) {
        isForwardUser = forward;
    }

    public void removeLastUser() {
        usersBackStack.remove(usersBackStack.size()-1);
    }
}
