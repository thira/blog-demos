package com.example.ksoap2.wsclient.weather.bo;

public class WeatherDescriptionBO {

    private final int id;
    private final String description;
    private final String pictureURL;

    public WeatherDescriptionBO(int id, String description, String pictureURL) {
        this.id = id;
        this.description = description;
        this.pictureURL = pictureURL;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureURL() {
        return pictureURL;
    }
}
