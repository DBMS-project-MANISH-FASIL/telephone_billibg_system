package com.dbms_project.telephone_bill;

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



public class UserDashboardController {

    @FXML
    private AnchorPane customerContainer; // Container to hold all the customer panes

    @FXML
    private TextField txt_search;

    @FXML
    private JFXButton search_btn;
    
    @FXML
    public void initialize() {
        Search();
       
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
    private void tocomplaint() throws IOException {
        App.setRoot("Ucomplaint");
    }
    
    @FXML
    private void logout() throws IOException {
        App.setRoot("primary");
    }
    
    @FXML
    private void viewbill(int customerId) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewBill.fxml"));
    Parent root = loader.load();
    
    // Get the controller instance
    ViewBillController controller = loader.getController();
    
    // Pass the customer ID to the controller
    controller.setCustomerId(customerId);
    
    // Set the scene
    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();
    }
    
    @FXML
    private void Search() {
        String searchText = txt_search.getText();
        // Clear previous search results
        customerContainer.getChildren().clear();
        
        // Fetch data from the database based on search text
        try {
            Connection conn = Connect.getConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT c_id, c_name, c_landphone FROM customer WHERE c_name LIKE ? OR c_landphone LIKE ?");
            statement.setString(1, "%" + searchText + "%");
            statement.setString(2, "%" + searchText + "%");
            ResultSet rs = statement.executeQuery();

            // Create a new customer pane for each search result
            int row = 0;
            int col = 0;
            while (rs.next()) {
                createCustomerPane(rs.getInt("c_id"), rs.getString("c_name"), rs.getLong("c_landphone"), row, col);
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
    
   

private void createCustomerPane(int customerId, String customerName, long customerLandphone, int row, int col) {
    // Create a new AnchorPane for the customer
    AnchorPane customerPane = new AnchorPane();
    customerPane.getStyleClass().add("pane_1"); // Apply CSS style
    customerPane.setPrefSize(291, 261); // Set preferred size

    // Create and configure labels for customer details
    Label idLabel = new Label("Customer ID: " + customerId);
    Label nameLabel = new Label( customerName);
    Label landphoneLabel = new Label(Long.toString(customerLandphone));


        // Increase font size for labels and make text bold
    idLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    nameLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
    landphoneLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

   // Create and configure buttons
    JFXButton updateButton = new JFXButton("Update");
    updateButton.getStyleClass().add("btn_customer11");
    updateButton.setLayoutX(14.0);
    updateButton.setLayoutY(151.0);
    updateButton.setStyle("-fx-font-size: 14; -fx-background-color: #007bff; -fx-text-fill: white;"); // Increase font size for button and set blue background with white text
    updateButton.setPrefWidth(130); // Set width
    updateButton.setPrefHeight(40); // Set height
    updateButton.setOnAction(event -> updateCustomer(customerId));

    JFXButton viewBillButton = new JFXButton("View Bill");
    viewBillButton.getStyleClass().add("btn_customer11");
    viewBillButton.setLayoutX(149.0);
    viewBillButton.setLayoutY(151.0);
    viewBillButton.setStyle("-fx-font-size: 14; -fx-background-color: #28a745; -fx-text-fill: white;"); // Increase font size for button and set green background with white text
    viewBillButton.setPrefWidth(130); // Set width
    viewBillButton.setPrefHeight(40); // Set height
    viewBillButton.setOnAction(event -> {
        try {
            viewbill(customerId);
        } catch (IOException ex) {
            Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    });
       

    JFXButton deleteButton = new JFXButton("Delete");
    deleteButton.getStyleClass().add("btn_customer11");
    deleteButton.setLayoutX(75.0);
    deleteButton.setLayoutY(197.0);
    deleteButton.setStyle("-fx-font-size: 14; -fx-background-color: #dc3545; -fx-text-fill: white;"); // Increase font size for button and set red background with white text
    deleteButton.setPrefWidth(130); // Set width
    deleteButton.setPrefHeight(40); // Set height
    deleteButton.setOnAction(event -> deleteCustomer(customerId)); // Set action for delete button


//    JFXButton viewCustomerButton = new JFXButton("View Customer");
//    viewCustomerButton.getStyleClass().add("btn_customer11");
//    viewCustomerButton.setLayoutX(149.0);
//    viewCustomerButton.setLayoutY(197.0);
//    viewCustomerButton.setStyle("-fx-font-size: 14; -fx-background-color: #28a745; -fx-text-fill: white;"); // Increase font size for button and set green background with white text
//    viewCustomerButton.setPrefWidth(130); // Set width
//    viewCustomerButton.setPrefHeight(40); // Set height


    // Add avatar image
    ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream("/images/user.jpeg")));
    avatar.setFitWidth(100);
    avatar.setFitHeight(100);
    avatar.setLayoutX(14.0);
    avatar.setLayoutY(14.0);

    // Add labels and buttons to the customer pane
    customerPane.getChildren().addAll(avatar, idLabel, nameLabel, landphoneLabel, updateButton, viewBillButton, deleteButton);

    // Set layout properties for labels and buttons to position them within the AnchorPane
    AnchorPane.setLeftAnchor(idLabel, 120.0);
    AnchorPane.setTopAnchor(idLabel, 14.0);
    AnchorPane.setLeftAnchor(nameLabel, 120.0);
    AnchorPane.setTopAnchor(nameLabel, 50.0);
    AnchorPane.setLeftAnchor(landphoneLabel, 120.0);
    AnchorPane.setTopAnchor(landphoneLabel, 90.0);

    // Add the customer pane to the customerContainer
    customerContainer.getChildren().add(customerPane);

    // Set layout properties for the customer pane within the customerContainer
    AnchorPane.setTopAnchor(customerPane, row * 280.0);
    AnchorPane.setLeftAnchor(customerPane, col * 320.0);
}


private void deleteCustomer(int customerId) {
        // SQL query to delete customer record based on c_id
        String sql = "DELETE FROM customer WHERE c_id = ?";

        try (Connection conn = Connect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set the customer ID parameter in the prepared statement
            stmt.setInt(1, customerId);

            // Execute the delete statement
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Deletion successful
                System.out.println("Customer with ID " + customerId + " deleted successfully.");
                // Show a popup message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer with ID " + customerId + " deleted successfully.");
                alert.showAndWait();
                
                Search();
            } else {
                // No rows affected, customer with provided ID not found
                System.out.println("Customer with ID " + customerId + " not found.");
                // Show a popup message
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Customer with ID " + customerId + " not found.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
            // Show a popup message for SQL error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while deleting the customer.");
            alert.showAndWait();
        }
    }



@FXML
private void updateCustomer(int customerId) {
    try {
        // Load the pop-up window FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateCustomer.fxml"));
        Parent root = loader.load();
        
        
        // Get the controller instance
        UpdateCustomerController controller = loader.getController();
        
        // Pass the customer ID to the controller
        controller.setCustomerId(customerId);
        
        System.out.println("dash id"+customerId);
        
        // Call loadCustomerDetails explicitly
        controller.loadCustomerDetails();
        
        // Create a new stage for the pop-up window
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Customer");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
    }
}




}
