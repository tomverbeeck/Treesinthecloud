package com.example.user.treesinthecloud.Login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.R;
import com.kosalgeek.android.md5simply.MD5;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    // private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private Button buttonRegister;
    private TextView already_member;

    final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup_acitivity);

        editTextUsername = (EditText) findViewById(R.id.c);
        editTextUsername.setOnClickListener(this);

        editTextPassword = (EditText) findViewById(R.id.input_password);
        editTextPassword.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextEmail.setOnClickListener(this);

        buttonRegister = (Button) findViewById(R.id.signup);
        buttonRegister.setOnClickListener(this);

        already_member = (TextView) findViewById(R.id.link_login);
        already_member.setOnClickListener(this);

        loginPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.link_login:
                finish();
                break;

            case R.id.signup:

                HashMap data = new HashMap();
                data.put("nickname", editTextUsername.getText().toString());
                data.put("email", MD5.encrypt(editTextEmail.getText().toString()));
                data.put("password", MD5.encrypt(editTextPassword.getText().toString()));


                PostResponseAsyncTask task = new PostResponseAsyncTask(SignupActivity.this, data, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(TAG,s);
                        if(s.contains("succes")){
                            loginPrefsEditor.putString("username", editTextUsername.getText().toString());
                            Toast.makeText(getApplicationContext(), "successfully registered ",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });

                task.execute("http://projectmovie.16mb.com/registration.php");

                break;
        }
    }
}