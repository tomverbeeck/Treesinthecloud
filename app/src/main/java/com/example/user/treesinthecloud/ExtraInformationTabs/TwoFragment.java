package com.example.user.treesinthecloud.ExtraInformationTabs;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.DatabaseHandler;
import com.example.user.treesinthecloud.TreeDatabase.Tree;

import java.util.List;
import java.util.Locale;

public class TwoFragment extends Fragment {

    private TextView addressLabel, titelDescr, descr;
    private double longitude;
    private double latitude;
    private String description, specie;
    private Button search;
    private Tree tree = new Tree();
    private DatabaseHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_extra_info_fragment_two, container, false);

        int id = getActivity().getIntent().getExtras().getInt("treeID");

        tree = db.getTree(id);

        longitude = tree.getLongitude();
        latitude = tree.getLatitude();
        specie = tree.getSpecie();
        description = getActivity().getIntent().getExtras().getString("treeDescr");


        search = (Button)view.findViewById(R.id.button3) ;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(view.getContext(), GoogleSearch.class);
                if(specie != null) {
                    browserIntent.putExtra("Specie", specie);
                    startActivity(browserIntent);
                }
            }
        });

        addressLabel = (TextView) view.findViewById(R.id.TextView_extra_info_street);
        titelDescr = (TextView) view.findViewById(R.id.TextView_extra_info_titel_descr_tree);
        descr = (TextView) view.findViewById(R.id.TextView_extra_info_tree_descr) ;

        if(description == null){
            titelDescr.setVisibility(View.INVISIBLE);
            descr.setVisibility(View.INVISIBLE);
            descr.setText("");
        }else{
            titelDescr.setVisibility(View.VISIBLE);
            descr.setVisibility(View.VISIBLE);
            descr.setText(description);
        }

        Location locationTree = new Location("");
        locationTree.setLatitude(latitude);
        locationTree.setLongitude(longitude);

        if(tree.getStatus().equals("Virtueel")){
            search.setVisibility(View.INVISIBLE);
        }else {
            search.setVisibility(View.VISIBLE);
        }

        String strAdd = "";
        Geocoder geocoder = new Geocoder(view.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                addressLabel.setText(strReturnedAddress.toString());
            } else {
                addressLabel.setText("No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            addressLabel.setText(getResources().getText(R.string.text_unable_to_load_address));
        }
        if(strAdd.equals("")){
            addressLabel.setText("Check your internet connection!");
        }else {
            addressLabel.setText(strAdd);
        }
        return view;
    }
}