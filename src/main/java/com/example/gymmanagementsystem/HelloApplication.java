package com.example.gymmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymmanagementsystem/views/service/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}