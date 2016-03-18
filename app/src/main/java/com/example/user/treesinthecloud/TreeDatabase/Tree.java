package com.example.user.treesinthecloud.TreeDatabase;

public class Tree {

    private int id;
    private double latitude;
    private double longitude;
    private String latinName;
    private String name;
    private String status;
    private String cuttingShape;
    private int girth;              //stamomtrek

    public Tree(){}

    public Tree (int id, double latitude, double longitude, String latinName, String name, String status, String cuttingShape, int girth){
        super();
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latinName = latinName;
        this.name = name;
        this.status = status;
        this.cuttingShape = cuttingShape;
        this.girth = girth;
    }

    @Override
    public String toString() {
        return  "latinName='" + latinName + '\'' +
                "\n status='" + status + '\'' +
                "\n cuttingShape='" + cuttingShape + '\'' +
                "\n girth=" + girth;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLatinName() {
        return latinName;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getCuttingShape() {
        return cuttingShape;
    }

    public int getGirth() {
        return girth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCuttingShape(String cuttingShape) {
        this.cuttingShape = cuttingShape;
    }

    public void setGirth(int girth) {
        this.girth = girth;
    }
}
