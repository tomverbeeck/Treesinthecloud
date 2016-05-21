package com.example.user.treesinthecloud.AddTree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.treesinthecloud.MapsActivity;
import com.example.user.treesinthecloud.R;
import com.example.user.treesinthecloud.TreeDatabase.DatabaseHandler;
import com.example.user.treesinthecloud.TreeDatabase.RequestHandler;
import com.example.user.treesinthecloud.TreeDatabase.Tree;

import java.util.HashMap;
import java.util.Random;

public class NewtreeActivity extends AppCompatActivity {

    double longitudeDouble;
    double latitudeDouble;
    String longitudeString;
    String latitudeString;
    PopupWindow popupMessage;
    private Tree tree;
    private String specieS;
    private String nameS;
    private int ID;
    private DatabaseHandler db;
    private EditText explanation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_tree__new_tree);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tree = new Tree();
        db = new DatabaseHandler(getApplicationContext());

        final EditText longitude = (EditText) findViewById(R.id.longtitudeNewTree);
        final EditText latitude = (EditText) findViewById(R.id.latitudeNewTree);
        final EditText specie = (EditText) findViewById(R.id.groupNewTree);
        final EditText name = (EditText) findViewById(R.id.TypeNewTree);
        explanation = (EditText) findViewById(R.id.reasonForNewTree);
        final TextView explanationError = (TextView) findViewById(R.id.explanationError_newtree);
        final TextView locationError = (TextView) findViewById(R.id.locationError_newtree);



        Button getMap = (Button) findViewById(R.id.button_add_tree_choose_location);

        Button submit = (Button) findViewById(R.id.submitNewTreeB);

        assert submit != null;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for decent explanation

                String expl = explanation.getText().toString();
                if (expl.trim().length() < 20) {
                    assert explanationError != null;
                    explanationError.setVisibility(view.VISIBLE);
                    return;
                }

                assert explanationError != null;
                explanationError.setVisibility(view.INVISIBLE);

                assert longitude != null;
                String longS = longitude.getText().toString();
                assert latitude != null;
                String latS = latitude.getText().toString();

                if (longS.isEmpty() || latS.isEmpty()) {
                    assert locationError != null;
                    locationError.setVisibility(view.VISIBLE);
                    return;
                }

                try {
                    Double longD = Double.parseDouble(longS);
                    Double latD = Double.parseDouble(latS);
                } catch (NumberFormatException e) {
                    assert locationError != null;
                    locationError.setVisibility(view.VISIBLE);
                    return;
                }

                assert locationError != null;
                locationError.setVisibility(view.INVISIBLE);

                assert specie != null;
                if(specie.getText().toString().equals("")){
                    specieS = "Onbekend";
                }else{
                    specieS = specie.getText().toString();
                }

                assert name != null;
                if(name.getText().toString().equals("")){
                    nameS = "Onbekend";
                }else{
                    nameS = name.getText().toString();
                }

                ID = db.getLastID();
                ID++;
                if(ID == -1){
                    Random ran = new Random();
                    ID = ran.nextInt(1000000)+1;
                }

                popupInit();

                popupMessage.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });

    }



    public void getLocationOnMap(View view){
        Intent openMap = new Intent(this, ChooseLocationActivity.class);
        startActivityForResult(openMap,1);
    }


    public void popupInit() {
        Context context = getBaseContext();
        RelativeLayout viewG = (RelativeLayout) findViewById(R.id.layout_popup_newtree);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.layout_popup_newtree, viewG);
        popupMessage = new PopupWindow(findViewById(R.id.layout_popup_newtree),ViewGroup.LayoutParams.WRAP_CONTENT ,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupMessage.setContentView(layout);
        popupMessage.setFocusable(true);

    }

    public void closePopup(View view){
        popupMessage.dismiss();
    }

    public void continuePopup(View view){
        popupMessage.dismiss();
        tree.setLongitude(longitudeDouble);
        tree.setLatitude(latitudeDouble);
        tree.setSpecie(specieS);
        tree.setName(nameS);
        tree.setStatus("Aangevraagd");
        tree.setCuttingShape("Onbekend");
        tree.setCurrentGirth(0);
        tree.setOriginalGirth(0);
        tree.setIdTree(ID);
        if(tree != null) {
            db.addTree(tree);
        }else{
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(), R.string.toast_save_new_tree, Toast.LENGTH_SHORT).show();

        String descr = explanation.getText().toString();

        requestTree(tree, descr);

        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("useremail", null);

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                longitudeDouble = Double.parseDouble(data.getStringExtra("longitude"));
                latitudeDouble = Double.parseDouble(data.getStringExtra("latitude"));

                final EditText longtitude = (EditText) findViewById(R.id.longtitudeNewTree);
                final EditText latitude = (EditText) findViewById(R.id.latitudeNewTree);
                assert longtitude != null;
                longtitude.setText("" + longitudeDouble);
                assert latitude != null;
                latitude.setText("" + latitudeDouble);
            }
        }

    }

    public void requestTree(final Tree tree, final String explanation){
        class RequestTree extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                HashMap data = new HashMap();
                SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                String email = sharedPreferences.getString("useremail", null);

                data.put("description", explanation);
                data.put("lat", "" + tree.getLatitude());
                data.put("long","" + tree.getLongitude());
                data.put("com_name", tree.getName());
                data.put("email", email);

                RequestHandler rh = new RequestHandler();
                return rh.sendPostRequest("http://projectmovie.16mb.com/AddTree.php", data);
            }
        }
        RequestTree rq = new RequestTree();
        rq.execute();
    }
}
