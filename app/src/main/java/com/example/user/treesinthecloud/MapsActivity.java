package com.example.user.treesinthecloud;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private GoogleMap mMap;

    private MenuItem mSearchAction;

    private boolean isSearchOpened = false;
    private EditText edtSeach;

    private DrawerLayout drawerlayout;
    private ListView listView;
    private String[]options;

    private ActionBarDrawerToggle drawerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        drawerlayout=(DrawerLayout) findViewById(R.id.drawer_layout);

        options=getResources().getStringArray(R.array.sideMenu);
        listView=(ListView) findViewById(R.id.drawersList);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options));

        listView.setOnItemClickListener(this);

        drawerListener = new ActionBarDrawerToggle(this, drawerlayout,
                R.drawable.horizontalbars, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(MapsActivity.this, "Drawer Closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(MapsActivity.this, "Drawer Opened", Toast.LENGTH_SHORT).show();
            }
        };
        drawerlayout.setDrawerListener(drawerListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(50.875, 4.708);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Group T"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, options[position] + " was selected", Toast.LENGTH_SHORT).show();
        selectItem(position);
    }

    public void selectItem(int position){
        listView.setItemChecked(position, true);
        setTitle(options[position]);
    }
    public void setTitle(String title) {
        if(title != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}