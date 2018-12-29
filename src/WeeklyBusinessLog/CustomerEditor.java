/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeeklyBusinessLog;

import com.mysql.cj.util.StringUtils;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;


/**
 *
 * @author Sady
 */
public class CustomerEditor extends javax.swing.JDialog {
    public boolean editingCustomer = false;
    private JFrame frame = new JFrame();
    private InstallAddOnsDialog addOnDialog = new InstallAddOnsDialog(frame, true);
    private HistoryDialog historyDialog = new HistoryDialog(frame, true);
    public ArrayList<String> selectedAddons = new ArrayList();
    public Customer currentCustomer = new Customer();
    private ButtonGroup accountTypeGroup = new ButtonGroup();
    private Customer selectedCustomer;
    private Connection connection;
    private static String username, password;
    private PlainDocument document;
    private String phoneMask = "(###) ###-####";
    private MaskFormatter phoneMaskFormatter, zipMaskFormatter;
    /**
     * Creates new form CustomerEditor
     * @param parent
     * @param modal
     */
    public CustomerEditor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle("Customer Editor");
        initComponents();
        document = (PlainDocument) phoneField.getDocument();
        document.setDocumentFilter(new DigitFilter());
        
        //Mask Formatters
        try {
            phoneMaskFormatter = new MaskFormatter(phoneMask);
            phoneMaskFormatter.setPlaceholderCharacter(' ');
            DefaultFormatterFactory phoneFormatterFactory = new DefaultFormatterFactory(phoneMaskFormatter);
            phoneField.setFormatterFactory(phoneFormatterFactory);
            
            zipMaskFormatter = new MaskFormatter("#####");
            zipMaskFormatter.setPlaceholderCharacter(' ');
            DefaultFormatterFactory zipFormatterFactory = new DefaultFormatterFactory(zipMaskFormatter);
            zipField.setFormatterFactory(zipFormatterFactory);
        } catch (ParseException ex) {
            Logger.getLogger(CustomerEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        accountTypeGroup.add(residentialRadioButton);
        accountTypeGroup.add(businessRadioButton);
        centerWindow(this);

        if (editingCustomer && selectedCustomer != null) {
            setFieldsForCustomer(selectedCustomer);
        }
    }
    
    public void setCustomer(Customer customer) {
        selectedCustomer = customer;
        editingCustomer = true;
    }
    
    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
    
    private void uploadNewCustomerToDatabase(Customer customer) { 
        try {
            connection = DriverManager.getConnection(DatabaseCredentials.databaseUrl, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into Customers (income_date, first_name, last_name, business_name, street, unit, city, state, zip, phone, email, "
                    + "account_type, source_details, source_specifics, source_type, installed_service, survey_date, install_date, add_ons, metric_status)" + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, customer.getIncomeDate());
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.setString(4, customer.getBusinessName());
            preparedStatement.setString(5, customer.getAddress().getStreet());
            preparedStatement.setString(6, customer.getAddress().getUnit());
            preparedStatement.setString(7, customer.getAddress().getCity());
            preparedStatement.setString(8, customer.getAddress().getState());
            preparedStatement.setString(9, customer.getAddress().getZip());
            preparedStatement.setString(10, customer.getPhoneNumber());
            preparedStatement.setString(11, customer.getEmail());
            
            preparedStatement.setString(12, customer.getAccountType());
            preparedStatement.setString(13, customer.getSourceDetails());
            preparedStatement.setString(14, customer.getSourceSpecifics());
            preparedStatement.setString(15, customer.getSourceType());
            preparedStatement.setString(16, customer.getIntalledSvc());
            preparedStatement.setInt(17, customer.getSurveyDate());
            preparedStatement.setInt(18, customer.getInstallDate());
            preparedStatement.setString(19, customer.getAddOns());
            preparedStatement.setString(20, customer.getMetricStatus());
            
            preparedStatement.execute();
            System.out.println("Customer Inserted into Database Successfully");
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }
    
    private void uploadCustomer(Customer customer, boolean isNew) {
        try {
            System.out.println(password);
            connection = DriverManager.getConnection(DatabaseCredentials.databaseUrl, username, password);
            PreparedStatement preparedStatement;
            
            if (isNew) {
                preparedStatement = connection.prepareStatement("INSERT into Customers (income_date, first_name, last_name, business_name, street, unit, city, state, zip, phone, email, "
                    + "account_type, source_details, source_specifics, source_type, installed_service, survey_date, install_date, add_ons, metric_status)" + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            } else {
                preparedStatement = connection.prepareStatement("UPDATE Customers set income_date = ?, first_name = ?, last_name = ?, business_name = ?, street = ?, unit = ?, city = ?, state = ?, zip = ?, phone = ?, email = ?,"
                        + "account_type = ?, source_details = ?, source_specifics = ?, source_type = ?, installed_service = ?, survey_date = ?, install_date = ?, add_ons = ?, metric_status = ? WHERE id = ?");
                preparedStatement.setInt(21, customer.getId());
            }
            
            preparedStatement.setInt(1, customer.getIncomeDate());
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.setString(4, customer.getBusinessName());
            preparedStatement.setString(5, customer.getAddress().getStreet());
            preparedStatement.setString(6, customer.getAddress().getUnit());
            preparedStatement.setString(7, customer.getAddress().getCity());
            preparedStatement.setString(8, customer.getAddress().getState());
            preparedStatement.setString(9, customer.getAddress().getZip());
            preparedStatement.setString(10, customer.getPhoneNumber());
            preparedStatement.setString(11, customer.getEmail());
            
            preparedStatement.setString(12, customer.getAccountType());
            preparedStatement.setString(13, customer.getSourceDetails());
            preparedStatement.setString(14, customer.getSourceSpecifics());
            preparedStatement.setString(15, customer.getSourceType());
            preparedStatement.setString(16, customer.getIntalledSvc());
            preparedStatement.setInt(17, customer.getSurveyDate());
            preparedStatement.setInt(18, customer.getInstallDate());
            preparedStatement.setString(19, customer.getAddOns());
            preparedStatement.setString(20, customer.getMetricStatus());
            
            preparedStatement.execute();
            System.out.println("Customer Updated/Insterted into Database Successfully");
            System.out.println(customer.toString());
        } catch (SQLException ex) {
            System.out.println(ex);
            String errorMessage;
            if (ex.getLocalizedMessage().contains("Access denied for user") && ex.getLocalizedMessage().contains("using password: NO")) {
                errorMessage = "Access denied for " + username + System.lineSeparator() + "Incorrect or expired username and/or password.";
            } else {
                errorMessage = ex.getLocalizedMessage();
            }
            JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }
    
    private Customer createCustomer(int id) {
        Address address = new Address(streetField.getText(), 
                                      unitField.getText(), 
                                      cityField.getText(), 
                                      stateField.getText(), 
                                      zipField.getText());
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstNameField.getText());
        customer.setLastName(lastNameField.getText());
        customer.setBusinessName(businessNameField.getText());
        customer.setAddress(address);
        customer.setPhoneNumber(phoneField.getText().replace("(", "").replace(")", "").replace("-", "").replace(" ", ""));
        customer.setEmail(emailField.getText());
        
        //Acount Specifics
        if (residentialRadioButton.isSelected()) {
            customer.setAccountType("Residential");
        } else if (businessRadioButton.isSelected()) {
            customer.setAccountType("Business");
        }
        
        customer.setSourceDetails(sourceDetailComboBox.getSelectedItem().toString());
        customer.setSourceSpecifics(specificsTextArea.getText());
        customer.setSourceType(sourceComboBox.getSelectedItem().toString());
        customer.setIntalledSvc(installedSvcComboBox.getSelectedItem().toString());
        
        //Dates
        int incomeDate = createDateFromComboBoxes(incomeMonthComboBox, incomeDayComboBox, incomeYearComboBox);
        int surveyDate = createDateFromComboBoxes(surveyMonthComboBox, surveyDayComboBox, surveyYearComboBox);
        int installDate = createDateFromComboBoxes(installMonthComboBox, installDayComboBox, installYearComboBox);
        customer.setIncomeDate(incomeDate);
        customer.setSurveyDate(surveyDate);
        customer.setInstallDate(installDate);
        
        //customer.setHistory(results.getInt("history"));
        customer.setAddOns(parseAddOns());
        customer.setMetricStatus(statusComboBox.getSelectedItem().toString());
        
        return customer;
    }
    
    private int createDateFromComboBoxes(JComboBox monthBox, JComboBox dayBox, JComboBox yearBox) {
        int month = monthBox.getSelectedIndex();
        int day = dayBox.getSelectedIndex();
        int year = yearBox.getSelectedIndex();
        
        if (month == 0 && day == 0 && year == 0) {
            return 0;
        }

        LocalDate localDate = LocalDate.of(Integer.parseInt(yearBox.getSelectedItem().toString()), monthBox.getSelectedIndex(), dayBox.getSelectedIndex());
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        Date date = Date.from(zonedDateTime.toInstant());
        return (int) date.toInstant().getEpochSecond();
    }
    
    
    
    private String parseAddOns() {
        StringBuilder addons = new StringBuilder();
        selectedAddons.forEach((item) -> {
            addons.append(item).append(",");
        });
        return addons.toString();
        
    }
    
    public void setFieldsForCustomer(Customer customer) {
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        streetField.setText(customer.getAddress().getStreet());
        unitField.setText(customer.getAddress().getUnit());
        cityField.setText(customer.getAddress().getCity());
        stateField.setText(customer.getAddress().getState());
        zipField.setText(customer.getAddress().getZip());
        emailField.setText(customer.getEmail());
        if (customer.getBusinessName() != null) {
            businessNameField.setText(customer.getBusinessName());
        }

        //Phone
        if (customer.getPhoneNumber().length() != 0) {
            try {
                phoneMaskFormatter = new MaskFormatter(phoneMask);
                phoneMaskFormatter.setValueContainsLiteralCharacters(false);
                phoneField.setText(phoneMaskFormatter.valueToString(customer.getPhoneNumber()));
            } catch (ParseException ex) {
                System.out.println(ex);
            }
        }

        //Account Info
        sourceComboBox.setSelectedItem(customer.getSourceType());
        sourceDetailComboBox.setSelectedItem(customer.getSourceDetails());
        specificsTextArea.setText(customer.getSourceSpecifics());
        if (customer.getAccountType() != null) {
            if (customer.getAccountType().equalsIgnoreCase("residential")) {
                residentialRadioButton.setSelected(true);
            } else {
                businessRadioButton.setSelected(true);
            }
        }
        
        //Metrics
        setMetrics(customer);
        setAddOns(customer);
    }
    
    private void setMetrics(Customer customer) {
        installedSvcComboBox.setSelectedItem(customer.getIntalledSvc());
        statusComboBox.setSelectedItem(customer.getMetricStatus());
        //Set Income Date Fields
        if (customer.getIncomeDate() != 0) {
            Date incomeDate = Date.from(Instant.ofEpochSecond((long) customer.getIncomeDate()));
            setDateFields(incomeMonthComboBox, incomeDayComboBox, incomeYearComboBox, incomeDate);            
        }
        
        if (customer.getInstallDate() != 0) {
            Date date = Date.from(Instant.ofEpochSecond((long) customer.getInstallDate()));
            setDateFields(installMonthComboBox, installDayComboBox, installYearComboBox, date);            
        }
        
        if (customer.getSurveyDate() != 0) {
            Date date = Date.from(Instant.ofEpochSecond((long) customer.getSurveyDate()));
            setDateFields(surveyMonthComboBox, surveyDayComboBox, surveyYearComboBox, date);            
        }
    }
    
    private void setAddOns(Customer customer) {
        System.out.println(customer.getAddOns());
        if (customer.getAddOns() == null) {
            return;
        }
        List<String> addonList =  Arrays.asList(customer.getAddOns().split(","));
        ArrayList<String> addons = new ArrayList<>(addonList);
        populateAddOns(addons);
    }
    
    private void setHistoryData(Customer customer, Connection connection, int date, String action) {
        try {
            connection = DriverManager.getConnection(DatabaseCredentials.databaseUrl, username, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT into History (customer, user, date, description)" + " values (?, ?, ?, ?)");
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, date);
            preparedStatement.setString(4, action);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerEditor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                Logger.getLogger(CustomerEditor.class.getName()).log(Level.SEVERE, null, e);
            }
        }
            
    }
    
    private void setDateFields(JComboBox monthBox, JComboBox dayBox, JComboBox yearBox, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        int year = localDate.getYear();
        monthBox.setSelectedIndex(month);
        dayBox.setSelectedIndex(day);
        yearBox.setSelectedItem(String.valueOf(year));
    }
    
    private void populateAddOns(ArrayList<String> list) {
        DefaultListModel listModel = new DefaultListModel();
        list.forEach( (item) -> {
           listModel.addElement(item);
        });
        addOnListView.setModel(listModel);
    }
    
    private boolean validateTextFields() {
        
        if (firstNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a first name.");
            return false;
        } else if (lastNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a last name.");
            return false;
        } else if (streetField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a street address.");
            return false;
        } else if (cityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city.");
            return false;
        } else if (stateField.getText().isEmpty()) { 
            JOptionPane.showMessageDialog(this, "Please enter a state.");
            return false;
        } else if (zipField.getText().isEmpty() || zipField.getText().length() > 5 || zipField.getText().length() < 5) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 5 digit zipcode.");
            return false;
        } else {
            return true;
        }
        
    }
    
    public void clearTextFields(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JTextField) {
                JTextField f = (JTextField) c;
                f.setText("");
            } else if (c instanceof Container) {
                clearTextFields((Container) c);
            }
            
            if (c instanceof JComboBox) {
                JComboBox box = (JComboBox) c;
                box.setSelectedIndex(0);
            } else if (c instanceof Container) {
                clearTextFields((Container) c);
            }
        }
        accountTypeGroup.clearSelection();
        selectedAddons.clear();
        specificsTextArea.setText("");
        
        
    }
    
    public void setUserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void saveCustomerAction() {
        if (firstNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a first name.");
        } else if (lastNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a last name.");
        } else if (streetField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a street address.");
        } else if (cityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a city.");
        } else if (stateField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a state.");
        } else if (zipField.getText().isEmpty() || zipField.getText().length() > 5 || zipField.getText().length() < 5) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 5 digit zipcode.");
        } else if (incomeDayComboBox.getSelectedIndex() == 0 || incomeMonthComboBox.getSelectedIndex() == 0 || incomeYearComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select an incoming date.");
        } else {
            if (selectedCustomer != null && editingCustomer) {
                //Update Selected Customer
                System.out.println("Updating Customer");
                int customerId = selectedCustomer.getId();
                selectedCustomer = createCustomer(customerId);
                uploadCustomer(selectedCustomer, false);
            } else if (!editingCustomer) {
                //Create New Customer
                System.out.println("Creating New Customer");
                //uploadNewCustomerToDatabase(createCustomer());
                uploadCustomer(createCustomer(0), true);
            }
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        stateField = new javax.swing.JTextField();
        cityField = new javax.swing.JTextField();
        streetField = new javax.swing.JTextField();
        unitField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        zipField = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lastNameField = new javax.swing.JTextField();
        firstNameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        sourceComboBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        sourceDetailComboBox = new javax.swing.JComboBox<>();
        phoneField = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        incomeMonthComboBox = new javax.swing.JComboBox<>();
        incomeDayComboBox = new javax.swing.JComboBox<>();
        incomeYearComboBox = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        installedSvcComboBox = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        addOnListView = new javax.swing.JList<>();
        addNewButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        surveyMonthComboBox = new javax.swing.JComboBox<>();
        surveyDayComboBox = new javax.swing.JComboBox<>();
        surveyYearComboBox = new javax.swing.JComboBox<>();
        installDayComboBox = new javax.swing.JComboBox<>();
        installMonthComboBox = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        installYearComboBox = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        businessNameField = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        historyButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        residentialRadioButton = new javax.swing.JRadioButton();
        businessRadioButton = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        specificsTextArea = new javax.swing.JTextArea();
        saveClearButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Address"));

        jLabel7.setText("Street:");

        jLabel8.setText("City:");

        jLabel9.setText("State:");

        jLabel10.setText("ZIP:");

        stateField.setText("NY");

        jLabel17.setText("Unit:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(streetField))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cityField)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(unitField, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                            .addComponent(zipField))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(streetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(zipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Personal Information"));

        jLabel1.setText("First Name:");

        jLabel2.setText("Last Name:");

        jLabel4.setText("Source:");

        sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "In Person", "Phone", "Email" }));

        jLabel5.setText("Phone:");

        jLabel6.setText("Email:");

        jLabel12.setText("Detail:");

        sourceDetailComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Call In", "Walk In", "Web Search", "Facebook", "Word of Mouth", "Referral - Employee", "Referral - Customer", "Redburn Tenant", "Postcard", "Signage", "Van", "Online Signup", "Previous Customer", "Event", "Landlord - Bulk", "MDU - Move in Docs", "MDU - Door Hanger", "Campaign" }));

        phoneField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lastNameField)
                            .addComponent(sourceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(emailField)
                            .addComponent(sourceDetailComboBox, 0, 183, Short.MAX_VALUE)
                            .addComponent(phoneField))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(sourceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(sourceDetailComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Metrics"));

        jLabel11.setText("Income Date:");

        incomeMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "January", "Febraury", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        incomeDayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        incomeYearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025" }));

        jLabel14.setText("Status:");

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Complete", "ROE Denied", "In Progress", "Out of Coverage", "Denied - Money", "Denied - Services", "Unresponsive", "Non-working Contract", "Customer Hold", "City Surfer Only" }));

        jLabel15.setText("Installed Service:");

        installedSvcComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Home Standard", "Premium HD", "Turbo 25", "Ultimate 50", "Business Basic", "Business Enhanced", "Business Pro", "News 20", "Powers Park 50", "Powers Park 100", "MDU Prewire" }));
        installedSvcComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installedSvcComboBoxActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Installed Add-Ons"));

        jScrollPane1.setViewportView(addOnListView);

        addNewButton.setText("Add New");
        addNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewButtonActionPerformed(evt);
            }
        });

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addNewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(addNewButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)))
                .addContainerGap())
        );

        jLabel19.setText("Survey Date:");

        surveyMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "January", "Febraury", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        surveyDayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        surveyYearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025" }));

        installDayComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        installMonthComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "January", "Febraury", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));

        jLabel20.setText("Install Date:");

        installYearComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(incomeMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(incomeDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(incomeYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel19)
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(surveyMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(surveyDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(surveyYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(installMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(installDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(installYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(9, 9, 9)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(installedSvcComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(incomeMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(incomeDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(incomeYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(surveyMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(surveyDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(surveyYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(installMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(installDayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(installYearComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 102, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(installedSvcComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Business"));

        jLabel13.setText("Name:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(businessNameField)
                .addGap(8, 8, 8))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(businessNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        historyButton.setText("View History");
        historyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyButtonActionPerformed(evt);
            }
        });

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Account"));

        jLabel16.setText("Type:");

        residentialRadioButton.setText("Residential");

        businessRadioButton.setText("Business");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(residentialRadioButton)
                .addGap(18, 18, 18)
                .addComponent(businessRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(residentialRadioButton)
                    .addComponent(businessRadioButton))
                .addGap(0, 13, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Source"));

        jLabel18.setText("Specifics:");

        specificsTextArea.setColumns(20);
        specificsTextArea.setRows(5);
        jScrollPane2.setViewportView(specificsTextArea);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        saveClearButton.setText("Save & Clear");
        saveClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveClearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(historyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton)
                        .addGap(18, 18, 18)
                        .addComponent(saveClearButton)
                        .addGap(18, 18, 18)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(historyButton)
                    .addComponent(saveButton)
                    .addComponent(cancelButton)
                    .addComponent(saveClearButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        //TODO: Confirm Dialog
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewButtonActionPerformed
        // TODO add your handling code here:
        addOnDialog.setAlwaysOnTop(true);
        addOnDialog.setVisible(true);
        ArrayList<String> selectedAddon = addOnDialog.getAddOns();
        System.out.println(selectedAddon.size());
        selectedAddon.forEach((item) -> {
            selectedAddons.add(item);
        });
        populateAddOns(selectedAddons);
    }//GEN-LAST:event_addNewButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        saveCustomerAction();
        this.dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        // TODO add your handling code here:
        if (addOnListView.getSelectedIndices().length == 0) {
            JOptionPane.showMessageDialog(null, "Please selected an add-on to remove.");
        } else {
            ArrayList<String> itemsToRemove = (ArrayList) addOnListView.getSelectedValuesList();
            selectedAddons.removeAll(itemsToRemove);
            populateAddOns(selectedAddons);
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void historyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyButtonActionPerformed
        // TODO add your handling code here:
        historyDialog.setAlwaysOnTop(true);
        historyDialog.setHistoryData(currentCustomer);
        historyDialog.setVisible(true);
        
    }//GEN-LAST:event_historyButtonActionPerformed

    private void saveClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveClearButtonActionPerformed
        // TODO add your handling code here:
        saveCustomerAction();
        clearTextFields(this.getContentPane());
    }//GEN-LAST:event_saveClearButtonActionPerformed

    private void installedSvcComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installedSvcComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_installedSvcComboBoxActionPerformed

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
            java.util.logging.Logger.getLogger(CustomerEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the addOnDialog */
        java.awt.EventQueue.invokeLater(() -> {
            CustomerEditor dialog = new CustomerEditor(new javax.swing.JFrame(), true);
            username = args[0];
            password = args[1];
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    //System.exit(0);
                }
            });
            dialog.setVisible(true);
                 
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addNewButton;
    private javax.swing.JList<String> addOnListView;
    private javax.swing.JTextField businessNameField;
    private javax.swing.JRadioButton businessRadioButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cityField;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField firstNameField;
    private javax.swing.JButton historyButton;
    private javax.swing.JComboBox<String> incomeDayComboBox;
    private javax.swing.JComboBox<String> incomeMonthComboBox;
    private javax.swing.JComboBox<String> incomeYearComboBox;
    private javax.swing.JComboBox<String> installDayComboBox;
    private javax.swing.JComboBox<String> installMonthComboBox;
    private javax.swing.JComboBox<String> installYearComboBox;
    private javax.swing.JComboBox<String> installedSvcComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField lastNameField;
    private javax.swing.JFormattedTextField phoneField;
    private javax.swing.JButton removeButton;
    private javax.swing.JRadioButton residentialRadioButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveClearButton;
    private javax.swing.JComboBox<String> sourceComboBox;
    private javax.swing.JComboBox<String> sourceDetailComboBox;
    private javax.swing.JTextArea specificsTextArea;
    private javax.swing.JTextField stateField;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JTextField streetField;
    private javax.swing.JComboBox<String> surveyDayComboBox;
    private javax.swing.JComboBox<String> surveyMonthComboBox;
    private javax.swing.JComboBox<String> surveyYearComboBox;
    private javax.swing.JTextField unitField;
    private javax.swing.JFormattedTextField zipField;
    // End of variables declaration//GEN-END:variables
}
