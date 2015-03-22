import com.equalexperts.weather1self.client.Lib1SelfClient;
import com.equalexperts.weather1self.model.lib1self.Stream;
import com.equalexperts.weather1self.model.lib1self.WeatherSource;
import com.equalexperts.weather1self.server.PostHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Main extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

//        if (req.getRequestURI().endsWith("/db")) {
//            showDatabase(req, resp);
//        } else {
//            showHome(req, resp);
//        }

        Map<String, String> queryParams = getQueryParams(req.getQueryString());
        String city = queryParams.get("city");
        String country = queryParams.get("country");
        WeatherSource weatherSource = determineWeatherSource(req.getHeader("x-weather-source"));

        Stream stream = null;
        try {
            stream = Lib1SelfClient.createStream();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        PrintWriter out = resp.getWriter();
        out.print("success : " + stream);
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

    private void showHome(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.getWriter().print("Hello from Java!");
    }

    private void showDatabase(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Connection connection = null;
        try {
            connection = getConnection();

            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

            String out = "Hello!\n";
            while (rs.next()) {
                out += "Read from DB: " + rs.getTimestamp("tick") + "\n";
            }

            resp.getWriter().print(out);
        } catch (Exception e) {
            resp.getWriter().print("There was an error: " + e.getMessage());
        } finally {
            if (connection != null) try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }

    private Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        int port = dbUri.getPort();

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    public static void main(String[] args) throws Exception {
        String port = System.getenv("PORT") == null ? "9000" : System.getenv("PORT");
        Server server = new Server(Integer.valueOf(port));

        ServletContextHandler defaultContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        defaultContext.setContextPath("/");
        defaultContext.addServlet(new ServletHolder(new Main()), "/*");

        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(new PostHandler());
        handlers.addHandler(defaultContext);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
