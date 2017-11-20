package com.example.asus.weather_aarhus_groupe_thomas_charles_1;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    WeatherService weatherService;
    ListView weatherHistoric;
    WeatherPastObjAdapter weatherAdapter;
    ArrayList<WeatherInfo> weatherPast;
    ArrayList<WeatherPastObj>weatherPastConditions;
    WeatherInfo weatherCurrent;
    PendingIntent alarmIntent;
    boolean bound = false;
    TextView city;
    TextView temperature;
    TextView name_description;
    TextView description;
    TextView humidity;
    TextView pressure;
    TextView weather_icon;
    TextView time_stamp;

    ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            WeatherService.LocalWeatherBinder weatherBinder = (WeatherService.LocalWeatherBinder)service;
            weatherService = weatherBinder.getBinder();
            bound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            bound = false;

        }
    };

    protected void onStart() {

        super.onStart();
        Intent intent = new Intent(WeatherActivity.this, WeatherService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    protected void onStop() {

        super.onStop();
        if(bound) {
            unbindService(mServiceConnection);
            bound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherHistoric = (ListView) findViewById(R.id.weatherHistoric);
        city = (TextView)findViewById(R.id.city_field);
        name_description = (TextView)findViewById(R.id.name_description_field);
        description = (TextView)findViewById(R.id.description_field);
        temperature = (TextView)findViewById(R.id.temperature_field);
        humidity = (TextView)findViewById(R.id.humidity_field);
        pressure = (TextView)findViewById(R.id.pressure_field);
        weather_icon = (TextView)findViewById(R.id.weatherIcon_field);
        time_stamp = (TextView)findViewById(R.id.timeStamp_field);

        if(isMyServiceRunning(WeatherService.class)) {

            generateCurrentWeather();
            weatherPastConditions = generatePastWeather();
            weatherAdapter = new WeatherPastObjAdapter(WeatherActivity.this, weatherPastConditions);
            weatherHistoric.setAdapter(weatherAdapter);

        }
        else {

            Intent weatherIntent = new Intent(WeatherActivity.this, WeatherService.class);
            startService(weatherIntent);

        }

    }

    private void generateCurrentWeather() {

        weatherCurrent = weatherService.getCurrentWeather();
        city.setText(weatherCurrent.getCity());
        name_description.setText(weatherCurrent.getNameDescription());
        description.setText(weatherCurrent.getDescription());
        temperature.setText(weatherCurrent.getTemperature());
        humidity.setText(weatherCurrent.getHumidity());
        pressure.setText(weatherCurrent.getPressure());
        weather_icon.setText(weatherCurrent.getTimestamp());

    }

    private ArrayList<WeatherPastObj> generatePastWeather(){

        ArrayList<WeatherPastObj> obj = new ArrayList<>();

        try {

            weatherPast = weatherService.getPastWeather();
        }
        catch(Exception e) {

            Log.d("Error","getWeatherHistory : displayHistoricWeather",e);

        }

        int records = weatherPast.size();
        WeatherPastObj[] weatherObjs = new WeatherPastObj[records];

        for(int i = 0; i<records; i++) {

            String city = weatherPast.get(i).getCity().toString();
            String temperature = weatherPast.get(i).getTemperature().toString();
            String name_description = weatherPast.get(i).getNameDescription().toString();
            String description = weatherPast.get(i).getDescription().toString();
            String humidity = weatherPast.get(i).getHumidity().toString();
            String pressure = weatherPast.get(i).getPressure().toString();
            String weatherIcon = weatherPast.get(i).getWeatherIcon().toString();
            String timestamp = weatherPast.get(i).getTimestamp();
            weatherObjs[i] = new WeatherPastObj(city, temperature, name_description, description, humidity, pressure, weatherIcon, timestamp);
            obj.add(weatherObjs[i]);

        }

        return obj;
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