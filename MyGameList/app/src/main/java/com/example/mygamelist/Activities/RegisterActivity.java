package com.example.mygamelist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.R;
import java.util.List;

public final class RegisterActivity extends AppCompatActivity {
    private boolean flag = false;
    private AppDatabase db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView error = findViewById(R.id.errorRegister);
        error.setVisibility(View.GONE);
        this.db = AppDatabase.getInstance(this.getApplicationContext());
        final EditText usernameEditText = findViewById(R.id.usernameReg);
        final EditText emailEditText = findViewById(R.id.emailReg);
        final EditText passwordEditText = findViewById(R.id.passwordReg);
        final EditText firstNameEditText = findViewById(R.id.firstNameReg);
        final EditText lastNameEditText = findViewById(R.id.lastNameReg);
        final Button registerButton = findViewById(R.id.registerReg);
        registerButton.setOnClickListener(v -> {
            String user = usernameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String psw = passwordEditText.getText().toString();
            String firstname = firstNameEditText.getText().toString();
            String lastname = lastNameEditText.getText().toString();
            flag = false;
            List<User> users = db.generalDao().getAllUsers();
            if(email.isEmpty() || psw.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || user.isEmpty()){
                String errorFields = getString(R.string.error_fields);
                error.setText(errorFields);
                error.setVisibility(View.VISIBLE);
                flag = true;
            } else {
                for(final User u: users){
                    if(u.userEmail.equals(email)){
                        error.setText(getString(R.string.error_email));
                        error.setVisibility(View.VISIBLE);
                        flag = true;
                        break;
                    }
                    if(u.username.equals(user)){
                        error.setText(getString(R.string.error_username));
                        error.setVisibility(View.VISIBLE);
                        flag = true;
                        break;
                    }
                }
            }
            if(!flag){
                long newid = createUserAndGetID(user, firstname, lastname, email, psw);
                Intent intentM = new Intent(this, HomeActivity.class);
                intentM.putExtra("userid", Long.toString(newid));
                startActivity(intentM);
            }
        });
    }

    private long createUserAndGetID(final String user, final String firstname, final String lastname, final String email, final String psw) {
        User u = new User(user, firstname, lastname, email, psw, "ic_user_24", "defaultbackground");
        db.generalDao().insertUser(u);
        return db.generalDao().getUserIdFromMail(email);
    }

    @Override
    public void onBackPressed() {
        Intent intentL = new Intent(this, LoginActivity.class);
        startActivity(intentL);
        finish();
    }

}
