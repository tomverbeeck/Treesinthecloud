package com.example.user.treesinthecloud.Routes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NewRouteMapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ArrayList<LatLng> markerPoints;
    private ArrayList<String> markerpointsString;
    private Button btnDraw, btnCreate, btnSelectTree, btnNext, btnPrev;
    private TextView help;
    private ArrayList<Tree> trees;
    private LatLng location;
    private int countTrees;

    private Route route = new Route();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initializing
        markerPoints = new ArrayList<>();
        markerpointsString = new ArrayList<>();
        trees = new ArrayList<>();
        btnDraw = (Button)findViewById(R.id.btnDraw);
        btnCreate = (Button)findViewById(R.id.btnCreate);
        btnSelectTree = (Button) findViewById(R.id.btnSelectTrees);
        btnNext = (Button) findViewById(R.id.btn_add_route_next_tree);
        btnPrev = (Button) findViewById(R.id.btn_add_route_prev_tree);

        help = (TextView)findViewById(R.id.textview_new_route_help);
        help.setText("Choose Start Location!");

        route.setName(getIntent().getExtras().getString("nameRoute"));
        route.setShortDescription(getIntent().getExtras().getString("descriptionRoute"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int zoomLevel = 15;

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

                View view1 = findViewById(R.id.btnSelectTrees);
                view1.setVisibility(View.INVISIBLE);

                View view2 = findViewById(R.id.btnDraw);
                view2.setVisibility(View.VISIBLE);

                View view3 = findViewById(R.id.btn_add_route_next_tree);
                view3.setVisibility(View.INVISIBLE);

                View view4 = findViewById(R.id.btn_add_route_prev_tree);
                view4.setVisibility(View.INVISIBLE);

                countTrees = 0;
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
                }else {
                    Toast.makeText(getApplicationContext(), "Please Select at Least Two Points", Toast.LENGTH_SHORT).show();
                    return;
                }
                help.setText("Good? then select your trees that you want to see during the walk (click per marker)");

                Button button = (Button) v;
                button.setVisibility(View.INVISIBLE);
                View view = findViewById(R.id.btnSelectTrees);
                view.setVisibility(View.VISIBLE);

                for(int j = 0; j<markerPoints.size(); j++){
                    markerpointsString.add(getStringFromLatLng(markerPoints.get(j)));
                }
                if(markerpointsString.size() != 10){
                    int place = markerpointsString.size();
                    for(int i = place ; i<=9; i++){
                        markerpointsString.add("not used");
                    }
                }
                route.setMarkers(markerpointsString);

                mMap.setOnMapClickListener(null);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String idTree = "";
                String shortDescr = "";
                String res = "";

                // Get a set of the entries
                Set set = route.getTrees().entrySet();
                // Get an iterator
                Iterator i = set.iterator();
                // Display elements
                while(i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    idTree = me.getKey().toString();
                    shortDescr = me.getValue().toString();
                    addTreeToRoute(idTree, shortDescr);
                }
                addWholeRoute();

                finish();
            }
        });

        btnSelectTree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                countTrees = 0 ;
                location = markerPoints.get(countTrees);
                getJSON();

                Button button = (Button) v;
                button.setVisibility(View.INVISIBLE);
                View view = findViewById(R.id.btn_add_route_next_tree);
                view.setVisibility(View.VISIBLE);
                View view2 = findViewById(R.id.btn_add_route_prev_tree);
                view2.setVisibility(View.VISIBLE);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                countTrees++;
                location = markerPoints.get(countTrees);
                getJSON();

                help.setText("Select the trees that you want to see at this point!\n");

                if(countTrees == markerPoints.size()-2)
                    help.setText("Look out if you press next you can't go back to selecting trees!");

                if(countTrees == markerPoints.size()-1) {
                    //end routine
                    Button button = (Button) v;
                    button.setVisibility(View.INVISIBLE);
                    View view = findViewById(R.id.btnCreate);
                    view.setVisibility(View.VISIBLE);
                    View view2 = findViewById(R.id.btn_add_route_prev_tree);
                    view2.setVisibility(View.INVISIBLE);
                }

            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                countTrees--;
                location = markerPoints.get(countTrees);
                getJSON();

                help.setText("Select the trees that you want to see at this point!");
                //end routine

            }
        });

        // Setting a custom info window adapter for the google map
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {
                View infoWindows = getLayoutInflater().inflate(R.layout.layout_new_route_infowindow, null);

                TextView name = (TextView) infoWindows.findViewById(R.id.textview_new_route_name_layout_window);
                TextView specie = (TextView) infoWindows.findViewById(R.id.textview_new_route_specie_layout_window);
                TextView status = (TextView) infoWindows.findViewById(R.id.textview_new_route_status_layout_window);

                String [] parts = marker.getSnippet().split("-");
                name.setText(marker.getTitle());
                specie.setText(parts[3]);
                status.setText(parts[4]);

                return (infoWindows);

            }
        });
        mMap.setOnInfoWindowClickListener(this);
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

    public void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

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

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);

                        tree.setIdTree(Integer.parseInt(jo.getString(ConfigIDTree.TAG_ID)));
                        tree.setName(jo.getString(ConfigIDTree.TAG_COMMON_NAME));
                        tree.setLongitude(Double.parseDouble(jo.getString(ConfigIDTree.TAG_LONGITUDE)));
                        tree.setLatitude(Double.parseDouble(jo.getString(ConfigIDTree.TAG_LATITUDE)));
                        tree.setSpecie(jo.getString(ConfigIDTree.TAG_SPECIE));
                        tree.setStatus(jo.getString(ConfigIDTree.TAG_STATUS));

                        if (tree != null) {
                            //Toast.makeText(getApplicationContext(), "Tree added", Toast.LENGTH_SHORT).show();
                            trees.add(tree);
                            String snippet = "" + tree.getIdTree()      //0
                                    + "-" + tree.getLatitude()          //1
                                    + "-" + tree.getLongitude()         //2
                                    + "-" + tree.getSpecie()            //3
                                    + "-" + tree.getStatus();           //4
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(tree.getLatitude(), tree.getLongitude()))
                                    .title(tree.getName())
                                    .snippet(snippet)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree)));
                        }
                    }

                    Toast.makeText(getApplicationContext(), "trees size: " + trees.size(), Toast.LENGTH_SHORT).show();
                    //this.loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                double dummy = location.longitude - 0.001;
                String curLongMin = "" + dummy;
                dummy = location.longitude + 0.001;
                String curLongMax = "" + dummy;
                dummy = location.latitude + 0.001;
                String curLatMax = "" + dummy;
                dummy = location.latitude - 0.001;
                String curLatMin = "" + dummy;
                String rest = "?latMin=" + curLatMin + "&latMax=" + curLatMax + "&longMin=" + curLongMin + "&longMax=" + curLongMax;

                String s = rh.sendGetRequest(ConfigIDTree.URL_GET_TREE_AREA + rest.trim());
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //add tree to database
        Intent intentNewTree = new Intent(getApplicationContext(), NewRouteDescriptionTree.class);

        String [] parts = marker.getSnippet().split("-");

        intentNewTree.putExtra("idTree", parts[0]);
        startActivityForResult(intentNewTree, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String stredittext=data.getStringExtra("descriptionTree");
                String id = data.getStringExtra("idTreesReturn");
                route.addTree(id , stredittext, getApplicationContext());
                Toast.makeText(getApplicationContext(), "Tree added to Database, trees size is " + route.getTrees().size(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<String> latlngToString (ArrayList<LatLng>waypoints){
        ArrayList<String> dummy = new ArrayList<>();
        Double latitude;
        Double longitude;
        String latlng;
        for(int i = 0; i<waypoints.size(); i++){
            latitude = waypoints.get(i).latitude;
            longitude = waypoints.get(i).longitude;
            latlng = latitude + "-" + longitude;

            dummy.add(latlng);
        }
        return dummy;
    }

    private String getStringFromLatLng(LatLng loc){
        String lat = "" + loc.latitude;
        String longi = "" + loc.longitude;
        return lat + "-" + longi;
    }

    private void addWholeRoute(){
        final String name = route.getName();
        final String shortDescription = route.getShortDescription();
        final String length = "0"; //+ route.getLength();
        final String startLatLng = route.getMarkers().get(0);
        final String endLatLng = route.getMarkers().get(1);
        final String w1 = route.getMarkers().get(2);
        final String w2 = route.getMarkers().get(3);
        final String w3 = route.getMarkers().get(4);
        final String w4 = route.getMarkers().get(5);
        final String w5 = route.getMarkers().get(6);
        final String w6 = route.getMarkers().get(7);
        final String w7 = route.getMarkers().get(8);
        final String w8 = route.getMarkers().get(9);

        class AddWholeRoute extends AsyncTask<Void,Void,String>{

            //ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(MainActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                Toast.makeText(NewRouteMapsActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(ConfigIDRoute.KEY_NAME,name);
                params.put(ConfigIDRoute.KEY_SHORTDESCRIPTION,shortDescription);
                params.put(ConfigIDRoute.KEY_LENGTH,length);
                params.put(ConfigIDRoute.KEY_START,startLatLng);
                params.put(ConfigIDRoute.KEY_END,endLatLng);
                params.put(ConfigIDRoute.KEY_W1,w1);
                params.put(ConfigIDRoute.KEY_W2,w2);
                params.put(ConfigIDRoute.KEY_W3,w3);
                params.put(ConfigIDRoute.KEY_W4,w4);
                params.put(ConfigIDRoute.KEY_W5,w5);
                params.put(ConfigIDRoute.KEY_W6,w6);
                params.put(ConfigIDRoute.KEY_W7,w7);
                params.put(ConfigIDRoute.KEY_W8,w8);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigIDRoute.ADDROUTE_URL, params);
                return res;
            }
        }

        AddWholeRoute adr = new AddWholeRoute();
        adr.execute();
    }

    private void addTreeToRoute(String idTree, String shortDescr){
        final String name = route.getName();
        final String idTrees = idTree;
        final String descr = shortDescr;

        class AddWholeRoute extends AsyncTask<Void,Void,String>{

            //ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(MainActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                Toast.makeText(NewRouteMapsActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(ConfigIDRoute.KEY_TREE_ID, idTrees);
                params.put(ConfigIDRoute.KEY_ROUTE_NAME, name);
                params.put(ConfigIDRoute.KEY_DESCR_TREE, descr);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(ConfigIDRoute.ADDTREETOROUTE_URL, params);
                return res;
            }
        }

        AddWholeRoute adr = new AddWholeRoute();
        adr.execute();
    }
}
