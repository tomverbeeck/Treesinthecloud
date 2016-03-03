package com.example.user.treesinthecloud;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity{

    public static final String LOGIN_URL = "http://projectmovie.16mb.com/volleyLogin.php";

    public static final String KEY_USERNAMELOGIN="username";
    public static final String KEY_PASSWORD="password";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin, buttonRegister;

    private String username;
    private String password;

    private boolean checkIfLoggedIn = false;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editTextUsername = (EditText) findViewById(R.id.prompt_username);
        editTextPassword = (EditText) findViewById(R.id.prompt_password);

        buttonLogin = (Button) findViewById(R.id.user_login_button);
        buttonRegister = (Button) findViewById(R.id.button_register);

    }*/


    /*private void userLogin() {
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            checkIfLoggedIn = true;
                        }else{
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                            checkIfLoggedIn = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_SHORT ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERNAMELOGIN,username);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getRegisterScreen(View view){
        Intent getRegisterScreenIntent = new Intent(this, RegisterScreen.class);
        startActivity(getRegisterScreenIntent);
    }

    public void getLoggedIn(View view){
        userLogin();

        if(checkIfLoggedIn) {
            Intent intent = new Intent(this, ActivityUserProfile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(ConfigProfile.KEY_USERNAME, username);
            startActivity(intent);
        }
    }*/
}


