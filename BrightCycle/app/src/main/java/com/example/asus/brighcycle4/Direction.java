package com.example.asus.brighcycle4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

public class Direction {

    String JString;
    ArrayList<String> directions;
    ArrayAdapter<String> arrayAdapter;
    private static List<LatLng> list;
    GoogleMap temp;
    URL url;

    public Direction(){
        list = new ArrayList<LatLng>();
    }

    public void getDirectionPoints(GoogleMap map, LatLng src, LatLng dest) {

        temp = map;
        new GetLocations().execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + Double.toString(src.latitude) + "," + Double.toString(src.longitude) + "&destination=" + Double.toString(dest.latitude) + "," + Double.toString(dest.longitude)+"&sensor=false");

    }

    public class GetLocations extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            return readConnectionString(urls[0]);

        }

        protected void onPostExecute(String JSONString) {

            JString = JSONString;

            try {

                JSONObject jsonObject = new JSONObject(JSONString);
                JSONArray routesArray = new JSONArray(jsonObject.getString("routes"));
                JSONObject direction = routesArray.getJSONObject(0);
                JSONObject overviewPolylines = direction.getJSONObject("overview_polyline");
                String encodedPoints = overviewPolylines.getString("points");
                list.addAll(decodeCoord(encodedPoints));

                System.out.println("list size = " + list.size());

                for(int z = 0; z < list.size()-1; z++){

                    LatLng src= list.get(z);
                    LatLng dest= list.get(z+1);
                    temp.addPolyline(new PolylineOptions().add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude)).width(5).color(Color.YELLOW).geodesic(true));

                }

                instructions();

            }
            catch (Exception e) {

                e.printStackTrace();

            }

        }
    }

    public String[] getList() {

        String[] temp = (String[]) list.toArray();

        return temp;
    }

    public ArrayList<String> getDirections() {

        return directions;

    }

    void instructions () throws JSONException {

        System.out.println("instructions");
        JSONObject JSONobject = new JSONObject(JString);
        JSONObject routes = JSONobject.getJSONArray("routes").getJSONObject(0);
        JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);
        JSONArray steps = legs.getJSONArray("steps");
        arrayAdapter.clear();
        directions.clear();

        for (int i = 0; i < steps.length(); i++) {

            JSONObject step = steps.getJSONObject(i);
            directions.add(step.getString("html_instructions").replaceAll("<(.*?)*>", " "));
            arrayAdapter.notifyDataSetChanged();

        }

        System.out.println("directions size = " + directions.size());

    }

    private List<LatLng> decodeCoord(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;

    }

    public String readConnectionString(String iL) {

        StringBuilder stringBuilder = new StringBuilder();

        try {

            URL url = new URL(iL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(isw);

            while(reader.readLine() != null) {

                stringBuilder.append(reader.readLine());

            }

            in.close();
            urlConnection.disconnect();

        }
        catch (IOException ex) {
            Log.e("httptest", Log.getStackTraceString(ex));
        }

        return stringBuilder.toString();

    }
}
