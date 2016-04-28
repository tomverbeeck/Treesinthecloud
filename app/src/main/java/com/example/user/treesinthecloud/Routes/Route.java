package com.example.user.treesinthecloud.Routes;

import com.example.user.treesinthecloud.TreeDatabase.Tree;

import java.util.List;

/**
 * Created by User on 21/04/2016.
 */
public class Route {

    private int idRoute;
    private String name;
    private String shortDescription;
    private int length;
    private List<Tree> trees;

    public Route(){

    }

    public Route(int idRoute, String name, String shortDescription, int length)
    {
        this.idRoute = idRoute;
        this.name = name;
        this.shortDescription = shortDescription;
        this.length = length;
    }

    public int getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
