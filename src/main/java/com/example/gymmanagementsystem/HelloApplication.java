package com.example.gymmanagementsystem;

import com.example.gymmanagementsystem.controllers.DailyReportController;
import com.example.gymmanagementsystem.dao.service.UserService;
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymmanagementsystem/views/dailyReports.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        DailyReportController controller=fxmlLoader.getController();
        controller.setActiveUser(UserService.users().get(3));
//        DashboardController controller=fxmlLoader.getController();
////        RegistrationsController controller = fxmlLoader.getController();
////
//////        CustomerInfoController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.users().get(0));
        //   controller.setCustomer(CustomerService.fetchAllCustomer(UserService.users().get(2)).get(1));
//        //controller.setUpdatePayment(PaymentService.fetchAllCustomersPayments("3791385").get(0));
//        // controller.checkPayment(CustomerService.fetchAllCustomer(UserService.users().get(2)).get(1));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}