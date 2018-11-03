/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeeklyBusinessLog;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Sady
 */
public class MainFrame extends javax.swing.JFrame {

    private Customer selectedCustomer;
    private JFrame frame = new JFrame();
    private static String username, password;
    private static Connection connection = null;
    private static ArrayList<Customer> customerList = new ArrayList();
    private static String[] columnHeaders = {"Name", "Address", "Phone", "Income Date", "Account Type"};
    private static DefaultTableModel tableModel = new DefaultTableModel(columnHeaders, 0){
        //Disables cell editing by user
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
        this.setTitle("Massivemesh Weekly Business Log");
        centerWindow(this);
        
        connectToDatabase();
    }
    
    private void connectToDatabase() {
        try (Connection conn = DriverManager.getConnection(DatabaseCredentials.databaseUrl, username, password)) {
            connection = conn;
            populateCustomerList(conn);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
    
    private void populateCustomerList(Connection connection) {
        try {
            String query = "SELECT * FROM Customers";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);
            
            while (results.next()) {
                Address address = new Address(results.getString("street"), 
                                              results.getString("unit"), 
                                              results.getString("city"), 
                                              results.getString("state"), 
                                              results.getString("zip"));
                Customer customer = new Customer();
                customer.setId(results.getInt("id"));
                customer.setFirstName(results.getString("first_name"));
                customer.setLastName(results.getString("last_name"));
                customer.setBusinessName(results.getString("business_name"));
                customer.setAddress(address);
                customer.setPhoneNumber(results.getString("phone"));
                customer.setEmail(results.getString("email"));
                customer.setAccountType(results.getString("account_type"));
                customer.setSourceDetails(results.getString("source_details"));
                customer.setSourceSpecifics(results.getString("source_specifics"));
                customer.setSourceType(results.getString("source_type"));
                customer.setIntalledSvc(results.getString("installed_service"));
                customer.setIncomeDate(results.getInt("income_date"));
                customer.setSurveyDate(results.getInt("survey_date"));
                customer.setInstallDate(results.getInt("install_date"));
                customer.setHistory(results.getInt("history"));
                customer.setAddOns(results.getString("add_ons"));
                customer.setMetricStatus(results.getString("metric_status"));
                System.out.println(customer.toString());
                customerList.add(customer);
            }
            
            //Populate JTable with data
        tableModel.setRowCount(0);
         for (int i = 0; i < customerList.size(); i++) {
             String name = customerList.get(i).getFirstName() + " " + customerList.get(i).getLastName();
             String street = customerList.get(i).getAddress().getStreet();
             String unit = customerList.get(i).getAddress().getUnit();
             String city = customerList.get(i).getAddress().getCity();
             String state = customerList.get(i).getAddress().getState();
             String zip = customerList.get(i).getAddress().getZip();
             String address;
             if (unit == null || unit.isEmpty()) {
                 address = street + ", " + city + " " + state + ", " + zip;
             } else {
                 address = street + " " + unit + ", " + city + " " + state + ", " + zip;
             }
             
             String phone = customerList.get(i).getPhoneNumber();
             String phoneMask = "(###) ###-####";
             MaskFormatter maskFormatter = new MaskFormatter(phoneMask);
             maskFormatter.setValueContainsLiteralCharacters(false);
             Date incomeDate = Date.from(Instant.ofEpochSecond((long) customerList.get(i).getIncomeDate()));
             String accountType = customerList.get(i).getAccountType();
             Object[] data = {name, address, maskFormatter.valueToString(phone), formatDate(incomeDate), accountType};
             tableModel.addRow(data);
             //jTable.setRowSelectionInterval(selectedIndex, selectedIndex);
         }
         
         
         jTable.setModel(tableModel); 
        } catch (SQLException e) {
            System.out.println("Populate Customers Error: " + e);
        } catch (ParseException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            } 
        }
        
    }
    
    public void resizeColumnWidth(JTable table) {
    final TableColumnModel columnModel = table.getColumnModel();
    for (int column = 0; column < table.getColumnCount(); column++) {
        int width = 15; // Min width
        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component comp = table.prepareRenderer(renderer, row, column);
            width = Math.max(comp.getPreferredSize().width +1 , width);
        }
        if(width > 300)
            width=300;
        columnModel.getColumn(column).setPreferredWidth(width);
    }
}
    
    private String formatDate(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);
        String dateStr = dateFormat.format(date);
        return dateStr;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        addNewButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "First Name", "Last Name", "Address", "Incoming Date"
            }
        ));
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable);

        addNewButton.setText("Add New");
        addNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewButtonActionPerformed(evt);
            }
        });

        removeButton.setText("Remove");

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Run Report");

        jLabel1.setText("End Date:");

        jLabel2.setText("Start Date:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addNewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jTextField1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addNewButton)
                    .addComponent(exitButton)
                    .addComponent(refreshButton)
                    .addComponent(editButton)
                    .addComponent(removeButton))
                .addGap(43, 43, 43))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void addNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewButtonActionPerformed
        // TODO add your handling code here:
        String[] credentials = {username, password};
        CustomerEditor.main(credentials);
    }//GEN-LAST:event_addNewButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        if (selectedCustomer != null) {
            CustomerEditor customerEditor = new CustomerEditor(frame, true);
            customerEditor.setCustomer(selectedCustomer);
            customerEditor.setFieldsForCustomer(selectedCustomer);
            customerEditor.setVisible(true);
            customerEditor.setAlwaysOnTop(true);
            customerEditor.setUserCredentials(username, password);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a customer.");
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        // TODO add your handling code here:
        JTable target = (JTable)evt.getSource();
        int selectedRow = target.getSelectedRow();
        selectedCustomer = customerList.get(selectedRow);
        
    }//GEN-LAST:event_jTableMouseClicked

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        // TODO add your handling code here:
        customerList.clear();
        connectToDatabase();
    }//GEN-LAST:event_refreshButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
        
        //Set Credentials on Load
        username = args[0];
        password = args[1];
        //connectToDatabase();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables
}
