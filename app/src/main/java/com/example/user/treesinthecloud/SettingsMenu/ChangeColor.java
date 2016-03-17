package com.example.user.treesinthecloud.SettingsMenu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.treesinthecloud.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeColor extends Activity{

    private ListView list_colors;
    private Context context = this;
    private CircleImageView sidebar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_color);

        list_colors = (ListView)findViewById(R.id.listView_colors);
        String[] colors = new String[]{"Blue", "Green" ,"Yellow", "Black"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colors);
        list_colors.setAdapter(adapter);
        list_colors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) list_colors.getItemAtPosition(position);
                String toast = "Color changed into " + itemValue;
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void changeColor(View view){
        String toast = "Sidebars color changed to white";
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
        finish();
    }
}
