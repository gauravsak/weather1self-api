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
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class Main extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getRequestURI().endsWith("/db")) {
            showDatabase(req, resp);
        } else {
            showHome(req, resp);
        }
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
