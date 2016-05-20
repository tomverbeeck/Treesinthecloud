package com.example.user.treesinthecloud.ExtraInformationTabs;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.ConfigIDTree;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;
import java.util.HashMap;
import java.util.List;

public class CommentAdapter extends BaseAdapter {
    private Context activity;
    private LayoutInflater inflater;
    private List<Comment> list;
    private View view;
    private ThreeFragment frag;
    private String nickname;

    public CommentAdapter(Context activity, List<Comment> list, ThreeFragment frag, String nickname) {
        this.activity = activity;
        this.list = list;
        this.frag = frag;
        this.nickname = nickname;

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
        final ViewHolderItem viewHolder;

        if(inflater== null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_comment, null);
            view = convertView;

            viewHolder = new ViewHolderItem();
            viewHolder.deleteB = (Button) convertView.findViewById(R.id.delete_btn);
            viewHolder.listrow = (RelativeLayout) view.findViewById(R.id.comment_listrow);
            viewHolder.text = (TextView) view.findViewById(R.id.commentText);
            viewHolder.timestamp = (TextView)convertView.findViewById(R.id.timestamp);

            convertView.setTag(viewHolder);

        }
        else{
            viewHolder = (ViewHolderItem) convertView.getTag();
        }


        if(viewHolder.text != null && list.get(position) !=null ) {
            viewHolder.text.setText(list.get(position).getUsername()+": "+list.get(position).getComment());
            String date = list.get(position).getTimestamp();
            viewHolder.timestamp.setText(date);

            viewHolder.deleteB.setVisibility(View.INVISIBLE);
    }

        viewHolder.deleteB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(position<list.size()) {
                    Comment c = list.get(position);
                    if(c.getId()>0)
                        deleteMessage(c.getId());
                    list.remove(position);
                    notifyDataSetChanged();
                    frag.UpdateViewOfList();

                }
            }
        });

        viewHolder.listrow.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

                String[] nickn = viewHolder.text.getText().toString().split(":");
                if(nickn[0].equals(nickname)) {
                    viewHolder.deleteB.setVisibility(View.VISIBLE);
                    viewHolder.timerHandler.postDelayed(viewHolder.timerRun, 4000);
                }
                return false;
            }
        }

    );

        viewHolder.timerHandler = new Handler();
        viewHolder.timerRun = new Runnable() {
            @Override
            public void run() {

                viewHolder.deleteB.setVisibility(View.INVISIBLE);
                viewHolder.timerHandler.removeCallbacks(viewHolder.timerRun);
            }
        };

        return convertView;
    }


    private class ViewHolderItem {
        Button deleteB;
        TextView text;
        TextView timestamp;
        RelativeLayout listrow;
        Handler timerHandler;
        Runnable timerRun;

    }

    public void deleteMessage(final int id){
        class DeleteMessage extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap postParam = new HashMap<>();
                postParam.put("idComment", ""+ id );
                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(ConfigIDTree.URL_DELETE_COMMENT_TREE, postParam);
                return s;
            }
        }
        DeleteMessage dm = new DeleteMessage();
        dm.execute();
    }

}


