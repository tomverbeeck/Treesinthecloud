package com.example.user.treesinthecloud;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.treesinthecloud.SettingsMenu.ChangeColor;
import com.example.user.treesinthecloud.SettingsMenu.ChangeLanguage;
import com.example.user.treesinthecloud.SettingsMenu.ChangePassword;
import com.example.user.treesinthecloud.SettingsMenu.CustomizeMarker;

/**
 * Created by User on 11/03/2016.
 */
public class SettingsActivity extends AppCompatActivity{

    private ListView list_settings;
    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        list_settings = (ListView)findViewById(R.id.listView_settings);
        String[] options = new String[]{"Color Menu", "Language", "Customize Marker",
                "Change Password"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        list_settings.setAdapter(adapter);
        list_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) list_settings.getItemAtPosition(position);
                String toast = "Position " + position + " Item: " + itemValue;
                if(itemValue == "Color Menu") {
                    Intent i = new Intent(context, ChangeColor.class);
                    startActivity(i);
                }
                if(itemValue == "Language"){
                    Intent j = new Intent(context, ChangeLanguage.class);
                    startActivity(j);
                }
                if(itemValue == "Customize Marker"){
                    Intent j = new Intent(context, CustomizeMarker.class);
                    startActivity(j);
                }
                if(itemValue == "Change Password"){
                    Intent j = new Intent(context, ChangePassword.class);
                    startActivity(j);
                }
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
