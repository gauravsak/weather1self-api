package com.equalexperts.weather1self.client.response.wu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {

    private List<WeatherDatum> weatherData;

    public WeatherResponse(List<WeatherDatum> weatherData) {
        this.weatherData = weatherData;
    }

    public List<WeatherDatum> getWeatherData() {
        return weatherData;
    }

    public static WeatherResponse fromJSON(JSONObject weatherResponseJson) {
        JSONObject weatherHistoryJSON = weatherResponseJson.getJSONObject("history");
        JSONArray weatherDataJSON = weatherHistoryJSON.getJSONArray("observations");
        List<WeatherDatum> weatherData = new ArrayList<>();
        for (int i = 0; i < weatherDataJSON.length(); i ++) {
            WeatherDatum weatherDatum = WeatherDatum.fromJSON(weatherDataJSON.getJSONObject(i));
            weatherData.add(weatherDatum);
        }
        return new WeatherResponse(weatherData);
    }
}
