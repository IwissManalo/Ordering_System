package FDSFinals;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RegistrationPanel extends JPanel {
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField addressField;

    public RegistrationPanel(JFrame parent) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        nameField = new JTextField(20);
        usernameField = new JTextField(20);
        passwordField = new JTextField(20);
        addressField = new JTextField(20);

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Address:"));
        add(addressField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitButtonClicked();
            }
        });

        add(submitButton);
    }

    private void submitButtonClicked() {
        String iName = nameField.getText();
        String iUName = usernameField.getText();
        String iPass = passwordField.getText();
        String iAdd = addressField.getText();


        QueryCommand qc = new QueryCommand();

        try {
            connect connect = new connect();
            PreparedStatement preparedStatement = qc.prepareInsertStatement(connect.getConnection(), iName, iUName, iPass, iAdd, "customer");
            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}


