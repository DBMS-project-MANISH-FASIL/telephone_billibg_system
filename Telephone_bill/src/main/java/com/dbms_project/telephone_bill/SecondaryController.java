//package com.dbms_project.telephone_bill;
//
//import java.io.IOException;
//import javafx.fxml.FXML;
//
//public class SecondaryController {
//
//    @FXML
//    private void User_login() throws IOException {
//        App.setRoot("primary");
//    }
//}

package com.dbms_project.telephone_bill;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecondaryController {

    @FXML
    private TextField landPhone;

    @FXML
    private Button login_btn;

    @FXML
    private Button u_btn;

    @FXML
    private Button c_btn;

    @FXML
    private void User_login() throws IOException {
        App.setRoot("primary");
    }
   
    @FXML
    void login()  throws IOException {
        String landline = landPhone.getText().trim();

        // Validate landline number
        if (!isValidLandline(landline)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Landline", "Please enter a valid 11-digit landline number.");
            return;
        }

        // Database connection and retrieval
        try {
            Connection conn = Connect.getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customer WHERE c_landphone = ?");
    stmt.setString(1, landline);
    ResultSet rs = stmt.executeQuery();

    if (rs.next()) {
        int cid = rs.getInt("c_id");
        String customerName = rs.getString("c_name");
                // Retrieve other required information
        
                
         try {
                
                String query = "INSERT INTO temp(c_id,c_name) VALUES (?, ?)";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, cid);
                statement.setString(2, customerName);
               

                statement.executeUpdate();
                
                App.setRoot("UserDashboard");
                statement.close();
                
            } catch (SQLException e) {
                    }
        App.setRoot("CustomerDashboard");
                // Open next window and pass retrieved information
    } else {
        showAlert(Alert.AlertType.ERROR, "Landline Not Found", "The provided landline number does not exist in our records.");
    }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
} catch (SQLException e) {
    showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while accessing the database.");
    e.printStackTrace();
}
    }

    private boolean isValidLandline(String landline) {
        return landline.matches("\\d{11}");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
