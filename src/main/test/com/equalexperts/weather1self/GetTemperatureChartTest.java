package com.equalexperts.weather1self;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetTemperatureChartTest {

    @Test
    public void getsTemperatureChart() throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://weather1self-api.heroku.com/");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("streamId", "some stream ID"));
        nameValuePairs.add(new BasicNameValuePair("readToken", "some read token"));
        nameValuePairs.add(new BasicNameValuePair("writeToken", "some write token"));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

    }
}
