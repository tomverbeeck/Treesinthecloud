package com.example.user.treesinthecloud.TreeDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 17/03/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Trees Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATINNAME = "latinName";
    private static final String KEY_NAME = "name";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CUTTINGSHAPE = "cuttingShape";
    private static final String KEY_GIRTH = "girth";

    private static final String[] COLUMNS = {KEY_ID,KEY_LATITUDE,KEY_LONGITUDE, KEY_LATINNAME, KEY_NAME, KEY_STATUS, KEY_CUTTINGSHAPE, KEY_GIRTH};

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create tree table
        String CREATE_TREE_TABLE = "CREATE TABLE trees ( " +
                "id INTEGER PRIMARY KEY, " +
                "latitude DOUBLE(12), "+
                "longitude DOUBLE(12), " +
                "latinName TEXT, " +
                "name TEXT, " +
                "status TEXT, " +
                "cuttingShape TEXT, " +
                "girth INT )";

        // create tree table
        db.execSQL(CREATE_TREE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tree table if existed
        db.execSQL("DROP TABLE IF EXISTS TreeDB");

        // create fresh tree table
        this.onCreate(db);
    }
}
