/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dbms_project.telephone_bill;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class ChargesController  {
    
    @FXML
    private Label lblRatePerMB;

    @FXML
    private Label lblRatePerSecLocal;

    @FXML
    private Label lblRatePerSecInternational;

    @FXML
    private Label lblFixedCharge;

    @FXML
    private Label lblFreeDataUsage;

    @FXML
    private Label lblFreeCallUsage;
    
     @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;


    @FXML
    private void initialize() {
        fetchBillingConstants();
    }

   


@FXML
    private void Addcust() throws IOException {
        App.setRoot("addcustomer");
    }
    
    @FXML
    private void tocust() throws IOException {
        App.setRoot("UserDashboard");
    }
    
    @FXML
    private void toanalyses() throws IOException {
        App.setRoot("Analyses");
    }
    
        @FXML
    private void tocomplaint() throws IOException {
        App.setRoot("Ucomplaint");
    }
    @FXML
    private void logout() throws IOException {
        App.setRoot("primary");
    }    
    
    
    
    
     private void fetchBillingConstants() {
        try {
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM billing_constants WHERE constant_id = ?");
            statement.setInt(1, 1); // Assuming constant_id is always 1
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                lblRatePerMB.setText(rs.getString("rate_per_mb"));
                lblRatePerSecLocal.setText(rs.getString("rate_per_sec_local"));
                lblRatePerSecInternational.setText(rs.getString("rate_per_sec_international"));
                lblFixedCharge.setText(rs.getString("fixed_charge"));
                lblFreeDataUsage.setText(rs.getString("free_data_usage"));
                lblFreeCallUsage.setText(rs.getString("free_call_usage"));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    @FXML
    
    
    
private void update() {
    try {
        // Load the pop-up window FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateDetails.fxml"));
        Parent root = loader.load();
        
        
        // Get the controller instance
        UpdateDetailsController controller = loader.getController();
        
       
        
        // Call loadCustomerDetails explicitly
        controller.loadBillingConstants();
        
        // Create a new stage for the pop-up window
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Details");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
    }
}




@FXML
    private void generateBills() {
        // Get selected start and end dates
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        // Loop through all customers and generate bills for the specified period
        for (Customer customer : getAllCustomers()) {
            System.out.println("cust id"+customer.getId());
            calculateTotalBill(customer.getId(), startDate, endDate);
        }
        
        
// Display pop-up message after generating all bills
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Bills Generated");
    alert.setHeaderText(null);
    alert.setContentText("All bills have been generated successfully.");
    alert.showAndWait();
    }
    
    // Method to retrieve all customers from the database
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = Connect.getConnection()) {
            String query = "SELECT c_id FROM customer";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("c_id");
                   
                    // Create Customer object and add to the list
                    Customer customer = new Customer(id);
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }
    
    
     public static double calculateTotalBill(int customerId, LocalDate startDate, LocalDate endDate) {
        double totalBillAmount = 0.0;

        // Fetch call duration and internet usage for the specified period
        double totalLCallDuration = getTotalLocalCallDuration(customerId, startDate, endDate);
        double totalICallDuration = getTotalInternationalCallDuration(customerId, startDate, endDate);
        double totalInternetUsage = getTotalInternetUsage(customerId, startDate, endDate);
        double ratePerMB = 0.0;
        double ratePerSecLocal = 0.0;
        double ratePerSecInternational= 0.0;
        double fixedCharge = 0.0;
        double freeDataUsage= 0.0;
        double freeCallUsage= 0.0;

        
        // Establish a connection to the database
        try (Connection connection = Connect.getConnection()) {
            // Prepare the SQL query
            String sql = "SELECT * FROM billing_constants WHERE constant_id = 1";

            // Create a PreparedStatement
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Execute the query
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                       ratePerMB = resultSet.getDouble("rate_per_mb");
                       ratePerSecLocal = resultSet.getDouble("rate_per_sec_local");
                       ratePerSecInternational = resultSet.getDouble("rate_per_sec_international");
                       fixedCharge = resultSet.getDouble("fixed_charge");
                       freeDataUsage = resultSet.getDouble("free_data_usage");
                       freeCallUsage = resultSet.getInt("free_call_usage");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        double totalL = totalLCallDuration- freeCallUsage ;
        if(totalL<0)
        {
            totalL =0;
        }
      

        // Calculate call cost for local call
        double LcallCost = totalL * ratePerSecLocal;
        //calculate call cost for international
        double IcallCost = totalICallDuration  * ratePerSecInternational;

        double totali = totalInternetUsage - freeDataUsage;
        
        if(totali<0)
        {
            totali =0;
        }
        // Calculate internet cost
        double internetCost = totali * ratePerMB;

        // Calculate total bill amount
        totalBillAmount = fixedCharge + LcallCost + IcallCost + internetCost;
        storeBill(customerId,startDate, endDate,totalLCallDuration, totalICallDuration, totalInternetUsage,
            LcallCost, IcallCost, internetCost, totalBillAmount);


        return totalBillAmount;
    }
     
     private static void storeBill(int customerId, LocalDate startDate, LocalDate endDate, double totalLocalCallDuration, double totalInternationalCallDuration,
                              double totalInternetUsage, double localCallCost, double internationalCallCost,
                              double internetCost, double totalBillAmount) {
    try (Connection connection = Connect.getConnection()) {
        String sql = "INSERT INTO bill (customer_id, start_date, end_date, lcall_duration, icall_duration, " +
                     "internet_usage, lcall_cost, icall_cost, internet_cost, total_cost) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.setObject(2, startDate);
            statement.setObject(3, endDate);
            statement.setDouble(4, totalLocalCallDuration);
            statement.setDouble(5, totalInternationalCallDuration);
            statement.setDouble(6, totalInternetUsage);
            statement.setDouble(7, localCallCost);
            statement.setDouble(8, internationalCallCost);
            statement.setDouble(9, internetCost);
            statement.setDouble(10, totalBillAmount);
            statement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


     
     private static double getTotalLocalCallDuration(int customerId, LocalDate startDate, LocalDate endDate) {
        double totalLocalCallDuration = 0.0;

        // Establish a connection to the database
        try (Connection connection = Connect.getConnection()) {
            // Prepare the SQL query
            String sql = "SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(duration))) AS total_duration " +
                         "FROM calls " +
                         "WHERE c_id = ? AND call_location = 'Local' AND call_type = 'Outgoing' AND start_time >= ? AND end_time <= ?";
            
            // Create a PreparedStatement
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customerId);
                statement.setObject(2, startDate.atStartOfDay());
                statement.setObject(3, endDate.plusDays(1).atStartOfDay()); // Add 1 day to include end_date
                
                // Execute the query
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String totalDurationStr = resultSet.getString("total_duration");
                        if (totalDurationStr != null) {
                            LocalTime totalDuration = LocalTime.parse(totalDurationStr);
                            totalLocalCallDuration = totalDuration.toSecondOfDay() / 60.0; // Convert seconds to minutes
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("lduration"+totalLocalCallDuration);

        return totalLocalCallDuration;
    }
     
     private static double getTotalInternationalCallDuration(int customerId, LocalDate startDate, LocalDate endDate) {
        double totalInternationalCallDuration = 0.0;

        // Establish a connection to the database
        try (Connection connection = Connect.getConnection()) {
            // Prepare the SQL query
            String sql = "SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(duration))) AS total_duration " +
                         "FROM calls " +
                         "WHERE c_id = ? AND call_location = 'International' AND call_type = 'Outgoing' AND start_time >= ? AND end_time <= ?";
            
            // Create a PreparedStatement
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customerId);
                statement.setObject(2, startDate.atStartOfDay());
                statement.setObject(3, endDate.plusDays(1).atStartOfDay()); // Add 1 day to include end_date
                
                // Execute the query
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String totalDurationStr = resultSet.getString("total_duration");
                        if (totalDurationStr != null) {
                            LocalTime totalDuration = LocalTime.parse(totalDurationStr);
                            totalInternationalCallDuration = totalDuration.toSecondOfDay() / 60.0; // Convert seconds to minutes
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalInternationalCallDuration;
    }
     
     
     
     // Method to fetch total internet usage for the specified period
    private static double getTotalInternetUsage(int customerId, LocalDate startDate, LocalDate endDate) {
        double totalInternetUsage = 0.0;

        // Establish a connection to the database
        try (Connection connection = Connect.getConnection()) {
            // Prepare the SQL query
            String sql = "SELECT SUM(data_usage) AS total_usage " +
                         "FROM internet_usage " +
                         "WHERE c_id = ? AND start_time >= ? AND end_time <= ?";
            
            // Create a PreparedStatement
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customerId);
                statement.setObject(2, startDate.atStartOfDay());
                statement.setObject(3, endDate.plusDays(1).atStartOfDay()); // Add 1 day to include end_date
                
                // Execute the query
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        totalInternetUsage = resultSet.getDouble("total_usage");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalInternetUsage;
    }
    
    
    
    
    
    private static class Customer {

        private int id;
    

    public Customer(int id) {
        this.id = id;
        
    }

    public int getId() {
        return id;
    }
    }

    

  
    
}
