package com.example.mygamelist.Recyclers.Top;

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

    private final List<TopCard> topCardList;
    private final Activity activity;
    private final OnItemListener listener;

    public CardAdapter(final List<TopCard> cardItemList, final Activity activity, final OnItemListener listener){
        this.topCardList = cardItemList;
        this.activity = activity;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_card, parent,false);
        return new CardViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, final int position) {
        TopCard currentTopCard = topCardList.get(position);
        holder.nameTextView.setText(currentTopCard.getPlace());
        String image_path = currentTopCard.getImageResource();
        Drawable drawable = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(image_path,"drawable",activity.getPackageName()));
        holder.imageCardView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return topCardList.size();
    }
}
