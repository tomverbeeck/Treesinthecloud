package com.example.user.treesinthecloud.TreeDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Tree implements ClusterItem{

    private int idTree;
    private double latitude;
    private double longitude;
    private String specie;
    private String name;
    private String status;
    private String cuttingShape;

    private int originalGirth;              //stamomtrek
    private int currentGirth;

    private String shortDescr;

    public Tree(){}

    public Tree(LatLng position){
        this.latitude = position.latitude;
        this.longitude = position.longitude;
    }

    public Tree (int idTree, double longitude, double latitude, String specie, String status, String name, int originalGirth, int currentGirth, String cuttingShape){
        super();
        this.idTree = idTree;
        this.latitude = latitude;
        this.longitude = longitude;
        this.specie = specie;
        this.status = status;
        this.name = name;
        this.originalGirth = originalGirth;
        this.currentGirth = currentGirth;
        this.cuttingShape = cuttingShape;
    }
    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public void setPosition(LatLng position) {
        latitude = position.latitude;
        longitude = position.longitude;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "currentGirth=" + currentGirth +
                ", idTree=" + idTree +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", specie='" + specie + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", cuttingShape='" + cuttingShape + '\'' +
                ", originalGirth=" + originalGirth +
                '}';
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

    public String getShortDescr() {
        return shortDescr;
    }

    public void setShortDescr(String shortDescr) {
        this.shortDescr = shortDescr;
    }
}
