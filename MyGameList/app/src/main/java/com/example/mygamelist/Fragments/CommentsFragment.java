package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.UserGame;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.Comment.CommentCard;
import com.example.mygamelist.Recyclers.Comment.CardAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CommentsFragment extends Fragment {

    private static final String LOG = "CommentsFragment";
    private AppDatabase db;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comments, container, false);
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
        RecyclerView recyclerViewComments = getView().findViewById(R.id.commentUsersRecycle);
        recyclerViewComments.setHasFixedSize(true);
        List<CommentCard> listComments = new ArrayList<>();
        for(final UserGame ug: db.generalDao().getAllUserGames()){
            if(ug.gameId == HomeActivity.currentGame && !ug.comment.isEmpty()){
                String username = db.generalDao().getUser(String.valueOf(ug.userId)).username;
                int likes = db.generalDao().getNumOfCommentLikesOfOneComment(ug.userId, HomeActivity.currentGame);
                listComments.add(new CommentCard(username, "", ug.comment, String.valueOf(likes), ug.userId));
            }
        }
        Collections.sort(listComments, (v, w) -> w.getLikes().compareTo(v.getLikes()));
        Collections.sort(listComments, (v, w) -> Integer.valueOf(w.getLikes()).compareTo(Integer.valueOf(v.getLikes())));
        CardAdapter cardAdapterComments = new CardAdapter(listComments, activity, null, true);
        recyclerViewComments.setAdapter(cardAdapterComments);
    }

}
