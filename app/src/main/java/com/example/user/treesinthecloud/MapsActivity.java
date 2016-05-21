package com.example.user.treesinthecloud;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.AddTree.NewtreeActivity;
import com.example.user.treesinthecloud.ExtraInformationTabs.ExtraInfoTreeActivity;
import com.example.user.treesinthecloud.ExtraInformationTabs.GoogleSearch;
import com.example.user.treesinthecloud.Login.LoginActivity;
import com.example.user.treesinthecloud.Login.Status;
import com.example.user.treesinthecloud.Login.UserLocalStore;
import com.example.user.treesinthecloud.TreeDatabase.DatabaseHandler;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;
import com.example.user.treesinthecloud.TreeDatabase.Tree;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private int zoomLevel;
    private DatabaseHandler db;

    private String whichMarker;

    private double range = 0.002;

    public static final String MyPREFERENCES = "MyPrefs" ;

    //get one tree
    private Tree tree = new Tree();
    private static MapsActivity instance;

    private LatLng currentLoc;

    private int MY_PERMISSIONS_REQUEST_LOCATION =11;

    private HeatmapTileProvider mProvider;

    private String update;
    List<Tree> trees = new ArrayList<>();
    private List<Integer> visibleMarkers = new ArrayList<>();
    private int globalMarker;

    private LatLngBounds camerachange;

    private String website ="http://leuven.be";
    private String SPONSOR_URL ="http://projectmovie.16mb.com/FetchSponsorImage.php";

    String user_name,greeting;
    EditText username;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    public static final String SP_NAME = "N/A";

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MapsActivity";
    Uri profile_picture;
    UserLocalStore userLocalStore;

    AppCompatImageView proPic;
    Button upload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        loginPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        View header=navigationView.getHeaderView(0);

        // View header = navigationView.getHeaderView(0);
        username = (EditText) header.findViewById(R.id.proName);
        proPic = (AppCompatImageView)header.findViewById (R.id.imageView);
        upload = (Button)header.findViewById(R.id.button);
        upload.setOnClickListener(MapsActivity.this);

        username.setText("Welcome, " + loginPreferences.getString("useremail", ""));
        Uri  profile = Uri.parse(loginPreferences.getString("profile", ""));

        proPic.setImageURI(profile);

        userLocalStore = new UserLocalStore(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        db = new DatabaseHandler(getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView img = (ImageView)findViewById(R.id.sponserlogo);
        if(img!=null)
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(getApplicationContext(), GoogleSearch.class);
                if(website != null) {
                    browserIntent.putExtra("Specie", website);
                    startActivity(browserIntent);
                }
            }
        });

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        if(navigationView != null)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.map:
                        return true;

                    case R.id.login:
                        userLocalStore.clearUserData();
                        userLocalStore.setUserLoggedIn(false);

                        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intentLogin);
                        return true;

                    case R.id.addTree:
                        boolean addtree = ((Status)getApplication()).getLoggedIn();
                        if(addtree){

                            Intent intentmakeGroup = new Intent(getApplicationContext(), NewtreeActivity.class);
                            startActivity(intentmakeGroup);
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                            builder.setMessage(R.string.alertdialog_login_to_unlock_feature)
                                    .setNegativeButton(R.string.alertdialog_retry, null)
                                    .create()
                                    .show();
                        }
                        return true;

                    case R.id.settings:
                        Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intentSettings);
                        return true;
                    case R.id.routes:
                        Intent intentRoutes = new Intent(getApplicationContext(), RoutesActivity.class);
                        startActivity(intentRoutes);
                    default:
                        if(getSupportActionBar() != null)
                            getSupportActionBar().setTitle(R.string.app_name);
                        return true;
                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        if(getSupportActionBar() != null)
            getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        whichMarker = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).getString("markerImage", "");

        Toast.makeText(getApplicationContext(), R.string.toast_select_marker_representation, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), R.string.toast_select_marker_representation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        zoomLevel = 18;

        String type = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).getString("typeMap", "");
        if(type != null) {
            switch (type) {
                case "NORMAL":
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case "SATELLITE":
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case "HYBRIDE":
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case "TERRAIN":
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                default:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
            }
        }else
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.instance,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }else{
            allLocationRequiredStuff();
        }

        mMap.setOnInfoWindowClickListener(this);

        trees = db.getAllTrees();

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButton_normal:
                if (checked)
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.875, 4.708), zoomLevel));
                    globalMarker = 0;
                    mMap.setOnCameraChangeListener(getCameraChangeListener());
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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

                            Tree treeDummy;
                            treeDummy = db.getTree(Integer.parseInt(marker.getSnippet()));

                            name.setText(treeDummy.getName());
                            specie.setText(treeDummy.getSpecie());
                            status.setText(marker.getTitle());

                            return (infoWindows);
                        }
                    });
                    break;
            case R.id.radioButton_cluster:
                if (checked)
                    mMap.clear();
                    mMap.setInfoWindowAdapter(null);
                    setUpClusterer();
                    break;
            case R.id.radioButton_heat_map:
                if (checked)
                    mMap.clear();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.875, 4.708), 10));
                    initHeatMapSettings();
                    mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        private float maxZoom = 12;
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                            if(maxZoom < cameraPosition.zoom){
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
                                Toast.makeText(getApplicationContext(), R.string.toast_max_zoom_reached , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
        }
    }

    public GoogleMap.OnCameraChangeListener getCameraChangeListener()
    {
        return new GoogleMap.OnCameraChangeListener()
        {

            @Override
            public void onCameraChange(CameraPosition position)
            {
                float minZoom = 18;
                if(minZoom > position.zoom){
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
                    Toast.makeText(getApplicationContext(), R.string.toast_min_zoom_reached , Toast.LENGTH_SHORT).show();
                }else {

                    //This is the current user-viewable region of the map
                    LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

                    if (!bounds.equals(camerachange)) {
                        if (globalMarker == 10) {
                            globalMarker = 0;
                            mMap.clear();
                        } else {
                            globalMarker++;
                        }
                        camerachange = bounds;
                        addItems(bounds);
                    }
                }
            }
        };
    }

    public void addItems(final LatLngBounds bounds){
        class AddItems extends AsyncTask<Void, Void, String>{

            @Override
            protected String doInBackground(Void... params) {
                if(mMap != null)
                {
                    //Loop through all the items that are available to be placed on the map
                    for(Tree treedummy : trees)
                    {
                        //If the item is within the the bounds of the screen
                        if(bounds.contains(new LatLng(treedummy.getLatitude(), treedummy.getLongitude())))
                        {
                            //If the item isn't already being displayed
                            if(!visibleMarkers.contains(treedummy.getIdTree()))
                            {
                                visibleMarkers.add(treedummy.getIdTree());
                            }
                        }

                        //If the marker is off screen
                        else
                        {
                            //If the course was previously on screen
                            if(visibleMarkers.contains(treedummy.getIdTree()))
                            {
                                //1. Remove the Marker from the GoogleMap
                                int index = visibleMarkers.indexOf(treedummy.getIdTree());
                                visibleMarkers.remove(index);
                            }
                        }
                    }
                }
                return "klaar";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                mMap.setOnInfoWindowClickListener(MapsActivity.this);

                Tree item;

                for(int i = 0; i < visibleMarkers.size(); i++){
                    item = db.getTree(visibleMarkers.get(i));

                    Marker mkr = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(item.getLatitude(), item.getLongitude()))
                            .title("" + item.getStatus())
                            .snippet("" + item.getIdTree()));

                    if (whichMarker == null) {
                        mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree));
                    } else {
                        switch (whichMarker) {
                            case "Tree Marker":
                                if (mkr.getTitle().equals("Virtueel")) {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree_unknown));
                                } else if (mkr.getTitle().equals("Aangevraagd")) {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree_new));
                                } else {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree));
                                }
                                break;
                            case "Green Dot":
                                if (mkr.getTitle().equals("Virtueel")) {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_dot_unknown));
                                } else if (mkr.getTitle().equals("Aangevraagd")) {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_yellow_dot));
                                } else {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green_dot));
                                }
                                break;
                            case "Basic Red Marker":
                                if (mkr.getTitle().equals("Virtueel")) {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_grey_marker));
                                } else if (mkr.getTitle().equals("Aangevraagd")) {
                                    mkr.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                } else {
                                    mkr.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                }
                                break;
                            case "Basic Green Marker":
                                if (mkr.getTitle().equals("Virtueel")) {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_grey_marker));
                                } else if (mkr.getTitle().equals("Aangevraagd")) {
                                    mkr.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                } else {
                                    mkr.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                }
                                break;
                            default:
                                if (mkr.getTitle().equals("Virtueel")) {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree_unknown));
                                } else if (mkr.getTitle().equals("Aangevraagd")) {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree_new));
                                } else {
                                    mkr.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree));
                                }
                                break;
                        }
                    }
                }
            }
        }
        AddItems ai = new AddItems();
        ai.execute();
    }

    private void setUpClusterer() {

        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.875, 4.708), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        ClusterManager<Tree> mClusterManager = new ClusterManager<Tree>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        double lat;
        double lng;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < trees.size(); i++) {
            lat = trees.get(i).getLatitude();
            lng = trees.get(i).getLongitude();
            Tree offsetItem = new Tree(new LatLng(lat, lng));
            mClusterManager.addItem(offsetItem);
        }

    }

    private void initHeatMapSettings(){
        mProvider = new HeatmapTileProvider.Builder().data(generateLocations()).build();
        mProvider.setRadius(100);
        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> generateLocations() {
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        double lat;
        double lng;

        for (int i = 0; i < trees.size(); i++) {
            lat = trees.get(i).getLatitude();
            lng = trees.get(i).getLongitude();
            locations.add(new LatLng(lat, lng));
        }
        return locations;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                allLocationRequiredStuff();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                getSponsor();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }


        }

        // other 'case' lines to check for other
        // permissions this app might request
    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intentMoreInfo = new Intent(getApplicationContext(), ExtraInfoTreeActivity.class);
        intentMoreInfo.putExtra("treeID", Integer.parseInt(marker.getSnippet()));
        startActivity(intentMoreInfo);
    }

    public void getSponsor() {
        class GetSponsor extends AsyncTask<Void, Void,Bitmap> {
            RequestHandler rh = new RequestHandler();

            @Override
            protected Bitmap doInBackground(Void...params) {
                String longitudeString;
                String latitudeString;

                if (currentLoc != null) {
                    double longitudeDouble = currentLoc.longitude;
                    double latitudeDouble = currentLoc.latitude;
                    longitudeString = "" + longitudeDouble;
                    latitudeString = "" + latitudeDouble;
                }
                else{
                    double longitudeDouble = 50.875;
                    double latitudeDouble = 4.708;
                    longitudeString = "" + longitudeDouble;
                    latitudeString = "" + latitudeDouble;
                }
                HashMap<String,String> param= new HashMap();
                param.put("longitude",longitudeString);
                param.put("latitude",latitudeString);

                String s = rh.sendPostRequest(SPONSOR_URL, param);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String site ="www.leuven.be";
                String image ="http://a15_ee5_trees1.studev.groept.be/Pictures/leuvenlogo.png";
                try {
                    if(jsonObject != null) {
                        site = jsonObject.getString("website");
                        image = jsonObject.getString("url");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setWebsite(site);

                return rh.DownloadImage(image);

            }
            @Override
            public void onPostExecute (Bitmap bitmap){

                ImageView img = (ImageView) findViewById(R.id.sponserlogo);
                if(img != null)
                img.setImageBitmap(bitmap);
            }
        }
        GetSponsor gs = new GetSponsor();
        gs.execute();
    }
    public void setWebsite (String text) {
        website= text;
    }

    public void allLocationRequiredStuff(){
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
        }

        //start with fixed location
        LatLng leuven = new LatLng(50.875, 4.708);
        //mMap.addMarker(new MarkerOptions().position(leuven).title("Marker on Group T"));

        if(currentLoc == null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(leuven));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(leuven, zoomLevel));
        }else{
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, zoomLevel));
        }



        getSponsor();
    }

    void setProfile(Uri profile){
        if (null != profile) {
            // Set the image in ImageView
            proPic.setImageURI(profile);
        }
    }

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                loginPrefsEditor.putString("profile", selectedImageUri.toString());

                if (selectedImageUri != null) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);

                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    proPic.setImageURI(selectedImageUri);
                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        if(cursor != null)
            cursor.close();
        return res;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                openImageChooser();
                break;

        }
    }
}