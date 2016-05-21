package com.example.user.treesinthecloud.Routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.treesinthecloud.R;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Context activity;
    private LayoutInflater inflater;
    private List<Route> routeItems;
    TextView title, shortDescription, length;
    private View view;

    public CustomListAdapter(Context activity, List<Route> routeItems) {
        this.activity = activity;
        this.routeItems = routeItems;
    }

    @Override
    public int getCount() {
        return routeItems.size();
    }

    @Override
    public Object getItem(int location) {
        return routeItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater== null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_listview_routes, null);
            view = convertView;
        }

        title = (TextView) convertView.findViewById(R.id.textview_routes_listview_title);
        shortDescription = (TextView) convertView.findViewById(R.id.textview_routes_listview_short_description);
        length = (TextView) convertView.findViewById(R.id.textview_routes_listview_length);

        title.setText(routeItems.get(position).getName());
        shortDescription.setText(routeItems.get(position).getShortDescription());
        String lengthS = "" + routeItems.get(position).getLength();
        length.setText(lengthS + " km");

        return convertView;
    }



}
