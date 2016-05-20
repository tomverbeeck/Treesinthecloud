package com.example.user.treesinthecloud.Login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "http://projectmovie.16mb.com/login.php";
    private Map<String, String> params;

    //http://treesinthecloud.net23.net/Login.php
    //http://a15_ee5_trees1.studev.groept.be/registration.php

    public LoginRequest(String useremail, String userpassword, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("nickname", useremail);
        params.put("password", userpassword);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

