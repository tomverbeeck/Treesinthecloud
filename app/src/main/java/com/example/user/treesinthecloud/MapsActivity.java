package com.example.user.treesinthecloud;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.AddTree.NewtreeActivity;
import com.example.user.treesinthecloud.Login.LoginActivity;
import com.example.user.treesinthecloud.Login.UserLocalStore;
import com.example.user.treesinthecloud.TreeDatabase.ConfigIDTree;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;
import com.example.user.treesinthecloud.TreeDatabase.Tree;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private int zoomLevel;

    UserLocalStore userLocalStore;

    //get one tree
    private ProgressDialog loading;
    private Tree tree = new Tree();
    private static MapsActivity instance;

    TextView textSpecie;

    //get all trees
    String myJSON;
    JSONArray trees = null;
    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userLocalStore = new UserLocalStore(this);

        instance = this;

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
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
                        getSupportActionBar().setTitle("Map");
                        return true;

                    case R.id.login:
                        userLocalStore.clearUserData();
                        userLocalStore.setUserLoggedIn(false);

                        Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                        getSupportActionBar().setTitle(R.string.title_activity_login);
                        startActivity(intentLogin);
                        return true;

                    case R.id.addTree:
                        Intent intentAddTree = new Intent(getApplicationContext(), NewtreeActivity.class);
                        startActivity(intentAddTree);
                        getSupportActionBar().setTitle(R.string.title_acitivty_add_tree);
                        return true;

                    case R.id.settings:
                        Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intentSettings);
                        getSupportActionBar().setTitle(R.string.title_activity_settings);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
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
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        zoomLevel = 10;

        //start with fixed location
        LatLng leuven = new LatLng(50.875, 4.708);
        mMap.addMarker(new MarkerOptions().position(leuven).title("Marker on Group T"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(leuven));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(leuven, zoomLevel));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        //start with current location
        /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(zoomLevel)            // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to north
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }*/

        getJSON();

        //loading.dismiss();

        /*mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            //private final View mWindow = getLayoutInflater().inflate(R.layout);
            private final View mWindow = getLayoutInflater().inflate(R.layout.layout_window, null);
            private final View mContents = getLayoutInflater().inflate(R.layout.layout_window, null);

            @Override
            public View getInfoWindow(Marker marker) {
                textSpecie = (TextView) findViewById(R.id.textview_specie_layout_window);
                textSpecie.setText("Specie: "); //+ tree.getSpecie());
                return mWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                textSpecie = (TextView) findViewById(R.id.textview_specie_layout_window);
                textSpecie.setText("Specie: "); //+ tree.getSpecie());

                return mContents;
            }
        });*/

    }

    public void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(getApplicationContext(),"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                JSON_STRING = s;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDTree.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);

                        tree.setIdTree(Integer.parseInt(jo.getString(ConfigIDTree.TAG_ID)));
                        tree.setLongitude(Double.parseDouble(jo.getString(ConfigIDTree.TAG_LONGITUDE)));
                        tree.setLatitude(Double.parseDouble(jo.getString(ConfigIDTree.TAG_LATITUDE)));
                        tree.setSpecie(jo.getString(ConfigIDTree.TAG_SPECIE));
                        tree.setStatus(jo.getString(ConfigIDTree.TAG_STATUS));
                        tree.setName(jo.getString(ConfigIDTree.TAG_COMMON_NAME));
                        tree.setOriginalGirth(Integer.parseInt(jo.getString(ConfigIDTree.TAG_ORIGINAL_GIRTH)));
                        tree.setCurrentGirth(Integer.parseInt(jo.getString(ConfigIDTree.TAG_CURRENT_GIRTH)));
                        tree.setCuttingShape(jo.getString(ConfigIDTree.TAG_CUTTING_SHAPE));

                        Toast.makeText(getApplicationContext(), tree.toString(), Toast.LENGTH_SHORT).show();

                        addMarkers(tree);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(ConfigIDTree.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void addMarkers(Tree tree){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(tree.getLatitude(), tree.getLongitude()))
                .title(tree.getSpecie())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree)));
    }
}