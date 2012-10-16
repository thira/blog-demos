package com.example.ksoap2.wsclient.weather.bo;

import java.util.ArrayList;
import java.util.List;

public class CityForecastBO {

    private boolean isSuccess;
    private String state;
    private String city;
    private String weatherStationCity;
    private String responseText;
    private List<ForecastInfo> forecasts;

    public CityForecastBO(boolean isSuccess, String state, String city, String weatherStationCity, String responseText) {
        this.isSuccess = isSuccess;
        this.state = state;
        this.city = city;
        this.weatherStationCity = weatherStationCity;
        this.responseText = responseText;
        this.forecasts = new ArrayList<CityForecastBO.ForecastInfo>();
    }

    public ForecastInfo addForecast(String date, int weatherId, String description) {
        ForecastInfo forecast = new ForecastInfo(date, weatherId, description);
        forecasts.add(forecast);

        return forecast;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getCity() {
        return city;
    }

    public List<ForecastInfo> getForecasts() {
        return forecasts;
    }

    public String getResponseText() {
        return responseText;
    }

    public String getState() {
        return state;
    }

    public String getWeatherStationCity() {
        return weatherStationCity;
    }

    public static class ForecastInfo {

        private String date;
        private int weatherId;
        private String description;
        private String temperatureMorningLow;
        private String temperatureDaytimeHigh;
        private String precipitationProbabilityNightTime;
        private String precipitationProbabilityDaytime;

        private ForecastInfo(String date, int weatherId, String description) {
            this.date = date;
            this.weatherId = weatherId;
            this.description = description;
        }

        public void setTemperature(String temperatureMorningLow, String temperatureDaytimeHigh) {
            this.temperatureDaytimeHigh = temperatureDaytimeHigh;
            this.temperatureMorningLow = temperatureMorningLow;
        }

        public void setPrecipiationProbability(String precipitationProbabilityNightTime, String precipitationProbabilityDaytime) {
            this.precipitationProbabilityNightTime = precipitationProbabilityNightTime;
            this.precipitationProbabilityDaytime = precipitationProbabilityDaytime;
        }

        public String getDate() {
            return date;
        }

        public int getWeatherId() {
            return weatherId;
        }

        public String getDescription() {
            return description;
        }

        public String getTemperatureMorningLow() {
            return temperatureMorningLow;
        }

        public String getTemperatureDaytimeHigh() {
            return temperatureDaytimeHigh;
        }

        public String getPrecipitationProbabilityNightTime() {
            return precipitationProbabilityNightTime;
        }

        public String getPrecipitationProbabilityDaytime() {
            return precipitationProbabilityDaytime;
        }
    }
}
