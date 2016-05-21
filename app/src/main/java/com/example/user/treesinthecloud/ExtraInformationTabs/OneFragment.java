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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.Login.Status;
import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.ConfigIDTree;
import com.example.user.treesinthecloud.TreeDatabase.DatabaseHandler;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;
import com.example.user.treesinthecloud.TreeDatabase.Tree;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class OneFragment extends Fragment{

    private TextView specie, status, commonName, originalGirth, currentGirth, cuttingShape, likes;
    private ImageView likeTreeImage;
    private boolean likedByUser;
    private boolean prevlikedByUser;
    private Tree tree = new Tree();
    ProgressDialog loading;
    private RequestHandler rh;
    private String nickname;
    private DatabaseHandler db;

    public OneFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(getContext());
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
        likes = (TextView) view.findViewById(R.id.number_of_likes);
        likeTreeImage = (ImageView) view.findViewById(R.id.image_like_tree);

        Context context = getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE);
        //SharedPreferences.Editor spEditor = sharedPreferences.edit();
        nickname = sharedPreferences.getString("useremail", null);

        rh = new RequestHandler();rh = new RequestHandler();
        tree.setIdTree(getActivity().getIntent().getExtras().getInt("treeID"));

        hasLikedTree();
        getLikes();

        int id = getActivity().getIntent().getExtras().getInt("treeID");

        tree = db.getTree(id);

        specie.setText(tree.getSpecie());
        status.setText(tree.getStatus());
        commonName.setText(tree.getName());
        originalGirth.setText("" + tree.getOriginalGirth());
        currentGirth.setText("" + tree.getCurrentGirth());
        cuttingShape.setText(tree.getCuttingShape());

        likeTreeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int likesInt = 0;
                boolean status = ((Status)getActivity().getApplication()).getLoggedIn();

                if (likes.getText().toString() != null || likes.getText().toString()!= "")
                    likesInt = Integer.parseInt(likes.getText().toString());

                if(status){

                    if (likedByUser) {
                        likedByUser = false;
                        likeTreeImage.setBackgroundResource(R.drawable.like_tree_empty);
                        likesInt--;
                        likes.setText("" + likesInt);
                    } else {
                        likedByUser = true;
                        likeTreeImage.setBackgroundResource(R.drawable.like_tree_full);
                        likesInt++;
                        likes.setText("" + likesInt);
                    }
                }
                else {
                    Toast.makeText(getContext(), R.string.toast_login_to_like_tree , Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void hasLikedTree() {
        class HasLikedTree extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(s);
                    int liked = jsonObject.getInt("liked");

                    if(liked==1) {
                        likedByUser = true;
                        prevlikedByUser = true;
                        likeTreeImage.setBackgroundResource(R.drawable.like_tree_full);
                    }

                    else {
                        likedByUser = false;
                        prevlikedByUser = false;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {

                HashMap postParam = new HashMap<>();
                postParam.put("idTree", ""+tree.getIdTree());
                postParam.put("nickname", nickname );

                String s = rh.sendPostRequest(ConfigIDTree.URL_HASLIKED_TREE, postParam);
                return s;
            }

        }

        HasLikedTree hlt = new HasLikedTree();
        hlt.execute();
    }


    public void likeTree(final boolean like, final HashMap postParam) {
        class LikeTree extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... params) {
                if(like) {
                    rh.sendPostRequest(ConfigIDTree.URL_lIKE_TREE, postParam);
                }
                else
                    rh.sendPostRequest(ConfigIDTree.URL_DISlIKE_TREE, postParam);

                return null;
            }

        }

        LikeTree lt = new LikeTree();
        lt.execute();
    }

    @Override
    public void onPause() {
        super.onPause();

        if(nickname!=null && prevlikedByUser != likedByUser) {
            HashMap postParam = new HashMap<>();
            postParam.put("idTree", ""+tree.getIdTree());
            postParam.put("username", nickname);

            if (likedByUser) {
                Toast.makeText(getContext(),"like" , Toast.LENGTH_SHORT).show();//TODO delete this
                likeTree(true, postParam);
            } else {
                Toast.makeText(getContext(),"dislike" , Toast.LENGTH_SHORT).show(); //TODO delete this
                likeTree(false, postParam);
            }
        }
    }

    public void getLikes(){
        class GetLikes extends AsyncTask<Void,Void,String> {

            private ProgressDialog loading = new ProgressDialog(getContext());

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSONObject jsonObject = null;

                try {

                    jsonObject = new JSONObject(s);
                    String numbLikes = jsonObject.getString("likes");

                    if(numbLikes==null || numbLikes.equals(""))
                        likes.setText("0");

                    else
                        likes.setText(numbLikes);


                    this.loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap postParam = new HashMap<>();
                postParam.put("idTree", "" + tree.getIdTree() );
                return rh.sendPostRequest(ConfigIDTree.URL_GET_LIKES, postParam);
            }
        }
        GetLikes gl = new GetLikes();
        gl.execute();
    }
}
