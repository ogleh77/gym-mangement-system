package com.example.gymmanagementsystem.controllers;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutLeft;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
    private Stage dashboardStage;
    private boolean visible = false;

    @FXML
    private HBox topPane;
    private double xOffset = 0;
    private double yOffset = 0;

    public DashboardController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            gymName.textProperty().bind(currentGym.gymNameProperty());
            activeUserName.textProperty().bind(activeUser.usernameProperty());
            dashboardStage = (Stage) activeProfile.getScene().getWindow();
            borderPane.setLeft(null);

            borderPaneDrag();
            borderPaneDropped();
            addUserBtn.setDisable(!activeUser.getRole().equals("super_admin"));
            updateUserBtn.setDisable(!activeUser.getRole().equals("super_admin"));
            gymBtn.setDisable(!activeUser.getRole().equals("super_admin"));
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


    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }

    //--------------------__Helpers_------------------
    private void borderPaneDrag() {
        topPane.setOnMousePressed(event -> {
            xOffset = dashboardStage.getX() - event.getScreenX();
            yOffset = dashboardStage.getY() - event.getScreenY();
        });
    }

    private void borderPaneDropped() {
        topPane.setOnMouseDragged(event -> {
            dashboardStage.setX(event.getScreenX() + xOffset);
            dashboardStage.setY(event.getScreenY() + yOffset);
        });
    }
}
