package com.example.user.treesinthecloud.ExtraInformationTabs;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.treesinthecloud.R;

import java.util.List;
import java.util.Locale;

public class TwoFragment extends Fragment {

    private TextView addressLabel;
    private double longitude;
    private double latitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_extra_info_fragment_two, container, false);

        longitude = getActivity().getIntent().getExtras().getDouble("treeLong");
        latitude = getActivity().getIntent().getExtras().getDouble("treeLat");
        addressLabel = (TextView) view.findViewById(R.id.TextView_extra_info_street);
        Location locationTree = new Location("");
        locationTree.setLatitude(latitude);
        locationTree.setLongitude(longitude);

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
        addressLabel.setText(strAdd);
        return view;
    }
}