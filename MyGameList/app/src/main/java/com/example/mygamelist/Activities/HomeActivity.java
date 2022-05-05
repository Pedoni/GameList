package com.example.mygamelist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Fragments.ForumFragment;
import com.example.mygamelist.Fragments.FragmentState;
import com.example.mygamelist.Fragments.NotificationFragment;
import com.example.mygamelist.Fragments.ProfileFragment;
import com.example.mygamelist.Fragments.WorldFragment;
import com.example.mygamelist.R;
import com.example.mygamelist.Utilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public final class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String FRAGMENT_TAG_HOME = "WorldFragment";
    public static long currentGame;
    public static long currentForum;
    public static String currentUser;
    private AppDatabase db;
    public static String USER_ID;
    private FragmentState fg;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.db = AppDatabase.getInstance(this.getApplicationContext());
        this.fg = FragmentState.getInstance(this.getApplicationContext());
        FragmentState.typeOfRec = 1;
        setContentView(R.layout.activity_home);
        USER_ID = getIntent().getStringExtra("userid");
        if(savedInstanceState == null) {
            Utilities.pushFragment(this, new WorldFragment(), FRAGMENT_TAG_HOME);
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        checkNotifications(navigation);
    }

    private void checkNotifications(final BottomNavigationView navigation) {
        MenuItem it = navigation.getMenu().findItem(R.id.notification);
        switch(db.generalDao().getNumberOfNotifications(USER_ID)){
            case 0:
                it.setIcon(R.drawable.notification_0);
                break;
            case 1:
                it.setIcon(R.drawable.notification_1);
                break;
            case 2:
                it.setIcon(R.drawable.notification_2);
                break;
            case 3:
                it.setIcon(R.drawable.notification_3);
                break;
            default:
                it.setIcon(R.drawable.notification_n);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        switch(item.getItemId()) {
            case R.id.world:
                FragmentState.typeOfRec = 1;
                setTagAndFragment(new WorldFragment(), "WorldFragment");
                break;
            case R.id.profile:
                currentUser = USER_ID;
                setTagAndFragment(new ProfileFragment(), "ProfileFragment");
                break;
            case R.id.notification:
                setTagAndFragment(new NotificationFragment(), "NotificationFragment");
                break;
            case R.id.forum:
                setTagAndFragment(new ForumFragment(), "ForumFragment");
                break;
        }
        return true;
    }

    private void setTagAndFragment(final Fragment fragment, final String tag) {
        fg.cleanVideogameList();
        fg.cleanUserList();
        FragmentState.fromSearch = false;
        Utilities.pushFragment(this, fragment, tag);
    }

    @Override
    public void onBackPressed() {
        Fragment world = getSupportFragmentManager().findFragmentByTag("WorldFragment");
        Fragment profile = getSupportFragmentManager().findFragmentByTag("ProfileFragment");
        Fragment notification = getSupportFragmentManager().findFragmentByTag("NotificationFragment");
        Fragment forum = getSupportFragmentManager().findFragmentByTag("ForumFragment");
        if ((world != null && world.isVisible()) || (notification != null && notification.isVisible()) || (forum != null && forum.isVisible())
                || (profile != null && profile.isVisible() && USER_ID.equals(currentUser) && fg.getUserListSize()==1) && !fg.fromSearch) {
            new MaterialAlertDialogBuilder(new ContextThemeWrapper(this, R.style.myDialog))
                    .setTitle("Logout")
                    .setMessage(getString(R.string.close_app))
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> finish())
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            Utilities.popFragment(this);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String getTimestamp(){
        return String.valueOf(System.currentTimeMillis()/1000);
    }

    public static String getDate(final long milliSeconds, final String dateFormat){
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        TimeZone tz = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}