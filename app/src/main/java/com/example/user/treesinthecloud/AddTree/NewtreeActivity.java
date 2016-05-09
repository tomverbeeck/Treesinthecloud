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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.treesinthecloud.R;

public class NewtreeActivity extends AppCompatActivity {

    double longitudeDouble;
    double latitudeDouble;
    String longitudeString;
    String latitudeString;
    PopupWindow popupMessage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_tree__new_tree);

        final EditText longitude = (EditText) findViewById(R.id.longtitudeNewTree);
        final EditText latitude = (EditText) findViewById(R.id.latitudeNewTree);
        EditText group = (EditText) findViewById(R.id.groupNewTree);
        final EditText type = (EditText) findViewById(R.id.TypeNewTree);
        final EditText explanation = (EditText) findViewById(R.id.reasonForNewTree);
        final TextView explanationError = (TextView) findViewById(R.id.explanationError_newtree);
        final TextView locationError = (TextView) findViewById(R.id.locationError_newtree);


        Button getCurrentLocation = (Button) findViewById(R.id.currentLocationB);
        Button getMap = (Button) findViewById(R.id.button_add_tree_choose_location);

        getCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                if (location != null) {
                    longitudeDouble = location.getLongitude();
                    latitudeDouble = location.getLatitude();
                    longitudeString = "" + longitudeDouble;
                    latitudeString = "" + latitudeDouble;
                } else {
                    Toast.makeText(getApplicationContext(), "Getting current location Failed", Toast.LENGTH_SHORT).show();

                }

                longitude.setText(longitudeString);
                latitude.setText(latitudeString);

            }
        });

        Button submit = (Button) findViewById(R.id.submitNewTreeB);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for decent explanation

                String expl = explanation.getText().toString();
                if (expl.trim().length() > 20) {
                    explanationError.setVisibility(view.VISIBLE);
                    return;
                }

                explanationError.setVisibility(view.INVISIBLE);

                String longS = longitude.getText().toString();
                String latS = latitude.getText().toString();

                if (longS.isEmpty() || latS.isEmpty()) {
                    locationError.setVisibility(view.VISIBLE);
                    return;
                }

                try {
                    Double longD = Double.parseDouble(longS);
                    Double latD = Double.parseDouble(latS);
                } catch (NumberFormatException e) {
                    locationError.setVisibility(view.VISIBLE);
                    return;
                }

                locationError.setVisibility(view.INVISIBLE);

                popupInit();

                popupMessage.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });

    }



    public void getLocationOnMap(View view){
        Intent openMap = new Intent(this, ChooseLocationActivity.class);
        openMap.putExtra("longitude", longitudeDouble);
        openMap.putExtra("latitude", latitudeDouble);
        startActivityForResult(openMap,1);
    }


    public void popupInit() {
        Context context = getBaseContext();
        RelativeLayout viewG = (RelativeLayout) findViewById(R.id.layout_popup_newtree);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_popup_newtree, viewG);
        popupMessage = new PopupWindow(findViewById(R.id.layout_popup_newtree),ViewGroup.LayoutParams.WRAP_CONTENT ,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupMessage.setContentView(layout);
        popupMessage.setFocusable(true);

    }

    public void closePopup(View view){
        popupMessage.dismiss();
    }

    public void continuePopup(View view){
        popupMessage.dismiss();
        // parse information to database
        Toast.makeText(getApplicationContext(), "Sending data to server succeeded", Toast.LENGTH_SHORT).show();
        finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences prefs = getSharedPreferences("location", MODE_PRIVATE);
        String restoredText = prefs.getString("longitude", null);
        if (restoredText != null) {
            longitudeString = prefs.getString("longitude", "0.0");
            latitudeString = prefs.getString("latitude", "0.0");
            final EditText longtitude = (EditText) findViewById(R.id.longtitudeNewTree);
            final EditText latitude = (EditText) findViewById(R.id.latitudeNewTree);
            longtitude.setText(longitudeString);
            latitude.setText(latitudeString);
            longitudeDouble = Double.parseDouble(longitudeString);
            latitudeDouble = Double.parseDouble(latitudeString);
        }
    }

}
