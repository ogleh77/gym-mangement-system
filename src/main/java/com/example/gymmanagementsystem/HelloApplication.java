package com.example.gymmanagementsystem;

import com.example.gymmanagementsystem.controllers.informations.OutdatedController;
import com.example.gymmanagementsystem.controllers.informations.ReportController;
import com.example.gymmanagementsystem.data.dto.UserService;
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/gymmanagementsystem/newviews/info/outdated.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        OutdatedController controller=fxmlLoader.getController();
        controller.setActiveUser(UserService.fetchAllUsers().get(0));
//        ReportController controller = fxmlLoader.getController();
//        controller.setActiveUser(UserService.fetchAllUsers().get(0));
        // CustomerInfoController controller = fxmlLoader.getController();
        //Data.setAllCustomersList(CustomerService.fetchAllCustomer(UserService.fetchAllUsers().get(0)));
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
        // TODO: 17/04/2023 Payment controller re check insha Allah

        // TODO: 17/04/2023 Add registration daily report insha Allah

        // TODO: 19/04/2023 User delete bind to home usercounter insha Allah 
        // TODO: 19/04/2023 wide pane remove the drop shadows insha Allah
        // TODO: 23/04/2023 Shift ga ka dhig afsomali 

        // TODO: 23/04/2023 hadii muddo joogo dashboard ka ku celi insha Allah
        // TODO: 23/04/2023 Samee window opener ka hore fade out ku samaynaya ka danbana fadin  insha Allah 

        // TODO: 25/04/2023 Tables ka fontkiisa bedel insha Allah

        // TODO: 27/04/2023 CHeck image upload remender

        // TODO: 27/04/2023 Backub insha Allah 

        // TODO: 28/04/2023 phone trigger
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED
        );
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}