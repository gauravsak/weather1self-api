package com.equalexperts.weather1self.client;

import com.equalexperts.weather1self.model.lib1self.Event;
import com.equalexperts.weather1self.model.lib1self.Stream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

public class Lib1SelfClient {

    private static final String API_BASE_URL = "sandbox.1self.co/v1";
    private static final String APP_ID = "app-id-36205833a524ce5df5b4fe3773efe7d6";
    private static final String APP_SECRET = "app-secret-f06c0d756376e27bf24c980e540441e7672b1186" +
            "fbfaeb943cd30ae01d10e3c1";

    public static Stream createStream() throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI createStreamURI = new URIBuilder()
                .setScheme("http")
                .setHost(API_BASE_URL)
                .setPath("/streams")
                .build();
        HttpPost createStreamPOST = new HttpPost(createStreamURI);
        createStreamPOST.addHeader(HttpHeaders.AUTHORIZATION, APP_ID + ":" + APP_SECRET);
        System.out.println(createStreamPOST.getURI());
        Stream stream = null;
        try (CloseableHttpResponse response = httpClient.execute(createStreamPOST)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK_200) {
                HttpEntity responseEntity = response.getEntity();
                JSONObject weatherResponseJson = new JSONObject(EntityUtils.toString(responseEntity));
                stream = Stream.fromJSON(weatherResponseJson);
                EntityUtils.consume(responseEntity);
            }
        }
        return stream;
    }

    public static void sendEventsBatch(List<Event> events, Stream stream) throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI eventsBatchURI = new URIBuilder()
                .setScheme("http")
                .setHost(API_BASE_URL)
                .setPath("/streams/" + stream.getId() + "/events/batch")
                .build();
        HttpPost eventsBatchPOST = new HttpPost(eventsBatchURI);
        eventsBatchPOST.addHeader(HttpHeaders.AUTHORIZATION, stream.getWriteToken());
        eventsBatchPOST.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());

        String eventsJSONString = getEventsJSONString(events);
        eventsBatchPOST.setEntity(new StringEntity(eventsJSONString, Charset.forName("UTF-8")));
        System.out.println(eventsBatchPOST.getURI());
        try (CloseableHttpResponse response = httpClient.execute(eventsBatchPOST)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK_200) {
                HttpEntity responseEntity = response.getEntity();
                System.out.println(EntityUtils.toString(responseEntity));
                EntityUtils.consume(responseEntity);
            }
        }
    }

    private static String getEventsJSONString(List<Event> events) {
        StringBuilder eventsJsonSB = new StringBuilder("[");
        for(Event event : events) {
            eventsJsonSB.append(Event.toJSON(event).toString());
        }
        eventsJsonSB.append("]");
        return eventsJsonSB.toString();
    }

}
