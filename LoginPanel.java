package FDSFinals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame loginFrame;

    public LoginPanel(JFrame loginFrame) {
        this.loginFrame = loginFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240)); // Light gray background

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPanel.setBackground(new Color(240, 240, 240));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        add(inputPanel, BorderLayout.CENTER);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(loginButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void performLogin() {
        char[] enteredPassword = passwordField.getPassword();

        QueryCommand qc = new QueryCommand();
        String query = qc.select(usernameField.getText(), new String(enteredPassword));

        // Assuming the role is retrieved from the database
        String userRole = getUserRoleFromDatabase(usernameField.getText());

        if (validateLogin(query)) {
            JOptionPane.showMessageDialog(this, "Login successful!");

            if ("customer".equalsIgnoreCase(userRole)) {
                HomePage homePage = new HomePage();
                homePage.showHomePage();
            } else if ("admin".equalsIgnoreCase(userRole)) {
                AdminHomePage adminHomePage = new AdminHomePage();
                adminHomePage.showAdminHomePage();
            }

            loginFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getUserRoleFromDatabase(String username) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");

            QueryCommand qc = new QueryCommand();
            PreparedStatement preparedStatement = qc.prepareSelectRoleStatement(connection, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Assuming 'role' is the column name for the role in your 'role' table
            String userRole = resultSet.next() ? resultSet.getString("role") : "";

            resultSet.close();
            connection.close();

            return userRole;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    private boolean validateLogin(String query) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");

            QueryCommand qc = new QueryCommand();
            PreparedStatement preparedStatement = qc.prepareSelectStatement(connection, usernameField.getText(), new String(passwordField.getPassword()));

            ResultSet resultSet = preparedStatement.executeQuery();

            boolean isValid = resultSet.next();

            resultSet.close();
            connection.close();

            return isValid;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void showLoginPanel() {
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
}