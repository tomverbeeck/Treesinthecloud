package com.example.user.treesinthecloud.Routes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.R;

public class NewRouteDescriptionTree extends AppCompatActivity {

    private TextView textDescription;
    private EditText descrption;
    private Button addTree;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_route_description_tree);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textDescription = (TextView)findViewById(R.id.Textview_new_route_new_tree);
        descrption = (EditText)findViewById(R.id.Edittext_new_route_new_tree);
        addTree = (Button)findViewById(R.id.button_new_route_add_tree);

        Intent i = getIntent();
        id = i.getStringExtra("idTree");
    }

    public void addTreeToRoute(View view){
        Intent intent = new Intent();
        String descriptionTree = descrption.getText().toString();
        intent.putExtra("descriptionTree", descriptionTree);
        intent.putExtra("idTreesReturn", id);
        Toast.makeText(getApplicationContext(), "id is " + id + " descr is " + descriptionTree, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK, intent);
        finish();
    }
}
