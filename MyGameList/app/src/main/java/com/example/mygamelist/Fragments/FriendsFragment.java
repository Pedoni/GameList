package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Friendship;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.User.CardAdapter;
import com.example.mygamelist.Recyclers.User.UserCard;
import com.example.mygamelist.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FriendsFragment extends Fragment {

    private static final String LOG = "FriendsFragment";
    private AppDatabase db;
    private FragmentState fs;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friend_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            this.fs = FragmentState.getInstance(activity.getApplicationContext());
            setView(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setView(final Activity activity) {
        if(fs.isUserBack()){
            HomeActivity.currentUser = String.valueOf(fs.getLastUser());
            if(fs.getUserListSize()!=1){
                fs.removeLastUser();
            }
        } else {
            this.fs.setUserGoing(false);
        }
        RecyclerView recyclerViewUsers = getView().findViewById(R.id.friendUsersRecycle);
        recyclerViewUsers.setHasFixedSize(true);
        List<UserCard> listUsers = new ArrayList<>();
        List<Friendship> fships = db.generalDao().getFriendUsers(HomeActivity.currentUser);
        List<Long> friendUsers = new ArrayList<>();
        for(Friendship f: fships) {
            if(String.valueOf(f.firstUserId).equals(HomeActivity.currentUser)){
                friendUsers.add(f.secondUserId);
            } else {
                friendUsers.add(f.firstUserId);
            }
        }
        List<User> finalUsers = new ArrayList<>();
        for(long l: friendUsers){
            User us = db.generalDao().getUser(String.valueOf(l));
            finalUsers.add(us);
            listUsers.add(new UserCard(us.userImage, us.username, us.userFirstName + " " + us.userLastName, us.userEmail));
        }
        Collections.sort(listUsers, (v, w)-> v.getName().compareTo(w.getName()));
        Collections.sort(finalUsers, (v, w)-> (v.username).compareTo((w.username)));
        CardAdapter cardAdapterUsers = new CardAdapter(listUsers, activity, (position) -> {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            if (appCompatActivity != null) {
                HomeActivity.currentUser = String.valueOf(finalUsers.get(position).userId);
                Utilities.pushFragment(appCompatActivity, new ProfileFragment(), "ProfileFragment");
            }
        });
        recyclerViewUsers.setAdapter(cardAdapterUsers);
    }

}
