package at.fhtw.persistence;

import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Setter
public class DBConnector {
    private final String host = "localhost";
    private final int port = 5432;
    private final String username = "root";
    private final String password = "root";

    private String database;

    public void testConnect() {
        setDatabase("root");
        String url = getUrl();

        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);

        try (Connection connection = DriverManager.getConnection(url, properties)) {
            System.out.println(url + " connected!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUrl() {
        return new StringBuilder("jdbc:postgresql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(database)
                .toString();
    }

    public ResultSet sendQuery(String query) {
        try (Connection connection = this.getConnection()) {
            return connection.prepareStatement(query).executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        setDatabase("root");
        String url = getUrl();

        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        try {
            return DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
