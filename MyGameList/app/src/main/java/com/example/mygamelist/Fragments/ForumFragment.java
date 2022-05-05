package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Forum;
import com.example.mygamelist.Database.Entities.ForumComment;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.Forum.ForumCard;
import com.example.mygamelist.Recyclers.Forum.CardAdapter;
import com.example.mygamelist.Utilities;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;
import java.util.List;

public final class ForumFragment extends Fragment {

    private static final String LOG = "ForumFragment";
    private AppDatabase db;
    private String title;
    private String text;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forum, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            setView(activity);
            setAddButton(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setAddButton(final Activity activity) {
        FloatingActionButton addButton = getView().findViewById(R.id.addForumButton);
        addButton.setOnClickListener(v -> callLoginDialog(activity));
    }

    private void setView(final Activity activity) {
        RecyclerView recyclerViewForum = getView().findViewById(R.id.forumRecycle);
        recyclerViewForum.setHasFixedSize(true);
        List<ForumCard> listForums = new ArrayList<>();
        List<Forum> forums = db.generalDao().getAllForumsOrdered();
        for(final Forum f: forums){
            Game g = db.generalDao().getGame(Long.parseLong(f.forumGame));
            String user = db.generalDao().getUser(String.valueOf(f.userId)).username;
            String com = String.valueOf(db.generalDao().getNumberOfForumComments(f.forumId)-1);
            listForums.add(new ForumCard(g.gameImage, f.forumTitle, g.gameName, user, com));
        }
        CardAdapter cardAdapterUsers = new CardAdapter(listForums, activity, position -> {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            if (appCompatActivity != null) {
                HomeActivity.currentForum = forums.get(position).forumId;
                Utilities.pushFragment(appCompatActivity, new DiscussionFragment(), "DiscussionFragment");
            }
        });
        recyclerViewForum.setAdapter(cardAdapterUsers);
    }

    private void callLoginDialog(final Activity activity) {
        Dialog newForum = new Dialog(getActivity());
        newForum.setContentView(R.layout.new_forum);
        newForum.setCancelable(false);
        MaterialButton add = newForum.findViewById(R.id.newForumAdd);
        MaterialButton canc = newForum.findViewById(R.id.newForumCancel);
        MaterialButton add2 = newForum.findViewById(R.id.newForumAdd2);
        MaterialButton canc2 = newForum.findViewById(R.id.newForumCancel2);
        LinearLayoutCompat l1 = newForum.findViewById(R.id.firstLayoutPopup);
        LinearLayoutCompat l2 = newForum.findViewById(R.id.secondLayoutPopup);
        ScrollView sg = newForum.findViewById(R.id.scrollGames);
        RadioGroup rg = newForum.findViewById(R.id.radioGroupPopup);
        newForum.show();
        add.setOnClickListener(v -> {
            this.title = ((EditText)newForum.findViewById(R.id.newForumTitle)).getText().toString();
            this.text = ((EditText)newForum.findViewById(R.id.newForumText)).getText().toString();
            if(!title.isEmpty() && !text.isEmpty()){
                newForum.findViewById(R.id.newForumTitle).setVisibility(View.GONE);
                newForum.findViewById(R.id.newForumText).setVisibility(View.GONE);
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.VISIBLE);
                sg.setVisibility(View.VISIBLE);
                createRadioGroup(activity, rg);
            }

        });
        add2.setOnClickListener(v -> {
            int selected = rg.getCheckedRadioButtonId();
            String gametext = ((RadioButton)newForum.findViewById(selected)).getText().toString();
            long gameId = db.generalDao().getGameId(gametext).gameId;
            String timestamp = HomeActivity.getTimestamp();
            Forum f = new Forum(Long.parseLong(HomeActivity.USER_ID), this.title, this.text, String.valueOf(gameId), timestamp);
            db.generalDao().insertForum(f);
            f = db.generalDao().getLastUserForum(Long.parseLong(HomeActivity.USER_ID));
            ForumComment fc = new ForumComment(f.forumId, Long.parseLong(HomeActivity.USER_ID), f.forumText, f.forumCreationDate);
            db.generalDao().insertForumComment(fc);
            Utilities.pushFragment((AppCompatActivity)activity, new RefreshFragment(), "RefreshFragment");
            newForum.dismiss();
        });
        canc.setOnClickListener(v -> newForum.dismiss());
        canc2.setOnClickListener(v -> {
            newForum.findViewById(R.id.newForumTitle).setVisibility(View.VISIBLE);
            newForum.findViewById(R.id.newForumText).setVisibility(View.VISIBLE);
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.GONE);
            sg.setVisibility(View.GONE);
        });
    }

    private void createRadioGroup(final Activity activity, final RadioGroup rg) {
        int i = 0;
        for(final Game g: db.generalDao().getAllGamesSorted()){
            rg.addView(getRadioButton(activity, g, i));
            i++;
        }
        rg.check(0);
        rg.setPadding(10, 10, 10, 10);
    }

    private View getRadioButton(final Activity activity, final Game g, final int id) {
        MaterialRadioButton bt = new MaterialRadioButton(activity);
        bt.setText(g.gameName);
        bt.setTextColor(Color.WHITE);
        bt.setPadding(10, 0, 0, 0);
        bt.setId(id);
        return bt;
    }

}
