package com.example.user.treesinthecloud.AddTree;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.user.treesinthecloud.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double longitude;
    private Double latitude;
    private String longitudeS;
    private String latitudeS;
    private boolean locationFound = false;
    LatLng positionMarker;
    Marker mark;
    Intent otherIntent;

    private int MY_PERMISSIONS_REQUEST_LOCATION =11;


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

        /*if (locationFound == true) {
            startLocation = new LatLng(latitude, longitude);

        } else {
            startLocation = new LatLng(50.875, 4.708);
        }*/

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ChooseLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return;
        }
        mMap.setMyLocationEnabled(true);

        //start with current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();

        if (location != null)
        {
            startLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }else{
            startLocation = new LatLng(latitude, longitude);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15));


        mark = mMap.addMarker(new MarkerOptions().position(startLocation).title("Choose Location").draggable(true));

    }


    @Override
    protected void onPause() {
        super.onPause();
        positionMarker = mark.getPosition();
        Toast.makeText(getApplicationContext(), positionMarker.toString(), Toast.LENGTH_SHORT).show();
        otherIntent.putExtra("latitude", positionMarker.latitude);
        otherIntent.putExtra("longitude", positionMarker.longitude);
        latitudeS = "" + positionMarker.latitude;
        longitudeS = "" + positionMarker.longitude;

        Toast.makeText(getApplicationContext(), longitudeS + " lat is " + latitudeS, Toast.LENGTH_SHORT).show();

        SharedPreferences.Editor editor = getSharedPreferences("location", MODE_PRIVATE).edit();
        editor.putString("latitude", latitudeS);
        editor.putString("longitude", longitudeS);
        editor.commit();

        finish();
    }

}
