package com.equalexperts.weather1self;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class GetTemperatureChartTest {

    @Test
    public void getsTemperatureChart() throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost("http://weather1self-api.heroku.com/");
        HttpPost httpPost = new HttpPost("http://localhost:9000?city=Pune&country=IN");
        httpPost.addHeader("x-weather-source", "wunderground.com");

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity));
            EntityUtils.consume(entity);
        }

    }
}
