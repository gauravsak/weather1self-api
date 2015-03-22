package com.equalexperts.weather1self.client.response.wu;

import org.json.JSONObject;

import java.math.BigDecimal;

public class WeatherDatum {

    private BigDecimal temperature;
    private DateTime instant;

    public WeatherDatum(BigDecimal temperature, DateTime instant) {
        this.temperature = temperature;
        this.instant = instant;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public String getISOTimestamp() {
        return instant.toISOString();
    }

    public static WeatherDatum fromJSON(JSONObject weatherDatumJSON) {
        BigDecimal temperature = new BigDecimal(weatherDatumJSON.getString("tempm"));

        JSONObject dateJson = weatherDatumJSON.getJSONObject("date");
        String year = dateJson.getString("year");
        String month = dateJson.getString("mon");
        String dayOfMonth = dateJson.getString("mday");
        String hour = dateJson.getString("hour");
        String minute = dateJson.getString("min");
        return new WeatherDatum(temperature, new DateTime(year, month, dayOfMonth, hour, minute));
    }
}
