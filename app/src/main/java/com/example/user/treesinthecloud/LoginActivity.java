package com.example.user.treesinthecloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button login, signup;
    TextView create_account;
    EditText email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.input_password);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        create_account = (TextView) findViewById(R.id.input_account);
        create_account.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_account:
                Intent SignupActivity = new Intent(this, SignupActivity.class);
                startActivity(SignupActivity);
                break;

            case R.id.login:
                Intent Dbase = new Intent(this, DataBase.class);
                startActivity(Dbase);
                break;
        }
    }
}