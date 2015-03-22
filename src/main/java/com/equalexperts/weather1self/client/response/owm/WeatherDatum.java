package com.equalexperts.weather1self.client.response.owm;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONObject;

import java.math.BigDecimal;

public class WeatherDatum {

    private BigDecimal temperature;
    private DateTime instant;

    private static final DateTimeFormatter ISO_FORMAT = ISODateTimeFormat.dateTime();
    private static final BigDecimal ONE_DEGREE_KELVIN = BigDecimal.valueOf(273.16);

    public WeatherDatum(BigDecimal temperature, DateTime instant) {
        this.temperature = temperature;
        this.instant = instant;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

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
}
