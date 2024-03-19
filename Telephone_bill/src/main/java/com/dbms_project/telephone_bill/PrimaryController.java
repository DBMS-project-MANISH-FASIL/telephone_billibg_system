package com.dbms_project.telephone_bill;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrimaryController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

//    @FXML
//    private void initialize() {
//        // Optional: Perform any initialization tasks here
//    }
    
    @FXML
    private void Cust_login() throws IOException {
        App.setRoot("secondary");
    }
    
    @FXML
    private void Register() throws IOException {
        App.setRoot("register");
    }

    @FXML
    private void login() {
        
        
        String user = username.getText();
        String pass = password.getText();
        
        

        // Here, you can connect to the backend to check the credentials
        boolean isValidCredentials = checkCredentials(user, pass);

        if (isValidCredentials) {
            // Navigate to the secondary screen or perform any other action
            try {
                App.setRoot("UserDashboard");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert for invalid credentials
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password. Please try again.");
            alert.showAndWait();
        }
    }

    // Example method to check credentials (replace with your backend logic)
    private boolean checkCredentials(String username, String password) {
        String query = "SELECT COUNT(*) FROM user WHERE e_email = ? AND e_password = ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the username and password parameters
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                // If count is greater than 0, credentials are correct
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exceptions
        }
        // If an exception occurs or no matching credentials found, return false
        return false;
    }
}
