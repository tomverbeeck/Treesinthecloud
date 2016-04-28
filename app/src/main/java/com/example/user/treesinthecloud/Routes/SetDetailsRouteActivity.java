package com.example.user.treesinthecloud.Routes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.user.treesinthecloud.R;

public class SetDetailsRouteActivity extends AppCompatActivity {

    Button startCreating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_details_route);

        startCreating = (Button)findViewById(R.id.button_set_details_route_start_creating);
    }

    public void setNewRoute(View view){
        Intent newRouteIntent = new Intent(getApplicationContext(), NewRouteMapsActivity.class);
        startActivity(newRouteIntent);
    }
}
