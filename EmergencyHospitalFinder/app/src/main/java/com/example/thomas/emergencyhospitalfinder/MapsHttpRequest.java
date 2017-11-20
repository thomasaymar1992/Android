package com.example.dr_charles.googlemapfullproject;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MapsHttpRequest {

    Map<String, String> mJSONResponse;
    //private String NEARBY_SEARCH_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s,%s&rankby=distance&types=hospital&sensor=false&key=AIzaSyAPrOxAoTKUdaXtktg4B2QrdPZO5SpM0VQ";
    private String NEARBY_SEARCH_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=48.862725,2.287592000000018&rankby=distance&types=hospital&sensor=false&key=AIzaSyBEBv53cS1chmmaCModuCeX6eU4gQmOVyE";
    //private String NEARBY_SEARCH_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%s,%s&rankby=distance&types=hospital&sensor=false&key=AIzaSyDwJFmXhO5gsHgL3TfnzY4tcEaaTuw_Jc0";

    public Map<String, String> getWeatherData(String latitude, String longitude, String api_key) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuffer buffer = null;

        try {

            URL url = new URL(NEARBY_SEARCH_API);
            //URL url = new URL(String.format(NEARBY_SEARCH_API, latitude, longitude));
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("x-api-key", api_key);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
            buffer = new StringBuffer();
            String tmp = "";

            while ((tmp = reader.readLine()) != null) {

                buffer.append(tmp).append("\n");

            }

            reader.close();

            connection.disconnect();

            return parseWeatherData(buffer.toString());

        } catch (Exception e) {

            Log.d("chiure", "chiure");
        }

        return null;

    }

    public Map<String, String> parseWeatherData(String data) {

        mJSONResponse = new HashMap<>();

        Log.d("response", data);

        try {

            Log.d("response", data);

            JSONObject response = new JSONObject(data);

            int cod = response.getInt("cod");

            if (response.getInt("cod") != 200) {

                Log.e("Error", "code différent de 200");

            } else {

                JSONArray EmergencyItems = new JSONArray(response.getString("results"));
                JSONObject EmergencyItem = EmergencyItems.getJSONObject(0);

                Log.d("AAAAAA", EmergencyItem.toString());

                if (EmergencyItem != null) {

                    mJSONResponse.put("hospital_name", EmergencyItem.getString("formatted_address"));
                    mJSONResponse.put("hospital_latitude", EmergencyItem.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                    mJSONResponse.put("hospital_longitude", EmergencyItem.getJSONObject("geometry").getJSONObject("location").getString("lng"));

                }

                Log.d("parseWeatherData result", mJSONResponse.toString());

                return mJSONResponse;
            }
        }
        catch (JSONException e) {

        }

        return null;
    }

}