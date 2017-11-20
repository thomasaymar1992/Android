package com.example.dr_charles.googlemapfullproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MapsPastObjAdapter extends ArrayAdapter<MapsPastObj> {


    public MapsPastObjAdapter(Context context, ArrayList<MapsPastObj> weatherPastObjs) {

        super(context, 0, weatherPastObjs);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MapsPastObj weatherPastObj = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_weather_history, parent, false);

        }

        TextView hospital_name_field = (TextView)convertView.findViewById(R.id.item_hospital_name_field);
        TextView latitude_field = (TextView)convertView.findViewById(R.id.item_latitude_field);
        TextView longitude_field = (TextView)convertView.findViewById(R.id.item_longitude_field);

        hospital_name_field.setText(weatherPastObj.getHospitalName());
        latitude_field.setText(weatherPastObj.getLatitude());
        longitude_field.setText(weatherPastObj.getLongitude());

        return convertView;
    }

}
