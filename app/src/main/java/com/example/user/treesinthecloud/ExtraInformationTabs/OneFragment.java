package com.example.user.treesinthecloud.ExtraInformationTabs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.ConfigIDTree;
import com.example.user.treesinthecloud.TreeDatabase.Tree;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        Bundle bundle = getArguments();
        String id = bundle.getString("idTREE");
        getData(id);
        return view;
    }

    private void getData(String id){
        loading = new ProgressDialog(getActivity());
        loading.setMessage(getResources().getString(R.string.text_fetching_tree));;
        loading.show();

        String url = ConfigIDTree.DATA_URL + id.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDTree.JSON_ARRAY);
                    JSONObject data = result.getJSONObject(0);
                    tree.setLongitude(data.getDouble(ConfigIDTree.KEY_LONGITUDE));
                    tree.setLatitude(data.getDouble(ConfigIDTree.KEY_LATITUDE));
                    tree.setSpecie(data.getString(ConfigIDTree.KEY_SPECIE));
                    tree.setStatus(data.getString(ConfigIDTree.KEY_STATUS));
                    tree.setName(data.getString(ConfigIDTree.KEY_COMMON_NAME));
                    tree.setOriginalGirth(data.getInt(ConfigIDTree.KEY_ORIGINAL_GIRTH));
                    tree.setCurrentGirth(data.getInt(ConfigIDTree.KEY_CURRENT_GIRTH));
                    tree.setCuttingShape(data.getString(ConfigIDTree.KEY_CUTTING_SHAPE));

                    specie.setText(tree.getSpecie());
                    status.setText(tree.getStatus());
                    commonName.setText(tree.getName());
                    originalGirth.setText("" + tree.getOriginalGirth());
                    currentGirth.setText("" + tree.getCurrentGirth());
                    cuttingShape.setText(tree.getCuttingShape());

                    loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
