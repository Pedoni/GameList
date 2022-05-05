package com.example.mygamelist.Recyclers.User;

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
    final TextView firstLastTextView;
    final TextView emailTextView;
    private final OnItemListener itemListener;

    public CardViewHolder(@NonNull final View itemView, final OnItemListener listener) {
        super(itemView);
        imageCardView = itemView.findViewById(R.id.userCardImage);
        nameTextView = itemView.findViewById(R.id.userCardName);
        firstLastTextView = itemView.findViewById(R.id.nameSurnameCardName);
        emailTextView = itemView.findViewById(R.id.emailCardName);
        itemListener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        itemListener.onItemClick(getAdapterPosition());
    }
}
