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

public class ChangeColor extends Activity{

    private ListView list_colors;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_change_color);

        list_colors = (ListView)findViewById(R.id.listView_colors);
        String[] colors = getResources().getStringArray(R.array.colors);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colors);
        list_colors.setAdapter(adapter);
        list_colors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) list_colors.getItemAtPosition(position);
                String toast = "Color changed into " + itemValue;

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                //intent.putExtra(/*here should be a string to pass*/);
                startActivity(intent);
            }
        });
    }

    public void changeColor(){

    }
}
