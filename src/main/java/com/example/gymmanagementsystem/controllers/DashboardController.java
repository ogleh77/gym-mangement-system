package com.example.gymmanagementsystem.controllers;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutLeft;
import com.example.gymmanagementsystem.controllers.done.RegistrationsController;
import com.example.gymmanagementsystem.dao.GymService;
import com.example.gymmanagementsystem.entities.Customers;
import com.example.gymmanagementsystem.entities.Gym;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController extends CommonClass implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label gymName;
    @FXML
    private Label warningLabel;
    @FXML
    private HBox warningParent;
    @FXML
    private VBox sidePane;
    @FXML
    private StackPane warningStack;
    @FXML
    private HBox menuHBox;

    @FXML
    private Circle activeProfile;
    @FXML
    private Label activeUserName;
    @FXML
    private MenuItem addUserBtn;
    @FXML
    private MenuItem updateUserBtn;
    @FXML
    private MenuItem gymBtn;

    private final Gym currentGym;
    private ObservableList<Customers> warningList;
    private boolean visible = false;

    @FXML
    private HBox topPane;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private final String url = "/com/example/gymmanagementsystem/views";

    public DashboardController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            // borderPane.setLeft(null);

            stage = (Stage) borderPane.getScene().getWindow();

        });
    }

    @FXML
    void menuClicked() {
        if (visible) {
            SlideOutLeft slideOutLeft = new SlideOutLeft();
            slideOutLeft.setNode(sidePane);
            slideOutLeft.play();
            slideOutLeft.setOnFinished(e -> borderPane.setLeft(null));
        } else {
            new SlideInLeft(sidePane).play();
            borderPane.setLeft(sidePane);
        }
        visible = !visible;
    }

    @FXML
    public void warningHandler() {
    }

    @FXML
    void minimizeHandler() {
        stage.setIconified(true);
    }

    @FXML
    public void closeHandler() {
        closeStage(stage, topPane.getParent());
    }

    @FXML
    public void dashboardHandler() {
    }

    @FXML
    public void homeHandler() {
        try {
            FXMLLoader loader = openWindow("/com/example/gymmanagementsystem/views/home.fxml", borderPane, null, warningStack);
            HomeController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }

    @FXML
    public void registrationHandler() {
        try {
            FXMLLoader loader = openWindow(url + "/main-create/registrations.fxml",
                    borderPane, null, warningStack);
            RegistrationsController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }

    }

    @FXML
    public void reportHandler() {

    }

    @FXML
    public void outdatedHandler() {
    }

    @FXML
    public void backupHandler() {
    }

    @FXML
    public void userCreationHandler() {
    }

    @FXML
    public void updateUserHandler() {
    }

    @FXML
    public void gymHandler() {
    }


}
