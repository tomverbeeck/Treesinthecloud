package com.example.user.treesinthecloud.ExtraInformationTabs;

import java.util.Date;

/**
 * Created by Gebruiker on 14/05/2016.
 */
public class Comment{

    private int id;
    private String username;
    private String comment;
    private String timestamp;

    public Comment(String username, String comment, String timestamp){
        this.username = username;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public Comment(int id, String username, String comment, String timestamp) {
        this.id = id;
        this.username = username;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}