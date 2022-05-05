package com.example.mygamelist.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.R;

import java.util.List;

public final class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private List<User> users;
    private TextView error;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long currentID = prefs.getLong("userid", 0);
        if(currentID>0){
            Intent intentM = new Intent(this, HomeActivity.class);
            intentM.putExtra("userid", Long.toString(currentID));
            startActivity(intentM);
            finish();
        } else {
            setContentView(R.layout.activity_login);
            error = findViewById(R.id.errorLogin);
            error.setVisibility(View.GONE);
            AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
            do {
                users = db.generalDao().getAllUsers();
            } while(users.size()==0);
            final EditText emailEditText = findViewById(R.id.username);
            final EditText passwordEditText = findViewById(R.id.password);
            final Button loginButton = findViewById(R.id.login);
            final Button registerButton = findViewById(R.id.register);
            loginButton.setOnClickListener(v -> {
                String email = emailEditText.getText().toString();
                String psw = passwordEditText.getText().toString();
                if(email.isEmpty() || psw.isEmpty()){
                    error.setText(getString(R.string.error_fields));
                    error.setVisibility(View.VISIBLE);
                } else {
                    User user = null;
                    for(User u: users){
                        if((u.userEmail.equals(email) || (u.username.equals(email))) && u.userPassword.equals(psw)){
                            user = u;
                        }
                    }
                    startActivityIfAllOk(user);
                }
            });
            registerButton.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        }
    }

    private void startActivityIfAllOk(final User user) {
        if(user!=null){
            prefs.edit().putLong("userid", user.userId).apply();
            startActivity(new Intent(this, HomeActivity.class).putExtra("userid", Long.toString(user.userId)));
        } else {
            error.setText(R.string.account_not_found);
            error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}