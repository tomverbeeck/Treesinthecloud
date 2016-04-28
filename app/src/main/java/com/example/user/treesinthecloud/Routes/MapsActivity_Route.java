package com.example.user.treesinthecloud.Routes;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.ConfigIDTree;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;
import com.example.user.treesinthecloud.TreeDatabase.Tree;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity_Route extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMapRoute;
    private ProgressDialog loading;
    private List<Tree> trees = new ArrayList<>();
    private Tree treeDummy;

    LocationManager locationManager;
    PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;
    int locationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity__route);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        getJSONRoute(intent.getStringExtra("route_id"));
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

        LatLng erasmusTuin = new LatLng(50.87780243724243, 4.708591103553772);
        mMapRoute.addMarker(new MarkerOptions().position(erasmusTuin));

        mMapRoute.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMapRoute.getUiSettings().setZoomGesturesEnabled(true);
        mMapRoute.getUiSettings().setZoomControlsEnabled(true);
        mMapRoute.getUiSettings().setScrollGesturesEnabled(true);
        mMapRoute.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMapRoute.setMyLocationEnabled(true);
        mMapRoute.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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
        });
    }


    public void getJSONRoute(final String id){
        class GetJSONRoute extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                //loading.setMessage("Get routes...");
                //loading.show();
                /*loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        GetJSONRoute.this.cancel(true);
                    }
                });*/
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDTree.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++){
                        Tree dummy = new Tree();
                        JSONObject jo = result.getJSONObject(i);

                        dummy.setIdTree(Integer.parseInt(jo.getString(ConfigIDTree.TAG_ID)));

                        trees.add(dummy);

                        getData("" + dummy.getIdTree());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(ConfigIDRoute.DATA_URL + id);
                return s;
            }
        }
        GetJSONRoute gj = new GetJSONRoute();
        gj.execute();
    }



    private void getData(String id) {
        //loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = ConfigIDTree.DATA_URL + id.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity_Route.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(ConfigIDTree.JSON_ARRAY);
            JSONObject data = result.getJSONObject(0);
            treeDummy = null;
            treeDummy = new Tree(
                    Integer.parseInt(data.getString(ConfigIDTree.KEY_ID)),
                    Double.parseDouble(data.getString(ConfigIDTree.KEY_LONGITUDE)),
                    Double.parseDouble(data.getString(ConfigIDTree.KEY_LATITUDE)),
                    data.getString(ConfigIDTree.KEY_SPECIE),
                    data.getString(ConfigIDTree.KEY_STATUS),
                    data.getString(ConfigIDTree.KEY_CUTTING_SHAPE),
                    data.getString(ConfigIDTree.KEY_COMMON_NAME),
                    Integer.parseInt(data.getString(ConfigIDTree.KEY_ORIGINAL_GIRTH)),
                    Integer.parseInt(data.getString(ConfigIDTree.KEY_CURRENT_GIRTH)));

            trees.add(treeDummy);

            Toast.makeText(getApplicationContext(), "" + treeDummy.getLongitude() + "  " + treeDummy.getLatitude(), Toast.LENGTH_SHORT).show();

            mMapRoute.addMarker(new MarkerOptions()
                    .position(new LatLng(treeDummy.getLongitude(), treeDummy.getLatitude()))
                    .title(treeDummy.getName())
                    .snippet("" + treeDummy.getIdTree()          //0
                            + "-" + treeDummy.getSpecie()        //1
                            + "-" + treeDummy.getStatus()        //2
                            + "-" + treeDummy.getLatitude()      //3
                            + "-" + treeDummy.getLongitude()     //4
                            + "-" + treeDummy.getOriginalGirth() //5
                            + "-" + treeDummy.getCurrentGirth()  //6
                            + "-" + treeDummy.getCuttingShape()  //7
                            + "-" + treeDummy.getName())         //8
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
