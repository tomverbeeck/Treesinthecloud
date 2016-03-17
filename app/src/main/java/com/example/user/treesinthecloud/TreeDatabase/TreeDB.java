package com.example.user.treesinthecloud.TreeDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import android.database.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TreeDB{

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

    private DatabaseHelper oDB;

    private SQLiteDatabase db;

    private static final String[] COLUMNS = {KEY_ID,KEY_LATITUDE,KEY_LONGITUDE, KEY_LATINNAME, KEY_NAME, KEY_STATUS, KEY_CUTTINGSHAPE, KEY_GIRTH};

    public TreeDB(Context context) {
        oDB = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void openWrite() throws SQLException{
        db = oDB.getWritableDatabase();
    }

    public void openRead() throws SQLException{
        db = oDB.getReadableDatabase();
    }

    public void close(){
        oDB.close();
    }

    public SQLiteDatabase getDB() {
        return db;
    }

    public void addTree(Tree tree){
        Log.d("addTree", tree.toString());

        openWrite();

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

        close();
    }

    public Tree getTree(int id){
        openRead();

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

        // 4. build tree object
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


        close();

        // 5. return book
        return tree;
    }

    public List<Tree> getAllTrees(){
        List<Tree> trees = new LinkedList<Tree>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TREE;

        // 2. get reference to writable DB
        openRead();
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

        close();

        // return books
        return trees;
    }

    public int updateTree(Tree tree) {

        openWrite();

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

        openWrite();

        // 2. delete
        db.delete(TABLE_TREE, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(tree.getId())}); //selections args

        close();

        //log
        Log.d("deleteBook", tree.toString());

    }
}
