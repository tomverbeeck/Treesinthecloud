package com.example.user.treesinthecloud.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.treesinthecloud.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login, signup;
    TextView create_account;
    EditText email,password;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.input_password);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        create_account = (TextView) findViewById(R.id.input_account);
        create_account.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_account:
                Intent SignupActivity = new Intent(this, SignupActivity.class);
                startActivity(SignupActivity);
                break;

            case R.id.login:
                //  User user = new User(null,null);

                // userLocalStore.storeUserData(user);
                userLocalStore.setUserLoggedIn(true);


                break;
        }
    }
}
