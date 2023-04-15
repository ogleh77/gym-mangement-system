package com.example.gymmanagementsystem;


import com.example.gymmanagementsystem.controllers.ReportControllerHandler;
import com.example.gymmanagementsystem.dao.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymmanagementsystem/newviews/dailyReports.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ReportControllerHandler controllerHandler=fxmlLoader.getController();
        controllerHandler.setActiveUser(UserService.users().get(0));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}