package com.example.asus.brighcycle4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class HereMapsDataBaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_1 = "maps_infos_thomas_charles";
    public static final String TABLE_NAME_2 = "maps_infos_thomas_charles";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_HOSPITAL = "hospital";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGITUDE = "longitude";
    private static final String SQL_CREATE_ENTRIES_1 = "CREATE TABLE " + TABLE_NAME_1 + "(" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME_HOSPITAL + " TEXT, " + COLUMN_NAME_LATITUDE + " TEXT, " + COLUMN_NAME_LONGITUDE + " TEXT" + " )";
    private static final String SQL_DELETE_ENTRIES_1 = "DROP TABLE IF EXISTS " + TABLE_NAME_1;
    private static final String SQL_CREATE_ENTRIES_2 = "CREATE TABLE " + TABLE_NAME_1 + "(" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME_HOSPITAL + " TEXT, " + COLUMN_NAME_LATITUDE + " TEXT, " + COLUMN_NAME_LONGITUDE + " TEXT" + " )";
    private static final String SQL_DELETE_ENTRIES_2 = "DROP TABLE IF EXISTS " + TABLE_NAME_1;
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "final_maps_database.db";

    public HereMapsDataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES_1);
        db.execSQL(SQL_CREATE_ENTRIES_2);
        Log.d("Database operations","Table created...");

    }

    public void insertCurrentWeatherDatas(SQLiteDatabase db, String hospital, String latitude, String longitude) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_HOSPITAL, hospital);
        values.put(COLUMN_NAME_LATITUDE, latitude);
        values.put(COLUMN_NAME_LONGITUDE, longitude);

        long newRowId1 = db.insert(TABLE_NAME_1, null, values);
        long newRowId2 = db.insert(TABLE_NAME_2, null, values);

        if(newRowId1 != -1) {
            Log.e("DB Helper", "l'insertion s'est effectuée correctement");
        }
        else {

            Log.e("DB Helper", "l'insertion ne s'est pas effectuée correctement");

        }

    }

    public void getCurrentWeather(SQLiteDatabase db) {

        String SQL_SELECT_LATEST_ENTRY = "SELECT * FROM " + HereMapsDataBaseHelper.TABLE_NAME_1 + " WHERE " + HereMapsDataBaseHelper.COLUMN_NAME_ID + " = (SELECT MAX(" + HereMapsDataBaseHelper.COLUMN_NAME_ID + ") " + "FROM " + HereMapsDataBaseHelper.TABLE_NAME_1 + ")";

        Cursor cursor = db.rawQuery(SQL_SELECT_LATEST_ENTRY, null);

        try {

            if(cursor.moveToFirst()) {

                //currentHospital.setHospitalName(cursor.getString(cursor.getColumnIndexOrThrow(MapsDataBaseHelper.COLUMN_NAME_HOSPITAL)));

            }

        }
        finally {

            cursor.close();

        }

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES_1);
        db.execSQL(SQL_DELETE_ENTRIES_2);
        onCreate(db);
        Log.d("Alarm", "onCreate(db)");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        Log.d("Alarm", "onUpgrade(db, oldVersion, newVersion)");
    }
}
