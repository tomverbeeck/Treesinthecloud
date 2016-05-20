package com.example.user.treesinthecloud;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.treesinthecloud.Routes.ConfigIDRoute;
import com.example.user.treesinthecloud.Routes.CustomListAdapter;
import com.example.user.treesinthecloud.Routes.MapsActivity_Route;
import com.example.user.treesinthecloud.Routes.Route;
import com.example.user.treesinthecloud.Routes.SetDetailsRouteActivity;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    ListView routeList;
    List<Route> routes = new ArrayList<>();
    //private ProgressDialog loading;
    private Context myContext;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_routes);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        routeList = (ListView)findViewById(R.id.listView_routes);

        if(this == null)
            myContext = this;

        getJSONRoute();

        routeList.setOnItemClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.btn_create_route);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNewRoute = new Intent(getApplicationContext(), SetDetailsRouteActivity.class);
                startActivity(intentNewRoute);

            }
        });
    }

    public void getJSONRoute(){
        class GetJSONRoute extends AsyncTask<Void,Void,String> {
            private Context context = getApplicationContext();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /*loading = new ProgressDialog(myContext);
                loading.setMessage("Get routes...");
                loading.show();*/
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObject;

                try {

                    jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray(ConfigIDRoute.JSON_ARRAY);
                    Route route;

                    for(int i = 0; i<result.length(); i++){
                        route = new Route();
                        JSONObject jo = result.getJSONObject(i);

                        route.setName(jo.getString(ConfigIDRoute.KEY_NAME));
                        route.setShortDescription(jo.getString(ConfigIDRoute.KEY_SHORTDESCRIPTION));
                        Double distance = jo.getDouble(ConfigIDRoute.KEY_LENGTH);;
                        route.setLength(distance);

                        routes.add(route);
                    }
                    //loading.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Check you internet connection and try again!", Toast.LENGTH_LONG).show();
                }

                CustomListAdapter adapter = new CustomListAdapter(context, routes);
                routeList.setAdapter(adapter);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(ConfigIDRoute.URL_GET_ALL);
                return s;
            }
        }
        GetJSONRoute gj = new GetJSONRoute();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MapsActivity_Route.class);
        String routeName = routes.get(position).getName();
        intent.putExtra("route_name", routeName);
        startActivity(intent);
    }
}
