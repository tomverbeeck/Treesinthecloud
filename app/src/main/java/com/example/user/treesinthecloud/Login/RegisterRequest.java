package com.example.user.treesinthecloud.Login;

/**
 * Created by GroepT on 5/1/2016.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {


    private static final String REGISTER_REQUEST_URL = "http://treesinthecloud.net23.net/Register.php";
    private Map<String, String> params;
    //http://treesinthecloud.net23.net/Register.php
    //http://a15_ee5_trees1.studev.groept.be/registration.php

    public RegisterRequest(String username, String useremail, String userpassword, Response.Listener<String> listener) {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("username", username);
        // params.put("age", age + "");
        params.put("useremail", useremail);
        params.put("userpassword", userpassword);

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

