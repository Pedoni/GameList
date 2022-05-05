package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Friendship;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.Notification;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.Notifications.NotificationCard;
import com.example.mygamelist.Recyclers.Notifications.CardAdapter;
import com.example.mygamelist.Utilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public final class NotificationFragment extends Fragment {

    private static final String LOG = "NotificationeFragment";
    private final List<Game> games = null;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            setView(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setView(final Activity activity) {
        db.generalDao().readAllNotification(HomeActivity.USER_ID);
        BottomNavigationView navigation = activity.findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem it = menu.findItem(R.id.notification);
        it.setIcon(R.drawable.notification_0);
        RecyclerView recyclerViewNotifications = getView().findViewById(R.id.notificationRecycle);
        recyclerViewNotifications.setHasFixedSize(true);
        List<NotificationCard> listNots = new ArrayList<>();
        List<Notification> nots = db.generalDao().getAllUserNotifications(HomeActivity.USER_ID);
        List<User> involvedUsers = new ArrayList<>();
        for(final Notification n: nots) {
            User us = db.generalDao().getUser(String.valueOf(n.involvedUser));
            involvedUsers.add(us);
            String type = getString(R.string.friend_requests);
            String fr = getString(R.string.not_friends);
            List<Friendship> fs = db.generalDao().getFriendUsers(HomeActivity.USER_ID);
            for(final Friendship f: fs){
                if(f.firstUserId==us.userId || f.secondUserId==us.userId){
                    fr = getString(R.string.friends);
                }
            }
            listNots.add(new NotificationCard(us.userImage, type, n.text, fr));
        }
        CardAdapter cardAdapterNots = new CardAdapter(listNots, activity, position -> {
            HomeActivity.currentUser = String.valueOf(involvedUsers.get(position).userId);
            Utilities.pushFragment((AppCompatActivity)activity, new ProfileFragment(), "ProfileFragment");
        });
        recyclerViewNotifications.setAdapter(cardAdapterNots);
    }

}
