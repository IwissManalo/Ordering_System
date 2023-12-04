package FDSFinals;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminHomePage {
    private JFrame adminFrame;
    private JTabbedPane tabbedPane;
    private JPanel productsPanel;
    private JPanel accountsPanel;
    private JTextArea productTextArea;
    private JTextField deleteProductNameField;
    private JTextField deleteAccountUsernameField;
    private JButton logoutButton;


    public AdminHomePage() {
        prepareAdminGUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminHomePage adminHomePage = new AdminHomePage();
            adminHomePage.showAdminHomePage();
        });
    }

    private void prepareAdminGUI() {
        adminFrame = new JFrame("Admin Panel");
        adminFrame.setSize(800, 400);
        adminFrame.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        productsPanel = ProductsPanel();
        accountsPanel = ManageAccountPanel();

        tabbedPane.addTab("List of Products", productsPanel);
        tabbedPane.addTab("Manage Accounts", accountsPanel);

        adminFrame.add(tabbedPane, BorderLayout.CENTER);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel ProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Components for Products panel
        productTextArea = new JTextArea();
        productTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(productTextArea);

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 0) {
                    viewProducts();
                }
            }
        });

        JPanel viewPanel = new JPanel(new BorderLayout());
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProductFrame();
                adminFrame.dispose();
            }
        });

        JButton deleteProductButton = new JButton("Delete Product");
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteProductFrame();
                adminFrame.dispose();
            }
        });

        JPanel addPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        addPanel.add(addProductButton);
        addPanel.add(deleteProductButton);

        panel.add(viewPanel, BorderLayout.CENTER);
        panel.add(addPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel ManageAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Components for Accounts panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel usernameLabel = new JLabel("Username:");
        deleteAccountUsernameField = new JTextField(20);
        JButton deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount(deleteAccountUsernameField.getText());

            }
        });

        inputPanel.add(usernameLabel);
        inputPanel.add(deleteAccountUsernameField);
        inputPanel.add(deleteAccountButton);

        // Logout button at the bottom right
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(logoutButton, BorderLayout.SOUTH); // Logout button at the bottom

        return panel;
    }


    private void DeleteProductFrame() {
        JFrame deleteProductFrame = new JFrame("Delete Product");
        deleteProductFrame.setSize(400, 150);
        deleteProductFrame.setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Product Name to Delete:");
        deleteProductNameField = new JTextField(20);

        JButton confirmButton = new JButton("Delete Product");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct(deleteProductNameField.getText());
                deleteProductFrame.dispose();
            }
        });

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProductFrame.dispose();
                adminFrame.setVisible(true);
            }
        });

        deleteProductFrame.add(nameLabel);
        deleteProductFrame.add(deleteProductNameField);
        deleteProductFrame.add(confirmButton);
        deleteProductFrame.add(goBackButton);

        deleteProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteProductFrame.setVisible(true);
    }

    private void AddProductFrame() {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(400, 300);
        addProductFrame.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel("Product Name:");
        JLabel priceLabel = new JLabel("Product Price:");
        JLabel quantityLabel = new JLabel("Product Quantity:");

        JTextField productNameField = new JTextField();
        JTextField productPriceField = new JTextField();
        JTextField productQuantityField = new JTextField();

        JButton confirmButton = new JButton("Add Product");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct(productNameField.getText(),
                        Double.parseDouble(productPriceField.getText()),
                        Integer.parseInt(productQuantityField.getText()));
                showAdminHomePage();
                addProductFrame.dispose();
            }
        });

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProductFrame.dispose();
                adminFrame.setVisible(true);
            }
        });

        addProductFrame.add(nameLabel);
        addProductFrame.add(productNameField);
        addProductFrame.add(priceLabel);
        addProductFrame.add(productPriceField);
        addProductFrame.add(quantityLabel);
        addProductFrame.add(productQuantityField);
        addProductFrame.add(confirmButton);
        addProductFrame.add(goBackButton);

        addProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addProductFrame.setVisible(true);
    }

    private void addProduct(String name, double price, int quantity) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");

            String insertQuery = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, quantity);
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(adminFrame, "Product added successfully!");

            preparedStatement.close();
            connection.close();
        } catch (SQLException | NumberFormatException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(adminFrame, "Error adding product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewProducts() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");

            String query = "SELECT id, name, price, quantity FROM products";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                StringBuilder productInfo = new StringBuilder("Product List:\n");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("quantity");

                    productInfo.append(String.format("ID: %d\n   Name: %s\n   Price: $%.2f\n   Quantity: %d\n\n", id, name, price, quantity));
                }

                productTextArea.setText(productInfo.toString());
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(adminFrame, "Error viewing products: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct(String productName) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");

            String deleteQuery = "DELETE FROM products WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, productName);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(adminFrame, "Product deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(adminFrame, "Product not found", "Delete Product", JOptionPane.WARNING_MESSAGE);
                }
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(adminFrame, "Error deleting product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAccount(String username) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");

            String deleteQuery = "DELETE FROM account WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, username);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(adminFrame, "Account deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(adminFrame, "Account not found", "Delete Account", JOptionPane.WARNING_MESSAGE);
                }
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(adminFrame, "Error deleting account: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(adminFrame, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            adminFrame.dispose();
            JFrame loginFrame = new JFrame("Login Panel");
            LoginPanel loginPanel = new LoginPanel(loginFrame);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setContentPane(loginPanel);

            // Set the size of the LoginPanel frame to 800 by 600
            loginFrame.setSize(800, 600);
            loginPanel.showLoginPanel();
        }
    }


    void showAdminHomePage() {
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setVisible(true);
    }
}
