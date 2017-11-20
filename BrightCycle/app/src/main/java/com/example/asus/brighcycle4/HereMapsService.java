package com.example.asus.brighcycle4;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

/**
 * Created by ASUS on 21/10/2017.
 */

public class HereMapsService extends Service {

    private final IBinder mBinder = (IBinder) new LocalBinder();

    public class LocalBinder extends Binder {

        HereMapsService getService() {

            return HereMapsService.this;

        }

    }

    public HereMapsService() {


    }

    @Override
    public void onCreate() {

        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            String action = intent.getStringExtra(HereMapsActivity.UPDATE_CURRENT_LOCATION_KEY);
            Intent returnIntent = new Intent("MYACTION");
            returnIntent.putExtra("action", action);
            LocalBroadcastManager.getInstance(this).sendBroadcast(returnIntent);

        }

        return START_STICKY;

    }

    @Nullable
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


}
