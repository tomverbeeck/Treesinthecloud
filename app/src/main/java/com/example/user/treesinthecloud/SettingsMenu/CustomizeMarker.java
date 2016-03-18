package com.example.user.treesinthecloud.SettingsMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.treesinthecloud.MapsActivity;
import com.example.user.treesinthecloud.R;

/**
 * Created by User on 11/03/2016.
 */
public class CustomizeMarker extends Activity{

    private ListView list_markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_language);


        list_markers = (ListView)findViewById(R.id.listView_change_language);
        String[] languages = new String[]{"English", "Nederlands", "Spaans"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        list_markers.setAdapter(adapter);
        list_markers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) list_markers.getItemAtPosition(position);
                String toast = "Language changed into " + itemValue;
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.putExtra(ConfigProfile.KEY_USERNAME, username);
                startActivity(intent);
            }
        });

    }



}
