package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Forum;
import com.example.mygamelist.Database.Entities.ForumComment;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.Comment.CardAdapter;
import com.example.mygamelist.Recyclers.Comment.CommentCard;
import com.example.mygamelist.Utilities;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public final class DiscussionFragment extends Fragment {

    private static final String LOG = "DiscussionFragment";
    private AppDatabase db;
    private Forum f;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discussion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            setView(activity);
            setButton(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setButton(final Activity activity) {
        EditText et = getView().findViewById(R.id.writeCommentForum);
        ImageButton ib = getView().findViewById(R.id.sendCommentForum);
        ib.setOnClickListener(v -> {
            String text = et.getText().toString();
            if(!text.isEmpty()){
                ForumComment fc = new ForumComment(HomeActivity.currentForum, Long.parseLong(HomeActivity.USER_ID), text, HomeActivity.getTimestamp());
                db.generalDao().insertAllForumComments(fc);
                et.setText("");
                et.clearFocus();
                Utilities.pushFragment((AppCompatActivity)activity, new RefreshFragment(), "RefreshFragment");
            }
        });
    }

    private void setView(final Activity activity) {
        RecyclerView recyclerViewComments = getView().findViewById(R.id.discussionUsersRecycle);
        recyclerViewComments.setHasFixedSize(true);
        List<CommentCard> listComments = new ArrayList<>();
        List<ForumComment> listForCom = db.generalDao().getAllForumComments(HomeActivity.currentForum);
        for(final ForumComment fc: listForCom){
            User u = db.generalDao().getUser(String.valueOf(fc.userId));
            CommentCard cc = new CommentCard(u.username, "", fc.commentText,
                    HomeActivity.getDate(Long.parseLong(fc.timestamp)*1000, "dd/MM/yyyy HH:mm"), fc.userId);
            listComments.add(cc);
        }
        CardAdapter cardAdapterComments = new CardAdapter(listComments, activity, null, false);
        recyclerViewComments.setAdapter(cardAdapterComments);
        f = db.generalDao().getForum(HomeActivity.currentForum);
        setImage(activity);
        setTitle();
        setBin(activity);
    }

    private void setBin(final Activity activity) {
        ImageButton ib = getView().findViewById(R.id.deletePostDiscussion);
        if(HomeActivity.USER_ID.equals(String.valueOf(this.f.userId))){
            ib.setOnClickListener(v -> new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                    .setTitle(R.string.post_delete)
                    .setMessage(getString(R.string.post_delete_confirm))
                    .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                        db.generalDao().deleteForum(f);
                        HomeActivity.currentForum = 0;
                        Utilities.pushFragment((AppCompatActivity)activity, new ForumFragment(), "ForumFragment");
                        dialog.dismiss();
                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                    .show());
        } else {
            ib.setVisibility(View.GONE);
        }
    }

    private void setTitle() {
        TextView tw = getView().findViewById(R.id.discussionTitleText);
        tw.setText(f.forumTitle);
    }

    private void setImage(final Activity activity) {
        Game g = db.generalDao().getGame(Long.parseLong(f.forumGame));
        ImageView iv = getView().findViewById(R.id.discussionImage);
        Drawable drawable = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(g.gameImage,"drawable",activity.getPackageName()));
        iv.setImageDrawable(drawable);
    }

}
