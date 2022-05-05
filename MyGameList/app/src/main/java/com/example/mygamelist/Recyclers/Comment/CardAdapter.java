package com.example.mygamelist.Recyclers.Comment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.CommentLike;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.OnItemListener;

import java.util.List;

public final class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private final List<CommentCard> commentCardList;
    private final Activity activity;
    private final OnItemListener listener;
    private final AppDatabase db;
    private final boolean isThereLike;

    public CardAdapter(final List<CommentCard> commentItemList, final Activity activity, final OnItemListener listener, final boolean like){
        this.commentCardList = commentItemList;
        this.activity = activity;
        this.listener = listener;
        this.db = AppDatabase.getInstance(activity.getApplicationContext());
        this.isThereLike = like;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card, parent,false);
        return new CardViewHolder(layoutView, listener, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        CommentCard currentCommentCard = commentCardList.get(position);
        holder.usernameTextView.setText(currentCommentCard.getName() + " " + currentCommentCard.getSurname());
        holder.commentTextView.setText(currentCommentCard.getComment());
        holder.likesTextView.setText(currentCommentCard.getLikes());
        holder.user_name = currentCommentCard.getName();
        holder.user_surname = currentCommentCard.getSurname();
        holder.userId = currentCommentCard.getCommentUserId();
        if(!isThereLike){
            holder.likeButton.setVisibility(View.GONE);
        }
        List<CommentLike> list = db.generalDao().getAllCommentLikes();
        for(final CommentLike cl: list){
            if(cl.gameId== HomeActivity.currentGame && cl.userId==Long.parseLong(HomeActivity.USER_ID) && cl.commentUserId==currentCommentCard.getCommentUserId()){
                holder.likeButton.setBackgroundResource(R.drawable.ic_favourite_24);
                return;
            }
        }
        holder.likeButton.setBackgroundResource(R.drawable.ic_not_favourite_24);
    }

    @Override
    public int getItemCount() {
        return commentCardList.size();
    }
}
