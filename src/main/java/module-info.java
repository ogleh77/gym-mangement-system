module com.example.gymmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires AnimateFX;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.gymmanagementsystem to javafx.fxml;
    opens com.example.gymmanagementsystem.controllers to javafx.fxml;

    exports com.example.gymmanagementsystem;
    opens com.example.gymmanagementsystem.controllers.done to javafx.fxml;
    opens com.example.gymmanagementsystem.controllers.services to javafx.fxml;
    opens com.example.gymmanagementsystem.controllers.printers to javafx.fxml;
    exports com.example.gymmanagementsystem.entities.main;
    exports com.example.gymmanagementsystem.entities.service;

}