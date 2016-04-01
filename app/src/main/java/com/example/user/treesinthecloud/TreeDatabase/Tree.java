package com.example.user.treesinthecloud.TreeDatabase;

public class Tree {

    private int idTree;
    private double latitude;
    private double longitude;
    private String specie;
    private String name;
    private String status;
    private String cuttingShape;

    private int originalGirth;              //stamomtrek
    private int currentGirth;

    public Tree(){}

    /*public Tree (int idTree, double latitude, double longitude, String specie, String name, String status, String cuttingShape, int girth){
        super();
        this.idTree = idTree;
        this.latitude = latitude;
        this.longitude = longitude;
        this.specie = specie;
        this.name = name;
        this.status = status;
        this.cuttingShape = cuttingShape;
        this.girth = girth;
    }*/

    @Override
    public String toString() {
        return "Name= " + name +
                ", Status= " + status +
                ", OrgGirth" + originalGirth;
    }

    public int getIdTree() {
        return idTree;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getSpecie() {
        return specie;
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

    public void setIdTree(int idTree) {
        this.idTree = idTree;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
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

    public int getOriginalGirth() {
        return originalGirth;
    }

    public void setOriginalGirth(int originalGirth) {
        this.originalGirth = originalGirth;
    }

    public int getCurrentGirth() {
        return currentGirth;
    }

    public void setCurrentGirth(int currentGirth) {
        this.currentGirth = currentGirth;
    }
}
