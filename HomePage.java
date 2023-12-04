package FDSFinals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomePage {
    private JFrame mainFrame;
    private JPanel headerPanel;
    private JPanel productPanel;
    private JPanel controlPanel;
    private Connection connection;

    public HomePage() {
        connectToDatabase(); // Connect to the database when creating the HomePage
        prepareGUI();
        populateProductPanel();  // Populate the productPanel with data from the database
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomePage homePage = new HomePage();
            homePage.showHomePage();
        });
    }

    private void connectToDatabase() {
        try {
            // Load the MySQL JDBC driver (no longer required in JDBC 4.0 and later)
            Class.forName("com.mysql.jdbc.Driver");

            // Replace these values with your actual database connection details
            String url = "jdbc:mysql://localhost:3306/orderingsystem";
            String user = "root";
            String password = "";

            // Establish the database connection
            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to the database");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareGUI() {
        mainFrame = new JFrame("Nics Bakes");
        mainFrame.setSize(800, 600);
        mainFrame.setLayout(new BorderLayout());

        // Create a header panel with GridBagLayout for precise layout control
        headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(Color.BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);  // Padding
        JLabel headerLabel = new JLabel("Welcome to Nics Bakes");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, gbc);

        // Create the productPanel and controlPanel
        productPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        productPanel.setBackground(Color.WHITE);

        controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(Color.GRAY);

        mainFrame.add(headerPanel, BorderLayout.NORTH);
        mainFrame.add(productPanel, BorderLayout.CENTER);
        mainFrame.add(controlPanel, BorderLayout.SOUTH);

        // Create a Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        controlPanel.add(logoutButton);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private void populateProductPanel() {
        try {
            // Query to retrieve product information
            String query = "SELECT name FROM products";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Populate the productPanel with product names
                while (resultSet.next()) {
                    String productName = resultSet.getString("name");
                    productPanel.add(new JLabel(productName, JLabel.CENTER));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving product information: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            mainFrame.dispose();
            JFrame loginFrame = new JFrame("Login Panel");
            LoginPanel loginPanel = new LoginPanel(loginFrame);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setContentPane(loginPanel);

            // Set the size of the LoginPanel frame to 800 by 600
            loginFrame.setSize(800, 600);

            loginPanel.showLoginPanel();
        }
    }


    void showHomePage() {
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}
