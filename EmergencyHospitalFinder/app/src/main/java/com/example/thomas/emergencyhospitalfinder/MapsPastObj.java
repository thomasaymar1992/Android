package com.example.dr_charles.googlemapfullproject;

public class MapsPastObj {

    public String hospital_name;
    public String latitude;
    public String longitude;

    public MapsPastObj(String hospital_name, String latitude, String longitude){

        this.hospital_name = hospital_name;

        this.latitude = latitude;

        this.longitude = longitude;

    }

    public MapsPastObj() {

    }

    public String getHospitalName() {

        return hospital_name;

    }

    public String getLatitude() {

        return latitude;

    }

    public String getLongitude() {

        return longitude;

    }

    public void setHospitalName(String hospital_name) {

        this.hospital_name = hospital_name;
    }

    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {

        this.longitude = longitude;

    }

}
