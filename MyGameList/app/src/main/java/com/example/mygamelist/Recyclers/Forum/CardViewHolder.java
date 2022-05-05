package com.example.mygamelist.Recyclers.Forum;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.OnItemListener;

public final class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final ImageView imageCardView;
    final TextView titleTextView;
    final TextView gameTextView;
    final TextView userTextView;
    final TextView commentsTextView;

    private final OnItemListener itemListener;

    public CardViewHolder(@NonNull final View itemView, final OnItemListener listener) {
        super(itemView);
        imageCardView = itemView.findViewById(R.id.forumCardImage);
        titleTextView = itemView.findViewById(R.id.forumTitleText);
        gameTextView = itemView.findViewById(R.id.forumGameText);
        userTextView = itemView.findViewById(R.id.forumMakerText);
        commentsTextView = itemView.findViewById(R.id.numberRepliesText);
        itemListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        itemListener.onItemClick(getAdapterPosition());
    }
}
