package com.example.user.treesinthecloud.TreeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class TreeDB extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TreeDB";

    // Trees table name
    private static final String TABLE_TREE = "trees";

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

    public TreeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public void addTree(Tree tree){
        Log.d("addTree", tree.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, tree.getId());
        values.put(KEY_LATITUDE, tree.getLatitude());
        values.put(KEY_LONGITUDE, tree.getLongitude());
        values.put(KEY_LATINNAME, tree.getLatinName());
        values.put(KEY_NAME, tree.getName());
        values.put(KEY_STATUS, tree.getStatus());
        values.put(KEY_CUTTINGSHAPE, tree.getCuttingShape());
        values.put(KEY_GIRTH, tree.getGirth());

        // 3. insert
        db.insert(TABLE_TREE, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Tree getTree(int id){
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TREE, // a. table
                        COLUMNS, // b. column names
                        "id=?", // c. selections
                        new String[] { String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Tree tree = new Tree();
        tree.setId(Integer.parseInt(cursor.getString(0)));
        tree.setLatitude(Double.parseDouble(cursor.getString(1)));
        tree.setLongitude(Double.parseDouble(cursor.getString(2)));
        tree.setLatinName(cursor.getString(3));
        tree.setName(cursor.getString(4));
        tree.setStatus(cursor.getString(5));
        tree.setCuttingShape(cursor.getString(6));
        tree.setGirth(Integer.parseInt(cursor.getString(7)));

        //log
        Log.d("getTree(" + id + ")", tree.toString());

        // 5. return book
        return tree;
    }

    public List<Tree> getAllTrees(){
        List<Tree> trees = new LinkedList<Tree>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TREE;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Tree tree = null;
        if (cursor.moveToFirst()) {
            do {
                tree = new Tree();
                tree.setId(cursor.getInt(0));
                tree.setLatitude(cursor.getDouble(1));
                tree.setLongitude(cursor.getDouble(2));
                tree.setLatinName(cursor.getString(3));
                tree.setName(cursor.getString(4));
                tree.setStatus(cursor.getString(5));
                tree.setCuttingShape(cursor.getString(6));
                tree.setGirth(cursor.getInt(7));

                // Add book to books
                trees.add(tree);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", trees.toString());

        // return books
        return trees;
    }

    public int updateTree(Tree tree) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, tree.getId());
        values.put(KEY_LATITUDE, tree.getLatitude());
        values.put(KEY_LONGITUDE, tree.getLongitude());
        values.put(KEY_LATINNAME, tree.getLatinName());
        values.put(KEY_NAME, tree.getName());
        values.put(KEY_STATUS, tree.getStatus());
        values.put(KEY_CUTTINGSHAPE, tree.getCuttingShape());
        values.put(KEY_GIRTH, tree.getGirth());

        // 3. updating row
        int i = db.update(TABLE_TREE, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(tree.getId()) }); //selection args

        // 4. close
        db.close();

        return i;
    }

    public void deleteTree(Tree tree) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_TREE, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(tree.getId())}); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteBook", tree.toString());

    }
}
