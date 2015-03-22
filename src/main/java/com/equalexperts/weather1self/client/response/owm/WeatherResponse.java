package com.equalexperts.weather1self.client.response.owm;

import com.equalexperts.weather1self.client.response.TemperatureDatum;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherResponse {

    private List<TemperatureDatum> weatherData;

    public WeatherResponse(List<TemperatureDatum> weatherData) {
        this.weatherData = weatherData;
    }

    public List<TemperatureDatum> getWeatherData() {
        return weatherData;
    }

    public static WeatherResponse fromJSON(JSONObject weatherResponseJson) {
        JSONArray weatherDataJSON = weatherResponseJson.getJSONArray("list");
        List<TemperatureDatum> weatherData = new ArrayList<>();
        for (int i = 0; i < weatherDataJSON.length(); i ++) {
            WeatherDatum weatherDatum = WeatherDatum.fromJSON(weatherDataJSON.getJSONObject(i));
            weatherData.add(weatherDatum);
        }
        return new WeatherResponse(weatherData);
    }
}
