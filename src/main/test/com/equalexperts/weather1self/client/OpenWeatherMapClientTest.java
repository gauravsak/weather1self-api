package com.equalexperts.weather1self.client;

import com.equalexperts.weather1self.client.response.WeatherDatum;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class OpenWeatherMapClientTest {

    @Test
    public void getsWeatherData() throws Exception {
        OpenWeatherMapClient openWeatherMapClient = new OpenWeatherMapClient();
        DateTime today = new DateTime();
        DateTime yesterday = today.minusDays(1);
        List<WeatherDatum> weatherData = openWeatherMapClient.getWeatherData("Pune", "IN", yesterday, today, "hour");
        for (WeatherDatum weatherDatum : weatherData) {
            assertNotNull(weatherDatum.getTemperature());
            assertNotNull(weatherDatum.getISOTimestamp());
        }
    }

}