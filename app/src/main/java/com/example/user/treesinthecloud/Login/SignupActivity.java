package com.example.user.treesinthecloud.Login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.treesinthecloud.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    Button create_account;
    TextView already_member;

    EditText name, email, passowrd;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup_acitivity);

        create_account = (Button) findViewById(R.id.signup);
        create_account.setOnClickListener(this);

        already_member = (TextView) findViewById(R.id.link_login);
        already_member.setOnClickListener(this);

        name = (EditText) findViewById(R.id.c);
        name.setOnClickListener(this);

        email = (EditText) findViewById(R.id.input_email);
        email.setOnClickListener(this);

        passowrd = (EditText) findViewById(R.id.input_password);
        passowrd.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.link_login:
                finish();
                break;

            case R.id.signup:

                String user_name = name.getText().toString();
                String user_email = email.getText().toString();
                String user_password = passowrd.getText().toString();

                // User registerData  = new User (name,email,password);
                break;

        }

    }
}
