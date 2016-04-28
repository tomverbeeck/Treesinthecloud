package com.example.user.treesinthecloud.TreeDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Tree implements Serializable{

    private int idTree;
    private double latitude;
    private double longitude;

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = new LatLng(getLatitude(), getLongitude());
    }

    private LatLng position;
    private String specie;
    private String name;
    private String status;
    private String cuttingShape;

    private int originalGirth;              //stamomtrek
    private int currentGirth;

    public Tree(){}

    public Tree (int idTree, double latitude, double longitude, String specie, String name, String status, String cuttingShape, int originalGirth, int currentGirth){
        super();
        this.idTree = idTree;
        this.latitude = latitude;
        this.longitude = longitude;
        this.specie = specie;
        this.name = name;
        this.status = status;
        this.cuttingShape = cuttingShape;
        this.originalGirth = originalGirth;
        this.currentGirth = currentGirth;
    }

    @Override
    public String toString() {
        return "Name= " + name +
                ", Specie= " + specie +
                ", Status= " + status +
                ", OrgGirth= " + originalGirth;
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
        if(specie != null)
            this.specie = specie;
    }

    public void setName(String name) {
        if(name != null)
            this.name = name;
    }

    public void setStatus(String status) {
        if(status != null)
            this.status = status;
    }

    public void setCuttingShape(String cuttingShape) {
        if(cuttingShape != null)
            this.cuttingShape = cuttingShape;
    }

    public int getOriginalGirth() {
        return originalGirth;
    }

    public void setOriginalGirth(int originalGirth) {
        if(originalGirth != 0)
            this.originalGirth = originalGirth;
    }

    public int getCurrentGirth() {
        return currentGirth;
    }

    public void setCurrentGirth(int currentGirth) {
        if(currentGirth != 0)
            this.currentGirth = currentGirth;
    }
}
