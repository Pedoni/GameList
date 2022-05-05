package com.example.mygamelist.Recyclers.Forum;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.OnItemListener;

import java.util.List;

public final class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private final List<ForumCard> forumCardList;
    private final Activity activity;
    private final OnItemListener listener;

    public CardAdapter(final List<ForumCard> cardItemList, final  Activity activity, final  OnItemListener listener){
        this.forumCardList = cardItemList;
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_card, parent,false);
        return new CardViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        ForumCard currentForumCard = forumCardList.get(position);
        holder.titleTextView.setText(currentForumCard.getTitle());
        holder.gameTextView.setText(currentForumCard.getGame());
        holder.userTextView.setText(currentForumCard.getUser());
        holder.commentsTextView.setText(currentForumCard.getComments());
        String image_path = currentForumCard.getImageResource();
        Drawable drawable = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(image_path,"drawable",activity.getPackageName()));
        holder.imageCardView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return forumCardList.size();
    }
}
