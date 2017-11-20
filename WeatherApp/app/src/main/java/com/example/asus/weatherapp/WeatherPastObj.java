package com.example.asus.weather_aarhus_groupe_thomas_charles_1;

public class WeatherPastObj {

    protected String city;
    protected String temperature;
    protected String name_description;
    protected String description;
    protected String humidity;
    protected String pressure;
    protected String weather_icon;
    protected String timestamp;

    public WeatherPastObj(String city, String temperature, String name_description, String description, String humidity, String pressure, String weather_icon, String timestamp){

        this.city = city;
        this.temperature = temperature;
        this.name_description = name_description;
        this.description = description;
        this.humidity = humidity;
        this.pressure = pressure;
        this.weather_icon = weather_icon;
        this.timestamp = timestamp;

    }

    public WeatherPastObj() {

    }

    public String getCity() {

        return city;

    }

    public String getTemperature() {

        return temperature;

    }

    public String getNameDecription() {

        return name_description;

    }

    public String getDescription() {

        return description;

    }

    public String getHumidity() {

        return humidity;

    }

    public String getPressure() {

        return pressure;

    }

    public String getWeatherIcon() {

        return weather_icon;

    }

    public String getTimeStamp() {

        return timestamp;

    }

    public void setCity(String city) {

        this.city = city;
    }

    public void setTemperature(String temperature) {

        this.temperature = temperature;
    }

    public void setNameDecription(String name_description) {

        this.name_description = name_description;
    }

    public void setDescription(String description) {

        this.description = description;

    }

    public void setHumidity(String humidity) {

        this.humidity = humidity;
    }

    public void setPressure(String pressure) {

        this.pressure = pressure;

    }

    public void setWeatherIcon(String weather_icon) {

        this.weather_icon = weather_icon;
    }

    public void setTimeStamp(String timestamp) {

        this.timestamp = timestamp;

    }

}