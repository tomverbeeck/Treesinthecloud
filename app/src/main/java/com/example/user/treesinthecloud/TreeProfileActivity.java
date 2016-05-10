package com.example.user.treesinthecloud;

/**
 * Created by Gebruiker on 17/03/2016.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by Gebruiker on 16/03/2016.
 */
public class TreeProfileActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView longitude = (TextView) findViewById(R.id.longValue);
        TextView latitude = (TextView) findViewById(R.id.latValue);
        TextView specie = (TextView) findViewById(R.id.specieValue);
        TextView status = (TextView) findViewById(R.id.statusValue);

        longitude.setText("0,00");
        latitude.setText("0,00");
        specie.setText("normal tree");
        status.setText("New tree");

        setContentView(R.layout.treeprofile);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(width,height);
/**

        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(
                inflater.inflate(R.layout.treeprofile, null, false),100, 100, true);

        // The code below assumes that the root container has an id called 'main'

        pw.showAtLocation(findViewById(R.id.scrollView), Gravity.CENTER, 0, 0);
**/
    }

}
