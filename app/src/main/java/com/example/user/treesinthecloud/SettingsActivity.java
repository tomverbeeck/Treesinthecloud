package com.example.user.treesinthecloud;

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

public class SettingsActivity extends AppCompatActivity{

    private ListView list_settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        list_settings = (ListView)findViewById(R.id.listView_settings);
        String[] options = getResources().getStringArray(R.array.settings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        list_settings.setAdapter(adapter);
        list_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Intent i = new Intent(getApplicationContext(), ChangeColor.class);
                        startActivity(i);
                        break;
                    case 1:
                        Intent j = new Intent(getApplicationContext(), ChangeLanguage.class);
                        startActivity(j);
                        break;
                    case 2:
                        Intent k = new Intent(getApplicationContext(), CustomizeMarker.class);
                        startActivity(k);
                        break;
                    case 3:
                        Intent m = new Intent(getApplicationContext(), ChangePassword.class);
                        startActivity(m);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
