package com.equalexperts.weather1self.client;

import com.equalexperts.weather1self.client.response.TemperatureDatum;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class OpenWeatherMapClientTest {

    @Test
    public void getsWeatherData() throws Exception {
        DateTime today = new DateTime();
        DateTime yesterday = today.minusDays(1);
        List<TemperatureDatum> weatherData = OpenWeatherMapClient.getWeatherData("Pune", "IN", yesterday, today, "hour");
        for (TemperatureDatum weatherDatum : weatherData) {
            assertNotNull(weatherDatum.getTemperature());
            assertNotNull(weatherDatum.getISOTimestamp());
        }
    }

}