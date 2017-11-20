package com.example.dr_charles.googlemapfullproject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsService extends Service {

    protected String api_key = "AIzaSyAPrOxAoTKUdaXtktg4B2QrdPZO5SpM0VQ";
    String mLatitude = "48.862725";
    String mLongitude = "2.287592000000018";
    protected ConnectivityManager connManager;
    private final IBinder mBinder = (IBinder) new LocalBinder();
    SQLiteDatabase db;

    public class LocalBinder extends Binder {

        MapsService getService() {

            return MapsService.this;

        }

    }

    public MapsService() {

    }

    @Override
    public void onCreate() {

        super.onCreate();

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(isNetworkAvailable()) {

            if (intent != null) {

                String action = intent.getStringExtra(MapsActivity.UPDATE_WEATHER_KEY);

                if (action.equals(MapsActivity.UPDATE_WEATHER_ACTION)) {

                    GetHospitalsTask hospitalsTask = new GetHospitalsTask(this);
                    hospitalsTask.execute(mLatitude, mLongitude, api_key);

                }
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;

    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    private boolean isNetworkAvailable() {

        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
