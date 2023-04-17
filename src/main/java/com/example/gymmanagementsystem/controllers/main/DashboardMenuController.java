package com.example.gymmanagementsystem.controllers.main;


import com.example.gymmanagementsystem.controllers.info.OutDatedController;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class DashboardMenuController extends CommonClass {
    private BorderPane borderPane;
    private VBox sidePane;
    private HBox menuHBo;
    private StackPane notificationsHBox;
    private final ButtonType ok = new ButtonType("Haa");
    private final ButtonType no = new ButtonType("Maya");

    @FXML
    void homeMenuHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymmanagementsystem/newviews/main/home.fxml", borderPane, menuHBo, notificationsHBox);
        HomeController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
    }

    @FXML
    void registrationMenuHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymmanagementsystem/newviews/main/registrations.fxml", borderPane, menuHBo, notificationsHBox);
        RegistrationController controller = loader.getController();
        controller.setActiveUser(activeUser);
        controller.setBorderPane(borderPane);
    }


    @FXML
    void outDatedMenuHandler() throws IOException {
        FXMLLoader loader = openWindow("/com/example/gymmanagementsystem/newviews/info/outdated.fxml", borderPane, menuHBo, notificationsHBox);
        OutDatedController controller = loader.getController();
        controller.setActiveUser(activeUser);
    }

    @FXML
    void logOutMenuHandler() {
        openLogin();
    }

    @FXML
    void reportMenuHandler() throws IOException {
        openWindow("/com/example/gymmanagementsystem/newviews/info/dailyReports.fxml", borderPane, menuHBo, notificationsHBox);

    }

    public void setMenus(BorderPane borderPane, VBox sidePane, HBox menuHBox, StackPane notificationsHBox) {
        this.borderPane = borderPane;
        this.sidePane = sidePane;
        this.notificationsHBox = notificationsHBox;
        this.menuHBo = menuHBox;
    }


    private void openLogin() {
        Stage thisStage = (Stage) menuHBo.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ma hubtaa inaad ka baxdo user ka " + activeUser.getUsername(), no, ok);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ok) {
            System.out.println("Yes");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/views/service/login.fxml"));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            Scene scene;
            closeStage(thisStage, menuHBo.getParent());
            try {
                scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        } else alert.close();
    }
}
