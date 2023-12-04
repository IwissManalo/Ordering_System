package FDSFinals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;

class connect {
    public Connection getConnection() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Establish a connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");

            // Return the connection object
            return connection;
        } catch (Exception e) {
            // Handle the exception appropriately
            throw new RuntimeException("Error connecting to database", e);
        }
    }

    void Connection(String query) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to database: " + e.getMessage(), e);
        }
    }
}