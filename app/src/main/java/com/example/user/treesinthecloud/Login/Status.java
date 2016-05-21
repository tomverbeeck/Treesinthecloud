package com.example.user.treesinthecloud.Login;

import android.app.Application;


public class Status extends Application  {

    public  boolean LoggedIn;

    @Override
    public void onCreate() {
        //LoggedIn = false;
        super.onCreate();
    }

    public Status() {
        LoggedIn = false;
    }

    public Boolean getLoggedIn() {
        return LoggedIn;
    }

    public void setLoggedIn(Boolean LoggedIn) {
        this.LoggedIn = LoggedIn;
    }

}
