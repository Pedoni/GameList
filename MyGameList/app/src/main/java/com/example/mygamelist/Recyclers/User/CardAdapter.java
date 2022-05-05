package com.example.mygamelist.Recyclers.User;

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

    private final List<UserCard> userCardList;
    private final Activity activity;
    private final OnItemListener listener;

    public CardAdapter(final List<UserCard> cardItemList, final Activity activity, final OnItemListener listener){
        this.userCardList = cardItemList;
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent,false);
        return new CardViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        UserCard currentUserCard = userCardList.get(position);
        holder.nameTextView.setText(currentUserCard.getName());
        holder.emailTextView.setText(currentUserCard.getEmail());
        holder.firstLastTextView.setText(currentUserCard.getFirstLastName());
        String image_path = currentUserCard.getImageResource();
        Drawable drawable = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(image_path,"drawable",activity.getPackageName()));
        holder.imageCardView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return userCardList.size();
    }
}
