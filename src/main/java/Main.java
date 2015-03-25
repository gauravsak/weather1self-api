import com.equalexperts.weather1self.client.Lib1SelfClient;
import com.equalexperts.weather1self.client.OpenWeatherMapClient;
import com.equalexperts.weather1self.client.WeatherUndergroundClient;
import com.equalexperts.weather1self.client.response.TemperatureDatum;
import com.equalexperts.weather1self.model.lib1self.Event;
import com.equalexperts.weather1self.model.lib1self.Stream;
import com.equalexperts.weather1self.model.lib1self.WeatherSource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.joda.time.DateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.equalexperts.weather1self.model.lib1self.WeatherEventAttributes.*;

public class Main extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if(req.getQueryString() == null) {
            showHome(req, resp);
            return;
        }

        Map<String, String> queryParams = getQueryParams(req.getQueryString());
        String city = queryParams.get("city");
        String country = queryParams.get("country");
        WeatherSource weatherSource = determineWeatherSource(req.getHeader("x-weather-source"));

        DateTime now = DateTime.now();
        DateTime sevenDaysBefore = now.minusDays(7);
        try {
            Stream stream = Lib1SelfClient.createStream();
            List<TemperatureDatum> temperatureData;
            switch (weatherSource) {
                case OWM:
                    temperatureData = OpenWeatherMapClient.getWeatherData(city, country,
                            sevenDaysBefore, now, "hour");
                    break;
                case WU:
                    temperatureData = WeatherUndergroundClient.getWeatherData(city, country, sevenDaysBefore, now);
                    break;
                default:
                    temperatureData = WeatherUndergroundClient.getWeatherData(city, country, sevenDaysBefore, now);
            }
            List<Event> events = convertTo1SelfEvents(temperatureData);
            Lib1SelfClient.sendEventsBatch(events, stream);
            PrintWriter out = resp.getWriter();
            out.print("{\"chartUri\":\"" + Lib1SelfClient.getEventsChartURI(stream, getCommaSeparatedListString(OBJECT_TAGS), getCommaSeparatedListString(ACTION_TAGS), "mean", PROPERTY) + "\"}");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private List<Event> convertTo1SelfEvents(List<TemperatureDatum> temperatureData) {
        List<Event> events = new ArrayList<>();
        for(TemperatureDatum temperatureDatum : temperatureData) {
            events.add(temperatureDatum.to1SelfEvent());
        }
        return events;
    }

    private static WeatherSource determineWeatherSource(String weatherSourceWebsite) {
        for(WeatherSource weatherSource : WeatherSource.values()) {
            if(weatherSource.getWeatherSourceWebsite().equals(weatherSourceWebsite)) {
                return weatherSource;
            }
        }
        return WeatherSource.WU;
    }

    private static Map<String, String> getQueryParams(String queryString) {
        String[] queryParamNameValuePairs = queryString.split("&");
        Map<String, String> queryParams = new HashMap<>();
        for (String queryParamNameValuePair : queryParamNameValuePairs) {
            String[] queryParamNameValue = queryParamNameValuePair.split("=");
            queryParams.put(queryParamNameValue[0], queryParamNameValue[1]);
        }
        return queryParams;
    }

    private static String getCommaSeparatedListString(List<String> list) {
        StringBuilder commaSeparatedListString = new StringBuilder();
        for(String string : list) {
            commaSeparatedListString.append(string).append(",");
        }
        return commaSeparatedListString.substring(0, commaSeparatedListString.length() - 1);
    }

    private void showHome(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().print("Hello from Java!");
    }

    public static void main(String[] args) throws Exception {
        String port = System.getenv("PORT") == null ? "9000" : System.getenv("PORT");
        Server server = new Server(Integer.valueOf(port));

        ServletContextHandler defaultContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        defaultContext.setContextPath("/");
        defaultContext.addServlet(new ServletHolder(new Main()), "/*");
        server.setHandler(defaultContext);

        server.start();
        server.join();
    }
}
