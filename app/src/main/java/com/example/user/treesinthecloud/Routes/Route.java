package com.example.user.treesinthecloud.Routes;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 21/04/2016.
 */
public class Route {

    private String name;
    private String shortDescription;
    private String length;

    private ArrayList<String> markers = new ArrayList<>();

    private HashMap<String, String> trees = new HashMap<>();

    public Route(){

    }

    @Override
    public String toString() {
        return "Route{" +
                "name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", length='" + length + '\'' +
                '}';
    }

    public Route(String name, String shortDescription, String length)
    {
        this.name = name;
        this.shortDescription = shortDescription;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public ArrayList<String> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<String> markers) {
        this.markers = markers;
    }

    public void addTree(String id, String descr, Context con){
        if(id != "" && descr != ""){
            trees.put(id, descr);
            Toast.makeText(con, "tree added", Toast.LENGTH_SHORT).show();
        }

    }

    public HashMap<String, String> getTrees() {
        return trees;
    }

    public void setTrees(HashMap<String, String> trees) {
        this.trees = trees;
    }
}
