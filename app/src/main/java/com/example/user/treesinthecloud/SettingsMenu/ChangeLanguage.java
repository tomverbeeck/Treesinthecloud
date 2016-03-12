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

/**
 * Created by User on 11/03/2016.
 */
public class ChangeLanguage extends Activity {

    private ListView list_languages;
    private Context context= this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_change_language);

        list_languages = (ListView)findViewById(R.id.listView_change_language);
        String[] languages = new String[]{"English", "Nederlands", "Spaans"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        list_languages.setAdapter(adapter);
        list_languages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) list_languages.getItemAtPosition(position);
                String toast = "Language changed into " + itemValue;
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
