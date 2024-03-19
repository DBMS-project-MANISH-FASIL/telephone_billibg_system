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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author wajid
 */


//public interface CustomerDashboardController {
//    void receiveParams(Object... params);
//}




public class CustomerDashboardController implements Initializable {
     @FXML
    private Label username;
    
     @FXML
    private Label phone;

    @FXML
    private Label name;

    @FXML
    private Label add1;

    @FXML
    private Label add2;

    @FXML
    private Label city;

    @FXML
    private Label state;
    
    @FXML
    private Label landphone;
    
    @FXML
    private Label pcode;
  
    
     @FXML
    private void tocomplaint() throws IOException {
        App.setRoot("Complaint");
    }
    
    @FXML
    private void tobills() throws IOException {
        App.setRoot("CViewBill");
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setdata();
        
        try {
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM customer WHERE c_id = ?");
            statement.setInt(1, id); // Assuming constant_id is always 1
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                name.setText(rs.getString("c_name"));
                phone.setText(rs.getString("c_phone"));
                add1.setText(rs.getString("address_line1"));
                add2.setText(rs.getString("address_line2"));
                city.setText(rs.getString("city"));
                state.setText(rs.getString("state"));
                pcode.setText(rs.getString("postal_code"));
                landphone.setText(rs.getString("c_landphone"));
            
            }
            rs.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
