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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class ComplaintController implements Initializable {
    
     @FXML
    private JFXTextArea textarea;

    @FXML
    private Label status;

    @FXML
    private JFXButton btnregister;
    
    @FXML
    private Label username;
    
    
    @FXML
    private void tobills() throws IOException {
        App.setRoot("CViewBill");
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

    public int id;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setdata();
        setstatus();
                // TODO
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
    
    public void setstatus(){
        try {
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT co_id ,status FROM complaint where cid = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                
                status.setText("complaint no :   "+rs.getString("co_id")+"\n"+rs.getString("status"));
               
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       
    }
    
   
    // Existing code...

    @FXML
    void register() {
        String problem = textarea.getText().trim();
        if (!problem.isEmpty()) {
            try (Connection conn = Connect.getConnection()) {
                String query = "INSERT INTO complaint (cid, problrm,status) VALUES (?, ?,'pending')";
                PreparedStatement statement = conn.prepareStatement(query);
                // Assuming cid is obtained from somewhere else, like user login
                statement.setInt(1, id); // Replace getCid() with the actual method to get cid
                statement.setString(2, problem);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert(AlertType.INFORMATION, "Success", "Complaint registered successfully");
                    setstatus();
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to register complaint");
                }
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Error", "Error occurred while registering complaint");
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.WARNING, "Warning", "Please enter a valid complaint");
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


    
    
    

