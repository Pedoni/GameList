package com.example.mygamelist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public final class SplashActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
