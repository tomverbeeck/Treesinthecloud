package com.example.user.treesinthecloud;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.AddTree.NewtreeActivity;
import com.example.user.treesinthecloud.ExtraInformationTabs.ExtraInfoTreeActivity;
import com.example.user.treesinthecloud.Groups.MakegroupActivity;
import com.example.user.treesinthecloud.Login.LoginActivity;
import com.example.user.treesinthecloud.Login.UserLocalStore;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback {

    private GoogleMap mMap;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private int zoomLevel;
    private DatabaseHandler db;

    private double range = 0.002;

    UserLocalStore userLocalStore;

    //get one tree
    private Tree tree = new Tree();
    private static MapsActivity instance;

    private LatLng currentLoc;

    private int MY_PERMISSIONS_REQUEST_LOCATION =11;

    private String update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        /*SharedPreferences prefs = getSharedPreferences("updateornot", MODE_PRIVATE);
        String restoredText = prefs.getString("updateornot", null);
        if(restoredText == null){
            SharedPreferences.Editor editor = getSharedPreferences("updateornot", MODE_PRIVATE).edit();
            editor.putString("updateornot", "false");
            editor.commit();
        }else{
            setUpdate();
        }*/

       // Toast.makeText(getApplicationContext(), "update is " + update, Toast.LENGTH_SHORT).show();

        db = new DatabaseHandler(getApplicationContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userLocalStore = new UserLocalStore(this);
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

                    case R.id.makeGroup:
                        Intent intentmakeGroup = new Intent(getApplicationContext(), MakegroupActivity.class);
                        startActivity(intentmakeGroup);
                        getSupportActionBar().setTitle(R.string.title_activity_make_group);
                        return true;

                    case R.id.settings:
                        Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intentSettings);
                        getSupportActionBar().setTitle(R.string.title_activity_settings);
                        return true;
                    case R.id.routes:
                        Intent intentRoutes = new Intent(getApplicationContext(), RoutesActivity.class);
                        startActivity(intentRoutes);
                        getSupportActionBar().setTitle(R.string.title_activity_routes);
                    default:
                        //Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
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

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchview = (SearchView) MenuItemCompat.getActionView(searchItem);

        //Define listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //do something when action item collapses
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //do something when expanded
                searchItem.collapseActionView();
                return true;
            }
        };

        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

        return super.onCreateOptionsMenu(menu);
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
        zoomLevel = 14;

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.instance,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return;



        }
        mMap.setMyLocationEnabled(true);
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

                String [] parts = marker.getSnippet().split("-");
                name.setText(marker.getTitle());
                specie.setText(parts[3]);
                status.setText(parts[4]);

                return (infoWindows);
            }
        });
        mMap.setOnInfoWindowClickListener(this);

                //start with current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
        }

        //getJSON();

        List<Tree> trees = new ArrayList<>();
        trees = db.getAllTrees();

        for(int i = 0; i < 10000; i++){
            addMarker(trees.get(i));
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }

        }

        // other 'case' lines to check for other
        // permissions this app might request
    }

    public void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            private ProgressDialog loading = new ProgressDialog(MapsActivity.this);
            @Override
            protected void onPreExecute() {
                loading.setMessage(getResources().getString(R.string.text_loading_trees));
                loading.show();
                loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        GetJSON.this.cancel(true);
                    }
                });
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSONObject jsonObject = null;

                /*SharedPreferences.Editor editor = getSharedPreferences("updateornot", MODE_PRIVATE).edit();
                editor.putString("updateornot", "true");
                editor.commit();
                setUpdate();*/

                try {

                    jsonObject = new JSONObject(s);
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

                        //addMarker(tree);

                        /*if(update == "false")
                            db.addTree(tree);*/
                        //Toast.makeText(getApplicationContext(), "update in loop is " + update, Toast.LENGTH_SHORT).show();
                    }

                   /* editor.putString("updateornot", "true");
                    editor.commit();
                    setUpdate();*/

                    Toast.makeText(getApplicationContext(), "resultsize: " + result.length(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "sqlite size is " + db.getTreesCount(), Toast.LENGTH_SHORT).show();
                    this.loading.dismiss();
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

    public void addMarker(Tree tree){
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
                        + "-" + tree.getName()         //8
                        + "-" + tree.getShortDescr())   //9

                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_tree)));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intentMoreInfo = new Intent(getApplicationContext(), ExtraInfoTreeActivity.class);

        String [] parts = marker.getSnippet().split("-");
        intentMoreInfo.putExtra("treeID", parts[0]);
        intentMoreInfo.putExtra("treeLat", Double.parseDouble(parts[1]));
        intentMoreInfo.putExtra("treeLong", Double.parseDouble(parts[2]));
        if(parts[8] != "") {
            intentMoreInfo.putExtra("treeSpecie", parts[3]);
            intentMoreInfo.putExtra("treeStatus", parts[4]);
            intentMoreInfo.putExtra("treeOrgGirth", Integer.parseInt(parts[6]));
            intentMoreInfo.putExtra("treeCurGirth", Integer.parseInt(parts[5]));
            intentMoreInfo.putExtra("treeCutShape", parts[7]);
            intentMoreInfo.putExtra("treeName", parts[8]);
            intentMoreInfo.putExtra("shortDescription", parts[9]);
        }
        startActivity(intentMoreInfo);
    }

    private void setUpdate(){
        SharedPreferences prefs = getSharedPreferences("updateornot", MODE_PRIVATE);
        String restoredText = prefs.getString("updateornot", null);
        update = restoredText;
    }
}