package com.example.dr_charles.googlemapfullproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MapsDataBaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "maps_infos_thomas_charles";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_HOSPITAL = "hospital";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGITUDE = "longitude";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME_HOSPITAL + " TEXT, " +
                    COLUMN_NAME_LATITUDE + " TEXT, " +
                    COLUMN_NAME_LONGITUDE + " TEXT" + " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "final_maps_database.db";

    public MapsDataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
        Log.d("Database operations","Table created...");

    }

    public void insertCurrentWeatherDatas(SQLiteDatabase db, String hospital, String latitude, String longitude) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_HOSPITAL, hospital);
        values.put(COLUMN_NAME_LATITUDE, latitude);
        values.put(COLUMN_NAME_LONGITUDE, longitude);

        long newRowId = db.insert(TABLE_NAME, null, values);

        if(newRowId != -1) {
            Log.e("DB Helper", "l'insertion s'est effectuée correctement");
        }
        else {

            Log.e("DB Helper", "l'insertion ne s'est pas effectuée correctement");

        }

    }

    public MapsInfo getCurrentWeather(SQLiteDatabase db) {

       MapsInfo currentHospital = new MapsInfo();

        String SQL_SELECT_LATEST_ENTRY = "SELECT * FROM " + MapsDataBaseHelper.TABLE_NAME +
                " WHERE " + MapsDataBaseHelper.COLUMN_NAME_ID +
                " = (SELECT MAX(" + MapsDataBaseHelper.COLUMN_NAME_ID + ") " +
                "FROM " + MapsDataBaseHelper.TABLE_NAME + ")";

        Cursor cursor = db.rawQuery(SQL_SELECT_LATEST_ENTRY, null);

        try {

            if(cursor.moveToFirst()) {

                currentHospital.setHospitalName(cursor.getString(cursor.getColumnIndexOrThrow(MapsDataBaseHelper.COLUMN_NAME_HOSPITAL)));

                currentHospital.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(MapsDataBaseHelper.COLUMN_NAME_LATITUDE)));

                currentHospital.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(MapsDataBaseHelper.COLUMN_NAME_LONGITUDE)));

            }

        }
        finally {

            cursor.close();

        }

        return currentHospital;

    }

    public ArrayList<MapsInfo> getWeatherPast(SQLiteDatabase db) {

        ArrayList<MapsInfo> weatherHistory = new ArrayList<>();

        String SQL_SELECT_ALL_ENTRIES = "SELECT * FROM " + MapsDataBaseHelper.TABLE_NAME + " ORDER BY " + MapsDataBaseHelper.COLUMN_NAME_ID + " DESC LIMIT 48";

        Cursor cursor = db.rawQuery(SQL_SELECT_ALL_ENTRIES, null);

        try {
            if(cursor.moveToFirst()) {

                do {

                    MapsInfo entry = new MapsInfo();

                    entry.setHospitalName(cursor.getString(cursor.getColumnIndexOrThrow(MapsDataBaseHelper.COLUMN_NAME_HOSPITAL)));

                    entry.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(MapsDataBaseHelper.COLUMN_NAME_LATITUDE)));

                    entry.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(MapsDataBaseHelper.COLUMN_NAME_LONGITUDE)));

                    weatherHistory.add(entry);

                }

                while (cursor.moveToNext());
            }
        }
        finally {

            cursor.close();

        }

        return weatherHistory;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        Log.d("Alarm", "onCreate(db)");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        Log.d("Alarm", "onUpgrade(db, oldVersion, newVersion)");
    }
}
