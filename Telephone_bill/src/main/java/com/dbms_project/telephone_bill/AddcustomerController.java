/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dbms_project.telephone_bill;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;



/**
 * FXML Controller class
 *
 * @author DELL
 */


public class AddcustomerController  {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField txtName;
    @FXML
    public TextField txtPhone;
    @FXML
    private TextField txtAddressLine1;
    @FXML
    private TextField txtAddressLine2;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtState;
    @FXML
    private TextField txtPostalCode;
    @FXML
    public TextField txtLandphone;
    
    
    
    public void initialize() {
        // Restricting input to numbers only
        Pattern pattern = Pattern.compile("\\d*");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (pattern.matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        };

        // Apply formatter to the Phone TextField
        TextFormatter<String> phoneFormatter = new TextFormatter<>(filter);
        txtPhone.setTextFormatter(phoneFormatter);

        // Apply formatter to the Landphone TextField
        TextFormatter<String> landphoneFormatter = new TextFormatter<>(filter);
        txtLandphone.setTextFormatter(landphoneFormatter);
    }

    @FXML
    private void tocust() throws IOException {
        App.setRoot("UserDashboard");
    }
    
    @FXML
    private void tocharges() throws IOException {
        App.setRoot("charges");
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
    
    
    
    
    
     @FXML
    private void addCustomer() throws IOException {
        String name = txtName.getText();
        String phone = txtPhone.getText();
        String addressLine1 = txtAddressLine1.getText();
        String addressLine2 = txtAddressLine2.getText();
        String city = txtCity.getText();
        String state = txtState.getText();
        String postalCode = txtPostalCode.getText();
        String landPhone = txtLandphone.getText();
            
        
        
        
       // Check if any required field is empty
if (name.isEmpty() || phone.isEmpty() || landPhone.isEmpty() ) {
    // Show an alert if any required field is empty
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText("Please fill in all required fields.");
    alert.showAndWait();
    return;
}

// Convert phone numbers to long type
        long phoneLong = Long.parseLong(phone);
        long landPhoneLong = Long.parseLong(landPhone) ;




        if (customerExists(name, landPhoneLong)) {
            showAlert("Customer already exists.");
        } else {
            try {
                Connection conn = Connect.getConnection();
                String query = "INSERT INTO customer (c_name, c_phone, address_line1, address_line2, city, state, postal_code, c_landPhone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, name);
                statement.setLong(2, phoneLong);
                statement.setString(3, addressLine1);
                statement.setString(4, addressLine2);
                statement.setString(5, city);
                statement.setString(6, state);
                statement.setString(7, postalCode);
                statement.setLong(8, landPhoneLong);

                statement.executeUpdate();
                showAlert("Customer added successfully.");
                App.setRoot("UserDashboard");
                clearFields();
                statement.close();
                conn.close();
            } catch (SQLException e) {
                showAlert("Error occurred while adding customer: " + e.getMessage());
            }
        }
    }

    private boolean customerExists(String name, Long landPhone) {
        try {
            Connection conn = Connect.getConnection();
            String query = "SELECT * FROM customer WHERE c_name=? OR c_landPhone=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setLong(2, landPhone);
            ResultSet rs = statement.executeQuery();
            boolean exists = rs.next();

            rs.close();
            statement.close();
            conn.close();

            return exists;
        } catch (SQLException e) {
            showAlert("Error occurred while checking customer existence: " + e.getMessage());
            return false;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        txtName.clear();
        txtPhone.clear();
        txtAddressLine1.clear();
        txtAddressLine2.clear();
        txtCity.clear();
        txtState.clear();
        txtPostalCode.clear();
        txtLandphone.clear();
    }
    
    
    
    
}
