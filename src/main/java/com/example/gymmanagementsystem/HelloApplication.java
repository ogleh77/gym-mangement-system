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
//        HomeController controller = fxmlLoader.getController();
//        Data.setAllCustomersList(CustomerService.fetchAllCustomer(UserService.fetchAllUsers().get(0)));
//        controller.setActiveUser(UserService.fetchAllUsers().get(0));


        //        ReportController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.fetchAllUsers().get(0));
        // CustomerInfoController controller = fxmlLoader.getController();
        //Customers customer = CustomerService.fetchAllCustomer(UserService.fetchAllUsers().get(0)).get(0);
        // Users users = UserService.fetchAllUsers().get(0);
        // controller.setActiveUser(users);
        //controller.setCustomer(customer);
        //  controller.checkPayment(customer);
        // controller.checkPayment(customer);
        //  controller.setCustomer(customer);
        //System.out.println(customer);
//        RegistrationController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.fetchAllUsers().get(0));
        //controller.setOutdatedCustomers(new );
//        UserChooserController controller = fxmlLoader.getController();
//        controller.setUsersWithoutActiveOne(UserService.fetchAllUsers().get(0));
        //        DashboardController controller = fxmlLoader.getController();
//        controller.setWarningList( CustomerService.fetchAllCustomer(UserService.users().get(0)));
//        controller.setActiveUser(UserService.users().get(0));

        //
//
//        UserChooserController controller = fxmlLoader.getController();
//        controller.tempActiveUser(UserService.users().get(0));
//       controller.setActiveUser(UserService.users().get(0));
        //OutDatedController controller = fxmlLoader.getController();
        // Customers customer = CustomerService.fetchAllCustomer(UserService.users().get(0)).get(7);
        //   controller.setActiveUser(UserService.users().get(0));

        //  controller.checkPayment(customer);
        // controller.setCustomer(customer);
//        DashboardController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.users().get(0));
//        OutDatedController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.users().get(4));
//        CustomerInfoController controller = fxmlLoader.getController();
//        Customers customer = CustomerService.fetchAllCustomer(UserService.users().get(0)).get(1);
//        customer.setPayments(PaymentService.fetchAllPayments(customer.getPhone()));
//        controller.setCustomer(customer);
        //UpdateUserController controller = fxmlLoader.getController();
        //controller.setUser(UserService.users().get(4));
        // controller.setActiveUser(UserService.users().get(4));
        //CustomerInfoController controller = fxmlLoader.getController();
        //  controller.setActiveUser(UserService.users().get(0));
        //   controller.setCustomer(CustomerService.fetchAllCustomer(UserService.users().get(0)).get(0));
        // controller.setUpdatePayment(PaymentService.fetchAllPayments("8435290").get(0));
        // TODO: 17/04/2023 Set payment off insha Allah customer delete open the delete

        // TODO: 19/04/2023 wide pane remove the drop shadows insha Allah
        // TODO: 28/04/2023 phone trigger
        // TODO: 06/05/2023 insha Allah warningka marka la furo counter ka visible ka ka qaad
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}