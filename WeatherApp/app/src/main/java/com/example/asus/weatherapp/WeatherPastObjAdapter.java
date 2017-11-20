package com.example.asus.weather_aarhus_groupe_thomas_charles_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class WeatherPastObjAdapter extends ArrayAdapter<WeatherPastObj> {

    public WeatherPastObjAdapter(Context context, ArrayList<WeatherPastObj> weatherPastConditions) {

        super(context, 0, weatherPastConditions);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_historic_weather, parent, false);

        }

        WeatherPastObjViewHolder viewHolder = (WeatherPastObjViewHolder) convertView.getTag();

        if(viewHolder == null){

            viewHolder = new WeatherPastObjViewHolder();
            viewHolder.city = (TextView) convertView.findViewById(R.id.item_city_field);
            viewHolder.temperature = (TextView) convertView.findViewById(R.id.item_temperature_field);
            viewHolder.name_description = (TextView) convertView.findViewById(R.id.item_name_description_field);
            viewHolder.description = (TextView) convertView.findViewById(R.id.item_description_field);
            viewHolder.humidity = (TextView) convertView.findViewById(R.id.item_humidity_field);
            viewHolder.pressure = (TextView) convertView.findViewById(R.id.item_pressure_field);
            viewHolder.weatherIcon = (TextView) convertView.findViewById(R.id.item_weatherIcon_field);
            viewHolder.timeStamp = (TextView) convertView.findViewById(R.id.item_timeStamp_field);
            convertView.setTag(viewHolder);

        }

        WeatherPastObj obj = getItem(position);

        viewHolder.city.setText(obj.getCity());
        viewHolder.temperature.setText(obj.getTemperature());
        viewHolder.name_description.setText(obj.getNameDecription());
        viewHolder.description.setText(obj.getDescription());
        viewHolder.humidity.setText(obj.getHumidity());
        viewHolder.pressure.setText(obj.getPressure());
        viewHolder.weatherIcon.setText(obj.getWeatherIcon());
        viewHolder.timeStamp.setText(obj.getTimeStamp());

        return convertView;
    }

    private class WeatherPastObjViewHolder{

        public TextView city;
        public TextView temperature;
        public TextView name_description;
        public TextView description;
        public TextView humidity;
        public TextView pressure;
        public TextView weatherIcon;
        public TextView timeStamp;

    }

}
