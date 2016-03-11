package com.example.user.treesinthecloud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by User on 11/03/2016.
 */
public class SettingsFragment extends Fragment {

    private ListView list_settings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.layout_settings, container, false);

        //list_settings = (ListView) findViewById(R.id.listView_settings);

        return v;
    }

}
