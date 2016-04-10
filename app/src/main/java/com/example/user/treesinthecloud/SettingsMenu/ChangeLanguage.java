package com.example.user.treesinthecloud.SettingsMenu;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.treesinthecloud.MapsActivity;
import com.example.user.treesinthecloud.R;

import java.util.Locale;

/**
 * Created by User on 11/03/2016.
 */
public class ChangeLanguage extends Activity {

    private ListView list_languages;

    private String language;
    private String languageCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings_change_language);

        //Only Dutch and English work at the moment

        list_languages = (ListView)findViewById(R.id.listView_change_language);
        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        list_languages.setAdapter(adapter);
        list_languages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                language = (String) list_languages.getItemAtPosition(position);
                String toast = "Language changed into " + language;

                switch(position){
                    case 0:
                        languageCode="en";
                        changeLanguage();
                        break;
                    case 1:
                        languageCode= "nl";
                        changeLanguage();
                        break;

                    default:
                        languageCode="en";
                        changeLanguage();
                        break;
                }

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);

                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
    }

    private void changeLanguage(){
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}
