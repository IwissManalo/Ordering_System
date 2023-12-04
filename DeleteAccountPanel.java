//package FDSFinals;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.Statement;
//
//public class DeleteAccountPanel extends JPanel {
//    private JTextField idField;
//
//    public DeleteAccountPanel(JFrame panel){
//        QueryCommand q = new QueryCommand();
//        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
//
//        add(new JLabel(q.view()));
//
//        idField = new JTextField(20);
//
//        add(new JLabel("Which account do you wish to delete?"));
//        add(idField);
//
//        JButton submitButton = new JButton("Submit");
//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e){
//                submitButtonClicked();
//            }
//        });
//    }
//
//    private void submitButtonClicked(){
//        String iId = idField.getText();
//
//        QueryCommand qc = new QueryCommand();
//        String query = qc.delete(Integer.parseInt(iId));
//
//        connectToDatabase(query);
//    }
//
//    private void connectToDatabase(String query) {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/orderingsystem", "root", "");
//            Statement statement = connection.createStatement();
//            statement.executeUpdate(query);
//            connection.close();
//
//            JOptionPane.showMessageDialog(this, "Delete successful!");
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}
