package com.example.user.treesinthecloud.Login;

/**
 * Created by GroepT on 5/1/2016.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {


    private static final String REGISTER_REQUEST_URL = "http://projectmovie.16mb.com/registration.php";
    private Map<String, String> params;

    public RegisterRequest(String username, String useremail, String userpassword, Response.Listener<String> listener) {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("nickname", username);
        // params.put("age", age + "");
        params.put("email", useremail);
        params.put("password", userpassword);

        /*params.put("nickname", username);
       // params.put("age", age + "");
        params.put("email", useremail);
        params.put("password", userpassword);*/
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

