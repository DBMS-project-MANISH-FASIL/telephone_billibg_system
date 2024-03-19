/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.dbms_project.telephone_bill;




import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    
    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField email;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField adminCode;

    @FXML
    private Button register_btn;
    
    @FXML
    private void Login() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    void register(ActionEvent event) {
        String empName = name.getText();
        String empPassword = password.getText();
        String empConfirmPassword = confirmPassword.getText();
        String empEmail = email.getText();
        String empPhoneNumber = phoneNumber.getText();
        String empAdminCode = adminCode.getText();

        // Check if passwords match
        if (!empPassword.equals(empConfirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match!");
            return;
        }

        // Check admin code against database
        if (!isValidAdminCode(empAdminCode)) {
            showAlert(Alert.AlertType.ERROR, "Admin Code Error", "Invalid admin code!");
            return;
        }

        // Insert employee details into user table
        if (insertEmployee(empName, empPassword, empEmail, empPhoneNumber)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Success", "Employee registered successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to register employee!");
        }
    }

    private boolean isValidAdminCode(String adminCode) {
        boolean isValid = false;
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM const WHERE admin_code = ?")) {
            statement.setString(1, adminCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                isValid = resultSet.next(); // If a row is returned, admin code is valid
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private boolean insertEmployee(String name, String password, String email, String phoneNumber) {
        try (Connection connection = Connect.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO user (e_name, e_email, e_phone, e_password) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, phoneNumber);
            statement.setString(4, password);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
