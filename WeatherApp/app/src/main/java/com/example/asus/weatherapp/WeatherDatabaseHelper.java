package com.example.asus.weather_aarhus_groupe_thomas_charles_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ASUS on 05/10/2016.
 */
public class WeatherDatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "historic_weather";
    public static final String COL_HISTORIC_ID = "id";
    public static final String COL_WEATHER_CITY = "city";
    public static final String COL_WEATHER_TEMPERATURE = "temperature";
    public static final String COL_WEATHER_NAME = "weather";
    public static final String COL_WEATHER_NAME_DESCRIPTION = "weather_description";
    public static final String COL_WEATHER_HUMIDITY = "weather";
    public static final String COL_WEATHER_PRESSURE = "weather_description";
    public static final String COL_WEATHER_ICON_TEXT = "icon_text";
    public static final String COL_TIMESTAMP = "time_stamp";
    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_NAME +
            " (" + COL_HISTORIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_WEATHER_CITY + " TEXT NOT NULL"
            + COL_WEATHER_TEMPERATURE + " TEXT NOT NULL"
            + COL_WEATHER_NAME + " TEXT NOT NULL"
            + COL_WEATHER_NAME_DESCRIPTION + " TEXT NOT NULL"
            + COL_WEATHER_HUMIDITY + " TEXT NOT NULL, "
            + COL_WEATHER_PRESSURE + " TEXT NOT NULL, "
            + COL_WEATHER_ICON_TEXT + " TEXT NOT NULL"
            + COL_TIMESTAMP + " TEXT NOT NULL " + " )";
    private static final String DATABASE_NAME = "aarhus_weather.db";
    private static final int DATABASE_VERSION = 4;

    public WeatherDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_BDD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + TABLE_NAME + ";");
        onCreate(db);

    }

}
