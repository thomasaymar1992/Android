package com.example.asus.weather_aarhus_groupe_thomas_charles_1;

public class WeatherInfo {

    protected String city;
    protected String temperature;
    protected String name_description;
    protected String description;
    protected String humidity;
    protected String pressure;
    protected String weatherIcon;
    protected String timeStamp;

    public WeatherInfo(String city, String temperature, String name_description, String description, String humidity, String pressure, String weatherIcon, String timeStamp) {

        setCity(city);
        setTemperature(temperature);
        setNameDescription(name_description);
        setDescription(description);
        setHumidity(humidity);
        setPressure(pressure);
        setIcon(weatherIcon);
        setTimestamp(timeStamp);

    }

    public WeatherInfo() {

    }

    public void setCity(String city) {

        this.city = city;

    }

    public void setNameDescription(String nameDesc) {

        this.name_description = nameDesc;

    }

    public void setDescription(String Desc) {

        this.description = Desc;

    }

    public void setTemperature(String temp) {

        this.temperature = temp;

    }

    public void setHumidity(String humidity) {

        this.humidity = humidity;

    }

    public void setIcon(String weatherIcon) {

        this.weatherIcon = weatherIcon;

    }

    public void setPressure(String pressure) {

        this.pressure = pressure;

    }

    public void setTimestamp(String timestamp) {

        this.timeStamp = timestamp;

    }

    public String getCity() {

        return city;

    }

    public String getNameDescription() {

        return name_description;

    }

    public String getDescription() {

        return description;

    }

    public String getTemperature() {

        return temperature;

    }

    public String getHumidity() {

        return humidity;

    }

    public String getPressure() {

        return pressure;

    }

    public String getWeatherIcon() {

        return weatherIcon;

    }

    public String getTimestamp() {

        return timeStamp;

    }

}
