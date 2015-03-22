package com.equalexperts.weather1self;

import com.equalexperts.weather1self.model.lib1self.Stream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
        HttpPost httpPost = new HttpPost("http://localhost:9000");
        Stream stream = new Stream("some stream Id", "some readToken", "some writeToken");
        httpPost.setEntity(new StringEntity(Stream.toJSON(stream).toString()));
        httpPost.addHeader("x-weather-source", "wunderground.com");

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity));
            EntityUtils.consume(entity);
        }

    }
}
