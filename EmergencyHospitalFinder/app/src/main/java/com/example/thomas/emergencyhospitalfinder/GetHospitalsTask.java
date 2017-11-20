package com.example.dr_charles.emergencyhospitalfinder;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
// import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Map;

public class GetHospitalsTask extends AsyncTask<String, Void, Map<String, String>> {

    protected Map<String, String> mJSONResponse;
    MapsHttpRequest mWeatherRequest;
    MapsDataBaseHelper mDbHelper;
    SQLiteDatabase db;
    String hospital_name;
    String latitude;
    String longitude;
    Context ctx;

    GetHospitalsTask(Context ctx) {

        this.ctx = ctx;

    }

    protected void onPreExecute() {

        super.onPreExecute();

    }

    protected Map<String, String> doInBackground(String... params) {

        mWeatherRequest = new MapsHttpRequest();
        mJSONResponse = mWeatherRequest.getWeatherData(params[0], params[1], params[2]);
        mDbHelper = new MapsDataBaseHelper(ctx);

        if(mJSONResponse != null) {

            try {

                String hospital_name = mJSONResponse.get("hospital_name").toString();
                Log.d("hospital_name", hospital_name);
                String latitude = mJSONResponse.get("latitude").toString();
                Log.d("latitude", latitude);
                String longitude = mJSONResponse.get("longitude").toString();
                Log.d("longitude", longitude);

                //db = mDbHelper.getWritableDatabase();
                //mDbHelper.insertCurrentWeatherDatas(db, hospital_name, latitude, longitude);

            }
            catch (Exception e) {

            }

        }
        else {

        }
        return null;
    }

    protected void onPostExecute(String result) {

    }
}