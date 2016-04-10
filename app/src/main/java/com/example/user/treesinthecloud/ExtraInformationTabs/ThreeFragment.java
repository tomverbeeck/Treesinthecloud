package com.example.user.treesinthecloud.ExtraInformationTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.treesinthecloud.R;

public class ThreeFragment extends Fragment {

    private ListView listComments;
    String[] comments = {"Comment one", "Comment two", "Comment three", "Comment four", "Comment five"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_extra_info_fragment_three, container, false);

        listComments = (ListView)view.findViewById(R.id.listview_extra_info_comments);
        listComments.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, comments));

        listComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String)listComments.getItemAtPosition(position);
                Toast.makeText(getActivity(), itemValue, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}
