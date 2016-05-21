package com.example.user.treesinthecloud.ExtraInformationTabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.treesinthecloud.Login.Status;
import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.ConfigIDTree;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ThreeFragment extends Fragment {

    private ListView listComments;
    private ArrayList<Comment> comments;
    private CommentAdapter adapter;
    private View view;
    private int treeId;
    private String nickname;
    private ThreeFragment frag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_extra_info_fragment_three, container, false);
        this.view = view;
        frag = this;
        Button add = (Button) view.findViewById(R.id.addComment);
        final EditText comment = (EditText) view.findViewById(R.id.addUserText) ;

        treeId = getActivity().getIntent().getExtras().getInt("treeID");


        comments = new ArrayList<>();

        listComments = (ListView)view.findViewById(R.id.listview_extra_info_comments);
        listComments.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, comments));

        listComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String)listComments.getItemAtPosition(position);
                Toast.makeText(getActivity(), itemValue, Toast.LENGTH_SHORT).show();
            }
        });

        getMessages();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        nickname = sharedPreferences.getString("useremail", null);

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean status = ((Status)getActivity().getApplication()).getLoggedIn();
                if(status) {

                    String cText = comment.getText().toString();
                    if (cText.equals("") || cText.isEmpty()) {

                    } else {
                        Date utilDate = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        Comment newC = new Comment(nickname, cText, sqlDate.toString());
                        comments.add(newC);
                        adapter.notifyDataSetChanged();
                        UpdateViewOfList();
                        comment.setText("");
                        sendMessage(cText, newC);
                    }
                }
                else{

                    Toast.makeText(getContext(), R.string.toast_please_log_in_to_comment, Toast.LENGTH_SHORT).show();
                }


            }

        });


        return view;
    }

    public void getMessages(){
        class GetMessages extends AsyncTask<Void,Void,String> {

            private ProgressDialog loading = new ProgressDialog(getContext());

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDTree.TAG_JSON_ARRAY);

                    for(int i = 0; i<result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String[] time = jo.getString("timestamp").split(" ");
                        Comment comment = new Comment( Integer.parseInt(jo.getString("idComment")), jo.getString("nickname"), jo.getString("comment"), time[0] );
                        comments.add(comment);
                    }

                    this.loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                listComments = (ListView) view.findViewById(R.id.listview_extra_info_comments);
                adapter = new CommentAdapter(getActivity(), comments, frag,nickname);
                listComments.setAdapter(adapter);
                UpdateViewOfList();

                listComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String itemValue = (String)listComments.getItemAtPosition(position);
                        Toast.makeText(getActivity(), itemValue, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap postParam = new HashMap<>();
                postParam.put("idTree", ""+ treeId );
                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(ConfigIDTree.URL_GET_ALL_COMMENTS_TREE, postParam);
            }
        }
        GetMessages gm = new GetMessages();
        gm.execute();
    }

    public void UpdateViewOfList(){

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listComments.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listComments);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listComments.getLayoutParams();
        params.height = totalHeight + (listComments.getDividerHeight() * (adapter.getCount() - 1));
        listComments.setLayoutParams(params);
        listComments.requestLayout();

    }


    public void sendMessage(final String text, final Comment comment){
        class SendMessage extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(s);
                    String idComment = jsonObject.getString("commentid");
                    int id = Integer.parseInt(idComment);
                    if(id>0)
                        comment.setId(id);

                } catch (JSONException |NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap postParam = new HashMap<>();
                postParam.put("idTree", ""+ treeId );
                postParam.put("username", nickname);
                postParam.put("comment", text);
                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest(ConfigIDTree.URL_COMMENT_TREE, postParam);
            }
        }
        SendMessage sm = new SendMessage();
        sm.execute();
    }
}
