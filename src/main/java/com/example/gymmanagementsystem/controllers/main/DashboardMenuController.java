package com.example.gymmanagementsystem.controllers.main;

import com.example.gymmanagementsystem.controllers.informations.OutdatedController;
import com.example.gymmanagementsystem.controllers.informations.ReportController;
import com.example.gymmanagementsystem.dependencies.Alerts;
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
    void homeMenuHandler() {
        try {
            FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/views/main/home.fxml", borderPane, menuHBo, logout);
            HomeController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }

    }

    @FXML
    void registrationMenuHandler() {
        try {
            FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/views/main/registrations.fxml", borderPane, menuHBo, logout);
            RegistrationController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }


    @FXML
    void outDatedMenuHandler() {
        try {
            FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/views/info/outdated.fxml", borderPane, menuHBo, logout);
            OutdatedController controller = loader.getController();
            controller.setActiveUser(activeUser);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void logOutMenuHandler() {
        OpenWindow.reOpenLogin(thisStage, activeUser.getUsername(), borderPane);
    }

    @FXML
    void reportMenuHandler() {
        try {
            FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/views/info/dailyReports.fxml", borderPane, menuHBo, logout);
            ReportController controller = loader.getController();
            controller.setActiveUser(activeUser);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    public void setMenus(HBox menuHBox, MenuItem logout) {
        this.menuHBo = menuHBox;
        this.logout = logout;
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }


}
