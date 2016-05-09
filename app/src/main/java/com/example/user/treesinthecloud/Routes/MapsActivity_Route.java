package com.example.user.treesinthecloud.Routes;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.treesinthecloud.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity_Route extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMapRoute;
    private ProgressDialog loading;
    private Route route = new Route();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity__route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        String nameRoute = intent.getStringExtra("route_name");
        Toast.makeText(getApplicationContext(), nameRoute, Toast.LENGTH_SHORT).show();
        getRoute(nameRoute);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapRoute = googleMap;

        //start with fixed location
        LatLng leuven = new LatLng(50.875, 4.708);
        //mMap.addMarker(new MarkerOptions().position(leuven).title("Marker on Group T"));
        mMapRoute.moveCamera(CameraUpdateFactory.newLatLng(leuven));
        mMapRoute.animateCamera(CameraUpdateFactory.newLatLngZoom(leuven, 10));

        mMapRoute.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMapRoute.getUiSettings().setZoomGesturesEnabled(true);
        mMapRoute.getUiSettings().setZoomControlsEnabled(true);
        mMapRoute.getUiSettings().setScrollGesturesEnabled(true);
        mMapRoute.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMapRoute.setMyLocationEnabled(true);
        /*mMapRoute.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View infoWindows = getLayoutInflater().inflate(R.layout.layout_window, null);

                TextView name = (TextView) infoWindows.findViewById(R.id.textview_name_layout_window);
                TextView specie = (TextView) infoWindows.findViewById(R.id.textview_specie_layout_window);
                TextView status = (TextView) infoWindows.findViewById(R.id.textview_status_layout_window);

                String [] parts = marker.getSnippet().split("-");
                name.setText(marker.getTitle());
                specie.setText(parts[1]);
                status.setText(parts[2]);

                return (infoWindows);
            }
        });*/
    }


    private void getRoute(final String name) {
        loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = ConfigIDRoute.GET_ROUTE_URL + name.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDRoute.JSON_ARRAY);
                    JSONObject data = result.getJSONObject(0);
                    route.setName(name);
                    route.setShortDescription(data.getString(ConfigIDRoute.KEY_SHORTDESCRIPTION));
                    route.setLength(data.getString(ConfigIDRoute.KEY_LENGTH));

                    ArrayList<LatLng> markers = new ArrayList<>();
                    markers.add(getLatLngFromString(data.getString(ConfigIDRoute.KEY_START)));
                    markers.add(getLatLngFromString(data.getString(ConfigIDRoute.KEY_END)));
                    addMarkers(markers.get(0));
                    addMarkers(markers.get(1));

                    LatLng location;
                    String query;
                    for(int i = 2; i<10; i++){
                        query = "w" + (i-1) + "LatLng";
                        String dataString = data.getString(query).toString();
                        location = getLatLngFromString(dataString);
                        if(location.latitude != 0) {
                            markers.add(location);
                        }
                        addMarkers(markers.get(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity_Route.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private LatLng getLatLngFromString(String latlong){
        if(latlong != "not used" || latlong != null || latlong != "") {
            String[] parts = latlong.split("-");
            LatLng dummy = new LatLng(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
            return dummy;
        }
        return new LatLng(0,0);
    }

    private void addMarkers(LatLng marker){
        mMapRoute.addMarker(new MarkerOptions()
                .position(marker)
                .title(route.getName())
                .snippet(route.getShortDescription()));
    }
}
