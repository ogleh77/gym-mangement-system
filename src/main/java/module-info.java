module com.example.gymmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jfoenix;
    requires AnimateFX;


    opens com.example.gymmanagementsystem to javafx.fxml;
    opens com.example.gymmanagementsystem.controllers to javafx.fxml;

    exports com.example.gymmanagementsystem;
    opens com.example.gymmanagementsystem.controllers.done to javafx.fxml;
}