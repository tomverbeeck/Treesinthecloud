package com.example.user.treesinthecloud.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.user.treesinthecloud.MapsActivity;
import com.example.user.treesinthecloud.R;
import com.kosalgeek.android.md5simply.MD5;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    Button login;
    TextView create_account;
    EditText email,password;

    private CheckBox CheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean checkflag;

    String user_email,user_password;
    String emailEd,passwordEd;
    String profile;


    final String TAG = this.getClass().getName();

    public static final String SP_NAME = "N/A";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_activity);

        email = (EditText) findViewById(R.id.proName);
        email.setOnClickListener(this);
        password = (EditText) findViewById(R.id.input_password);
        password.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        create_account = (TextView) findViewById(R.id.input_account);
        create_account.setOnClickListener(this);


        CheckBox = (CheckBox)findViewById(R.id.checkBox);
        CheckBox.setOnCheckedChangeListener(this);
        checkflag = CheckBox.isChecked();
        Log.d(TAG,"is checked" + checkflag);

        loginPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        user_email = loginPreferences.getString("useremail", "");
        user_password = loginPreferences.getString("userpassword", "");
        profile = loginPreferences.getString("profile", "");

        if (checkflag) {
            email.setText(user_email);
            password.setText(user_password);

        }if(!checkflag){
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
            email.setText("  ");
            password.setText("  ");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.input_account:
                Intent SignupActivity = new Intent(this, SignupActivity.class);
                startActivity(SignupActivity);
                break;

            case R.id.login:

                if (checkflag ){

                    emailEd = email.getText().toString();
                    passwordEd = password.getText().toString();

                    if(user_email.equals(emailEd) && user_password.equals(passwordEd)){
                        Toast.makeText(this, "Welcome you are logged in", Toast.LENGTH_LONG).show();
                        ((Status)getApplication()).setLoggedIn(true);
                        finish();
                    }else{

                        Toast.makeText(this, "user info fetching  from server ", Toast.LENGTH_LONG).show();
                        String useremail1  = email.getText().toString();
                        String userpassword1  = MD5.encrypt(password.getText().toString());

                        // Response received from the server
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        loginPrefsEditor.putString("useremail", email.getText().toString());
                                        loginPrefsEditor.putString("userpassword", password.getText().toString());

                                        ((Status)getApplication()).setLoggedIn(true);
                                        loginPrefsEditor.commit();

                                        Context context = getApplicationContext();
                                        int duration = Toast.LENGTH_SHORT;
                                        Toast toast = Toast.makeText(context, "Welcome you are now loggedIn", duration);
                                        toast.show();

                                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                        builder.setMessage("Login Failed")
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();

                                        ((Status)getApplication()).setLoggedIn(false);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        LoginRequest loginRequest = new LoginRequest(useremail1, userpassword1, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                        queue.add(loginRequest);}
                } else {

                    Toast.makeText(this, "fetching data from server ", Toast.LENGTH_LONG).show();
                    String useremail1  = MD5.encrypt(email.getText().toString());
                    String userpassword1  = MD5.encrypt(password.getText().toString());

                    // Response received from the server
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    loginPrefsEditor.clear();
                                    loginPrefsEditor.commit();

                                    Toast.makeText(getApplicationContext(), "Welcome you are now loggedIn", Toast.LENGTH_SHORT).show();

                                    ((Status)getApplication()).setLoggedIn(true);

                                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                } else {
                                    ((Status)getApplication()).setLoggedIn(false);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Login Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(useremail1, userpassword1, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);}

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkflag = isChecked;
        Log.d(TAG,"is checked" + checkflag);
    }

}