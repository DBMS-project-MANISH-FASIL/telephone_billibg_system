/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dbms_project.telephone_bill;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author DELL
 */
public class UcomplaintController  {
 @FXML
    private AnchorPane customerContainer; // Container to hold all the customer panes

  
    
    @FXML
    public void initialize() {
        
        // Fetch data from the database based on search text
        try {
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT cid,problrm , status FROM complaint  ");
            ResultSet rs = statement.executeQuery();

            // Create a new customer pane for each search result
            int row = 0;
            int col = 0;
            while (rs.next()) {
                createCustomerPane(rs.getInt("cid"), rs.getString("problrm"), rs.getString("status"), row, col);
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }

            // Close resources
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
       
    }
    
    @FXML
    private void Addcust() throws IOException {
        App.setRoot("addcustomer");
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
    private void todashboard() throws IOException {
        App.setRoot("UserDashboard");
    }
    
    @FXML
    private void logout() throws IOException {
        App.setRoot("primary");
    }
    
    
    
    
   

private void createCustomerPane(int customerId, String problem, String status, int row, int col) {
    // Create a new AnchorPane for the customer
   AnchorPane customerPane = new AnchorPane();
customerPane.getStyleClass().add("pane_1"); // Apply CSS style
customerPane.setPrefSize(291, 261); // Set preferred size

// Create and configure labels for customer details
Label idLabel = new Label("Customer ID: " + customerId);
Label nameLabel = new Label("Problem:\n" + problem);
Label landphoneLabel = new Label("\n Status: " + status);

// Increase font size for labels and make text bold
idLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
landphoneLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

// Create and configure buttons
JFXButton updateButton = new JFXButton("Solved");
updateButton.getStyleClass().add("btn_customer11");
updateButton.setStyle("-fx-font-size: 14; -fx-background-color: #007bff; -fx-text-fill: white;"); // Increase font size for button and set blue background with white text
updateButton.setPrefWidth(130); // Set width
updateButton.setPrefHeight(40); // Set height
updateButton.setOnAction(event -> updateCustomer(customerId));

// Add labels and buttons to the customer pane
customerPane.getChildren().addAll(idLabel, nameLabel, landphoneLabel, updateButton);

// Set layout properties for labels and buttons to position them within the AnchorPane
AnchorPane.setLeftAnchor(idLabel, 10.0);
AnchorPane.setTopAnchor(idLabel, 14.0);
AnchorPane.setLeftAnchor(nameLabel, 10.0);
AnchorPane.setTopAnchor(nameLabel, 50.0);
AnchorPane.setLeftAnchor(landphoneLabel, 10.0);
AnchorPane.setTopAnchor(landphoneLabel, 90.0);

// Set layout properties for the button to align it to the center bottom
AnchorPane.setBottomAnchor(updateButton, 10.0);
AnchorPane.setLeftAnchor(updateButton, (291 - updateButton.getPrefWidth()) / 2); // Center horizontally

// Add the customer pane to the customerContainer
customerContainer.getChildren().add(customerPane);

// Set layout properties for the customer pane within the customerContainer
AnchorPane.setTopAnchor(customerPane, row * 280.0);
AnchorPane.setLeftAnchor(customerPane, col * 320.0);
}





@FXML
private void updateCustomer(int customerId) {
    try {
        Connection conn = Connect.getConnection();
        PreparedStatement statement = conn.prepareStatement("UPDATE complaint SET status = 'solved' WHERE cid = ?");
        statement.setInt(1, customerId);
        int rowsUpdated = statement.executeUpdate();

        if (rowsUpdated > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Complaint Status Updated");
            alert.setHeaderText(null);
            alert.setContentText("Complaint status has been set to 'solved'");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update complaint status");
            alert.showAndWait();
        }

        statement.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Database Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occurred while updating complaint status");
        alert.showAndWait();
    }
}

   
}