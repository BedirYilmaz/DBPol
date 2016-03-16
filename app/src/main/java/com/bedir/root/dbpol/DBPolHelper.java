package com.bedir.root.dbpol;

/**
 * Created by 3yanlis1bos on 3/15/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class DBPolHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PolDB";
    private static final String KEY_PLATE = "plate";
    private static final String KEY_RECORD = "record";
    private static final String TABLE_PLATES = "PLATES";
    private static final String KEY_ID = "id";

    private static final String[] COLUMNS = {KEY_ID,KEY_PLATE,KEY_RECORD};

    public DBPolHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create Plate table
        String CREATE_PLATE_TABLE = "CREATE TABLE Plates ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "plate TEXT, "+
                "record TEXT )";

        // create Plates table
        db.execSQL(CREATE_PLATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older Plates table if existed
        db.execSQL("DROP TABLE IF EXISTS PLATES");

        // create fresh Plates table
        this.onCreate(db);
    }

    public void addPlate(Plate Plate){
        //for logging
        Log.d("addPlate", Plate.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_PLATE, Plate.getPlate()); // get title
        values.put(KEY_RECORD, Plate.getRecord()); // get author

        // 3. insert
        db.insert(TABLE_PLATES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Plate getPlate(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PLATES, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Plate plate = new Plate();
        plate.setId(Integer.parseInt(cursor.getString(0)));
        plate.setPlate(cursor.getString(1));
        plate.setRecord(cursor.getString(2));

        //log
        Log.d("getPlate(" + id + ")", plate.toString());

        // 5. return book
        return plate;
    }

    public List<Plate> getAllPlates() {
        List<Plate> plates = new LinkedList<Plate>();

        String query = "SELECT  * FROM " + TABLE_PLATES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Plate plate = null;
        if (cursor.moveToFirst()) {
            do {
                plate = new Plate();
                plate.setId(Integer.parseInt(cursor.getString(0)));
                plate.setPlate(cursor.getString(1));
                plate.setRecord(cursor.getString(2));

                plates.add(plate);
            } while (cursor.moveToNext());
        }

        Log.d("getAllPlates()", plates.toString());

        return plates;
    }

    public int updatePlate(Plate plate) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", plate.getPlate()); // get title
        values.put("author", plate.getRecord()); // get author

        // 3. updating row
        int i = db.update(TABLE_PLATES, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(plate.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public void deletePlate(Plate plate) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_PLATES, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(plate.getId())}); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteBook", plate.toString());

    }
}