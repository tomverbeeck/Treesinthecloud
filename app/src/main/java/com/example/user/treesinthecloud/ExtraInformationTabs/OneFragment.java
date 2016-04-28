package com.example.user.treesinthecloud.ExtraInformationTabs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.Tree;

public class OneFragment extends Fragment{

    private TextView specie, status, commonName, originalGirth, currentGirth, cuttingShape;
    private Tree tree = new Tree();
    ProgressDialog loading;

    public OneFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_extra_info_fragment_one, container, false);
        specie = (TextView)view.findViewById(R.id.TextView_extra_info_specie);
        commonName = (TextView)view.findViewById(R.id.TextView_extra_info_name);
        status = (TextView)view.findViewById(R.id.TextView_extra_info_status);
        originalGirth = (TextView)view.findViewById(R.id.TextView_extra_info_original_girth);
        currentGirth = (TextView)view.findViewById(R.id.TextView_extra_info_Current_girth);
        cuttingShape = (TextView)view.findViewById(R.id.TextView_extra_info_cutting_shape);

        tree.setLongitude(getActivity().getIntent().getExtras().getDouble("treeLong"));
        tree.setLatitude(getActivity().getIntent().getExtras().getDouble("treeLat"));
        tree.setSpecie(getActivity().getIntent().getExtras().getString("treeSpecie"));
        tree.setStatus(getActivity().getIntent().getExtras().getString("treeStatus"));
        tree.setName(getActivity().getIntent().getExtras().getString("treeName"));
        tree.setOriginalGirth(getActivity().getIntent().getExtras().getInt("treeOrgGirth"));
        tree.setCurrentGirth(getActivity().getIntent().getExtras().getInt("treeCurGirth"));
        tree.setCuttingShape(getActivity().getIntent().getExtras().getString("treeCutShape"));

        specie.setText("" + tree.getSpecie());
        status.setText(tree.getStatus());
        commonName.setText(tree.getName());
        originalGirth.setText("" + tree.getOriginalGirth());
        currentGirth.setText("" + tree.getCurrentGirth());
        cuttingShape.setText(tree.getCuttingShape());

        return view;
    }
}
