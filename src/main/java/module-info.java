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
    opens com.example.gymmanagementsystem.controllers.main to javafx.fxml;

    exports com.example.gymmanagementsystem;

    exports com.example.gymmanagementsystem.entities.main;
    exports com.example.gymmanagementsystem.entities.service;
    opens com.example.gymmanagementsystem.dao to javafx.fxml;
    exports com.example.gymmanagementsystem.controllers;
    exports com.example.gymmanagementsystem.controllers.service;
    opens com.example.gymmanagementsystem.controllers.service to javafx.fxml;

}