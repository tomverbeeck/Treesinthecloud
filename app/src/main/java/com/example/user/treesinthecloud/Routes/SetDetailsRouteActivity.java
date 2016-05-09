package com.example.user.treesinthecloud.Routes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.treesinthecloud.R;

public class SetDetailsRouteActivity extends AppCompatActivity {

    Button startCreating;
    EditText name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_details_route);

        startCreating = (Button)findViewById(R.id.button_set_details_route_start_creating);
        description = (EditText)findViewById(R.id.editText_new_route_details_short_description);
        name = (EditText)findViewById(R.id.editText_new_route_details_name);

    }

    public void setNewRoute(View view){
        String nameRoute = name.getText().toString();
        String shortdes = description.getText().toString();
        Intent newRouteIntent = new Intent(getApplicationContext(), NewRouteMapsActivity.class);

        newRouteIntent.putExtra("nameRoute", nameRoute);
        newRouteIntent.putExtra("descriptionRoute", shortdes);
        startActivity(newRouteIntent);
    }
}
