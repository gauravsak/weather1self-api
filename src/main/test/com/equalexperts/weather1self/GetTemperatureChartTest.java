package com.equalexperts.weather1self;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

public class GetTemperatureChartTest {

    @Test
    public void getsTemperatureChart() throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost("http://weather1self-api.heroku.com/");
        HttpPost httpPost = new HttpPost("http://localhost:9000");
        JSONObject streamJSON = new JSONObject();
        streamJSON.put("streamId", "some stream Id");
        streamJSON.put("readToken", "some readToken");
        streamJSON.put("writeToken", "some writeToken");
        httpPost.setEntity(new StringEntity(streamJSON.toString()));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            System.out.println(response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

    }
}
