package com.equalexperts.weather1self.client;

import com.equalexperts.weather1self.client.response.TemperatureDatum;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OpenWeatherMapClientTest {

    @Test
    public void getsWeatherData() throws Exception {
        DateTime today = new DateTime();
        DateTime threeDaysBefore = today.minusDays(3);
        List<TemperatureDatum> weatherData = OpenWeatherMapClient.getWeatherData("Pune", "IN", threeDaysBefore, today, "hour");
        assertTrue(!weatherData.isEmpty());
        for (TemperatureDatum weatherDatum : weatherData) {
            assertNotNull(weatherDatum.getTemperature());
            assertNotNull(weatherDatum.getISOTimestamp());
            System.out.println("[ " + weatherDatum.getTemperature() + ", " + weatherDatum.getTemperature() + "]");
        }
    }

}