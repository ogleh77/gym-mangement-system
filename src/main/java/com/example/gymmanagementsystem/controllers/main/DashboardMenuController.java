package com.example.gymmanagementsystem.controllers.main;

import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardMenuController extends CommonClass implements Initializable {
    private BorderPane borderPane;
    private HBox menuHBo;
    private Stage thisStage;
    private MenuItem logout;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> thisStage = (Stage) borderPane.getScene().getWindow());
    }

    @FXML
    void homeMenuHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/newviews/main/home.fxml", borderPane, menuHBo, logout);
//        HomeController controller = loader.getController();
//        controller.setActiveUser(activeUser);
//        controller.setBorderPane(borderPane);
    }

    @FXML
    void registrationMenuHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/newviews/main/registrations.fxml", borderPane, menuHBo, logout);
//        RegistrationController controller = loader.getController();
//        controller.setActiveUser(activeUser);
//        controller.setBorderPane(borderPane);
    }


    @FXML
    void outDatedMenuHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/newviews/info/outdated.fxml", borderPane, menuHBo, logout);
//        OutDatedController controller = loader.getController();
//        controller.setActiveUser(activeUser);
    }

    @FXML
    void logOutMenuHandler() {
        //  OpenWindow.reOpenLogin(thisStage, activeUser.getUsername(), borderPane);
    }

    @FXML
    void reportMenuHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/newviews/info/dailyReports.fxml", borderPane, menuHBo, logout);
//        ReportController controller = loader.getController();
//        controller.setActiveUser(activeUser);
    }

    public void setMenus(HBox menuHBox, MenuItem logout) {
        this.menuHBo = menuHBox;
        this.logout = logout;
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }


//    public static SlideInUp getSlideUp() {
//        if (slideInUp != null) return slideInUp;
//        slideInUp = new SlideInUp();
//        return slideInUp;
//    }

}
