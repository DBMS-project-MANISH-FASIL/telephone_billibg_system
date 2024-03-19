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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.sql.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class CViewBillController implements Initializable {
    @FXML
    private Label username;
     @FXML
    private void tocomplaint() throws IOException {
        App.setRoot("Complaint");
    }
    
    @FXML
    private void todetails() throws IOException {
        App.setRoot("CustomerDashboard");
    }
        @FXML
    private void logout() throws IOException {
        try (Connection conn = Connect.getConnection()) {
    PreparedStatement statement = conn.prepareStatement("DELETE FROM temp");
    int rowsAffected = statement.executeUpdate();
    if (rowsAffected > 0) {
        System.out.println("Deleted records successfully");
    } else {
        System.out.println("No records to delete");
    }
    statement.close();
} catch (SQLException e) {
    e.printStackTrace();
}
        App.setRoot("Secondary");
    }

    
       @FXML
    private ComboBox<String> comboMonth;

    @FXML
    private ComboBox<Integer> comboYear;

    @FXML
    private Label lblTotalLocalCallDuration;

    @FXML
    private Label lblTotalInternationalCallDuration;

    @FXML
    private Label lblTotalInternetUsage;

    @FXML
    private Label lblLocalCallCost;

    @FXML
    private Label lblInternationalCallCost;

    @FXML
    private Label lblInternetCost;

    @FXML
    private Label lblTotalBillAmount;
    
    public int id;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setdata();
         // Populate month ComboBox
        populateMonthComboBox();

        // Populate year ComboBox
        populateYearComboBox();

        // Populate combo box for years
List<Integer> years  = getDistinctYears();
ObservableList<Integer> yearOptions = FXCollections.observableArrayList(years);
comboYear.setItems(yearOptions);

// Populate combo box for months
List<String> months = getDistinctMonths();
ObservableList<String> monthOptions = FXCollections.observableArrayList(months);
comboMonth.setItems(monthOptions);
        
        // TODO
    }    
    
     public List<Integer> getDistinctYears() {
        List<Integer> years = new ArrayList<>();
        String query = "SELECT DISTINCT YEAR(start_date) AS year FROM bill";

        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int year = resultSet.getInt("year");
                years.add(year);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return years;
    }
     
     
     public List<String> getDistinctMonths() {
    List<String> months = new ArrayList<>();
    String query = "SELECT DISTINCT MONTHNAME(start_date) AS month FROM bill";

    try (Connection connection = Connect.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {
            // Convert month name to title case and add to list
            String monthName = resultSet.getString("month");
            monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
            months.add(monthName);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return months; 
}
     
       // Method to populate the month ComboBox
    private void populateMonthComboBox() {
        for (int i = 1; i <= 12; i++) {
            comboMonth.getItems().add(LocalDate.of(2000, i, 1).format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)));
        }
    }

    // Method to populate the year ComboBox
    private void populateYearComboBox() {
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= currentYear - 10; i--) {
            comboYear.getItems().add(i);
        }
    }
    
    
     @FXML
    public void ViewBill() {
       
        // Get the selected year and month from ComboBoxes
Integer selectedYear = comboYear.getValue();
String selectedMonth = comboMonth.getValue();
if (selectedYear == null || selectedMonth == null) {
        // Display an alert message
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Please select both year and month before generating the bill.");
        alert.showAndWait();
        return; // Exit the method if year or month is not selected
    }


// Build the start date using the selected year and month
LocalDate startDate = LocalDate.of(selectedYear, Month.valueOf(selectedMonth.toUpperCase()), 1);

// Get the end date by adding one month to the start date and subtracting one day
LocalDate endDate = startDate.plusMonths(1).minusDays(1);

// Fetch the bill details from the database based on the selected date range
fetchBillDetails(id, startDate, endDate);
    }
    
    // Method to fetch bill details from the database for a specific customer and date range
private void fetchBillDetails(int customerId, LocalDate startDate, LocalDate endDate) {
    try (Connection connection = Connect.getConnection()) {
        String query = "SELECT * FROM bill WHERE customer_id = ? AND start_date >= ? AND end_date <= ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, customerId);
        statement.setDate(2, java.sql.Date.valueOf(startDate));
        statement.setDate(3, java.sql.Date.valueOf(endDate));
        ResultSet resultSet = statement.executeQuery();
        
        // Iterate through the result set and update the UI labels with fetched data
        if (resultSet.next()) {
            
//         // Retrieve the start_date and end_date as java.sql.Date objects
//    java.sql.Date startDateSql = resultSet.getDate("start_date");
//    java.sql.Date endDateSql = resultSet.getDate("end_date");
//
//    // Convert java.sql.Date to LocalDate
//    LocalDate sstartDate = startDateSql.toLocalDate();
//    LocalDate eendDate = endDateSql.toLocalDate();
//  
            
            lblTotalLocalCallDuration.setText(resultSet.getString("lcall_duration"));
            System.out.println(lblTotalLocalCallDuration);
       
            lblTotalInternationalCallDuration.setText(resultSet.getString("icall_duration"));
            lblTotalInternetUsage.setText(resultSet.getString("internet_usage"));
            lblLocalCallCost.setText(resultSet.getString("lcall_cost"));
            lblInternationalCallCost.setText(resultSet.getString("icall_cost"));
            lblInternetCost.setText(resultSet.getString("internet_cost"));
            lblTotalBillAmount.setText(resultSet.getString("total_cost"));
        } else {
            // Handle case when no bill data is found for the selected date range
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle database connection or query errors
    }
}
    
    
     
     
    
    public void setdata(){
        try {
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM temp");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                id = rs.getInt("c_id");
                username.setText(rs.getString("c_name"));
               
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
    }
    
    
    
}
