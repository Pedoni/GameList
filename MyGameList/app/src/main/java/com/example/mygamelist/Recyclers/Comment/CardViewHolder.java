package com.example.mygamelist.Recyclers.Comment;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.CommentLike;
import com.example.mygamelist.Fragments.RefreshFragment;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.OnItemListener;
import com.example.mygamelist.Utilities;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final AppDatabase db;
    final TextView usernameTextView;
    final TextView commentTextView;
    final TextView likesTextView;
    final  ImageView likeButton;
    String user_name;
    String user_surname;
    long userId;

    public CardViewHolder(@NonNull final View itemView, final OnItemListener listener, final Activity activity) {
        super(itemView);
        this.db = AppDatabase.getInstance(activity.getApplicationContext());
        usernameTextView = itemView.findViewById(R.id.commentMakerText);
        //setColors();
        usernameTextView.setTextColor(Color.GRAY);
        commentTextView = itemView.findViewById(R.id.commentTextView);
        likesTextView = itemView.findViewById(R.id.likesText);
        likeButton = itemView.findViewById(R.id.favCommentView);
        itemView.setOnClickListener(this);
        likeButton.setOnClickListener(v -> {
            setLike();
            Utilities.pushFragment((AppCompatActivity)activity, new RefreshFragment(), "RefreshFragment");
        });
    }

    private void setColors() {
        List<Integer> colors = new ArrayList<>();
        for(final int i: ColorTemplate.MATERIAL_COLORS){ colors.add(i); }
        for(final int i: ColorTemplate.JOYFUL_COLORS){ colors.add(i); }
        for(final int i: ColorTemplate.COLORFUL_COLORS){ colors.add(i); }
        usernameTextView.setTextColor(colors.get(new Random().nextInt(colors.size())));
    }

    private void setLike() {
        List<CommentLike> cls = db.generalDao().getAllCommentLikes();
        for(final CommentLike cl: cls){
            if(cl.gameId==HomeActivity.currentGame && cl.userId==Long.parseLong(HomeActivity.USER_ID) && cl.commentUserId==userId){
                removeLike(cl);
                return;
            }
        }
        addLike();
    }

    private void removeLike(final CommentLike cl) {
        db.generalDao().removeLike(cl);
        likeButton.setBackgroundResource(R.drawable.ic_not_favourite_24);
        likesTextView.setText(String.valueOf(Integer.parseInt(likesTextView.getText().toString())-1));
    }

    private void addLike() {
        db.generalDao().insertLike(new CommentLike(Long.parseLong(HomeActivity.USER_ID), userId, HomeActivity.currentGame));
        likeButton.setBackgroundResource(R.drawable.ic_favourite_24);
        likesTextView.setText(String.valueOf(Integer.parseInt(likesTextView.getText().toString())+1));
    }

    @Override
    public void onClick(final View v) {}
}
