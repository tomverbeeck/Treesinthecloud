package com.example.user.treesinthecloud.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.user.treesinthecloud.R;
import com.kosalgeek.android.md5simply.MD5;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    Button login, signup;
    TextView create_account;
    EditText email,password;

    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;


    public static final String SP_NAME = "N/A";
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


        saveLoginCheckBox = (CheckBox)findViewById(R.id.checkBox);
        saveLoginCheckBox.setOnCheckedChangeListener(this);
        saveLogin = saveLoginCheckBox.isChecked();


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin == true) {
            email.setText(loginPreferences.getString("email", ""));
            password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
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

                SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
                String user_name = sharedPreferences.getString("email", "SP_NAME");
                String user_password = sharedPreferences.getString("password", "SP_NAME");


                String useremail  = email.getText().toString();
                String userpassword  = password.getText().toString();

                if (saveLogin) {

                    loginPrefsEditor.putString("email", useremail);
                    loginPrefsEditor.putString("password", userpassword);
                    loginPrefsEditor.commit();

                    if(user_name.equals(useremail) && user_password.equals(userpassword)){
                        Toast.makeText(this,"login sucessful",Toast.LENGTH_LONG).show();
                        //String status = sharedPreferences.getString("status", "SP_NAME");
                        // Intent startfarmplace = new Intent(this, FarmPlace.class);
                        // startActivity(startfarmplace);
                        finish();
                    }
                } else {

                    Toast.makeText(this, "no data was found in sharedPreference ", Toast.LENGTH_LONG).show();
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();

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
                                    // Intent intent = new Intent(MainActivity.this, FarmPlace.class);
                                    //  MainActivity.this.startActivity(intent);
                                } else {
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
                    queue.add(loginRequest);
                }

                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        saveLogin = isChecked;
    }

}