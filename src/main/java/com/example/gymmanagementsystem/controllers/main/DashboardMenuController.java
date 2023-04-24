package com.example.gymmanagementsystem.controllers.main;


import com.example.gymmanagementsystem.controllers.info.OutDatedController;
import com.example.gymmanagementsystem.controllers.info.ReportController;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> thisStage = (Stage) borderPane.getScene().getWindow());
    }

    @FXML
    void homeMenuHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/newviews/main/home.fxml", borderPane, menuHBo);
        HomeController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
    }

    @FXML
    void registrationMenuHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/newviews/main/registrations.fxml", borderPane, menuHBo);
        RegistrationController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
    }


    @FXML
    void outDatedMenuHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/newviews/info/outdated.fxml", borderPane, menuHBo);
        OutDatedController controller = loader.getController();
        controller.setActiveUser(activeUser);
    }

    @FXML
    void logOutMenuHandler() {
        OpenWindow.reOpenLogin(thisStage, activeUser.getUsername(), borderPane);
    }

    @FXML
    void reportMenuHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openFromDashboardWindow("/com/example/gymmanagementsystem/newviews/info/dailyReports.fxml", borderPane, menuHBo);
        ReportController controller = loader.getController();
        controller.setActiveUser(activeUser);
    }

    public void setMenus(HBox menuHBox) {
        this.menuHBo = menuHBox;

    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
