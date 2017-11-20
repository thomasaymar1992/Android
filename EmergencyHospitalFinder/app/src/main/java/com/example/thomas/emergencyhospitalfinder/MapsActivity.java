package com.example.dr_charles.googlemapfullproject;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String UPDATE_WEATHER_KEY = "update_weather_key";
    public static final String UPDATE_WEATHER_ACTION = "update_weather_action";
    MapsService mService;
    ListView listView;
    TextView hospital_name;
    TextView latitude;
    TextView longitude;
    protected AlarmManager alarmMgr;
    protected PendingIntent alarmIntent;
    protected Intent weatherUpdateIntent;
    protected final int mUpdateInterval_ms = 1;
    protected boolean mBound = false;
    protected ArrayList<MapsPastObj> mArrayOfWeatherPastObjs;
    protected MapsPastObjAdapter mWeatherAdapter;
    MapsDataBaseHelper mDbHelper;
    SQLiteDatabase db;
    protected MapsInfo mCurrentWeather;
    protected ArrayList<MapsInfo> mWeatherPast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hospital_name = (TextView) findViewById(R.id.hospital_name_field);

        latitude = (TextView) findViewById(R.id.latitude_field);

        longitude = (TextView) findViewById(R.id.longitude_field);

        listView = (ListView)findViewById(R.id.listView);

        if (!isMyServiceRunning(MapsService.class)) {

            weatherUpdateIntent = new Intent(MapsActivity.this, MapsService.class);

            weatherUpdateIntent.putExtra(UPDATE_WEATHER_KEY,UPDATE_WEATHER_ACTION);

            alarmIntent = PendingIntent.getService(getApplicationContext(), 0, weatherUpdateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, mUpdateInterval_ms, alarmIntent);

            startService(weatherUpdateIntent);

        }

        while(!isMyServiceRunning(MapsService.class)) {

            bindToService();

        }

        mDbHelper = new MapsDataBaseHelper(this);

        db = mDbHelper.getReadableDatabase();

        mCurrentWeather = mDbHelper.getCurrentWeather(db);

        hospital_name.setText(mCurrentWeather.getHospitalName());
        latitude.setText(mCurrentWeather.getLatitude());
        longitude.setText(mCurrentWeather.getLongitude());

        mWeatherPast = mDbHelper.getWeatherPast(db);

        int entries = mWeatherPast.size();
        MapsPastObj[] weatherObjsTest = new MapsPastObj[entries];

        for(int i = 0; i<entries; i++) {

            String hospital_name = mWeatherPast.get(i).mHospitalName;
            String latitude = mWeatherPast.get(i).mLatitude;
            String longitude = mWeatherPast.get(i).mLongitude;
            weatherObjsTest[i] = new MapsPastObj(hospital_name, latitude, longitude);
            mWeatherAdapter.add(weatherObjsTest[i]);

        }

        mArrayOfWeatherPastObjs = new ArrayList<>();

        mWeatherAdapter = new MapsPastObjAdapter(this, mArrayOfWeatherPastObjs);
        listView.setAdapter(mWeatherAdapter);

        mWeatherAdapter.clear();

        MapsHttpRequest mWeatherRequest = new MapsHttpRequest();
        Log.d("merde", mWeatherRequest.getWeatherData("45.5", "7.2", "AIzaSyDwJFmXhO5gsHgL3TfnzY4tcEaaTuw_Jc0").toString());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onPause() {

        unbindFromService();

        super.onPause();

    }

    @Override
    protected void onResume() {

        bindToService();

        super.onResume();

    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            MapsService.LocalBinder binder = (MapsService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    protected void bindToService() {
        // Bind to LocalService
        Intent intent = new Intent(this, MapsService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    protected void unbindFromService() {
        //Unbind from the service
        if (mBound) {
            unbindService(mConnection);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceClass.getName().equals(service.service.getClassName())) {

                return true;

            }
        }

        return false;
    }
}
