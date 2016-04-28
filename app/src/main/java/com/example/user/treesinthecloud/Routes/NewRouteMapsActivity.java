package com.example.user.treesinthecloud.Routes;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.ConfigIDTree;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;
import com.example.user.treesinthecloud.TreeDatabase.Tree;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
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

public class NewRouteMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<LatLng> markerPoints;
    private Button btnDraw, btnCreate, btnSelectTree;
    private TextView help;
    private ArrayList<Tree> trees;
    private LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initializing
        markerPoints = new ArrayList<LatLng>();
        trees = new ArrayList<>();
        btnDraw = (Button)findViewById(R.id.btnDraw);
        btnCreate = (Button)findViewById(R.id.btnCreate);
        btnSelectTree = (Button) findViewById(R.id.btnSelectTrees);
        help = (TextView)findViewById(R.id.textview_new_route_help);
        help.setText("Choose Start Location!");

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
        mMap = googleMap;

        int zoomLevel = 12;

        //start with fixed location
        LatLng leuven = new LatLng(50.875, 4.708);
        //mMap.addMarker(new MarkerOptions().position(leuven).title("Marker on Group T"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(leuven));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(leuven, zoomLevel));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already 10 locations with 8 waypoints and 1 start location and 1 end location.
                // Upto 8 waypoints are allowed in a query for non-business users
                if(markerPoints.size()>=10){
                    Toast.makeText(getApplicationContext(), "You can't add more then 8 waypoints!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Adding new item to the ArrayList
                markerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED and
                 * for the rest of markers, the color is AZURE
                 */

                drawCircle(point);

                if(markerPoints.size()==1){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    help.setText("Set End Location!");
                }else if(markerPoints.size()==2){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    help.setText("Set Points on the Way!");
                }else{
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);
            }
        });

        // The map will be cleared on long click
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                // Removes all the points from Google Map
                mMap.clear();

                // Removes all the points in the ArrayList
                markerPoints.clear();

                help.setText("Choose Start Location!");

                View view = findViewById(R.id.btnCreate);
                view.setVisibility(View.INVISIBLE);

                View view2 = findViewById(R.id.btnDraw);
                view2.setVisibility(View.VISIBLE);
            }
        });

        // Click event handler for Button btn_draw
        btnDraw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Checks, whether start and end locations are captured
                if(markerPoints.size() >= 2){
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
                help.setText("Good? then select your trees that you want to see during the walk (click per marker)");

                Button button = (Button) v;
                button.setVisibility(View.INVISIBLE);
                View view = findViewById(R.id.btnSelectTrees);
                view.setVisibility(View.VISIBLE);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();

            }
        });

        btnSelectTree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                for (int i = 0; i < markerPoints.size(); i++) {
                    location = markerPoints.get(i);
                    if (location != null) {
                        Toast.makeText(getApplicationContext(), "Load trees for marker " + i, Toast.LENGTH_SHORT).show();
                        getJSON();
                    }
                }

                Button button = (Button) v;
                button.setVisibility(View.INVISIBLE);
                View view = findViewById(R.id.btnCreate);
                view.setVisibility(View.VISIBLE);
            }
        });
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
        for(int i=2;i<markerPoints.size();i++){
            LatLng point  = (LatLng) markerPoints.get(i);
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
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

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
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(100);

        // Border color of the circle
        circleOptions.strokeColor(Color.GREEN);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);
    }

    public void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            //private ProgressDialog loading = new ProgressDialog(NewRouteMapsActivity.this);
            @Override
            protected void onPreExecute() {
                /*loading.setMessage(getResources().getString(R.string.text_loading_trees));
                loading.show();
                loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        GetJSON.this.cancel(true);
                    }
                });*/
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                JSONObject jsonObject = null;
                try {

                    jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDTree.TAG_JSON_ARRAY);

                    Tree tree = new Tree();

                    for(int i = 0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);

                        tree.setIdTree(Integer.parseInt(jo.getString(ConfigIDTree.TAG_ID)));
                        tree.setLongitude(Double.parseDouble(jo.getString(ConfigIDTree.TAG_LONGITUDE)));
                        tree.setLatitude(Double.parseDouble(jo.getString(ConfigIDTree.TAG_LATITUDE)));

                        if(tree!=null) {
                            //Toast.makeText(getApplicationContext(), "Tree added", Toast.LENGTH_SHORT).show();
                            trees.add(tree);
                            addMarkers(tree);
                        }
                    }

                    Toast.makeText(getApplicationContext(), "result size: " + result.length(), Toast.LENGTH_SHORT).show();
                    //this.loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                double dummy = location.longitude -0.001;
                String curLongMin = "" + dummy;
                dummy = location.longitude + 0.001;
                String curLongMax = "" + dummy;
                dummy = location.latitude + 0.001;
                String curLatMax = "" + dummy;
                dummy = location.latitude - 0.001;
                String curLatMin = "" + dummy;
                String rest = "?latMin=" + curLatMin + "&latMax=" + curLatMax + "&longMin=" + curLongMin + "&longMax=" + curLongMax;
                //Toast.makeText(getApplicationContext(), rest, Toast.LENGTH_LONG).show();

                String s = rh.sendGetRequest(ConfigIDTree.URL_GET_TREE_AREA + rest.trim());
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private boolean alreadyExist(Tree tree){
        for(int i = 0; i<trees.size(); i++){
            if(trees.get(i).getLatitude() == tree.getLatitude() && trees.get(i).getLongitude() == tree.getLongitude())
                return true;
        }
        return false;
    }

    private void addMarkers(Tree tree){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(tree.getLatitude(), tree.getLongitude()))
                .title(tree.getName())
                .snippet("" + tree.getIdTree()          //0
                        + "-" + tree.getLatitude()      //1
                        + "-" + tree.getLongitude()     //2
                        + "-" + tree.getSpecie()        //3
                        + "-" + tree.getStatus()        //4
                        + "-" + tree.getOriginalGirth() //5
                        + "-" + tree.getCurrentGirth()  //6
                        + "-" + tree.getCuttingShape()  //7
                        + "-" + tree.getName())         //8
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree)));
    }
}
