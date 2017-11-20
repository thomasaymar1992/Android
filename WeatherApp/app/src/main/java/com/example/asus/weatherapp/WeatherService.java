package com.example.asus.weather_aarhus_groupe_thomas_charles_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.asus.TestListView.R;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherService extends Service {

    public final IBinder WeatherServiceBinder = new LocalWeatherBinder();
    public WeatherHttpRequest ArhusRequest;
    public WeatherDatabaseHelper WeatherDatabase;
    public WeatherBDD WeatherBdd;
    public WeatherInfo currentWeather;
    private static final String TAG = "MyService";

    public class LocalWeatherBinder extends Binder {

        WeatherService getBinder() {
            // Return this instance of LocalService so clients can call public methods
            return WeatherService.this;

        }
    }

    public WeatherService() {

    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            long delay = 0;
            long period = 1800000;

            WeatherBdd = new WeatherBDD(getApplicationContext());
            MyCityTimerTask myTimerTask = new MyCityTimerTask();
            Timer myTimer = new Timer();
            myTimer.schedule(myTimerTask, delay, period);

        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return WeatherServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    public WeatherInfo getCurrentWeather() {

        WeatherInfo currentWeather = new WeatherInfo();
        SQLiteDatabase db = WeatherBdd.openReadable();
        String query = "SELECT * FROM " + WeatherBdd.TABLE_NAME + " WHERE " + WeatherBdd.COL_HISTORIC_ID + " = MAX(" + WeatherBdd.COL_HISTORIC_ID + ")";
        Cursor cursor = db.rawQuery(query, null);

        try {

            if (cursor.moveToFirst()) {

                currentWeather.setCity(getApplicationContext().getString(R.string.city));
                currentWeather.setTemperature(cursor.getString(0));
                currentWeather.setNameDescription(cursor.getString(1));
                currentWeather.setDescription(cursor.getString(2));
                currentWeather.setHumidity(cursor.getString(3));
                currentWeather.setPressure(cursor.getString(4));
                currentWeather.setIcon(cursor.getString(5));
                currentWeather.setTimestamp(cursor.getString(6));

            }
        } catch (Exception e) {

            Log.d("Error", "getCurrentWeather", e);

        } finally {
            cursor.close();
        }

        return currentWeather;
    }

    //Returns the current weather history of the last 24 hours
    public ArrayList<WeatherInfo> getPastWeather() {

        ArrayList<WeatherInfo> weatherPast = new ArrayList<>();

        SQLiteDatabase db = WeatherBdd.openReadable();

        String query = "SELECT * FROM " + WeatherBdd.TABLE_NAME + " ORDER BY " + WeatherBdd.COL_HISTORIC_ID + " DESC LIMIT 48";
        Cursor cursor = db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext() && !cursor.isLast()) {

                    WeatherInfo infos = new WeatherInfo();

                    infos.setTemperature(cursor.getString(0));
                    infos.setNameDescription(cursor.getString(1));
                    infos.setDescription(cursor.getString(2));
                    infos.setHumidity(cursor.getString(3));
                    infos.setPressure(cursor.getString(4));
                    infos.setIcon(cursor.getString(5));
                    infos.setTimestamp(cursor.getString(6));
                    weatherPast.add(infos);
                }
            }
        } catch (Exception e) {

            Log.d("Error", "getPastWeather", e);

        } finally {
            cursor.close();
        }

        return weatherPast;
    }

    public String setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getApplicationContext().getString(R.string.weather_sunny);
            } else {
                icon = getApplicationContext().getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = getApplicationContext().getString(R.string.weather_foggy);
                    break;
                case 3:
                    icon = getApplicationContext().getString(R.string.weather_cloudy);
                    break;
                case 7:
                    icon = getApplicationContext().getString(R.string.weather_rainy);
                    break;
                case 8:
                    icon = getApplicationContext().getString(R.string.weather_snowy);
                    break;
                case 6:
                    icon = getApplicationContext().getString(R.string.weather_thunder);
                    break;
                case 5:
                    icon = getApplicationContext().getString(R.string.weather_drizzle);
                    break;
            }
        }
        return icon;
    }

    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8);

    }

    public class MyCityTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;//Call back interface

        public MyCityTask(AsyncResponse delegate) {
            this.delegate = delegate;//Assigning call back interfacethrough constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            Looper looper = Looper.getMainLooper();
            looper.prepare();
            looper.loop();

            ArhusRequest = new WeatherHttpRequest();

            JSONObject jsonWeather = null;
            try {
                jsonWeather = ArhusRequest.getWeatherData(params[0]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);

            }

            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json != null) {

                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    String city = json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country");
                    String temperature = String.format("%.2f", main.getDouble("temp")) + "°";
                    String name_description = details.getString("main").toUpperCase(Locale.US);
                    String description = details.getString("description").toUpperCase(Locale.US);
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";
                    String timestamp = df.format(new Date(json.getLong("dt") * 1000));
                    String iconText = setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000);

                    delegate.processFinish(city, temperature, name_description, description, humidity, pressure, iconText, timestamp);

                }
            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }

        }

    }

    public class MyCityTimerTask extends TimerTask {

        public void run() {

            MyCityTask asyncTask = new MyCityTask(new AsyncResponse() {

                public void processFinish(String weather_city, String weather_temperature, String weather_name, String weather_description, String weather_humidity, String weather_pressure, String weather_iconText, String time_stamp) {

                    currentWeather = new WeatherInfo();
                    currentWeather.setCity(weather_city);
                    currentWeather.setTemperature(weather_temperature);
                    currentWeather.setNameDescription(weather_name);
                    currentWeather.setDescription(weather_description);
                    currentWeather.setHumidity(weather_humidity);
                    currentWeather.setPressure(weather_pressure);
                    currentWeather.setIcon(weather_iconText);
                    currentWeather.setTimestamp(time_stamp);

                    try {
                        WeatherBdd.insertCurrentWeatherDatas(weather_city, weather_temperature, weather_name, weather_description, weather_humidity, weather_pressure, weather_iconText, time_stamp);
                    }
                    catch (Exception e) {
                        Log.d("Error", "MyCityTask or MyCityTimerTask", e);
                    } finally {
                        WeatherBdd.removeOldWeatherDatas();
                    }
                }
            });
            asyncTask.execute();
        }

    }

    public class WeatherHttpRequest {

        private String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

        public JSONObject getWeatherData(String city) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer json = null;

            try {
                URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
                connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("x-api-key", getApplicationContext().getString(R.string.open_weather_map_api_key));
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                json = new StringBuffer();
                String tmp;

                while ((tmp = reader.readLine()) != null) {

                    json.append(tmp).append("\n");
                    reader.close();

                }

                JSONObject data = new JSONObject(json.toString());

                // This value will be 404 if the request was not
                // successful
                if (data.getInt("cod") != 200) {
                    return null;
                }
                else {
                    return data;
                }

            } catch (Exception e) {
                Log.d("Error", "WeatherHttpRequest getWeatherData", e);
                return null;
            }
        }
    }

    public class WeatherBDD {

        public final String TABLE_NAME = "historic_weather";
        public final String COL_HISTORIC_ID = null;
        public final String COL_WEATHER_CITY = null;
        public final String COL_WEATHER_TEMPERATURE = null;
        public final String COL_WEATHER_NAME = null;
        public final String COL_WEATHER_DESCRIPTION = null;
        public final String COL_LEVEL_HUMIDITY = null;
        public final String COL_LEVEL_PRESSURE = null;
        public final String COL_WEATHER_ICON = null;
        public final String COL_TIMESTAMP = null;
        private SQLiteDatabase bdd;

        private WeatherDatabaseHelper myBaseSQLite;

        public WeatherBDD(Context context){

            myBaseSQLite = new WeatherDatabaseHelper(context);

        }

        public SQLiteDatabase openWritable(){

            bdd = myBaseSQLite.getWritableDatabase();
            return bdd;
        }

        public SQLiteDatabase openReadable() {

            bdd = myBaseSQLite.getReadableDatabase();
            return bdd;

        }

        public void close(){

            bdd.close();

        }

        public SQLiteDatabase getBDD(){

            return bdd;

        }

        public long insertCurrentWeatherDatas(String weather_city, String weather_temperature, String weather_name, String weather_desc, String weather_humidity, String weather_pressure, String weather_icon, String weather_time){

            ContentValues values = new ContentValues();
            values.put(COL_WEATHER_CITY, weather_city);
            values.put(COL_WEATHER_TEMPERATURE, weather_temperature);
            values.put(COL_WEATHER_NAME, weather_name);
            values.put(COL_WEATHER_DESCRIPTION, weather_desc);
            values.put(COL_LEVEL_HUMIDITY, weather_humidity);
            values.put(COL_LEVEL_PRESSURE, weather_pressure);
            values.put(COL_WEATHER_ICON, weather_icon);
            values.put(COL_TIMESTAMP, weather_time);

            return bdd.insert(TABLE_NAME, null, values);

        }

        private void removeOldWeatherDatas() {

            try {

                bdd = openWritable();

            }
            catch(Exception e) {

                Log.d("Error", "removeOldWeatherDatas", e);
            }
            long now = System.currentTimeMillis() / 1000L;
            long old = now - 24*60*60;  //24 hours

            String query = "DELETE FROM " + myBaseSQLite.TABLE_NAME +
                    " WHERE " + myBaseSQLite.COL_TIMESTAMP +
                    " < " + Long.toString(old);
            bdd.execSQL(query);

        }

    }

}