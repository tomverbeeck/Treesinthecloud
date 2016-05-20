package com.example.user.treesinthecloud.Routes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.treesinthecloud.ExtraInformationTabs.ExtraInfoTreeActivity;
import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.ConfigIDTree;
import com.example.user.treesinthecloud.TreeDatabase.DatabaseHandler;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity_Route extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMapRoute;
    //private ProgressDialog loading;
    private Route route = new Route();

    ArrayList<LatLng> markers = new ArrayList<>();
    private DatabaseHandler db;

    private HashMap<Integer, String> descr = new HashMap<>();

    private int MY_PERMISSIONS_REQUEST_LOCATION =11;

    private LatLng currentLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity__route);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        db = new DatabaseHandler(getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        String nameRoute = intent.getStringExtra("route_name");
        Toast.makeText(getApplicationContext(), nameRoute, Toast.LENGTH_SHORT).show();
        getRoute(nameRoute);
        getJSONRoute(nameRoute);
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
        mMapRoute.animateCamera(CameraUpdateFactory.newLatLngZoom(leuven, 15));

        mMapRoute.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMapRoute.getUiSettings().setZoomGesturesEnabled(true);
        mMapRoute.getUiSettings().setZoomControlsEnabled(true);
        mMapRoute.getUiSettings().setScrollGesturesEnabled(true);
        mMapRoute.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity_Route.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }else{
            allLocationRequiredStuff();
        }
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

                Tree treeDummy = new Tree();

                treeDummy = db.getTree(Integer.parseInt(marker.getSnippet()));

                name.setText(treeDummy.getName());
                specie.setText(treeDummy.getSpecie());
                status.setText(treeDummy.getStatus());

                return (infoWindows);
            }
        });
        mMapRoute.setOnInfoWindowClickListener(this);
    }


    private void getRoute(final String name) {
        //loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = ConfigIDRoute.GET_ROUTE_URL + name.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDRoute.JSON_ARRAY);
                    JSONObject data = result.getJSONObject(0);
                    route.setName(name);
                    route.setShortDescription(data.getString(ConfigIDRoute.KEY_SHORTDESCRIPTION));
                    route.setLength(data.getDouble(ConfigIDRoute.KEY_LENGTH));

                    markers.add(getLatLngFromString(data.getString(ConfigIDRoute.KEY_START)));
                    markers.add(getLatLngFromString(data.getString(ConfigIDRoute.KEY_END)));
                    addMarkers(markers.get(0));
                    addMarkers(markers.get(1));

                    LatLng location;
                    String query;

                    for(int i = 2; i<10; i++){
                        query = "w" + (i-1) + "LatLng";
                        String dataString = data.getString(query);
                        if(dataString.equals("not used")) {
                        }else{
                            location = getLatLngFromString(dataString);
                            markers.add(location);
                            addMarkers(markers.get(i));
                        }
                    }
                    // Checks, whether start and end locations are captured
                    if(markers.size() >= 2){
                        LatLng origin = markers.get(0);
                        LatLng dest = markers.get(1);

                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }else {
                        Toast.makeText(getApplicationContext(), "Please Select at Least Two Points", Toast.LENGTH_SHORT).show();
                        return;
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
                .title("waypoint")
                .snippet(route.getShortDescription())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_yellow_dot)));
    }

    private void addTreeMarkers(Tree tree){
        mMapRoute.addMarker(new MarkerOptions()
                .position(new LatLng(tree.getLatitude(), tree.getLongitude()))
                .title(tree.getStatus())
                .snippet("" + tree.getIdTree())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree)));
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        //set mode in walking
        String mode = "mode=walking";

        // Waypoints
        String waypoints = "";
        for(int i=2;i<markers.size();i++){
            LatLng point  = (LatLng) markers.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException, IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String shortDescr = descr.get(Integer.parseInt(marker.getSnippet()));
        Intent intentMoreInfo = new Intent(getApplicationContext(), ExtraInfoTreeActivity.class);
        intentMoreInfo.putExtra("treeID", Integer.parseInt(marker.getSnippet()));
        intentMoreInfo.putExtra("treeDescr", shortDescr);
        startActivity(intentMoreInfo);
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                e.printStackTrace();
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMapRoute.addPolyline(lineOptions);
        }
    }

    public void getJSONRoute(final String nameRoute){
        class GetJSONRoute extends AsyncTask<Void,Void,String> {
            private Context context = getApplicationContext();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /*loading = new ProgressDialog(myContext);
                loading.setMessage("Get routes...");
                loading.show();*/
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDRoute.JSON_ARRAY);
                    Route route = null;

                    for(int i = 0; i<result.length(); i++){
                        route = new Route();
                        JSONObject jo = result.getJSONObject(i);

                        getTree(jo.getString(ConfigIDRoute.KEY_TREE_ID), jo.getString(ConfigIDRoute.KEY_ROUTE_NAME), jo.getString(ConfigIDRoute.KEY_DESCR_TREE));
                    }
                    //loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String url = ConfigIDRoute.GET_TREE_URL + nameRoute.toString().trim();
                String s = rh.sendGetRequest(url);
                return s;
            }
        }
        GetJSONRoute gj = new GetJSONRoute();
        gj.execute();
    }

    private void getTree(final String name, final String nameRoute, final String descrTree) {
        //loading = ProgressDialog.show(this, "Please wait...", "Fetching...", false, false);

        String url = ConfigIDTree.DATA_URL + name.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDRoute.JSON_ARRAY);
                    JSONObject data = result.getJSONObject(0);

                    Tree tree = new Tree();
                    tree.setIdTree(Integer.parseInt(data.getString(ConfigIDTree.TAG_ID)));
                    tree.setLongitude(Double.parseDouble(data.getString(ConfigIDTree.TAG_LONGITUDE)));
                    tree.setLatitude(Double.parseDouble(data.getString(ConfigIDTree.TAG_LATITUDE)));
                    tree.setSpecie(data.getString(ConfigIDTree.TAG_SPECIE));
                    tree.setStatus(data.getString(ConfigIDTree.TAG_STATUS));
                    tree.setName(data.getString(ConfigIDTree.TAG_COMMON_NAME));
                    tree.setOriginalGirth(Integer.parseInt(data.getString(ConfigIDTree.TAG_ORIGINAL_GIRTH)));
                    tree.setCurrentGirth(Integer.parseInt(data.getString(ConfigIDTree.TAG_CURRENT_GIRTH)));
                    tree.setCuttingShape(data.getString(ConfigIDTree.TAG_CUTTING_SHAPE));
                    tree.setShortDescr(descrTree);

                    descr.put(tree.getIdTree(), tree.getShortDescr());

                    route.setTrees(tree);

                    addTreeMarkers(tree);

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

    public void allLocationRequiredStuff() {
        mMapRoute.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
        }

        //start with fixed location
        LatLng leuven = new LatLng(50.875, 4.708);
        //mMap.addMarker(new MarkerOptions().position(leuven).title("Marker on Group T"));

        if (currentLoc == null) {
            mMapRoute.moveCamera(CameraUpdateFactory.newLatLng(leuven));
            mMapRoute.animateCamera(CameraUpdateFactory.newLatLngZoom(leuven, 15));
        } else {
            mMapRoute.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
            mMapRoute.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
        }

    }

    }
