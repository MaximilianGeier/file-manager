package servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class Config implements ServletContextListener {
    public static Connection connection;

    public static void doUpdate(String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    public static ResultSet doSelect(String query) throws SQLException {
        ResultSet resultSet = null;
        Statement statement = null;
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        return resultSet;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            String url = getDatabaseURL();
            connection = DriverManager.getConnection(url.toString());
            Statement statement = connection.createStatement();
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDatabaseURL() throws IOException {
        File file = new File("/home/filemanager/database.properties");
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        return properties.getProperty("url");
    }

}