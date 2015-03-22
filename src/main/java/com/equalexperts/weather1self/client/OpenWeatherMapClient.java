package com.equalexperts.weather1self.client;

import com.equalexperts.weather1self.client.response.TemperatureDatum;
import com.equalexperts.weather1self.client.response.owm.WeatherResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class OpenWeatherMapClient {

    private static final String API_BASE_URL = "api.openweathermap.org/data/2.5";
    private static final String API_KEY = "a19d564d7f6db6df2c4d18a3c218131d";

    public static List<TemperatureDatum> getWeatherData(String city, String country, DateTime fromInstant, DateTime toInstant, String frequency)
            throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI weatherHistoryURI = new URIBuilder()
                .setScheme("http")
                .setHost(API_BASE_URL)
                .setPath("/history/city")
                .addParameter("q", getCityAndCountryParam(city, country))
                .addParameter("start", getEpoch(fromInstant).toString())
                .addParameter("end", getEpoch(toInstant).toString())
                .addParameter("type", frequency)
                .build();
        HttpGet weatherDataGET = new HttpGet(weatherHistoryURI);
        weatherDataGET.setHeader("x-api-key", API_KEY);
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
        return weatherResponse != null ? weatherResponse.getWeatherData() : Collections.<TemperatureDatum>emptyList();
    }

    private static String getCityAndCountryParam(String city, String country) {
        return city + ", " + country.toLowerCase(Locale.ENGLISH);
    }

    private static Long getEpoch(DateTime instant) {
        return instant.getMillis() / 1000;
    }
}
