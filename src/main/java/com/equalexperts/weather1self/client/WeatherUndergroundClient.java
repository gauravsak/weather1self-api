package com.equalexperts.weather1self.client;

import com.equalexperts.weather1self.client.response.TemperatureDatum;
import com.equalexperts.weather1self.client.response.wu.WeatherDatum;
import com.equalexperts.weather1self.client.response.wu.WeatherResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WeatherUndergroundClient {

    private static final String API_KEY = "d9be903ff6644e14";
    private static final String API_BASE_URL = "api.wunderground.com/api/" + API_KEY;

    public static List<TemperatureDatum> getWeatherData(String city, String country, DateTime fromInstant, DateTime toInstant)
            throws URISyntaxException, IOException {
        DateTime currentInstant = fromInstant;
        DateTimeFormatter yyyyMMddFormat = DateTimeFormat.forPattern("yyyyMMdd");
        List<TemperatureDatum> weatherDataForAllDays = new ArrayList<>();
        while (currentInstant.isBefore(toInstant)) {
            String dateParam = yyyyMMddFormat.print(currentInstant);
            List<WeatherDatum> weatherData = weatherFor(dateParam, city, country);
            weatherDataForAllDays.addAll(weatherData);
            currentInstant = currentInstant.plusDays(1);
        }
        return weatherDataForAllDays;
    }

    private static List<WeatherDatum> weatherFor(String yyyyMMdd, String city, String country)
            throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI weatherHistoryURI = new URIBuilder()
                .setScheme("http")
                .setHost(API_BASE_URL)
                .setPath("/history_" + yyyyMMdd + "/q/" + country + "/" + city + ".json")
                .build();
        HttpGet weatherDataGET = new HttpGet(weatherHistoryURI);
        System.out.println(weatherDataGET.getURI());
        WeatherResponse weatherResponse = null;
        try (CloseableHttpResponse response = httpClient.execute(weatherDataGET)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK_200) {
                HttpEntity responseEntity = response.getEntity();
                JSONObject weatherResponseJson = new JSONObject(EntityUtils.toString(responseEntity));
                weatherResponse = WeatherResponse.fromJSON(weatherResponseJson);
                EntityUtils.consume(responseEntity);
            }
        }
        return weatherResponse != null ? weatherResponse.getWeatherData() : Collections.<WeatherDatum>emptyList();
    }
}
