package com.equalexperts.weather1self.client;

import com.equalexperts.weather1self.client.response.wu.DateTime;
import com.equalexperts.weather1self.client.response.wu.WeatherDatum;
import com.equalexperts.weather1self.model.lib1self.Event;
import com.equalexperts.weather1self.model.lib1self.Stream;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class Lib1SelfClientTest {

    @Test
    public void createsNew1SelfStream() throws Exception {
        Stream stream = Lib1SelfClient.createStream();
        assertNotNull(stream);
        assertNotNull(stream.getId());
        assertNotNull(stream.getReadToken());
        assertNotNull(stream.getWriteToken());
    }

    @Test
    public void sendsEventBatchTo1Self() throws Exception {
        Stream stream = Lib1SelfClient.createStream();
        WeatherDatum firstWeatherDatum = new WeatherDatum(BigDecimal.ONE, new DateTime("2015", "03", "01", "02", "30"));
        WeatherDatum secondWeatherDatum = new WeatherDatum(BigDecimal.TEN, new DateTime("2015", "03", "02", "02", "30"));
        List<Event> events = Arrays.asList(firstWeatherDatum.to1SelfEvent(), secondWeatherDatum.to1SelfEvent());
        Lib1SelfClient.sendEventsBatch(events, stream);
    }

}