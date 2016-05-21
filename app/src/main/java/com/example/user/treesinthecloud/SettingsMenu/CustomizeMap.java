package com.example.user.treesinthecloud.SettingsMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.treesinthecloud.MapsActivity;
import com.example.user.treesinthecloud.R;

public class CustomizeMap extends AppCompatActivity {

    private ListView list_types;
    public static final String MyPREFERENCES = "MyPrefs" ;

    private String[] data = {"NORMAL","SATELLITE", "HYBRIDE", "TERRAIN"};
    private Integer[] images = {R.drawable.ic_normal_map, R.drawable.ic_satellite_map, R.drawable.ic_hybride_map, R.drawable.ic_terrain_map};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_map);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        list_types = (ListView)findViewById(R.id.listView_customize_map);
        list_types.setAdapter(new MyAdapter(getApplicationContext(), R.layout.layout_listview_with_image, data));
        list_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
                editor.remove("typeMap");
                editor.putString("typeMap", data[position]);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context c, int i, String[] s) {
            super(c, i, s);
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.layout_listview_with_image, null);
            }

            TextView tw = (TextView) v.findViewById(R.id.textview_layout_listview_with_image);
            if (tw != null) tw.setText(data[position]);

            ImageView iv = (ImageView)v.findViewById(R.id.imageview_layout_listview_with_image);
            if (iv != null){
                iv.setImageResource(images[position]);
            }
            return v;
        }
    }
}
