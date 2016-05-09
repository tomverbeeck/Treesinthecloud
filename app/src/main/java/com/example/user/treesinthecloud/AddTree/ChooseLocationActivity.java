package com.example.user.treesinthecloud.AddTree;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.example.user.treesinthecloud.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double longitude;
    private Double latitude;
    private String longitudeS;
    private String latitudeS;
    private boolean locationFound = false;
    LatLng positionMarker;
    Marker mark;
    Intent otherIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_tree_choose_location);

        Intent i = getIntent();
        longitude = i.getExtras().getDouble("longitude");
        latitude = i.getExtras().getDouble("latitude");

        otherIntent = new Intent();

        if (longitude != 0.0 && latitude != 0.0) {
            locationFound = true;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng startLocation;

        if (locationFound == true) {
            startLocation = new LatLng(latitude, longitude);

        } else {
            startLocation = new LatLng(50.875, 4.708);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mark = mMap.addMarker(new MarkerOptions().position(startLocation).title("Choose Location").draggable(true));

    }


    @Override
    protected void onPause() {
        super.onPause();
        positionMarker = mark.getPosition();
        otherIntent.putExtra("latitude", positionMarker.latitude);
        otherIntent.putExtra("longitude", positionMarker.longitude);
        latitudeS = "" + positionMarker.latitude;
        longitudeS = "" + positionMarker.longitude;

        SharedPreferences.Editor editor = getSharedPreferences("location", MODE_PRIVATE).edit();
        editor.putString("latitude", latitudeS);
        editor.putString("longitude", longitudeS);
        editor.commit();

        finish();
    }

}
