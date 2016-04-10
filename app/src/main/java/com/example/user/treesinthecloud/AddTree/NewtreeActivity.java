package com.example.user.treesinthecloud.AddTree;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.treesinthecloud.R;

public class NewtreeActivity extends AppCompatActivity {

    double longitudeDouble;
    double latitudeDouble;
    String longitudeString;
    String latitudeString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_tree__new_tree);

        final EditText longtitude = (EditText) findViewById(R.id.longtitudeNewTree);
        final EditText latitude = (EditText) findViewById(R.id.latitudeNewTree);
        EditText group = (EditText) findViewById(R.id.groupNewTree);
        EditText type = (EditText) findViewById(R.id.TypeNewTree);
        final EditText explanation = (EditText) findViewById(R.id.reasonForNewTree);
        final TextView explanationError = (TextView) findViewById(R.id.explanationError_newtree);
        final TextView locationError = (TextView) findViewById(R.id.locationError_newtree);

        Button getCurrentLocation = (Button) findViewById(R.id.currentLocationB);
        Button getMap = (Button) findViewById(R.id.button_add_tree_choose_location);

        //start with current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        //Checing if the phone can get their current location, needs to be declared in manifest
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if(location!=null) {
            longitudeDouble = location.getLongitude();
            latitudeDouble = location.getLatitude();
            longitudeString = "" + longitudeDouble;
            latitudeString = "" + latitudeDouble;
        }

        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                longtitude.setText(longitudeString);
                latitude.setText(latitudeString);
            }
        });

        Button submit = (Button) findViewById(R.id.submitNewTreeB);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //check for decent explanation

                String expl = explanation.getText().toString();
                if(expl.trim().length()<20){
                    explanationError.setVisibility(view.VISIBLE );
                    return;
                }

                explanationError.setVisibility(view.INVISIBLE);

                String longS = longtitude.getText().toString();
                String latS = latitude.getText().toString();

                if(longS.isEmpty() || latS.isEmpty()){
                    locationError.setVisibility(view.VISIBLE );
                    return;
                }

                try{
                    Double longD = Double.parseDouble(longS);
                    Double latD = Double.parseDouble(latS);
                }
                catch(NumberFormatException e){
                    locationError.setVisibility(view.VISIBLE );
                    return;
                }

                locationError.setVisibility(view.INVISIBLE );

               /* Intent i = new Intent(this, treeProfileActivity.class);
                startActivity(i);*/



                // parse information to database



            }
        });
    }

    public void getLocationOnMap(View view){
        Intent openMap = new Intent(this, ChooseLocationActivity.class);
        openMap.putExtra("longitude", longitudeDouble);
        openMap.putExtra("latitude", latitudeDouble);
        startActivity(openMap);
    }
}
