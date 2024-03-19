/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dbms_project.telephone_bill;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class UpdateDetailsController {
    @FXML
    private TextField txtRatePerMB;
    @FXML
    private TextField txtRatePerSecLocal;
    @FXML
    private TextField txtRatePerSecInternational;
    @FXML
    private TextField txtFixedCharge;
    @FXML
    private TextField txtFreeDataUsage;
    @FXML
    private TextField txtFreeCallUsage;
    @FXML
    private Label lblStatus;

    private static final int CONSTANT_ID = 1;

    @FXML
    private void initialize() {
        // Load billing constants details for the given constant ID
        loadBillingConstants();
    }

    public void loadBillingConstants() {
        try {
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM billing_constants WHERE constant_id = ?");
            statement.setInt(1, CONSTANT_ID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                txtRatePerMB.setText(rs.getString("rate_per_mb"));
                txtRatePerSecLocal.setText(rs.getString("rate_per_sec_local"));
                txtRatePerSecInternational.setText(rs.getString("rate_per_sec_international"));
                txtFixedCharge.setText(rs.getString("fixed_charge"));
                txtFreeDataUsage.setText(rs.getString("free_data_usage"));
                txtFreeCallUsage.setText(rs.getString("free_call_usage"));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateBillingConstants() {
        try {
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("UPDATE billing_constants SET rate_per_mb = ?, rate_per_sec_local = ?, rate_per_sec_international = ?, fixed_charge = ?, free_data_usage = ?, free_call_usage = ? WHERE constant_id = ?");
            statement.setString(1, txtRatePerMB.getText());
            statement.setString(2, txtRatePerSecLocal.getText());
            statement.setString(3, txtRatePerSecInternational.getText());
            statement.setString(4, txtFixedCharge.getText());
            statement.setString(5, txtFreeDataUsage.getText());
            statement.setString(6, txtFreeCallUsage.getText());
            statement.setInt(7, CONSTANT_ID);
            int rowsUpdated = statement.executeUpdate();
            statement.close();
            conn.close();
            if (rowsUpdated > 0) {
            // Show a popup message upon successful update
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("details updated successfully.");
            alert.showAndWait();
            
            // Close the UpdateCustomer window
            Stage stage = (Stage) txtRatePerMB.getScene().getWindow(); // Get the current stage
            stage.close();
        } else {
            // Show an error message if no rows were updated
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update details.");
            alert.showAndWait();
        }} catch (SQLException e) {
             e.printStackTrace();
        // Show an error message if SQL exception occurs
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while updating details.");
        alert.showAndWait();
        }
    }

    @FXML
    private void cancelUpdate() {
        
        // Close the UpdateCustomer window
            Stage stage = (Stage) txtRatePerMB.getScene().getWindow(); // Get the current stage
            stage.close();
        // Close the window or perform any necessary action
    }
}

