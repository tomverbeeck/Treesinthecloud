package com.example.user.treesinthecloud.Routes;

import android.content.Context;

import com.example.user.treesinthecloud.TreeDatabase.Tree;

import java.util.ArrayList;
import java.util.HashMap;

public class Route {

    private String name;
    private String shortDescription;
    private Double length;

    private ArrayList<String> markers = new ArrayList<>();

    private ArrayList<Tree> trees = new ArrayList<>();

    private HashMap<String, String> treesHash = new HashMap<>();

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

    public Route(String name, String shortDescription, Double length)
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

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
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

    public ArrayList<Tree> getTrees() {
        return trees;
    }

    public void setTrees(Tree tree) {
        if(tree != null)
            trees.add(tree);
    }

    public void addTree(String id, String descr, Context con){
        treesHash.put(id, descr);
    }

    public HashMap<String, String> getTreesHash() {
        return treesHash;
    }
}
