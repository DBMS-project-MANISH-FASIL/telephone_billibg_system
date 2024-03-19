module com.dbms_project.telephone_bill {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires com.jfoenix;
    
    

    opens com.dbms_project.telephone_bill to javafx.fxml;
    exports com.dbms_project.telephone_bill;
}
