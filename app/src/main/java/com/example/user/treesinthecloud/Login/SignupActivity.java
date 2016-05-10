package com.example.user.treesinthecloud.Login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    // private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private Button buttonRegister;
    Button create_account;
    TextView already_member;
    private String loggedin = "false";


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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.link_login:
                finish();
                break;

            case R.id.signup:



                SharedPreferences sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPreferences.edit();

                spEditor.putString("name", editTextUsername.getText().toString());
                spEditor.putString("email", editTextEmail.getText().toString());
                spEditor.putString("password", editTextPassword.getText().toString());
                spEditor.putString("loggedin", "true");
                spEditor.commit();

                String username = editTextUsername.getText().toString();
                String useremail = MD5.encrypt(editTextEmail.getText().toString());
                String userpassword = MD5.encrypt(editTextPassword.getText().toString());

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "successfully regestered ",Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(username, useremail,userpassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(registerRequest);

                break;

        }
    }
}