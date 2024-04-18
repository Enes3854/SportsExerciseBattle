package com.seb.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Properties prop = new Properties();
                try (InputStream inputStream = DbUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
                    if (inputStream != null) {
                        prop.load(inputStream);
                        String driver = prop.getProperty("db.driver", "org.postgresql.Driver");
                        String url = "jdbc:postgresql://localhost:5432/SEB?sslmode=prefer&connect_timeout=10";
                        String user = "postgres";
                        String password = "mycode";

                        System.out.println("Connecting to database with URL: " + url);
                        Class.forName(driver);
                        connection = DriverManager.getConnection(url, user, password);
                    } else {
                        throw new SQLException("Properties file not found");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

}
