package com.example.mygamelist.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mygamelist.R;
import com.example.mygamelist.Utilities;

public final class RefreshFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        Utilities.popFragment((AppCompatActivity)getActivity());
        return inflater.inflate(R.layout.videogame, container, false);
    }

}
