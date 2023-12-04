package FDSFinals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QueryCommand {
    String name;
    String username;
    String password;
    String address;
    String role = "customer";
    int id;

    public String insert(String name, String username, String password, String address) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;

        return "INSERT INTO account (name, username, passwords, address, role) VALUES (?, ?, ?, ?, ?)";
    }

    public String delete(int id) {
        this.id = id;

        return "DELETE FROM account WHERE id = ?";
    }

    public String view() {
        return "SELECT * FROM account";
    }

    public PreparedStatement prepareInsertStatement(Connection connection, String name, String username, String password, String address, String role) {
        try {
            String query = insert(name, username, password, address);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, role);
            return preparedStatement;
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement prepareDeleteStatement(Connection connection, int id) {
        try {
            String query = delete(id);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement;
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }

    public PreparedStatement prepareSelectStatement(Connection connection, String username, String password) {
        try {
            String query = select(username, password);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            return preparedStatement;
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return null;
        }
    }

    public String select(String username, String password) {
        return "SELECT * FROM account WHERE username = ? AND passwords = ?";
    }

    public PreparedStatement prepareSelectRoleStatement(Connection connection, String username) throws SQLException, SQLException {
        // Assuming 'role' is the column name for the role in your 'role' table
        String query = "SELECT role FROM account WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        return preparedStatement;
    }
}