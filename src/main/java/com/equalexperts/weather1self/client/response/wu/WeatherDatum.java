package com.equalexperts.weather1self.client.response.wu;

import com.equalexperts.weather1self.client.response.TemperatureDatum;
import com.equalexperts.weather1self.model.lib1self.Event;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.equalexperts.weather1self.model.lib1self.WeatherEventAttributes.*;

public class WeatherDatum implements TemperatureDatum {

    private BigDecimal temperature;
    private DateTime instant;

    public WeatherDatum(BigDecimal temperature, DateTime instant) {
        this.temperature = temperature;
        this.instant = instant;
    }

    @Override
    public BigDecimal getTemperature() {
        return temperature;
    }

    @Override
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

    @Override
    public Event to1SelfEvent() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(PROPERTY, getTemperature());
        return new Event(OBJECT_TAGS, ACTION_TAGS, properties, getISOTimestamp());
    }
}
