/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dbms_project.telephone_bill;




import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class UpdateCustomerController {
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
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
    private TextField txtLandphone;

    public int customerId;

    public void setCustomerId(int custId) {
        this.customerId = custId;
        
        
    }
    
    @FXML
    public void Cancel() {
       // Close the UpdateCustomer window
            Stage stage = (Stage) txtName.getScene().getWindow(); // Get the current stage
            stage.close();
        
        
    }
    

    

    public void loadCustomerDetails() {
        try {
            
            
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM customer WHERE c_id = ?");
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                txtName.setText(rs.getString("c_name"));
                txtPhone.setText(rs.getString("c_phone"));
                txtAddressLine1.setText(rs.getString("address_line1"));
                txtAddressLine2.setText(rs.getString("address_line2"));
                txtCity.setText(rs.getString("city"));
                txtState.setText(rs.getString("state"));
                txtPostalCode.setText(rs.getString("postal_code"));
                txtLandphone.setText(rs.getString("c_landphone"));
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateCustomer() {
    try {
        Connection conn = Connect.getConnection();
        PreparedStatement statement = conn.prepareStatement("UPDATE customer SET c_name = ?, c_phone = ?, address_line1 = ?, address_line2 = ?, city = ?, state = ?, postal_code = ?, c_landphone = ? WHERE c_id = ?");
        statement.setString(1, txtName.getText());
        statement.setString(2, txtPhone.getText());
        statement.setString(3, txtAddressLine1.getText());
        statement.setString(4, txtAddressLine2.getText());
        statement.setString(5, txtCity.getText());
        statement.setString(6, txtState.getText());
        statement.setString(7, txtPostalCode.getText());
        statement.setString(8, txtLandphone.getText());
        statement.setInt(9, customerId);
        int rowsUpdated = statement.executeUpdate();
        statement.close();
        conn.close();
        
        if (rowsUpdated > 0) {
            // Show a popup message upon successful update
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Customer details updated successfully.");
            alert.showAndWait();
            
            // Close the UpdateCustomer window
            Stage stage = (Stage) txtName.getScene().getWindow(); // Get the current stage
            stage.close();
        } else {
            // Show an error message if no rows were updated
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update customer details.");
            alert.showAndWait();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Show an error message if SQL exception occurs
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while updating customer details.");
        alert.showAndWait();
    }
}

}
