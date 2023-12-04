package FDSFinals;

import javax.swing.*;


public class Main extends JFrame {

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("User Registration and Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JTabbedPane tabbedPane = new JTabbedPane();

            RegistrationPanel registrationPanel = new RegistrationPanel(frame);
            LoginPanel loginPanel = new LoginPanel(frame);
//            DeleteAccountPanel deleteAccountPanel = new DeleteAccountPanel(frame);

            tabbedPane.addTab("Registration", registrationPanel);
            tabbedPane.addTab("Login", loginPanel);
//            tabbedPane.addTab("Delete Account", deleteAccountPanel);

            frame.getContentPane().add(tabbedPane);
            frame.pack();
            frame.setSize(700, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}



