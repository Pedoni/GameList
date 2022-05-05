package com.example.mygamelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public final class Utilities {

    public static void pushFragment(final AppCompatActivity activity, final Fragment fragment, final String tag) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
    public static void popFragment(final AppCompatActivity activity){
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.popBackStack(fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
