package com.example.user.treesinthecloud.TreeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 23/04/2016.
 */
public class LocationsDB extends SQLiteOpenHelper{
    /** Database name */
    private static String DBNAME = "locationmarkersqlite";

    /** Version number of the database */
    private static int VERSION = 1;

    public static final String FIELD_ROW_ID = "idTrees";

    public static final String FIELD_LAT = "latitude";

    public static final String FIELD_LNG = "longitude";

    public static final String FIELD_SPECIE = "specie";

    public static final String FIELD_STATUS = "zom";

    public static final String FIELD_COMMON_NAME = "common name";

    public static final String FIELD_ORG_GIRTH = "original girth";

    public static final String FIELD_CUR_GIRTH = "current girth";

    public static final String FIELD_CUTTING_SHAPE = "cutting shape";

    /** A constant, stores the the table name */
    private static final String DATABASE_TABLE = "locations";

    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;

    /** Constructor */
    public LocationsDB(Context context) {
        super(context, DBNAME, null, VERSION);
        this.mDB = getWritableDatabase();
    }

    /** This is a callback method, invoked when the method getReadableDatabase() / getWritableDatabase() is called
     * provided the database does not exists
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =     "create table " + DATABASE_TABLE + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                FIELD_LNG + " double , " +
                FIELD_LAT + " double , " +
                FIELD_SPECIE + " varchar " +
                FIELD_STATUS + " varchar , " +
                FIELD_COMMON_NAME + " varchar, " +
                FIELD_ORG_GIRTH + " int, " +
                FIELD_CUR_GIRTH + " int, " +
                FIELD_CUTTING_SHAPE + " varchar, " +
                " ) ";

        db.execSQL(sql);
    }

    /** Inserts a new location to the table locations */
    public long insert(ContentValues contentValues){
        long rowID = mDB.insert(DATABASE_TABLE, null, contentValues);
        return rowID;
    }

    /** Deletes all locations from the table */
    public int del(){
        int cnt = mDB.delete(DATABASE_TABLE, null , null);
        return cnt;
    }

    /** Returns all the locations from the table */
    public Cursor getAllLocations(){
        return mDB.query(DATABASE_TABLE, new String[] {
                FIELD_ROW_ID,
                FIELD_LAT,
                FIELD_LNG,
                FIELD_SPECIE,
                FIELD_STATUS,
                FIELD_COMMON_NAME,
                FIELD_ORG_GIRTH,
                FIELD_CUR_GIRTH,
                FIELD_CUTTING_SHAPE
        } , null, null, null, null, null, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
