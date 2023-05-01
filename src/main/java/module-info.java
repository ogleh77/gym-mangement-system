module com.example.gymmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires AnimateFX;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.xerial.sqlitejdbc;

    opens com.example.gymmanagementsystem to javafx.fxml;
    opens com.example.gymmanagementsystem.temcont to javafx.fxml;

    exports com.example.gymmanagementsystem;

    exports com.example.gymmanagementsystem.data.entities.main;
    exports com.example.gymmanagementsystem.data.entities.service;


}