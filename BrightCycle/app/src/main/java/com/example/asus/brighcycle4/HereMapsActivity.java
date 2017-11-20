package com.example.asus.brighcycle4;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HereMapsActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    MapView mapView;
    HereMapsService mService;
    Location currentLm;
    LocationManager locationManager;
    LocalBroadcastManager bManager;
    BroadcastReceiver bReceiver;
    public static final String UPDATE_CURRENT_LOCATION_KEY = "update_current_location_key";
    public static final String UPDATE_CURRENT_LOCATION_ACTION = "update_current_location_action";
    ArrayList<String> itemlist = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private String[] drawerItemsList;
    private ListView myDrawer;
    private TextView myTextView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;
    RelativeLayout rl;
    Toolbar toolbar;
    Toolbar toolbar1;
    EditText editText;
    protected AlarmManager alarmMgr;
    protected PendingIntent alarmIntent;
    protected Intent updateIntent;
    protected final int mUpdateInterval_ms = 1;
    protected boolean mBound = false;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_here_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_test);
        toolbar.inflateMenu(R.menu.menu_search);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                /*switch (item.getItemId()){
                    case R.id.action_search:
                        editText.setVisibility(View.VISIBLE);
                        onMapSearch(item);
                            return true;
                    case R.id.action_delete:
                        delete();
                            return true;
                    case R.id.action_settings:
                            return true;
                }*/
                    return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        });

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MYACTION");
        bReceiver = new MyReceiver();
        bManager.registerReceiver(bReceiver, intentFilter);
                if(!isMyServiceRunning(HereMapsService.class)) {
                    updateIntent = new Intent(HereMapsActivity.this, HereMapsService.class);
                    updateIntent.putExtra(UPDATE_CURRENT_LOCATION_KEY,UPDATE_CURRENT_LOCATION_ACTION);
                    alarmIntent = PendingIntent.getService(getApplicationContext(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, mUpdateInterval_ms, alarmIntent);
                    startService(updateIntent);
                }

        while(isMyServiceRunning(HereMapsService.class)) {

            bindToService();

        }

        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            else {
                super.onBackPressed();
            }
    }

    private void save(){
        Toast.makeText(this,R.string.action_save,Toast.LENGTH_LONG).show();
    }

    private void delete(){
        Toast.makeText(this,R.string.action_delete,Toast.LENGTH_LONG).show();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
    //Handle navigation view item clicks here.
        int id = item.getItemId();

            if (id == R.id.nav_camera) {

            }
            else if (id == R.id.nav_gallery) {

            }
            else if (id == R.id.nav_slideshow) {

            }
            else if (id == R.id.nav_manage) {

            }
            else if (id == R.id.nav_share) {

            }
            else if (id == R.id.nav_send) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
                return true;
    }

    public void onMapSearch(MenuItem item) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

            if (location != null || !location.equals("")) {
                Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

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
        unregisterReceiver(bReceiver);
        super.onPause();

    }

    @Override
    protected void onResume() {

        if (!isMyServiceRunning(HereMapsService.class)) {

            updateIntent = new Intent(HereMapsActivity.this, HereMapsService.class);
            updateIntent.putExtra(UPDATE_CURRENT_LOCATION_KEY,UPDATE_CURRENT_LOCATION_ACTION);
            alarmIntent = PendingIntent.getService(getApplicationContext(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, mUpdateInterval_ms, alarmIntent);
            startService(updateIntent);

        }

        while(!isMyServiceRunning(HereMapsService.class)) {

            bindToService();

        }

        super.onResume();

        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MYACTION");
        registerReceiver(bReceiver, intentFilter);*/

    }

    private ServiceConnection mConnection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {

        Toast.makeText(HereMapsActivity.this, "onServiceConnected", Toast.LENGTH_LONG).show();
        HereMapsService.LocalBinder binder = (HereMapsService.LocalBinder) service;
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
        Intent intent = new Intent(HereMapsActivity.this, HereMapsService.class);
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

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals("MYACTION")) {

                    Toast.makeText(HereMapsActivity.this, "GETACTION", Toast.LENGTH_LONG).show();

                }

        }

    }
}
