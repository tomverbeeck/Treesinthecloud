package com.example.user.treesinthecloud;

import android.content.Context;
import android.os.AsyncTask;

import java.net.ContentHandler;

/**
 * Created by GroepT on 3/17/2016.
 */
public class User {
    String name, email, password;

    public void CreateAccount(String name, String email, String password){

        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void LogIn(String email, String password, String name){
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
