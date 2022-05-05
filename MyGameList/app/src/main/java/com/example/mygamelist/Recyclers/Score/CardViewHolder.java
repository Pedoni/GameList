package com.example.mygamelist.Recyclers.Score;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.OnItemListener;

public final class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final ImageView imageCardView;
    final TextView nameTextView;
    final TextView consoleTextView;
    final TextView stateTextView;
    final TextView ratingTextView;
    private final OnItemListener itemListener;

    public CardViewHolder(@NonNull final View itemView, final OnItemListener listener) {
        super(itemView);
        imageCardView = itemView.findViewById(R.id.gameImageList);
        nameTextView = itemView.findViewById(R.id.titleGameCard);
        consoleTextView = itemView.findViewById(R.id.consoleGameCard);
        stateTextView = itemView.findViewById(R.id.stateGameCard);
        ratingTextView = itemView.findViewById(R.id.gameCardScore);
        itemListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        itemListener.onItemClick(getAdapterPosition());
    }
}
