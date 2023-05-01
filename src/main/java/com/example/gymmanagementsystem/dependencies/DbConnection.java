package com.example.gymmanagementsystem.dependencies;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class DbConnection {
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:src/database/database.db");
             }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
        return connection;
    }
}
