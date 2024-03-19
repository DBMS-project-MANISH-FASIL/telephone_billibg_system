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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
/**
 * FXML Controller class
 *
 * @author DELL
 */


public class AnalysesController  {

    
    @FXML
    private AnchorPane analysis_anchor;
    @FXML
    private BarChart<String, Number> barChart;
       /**
     * Initializes the controller class.
     */
    public void initialize() {
        // Create axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month-Year");
        yAxis.setLabel("Total Revenue");

        // Create bar chart
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Total Revenue per Month-Year");

        // Fetch data
        Map<String, Double> monthlyRevenue = aggregateRevenue();

        // Populate chart with data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {
            String key = entry.getKey(); // Key is already in the format "YYYY-MM"
            double total = entry.getValue();
            series.getData().add(new XYChart.Data<>(key, total));
        }

        chart.getData().add(series);

        // Add chart to the anchor pane
        analysis_anchor.getChildren().add(chart);
    }
    
  
    
   private Map<String, Double> aggregateRevenue() {
    Map<String, Double> monthlyRevenue = new HashMap<>();

    try (Connection connection = Connect.getConnection()) {
        String sql = "SELECT CONCAT(YEAR(start_date), '-', LPAD(MONTH(start_date), 2, '0')) AS month_year, " +
                     "SUM(total_cost) AS total " +
                     "FROM bill " +
                     "GROUP BY YEAR(start_date), MONTH(start_date), CONCAT(YEAR(start_date), '-', LPAD(MONTH(start_date), 2, '0'))";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String monthYear = resultSet.getString("month_year");
                double total = resultSet.getDouble("total");
                monthlyRevenue.put(monthYear, total);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return monthlyRevenue;
}    
    
    
    
    @FXML
    private void Addcust() throws IOException {
        App.setRoot("addcustomer");
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
    private void tocomplaint() throws IOException {
        App.setRoot("Ucomplaint");
    }
    
    @FXML
    private void logout() throws IOException {
        App.setRoot("primary");
    }    
    
}
