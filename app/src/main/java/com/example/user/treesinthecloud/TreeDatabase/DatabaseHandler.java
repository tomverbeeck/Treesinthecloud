package com.example.user.treesinthecloud.TreeDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "treesinthecloud";

    // trees table name
    private static final String TABLE_NAME = "trees";

    // trees Table Columns names
    public static final String KEY_ID = "idTrees";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_SPECIE = "specie";
    public static final String KEY_STATUS = "status";
    public static final String KEY_COMMON_NAME = "common_name";
    public static final String KEY_ORIGINAL_GIRTH = "original_girth";
    public static final String KEY_CURRENT_GIRTH = "current_girth";
    public static final String KEY_CUTTING_SHAPE = "cutting_shape";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "( " +
                KEY_ID + " INT," +
                KEY_LONGITUDE + " DOUBLE," +
                KEY_LATITUDE + " DOUBLE, " +
                KEY_SPECIE + " VARCHAR(255)," +
                KEY_STATUS + " VARCHAR(255)," +
                KEY_COMMON_NAME + " VARCHAR(255)," +
                KEY_ORIGINAL_GIRTH + " INT," +
                KEY_CURRENT_GIRTH + " INT," +
                KEY_CUTTING_SHAPE + " VARCHAR(255)" +
                ");";
        db.execSQL(sql);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);

    }

    public List<Tree> getAllTrees() {
        List<Tree> treeList = new ArrayList<Tree>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Tree tree = new Tree();
                tree.setIdTree(Integer.parseInt(cursor.getString(0)));
                tree.setLongitude(Double.parseDouble(cursor.getString(1)));
                tree.setLatitude(Double.parseDouble(cursor.getString(2)));
                tree.setSpecie(cursor.getString(3));
                tree.setStatus(cursor.getString(4));
                tree.setName(cursor.getString(5));
                tree.setOriginalGirth(Integer.parseInt(cursor.getString(6)));
                tree.setCurrentGirth(Integer.parseInt(cursor.getString(7)));
                tree.setCuttingShape(cursor.getString(8));
                // Adding contact to list
                treeList.add(tree);
            } while (cursor.moveToNext());
        }

        // return contact list
        return treeList;
    }

    public int getTreesCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public Tree getTree(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_LONGITUDE, KEY_LATITUDE, KEY_SPECIE, KEY_STATUS, KEY_COMMON_NAME,
                        KEY_ORIGINAL_GIRTH,
                        KEY_CURRENT_GIRTH, KEY_CUTTING_SHAPE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Tree tree = new Tree(Integer.parseInt(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5), Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), cursor.getString(8));
        // return contact
        return tree;
    }

    public void addTree(Tree tree) {
        SQLiteDatabase db = this.getWritableDatabase();

        tree.setSpecie(tree.getSpecie().replace("'", " "));
        tree.setName(tree.getName().replace("'", " "));

        String sql =
                "INSERT INTO " + TABLE_NAME + " VALUES(" + tree.getIdTree() + "," + tree.getLongitude() + "," + tree.getLatitude()
                                         + "," + "'" + tree.getSpecie() + "'"  + "," + "'" + tree.getStatus() + "'"  + "," + "'" +
                                            tree.getName() + "'" + "," + tree.getOriginalGirth() + "," +
                                            tree.getCurrentGirth()  + "," + "'" + tree.getCuttingShape() + "'" + ");";
        db.execSQL(sql);
        db.close();
    }

    public int getLastID(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        int id = cursor.getInt(0);
        db.close();
        if(id != 0)
            return id;
        else
            return -1;
    }

}
