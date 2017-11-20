package com.example.dr_charles.googlemapfullproject;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MapsInfo {

    public String mHospitalName;
    public String mLatitude;
    public String mLongitude;

    public MapsInfo(String hospital_name, String latitude, String longitude) {

        setHospitalName(hospital_name);

        setLatitude(latitude);

        setLongitude(longitude);

    }

    //default constructor
    public MapsInfo() {

    }

    public void setHospitalName(String weather_name) {
        mHospitalName = weather_name;
    }

    public void setLatitude(String weather_description) {
        mLatitude = weather_description;
    }

    public void setLongitude(String temperature) {
        mLongitude = temperature;
    }

    public String getHospitalName() {
        return mHospitalName;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }


}
