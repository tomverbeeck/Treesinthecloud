package com.example.user.treesinthecloud.Login;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences UserLocalDatabase;

    public UserLocalStore(Context context){

        UserLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    public void storeUserData(User user){

        SharedPreferences.Editor spEditor = UserLocalDatabase.edit();
        spEditor.putString("name",user.name);
        spEditor.putString("email",user.email);
        spEditor.putString("password",user.password);
        spEditor.commit();
    }

  /*  public  User getLoggedInUser(){
        String user_name = UserLocalDatabase.getString("name", "");
        String user_email = UserLocalDatabase.getString("email","");
        String user_password = UserLocalDatabase.getString("password","");
        User storedUser = new User(user_name,user_email,user_password);
        return  storedUser;
    }*/

    public void setUserLoggedIn (boolean loggedIn){
        SharedPreferences.Editor spEditor = UserLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = UserLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}
