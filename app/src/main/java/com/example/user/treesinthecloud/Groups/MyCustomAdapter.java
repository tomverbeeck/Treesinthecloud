package com.example.user.treesinthecloud.Groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.treesinthecloud.R;

import java.util.List;

public class MyCustomAdapter extends BaseAdapter {
    private Context activity;
    private LayoutInflater inflater;
    private List<String> list;
    private View view;
    private MakegroupActivity makegroup;

    public MyCustomAdapter(Context activity, List<String> list, MakegroupActivity makegroup) {
        this.activity = activity;
        this.list = list;
        this.makegroup = makegroup;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int location) {
        return list.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(inflater== null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_list_make_group_row, null);
            view = convertView;
        }

        TextView listItemText = (TextView)convertView.findViewById(R.id.list_item_string);
        if(listItemText != null && list.get(position) !=null ) {
            listItemText.setText(list.get(position));
        }

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do somethin
                if(position<list.size() && list.size()>1) {
                    list.remove(position); //or some other task
                    notifyDataSetChanged();
                    makegroup.UpdateViewOfList(view);
                }
            }
        });

        return convertView;
    }

}

