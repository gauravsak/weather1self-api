package com.equalexperts.weather1self.client.response.owm;

import com.equalexperts.weather1self.client.response.TemperatureDatum;
import com.equalexperts.weather1self.model.lib1self.Event;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.equalexperts.weather1self.model.lib1self.WeatherEventAttributes.*;

public class WeatherDatum implements TemperatureDatum {

    private BigDecimal temperature;
    private DateTime instant;

    private static final DateTimeFormatter ISO_FORMAT = ISODateTimeFormat.dateTime();
    private static final BigDecimal ONE_DEGREE_KELVIN = BigDecimal.valueOf(273.16);

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
        return ISO_FORMAT.print(instant);
    }

    public static WeatherDatum fromJSON(JSONObject weatherDatumJSON) {
        JSONObject weatherSummaryJSON = weatherDatumJSON.getJSONObject("main");
        BigDecimal temperatureInKelvin = BigDecimal.valueOf(weatherSummaryJSON.getDouble("temp"));
        BigDecimal temperature = temperatureInKelvin.subtract(ONE_DEGREE_KELVIN).setScale(2, BigDecimal.ROUND_HALF_UP);
        DateTime instant = new DateTime(weatherDatumJSON.getLong("dt") * 1000);
        return new WeatherDatum(temperature, instant);
    }

    @Override
    public Event to1SelfEvent() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(PROPERTY, getTemperature());
        return new Event(OBJECT_TAGS, ACTION_TAGS, properties, getISOTimestamp());
    }
}
