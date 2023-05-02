package com.example.gymmanagementsystem.controllers;

import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController extends CommonClass implements Initializable {
    @FXML
    private ProgressBar progress;
    @FXML
    private Label waiting;
    @FXML
    private Label username;
    @FXML
    private ImageView loadingImage;
    @FXML
    private HBox topPane;
    private Stage stage;
    private final ObservableList<Customers> warningList;

    public SplashScreenController() {
        this.warningList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) topPane.getScene().getWindow();
            OpenWindow.stageDrag(stage, topPane);
            OpenWindow.stageDropped(stage, topPane);
        });
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        Thread thread = new Thread(fetchOnlineCustomers);
        thread.setDaemon(true);
        thread.start();
        progress.progressProperty().bind(fetchOnlineCustomers.progressProperty());
        username.setText(activeUser.getUsername());
        waiting.textProperty().bind(fetchOnlineCustomers.messageProperty());

        URL url = getClass().getResource(activeUser.getGender().equals("Male") ? images[1] : images[2]);
        Image image = new Image(String.valueOf(url));
        loadingImage.setImage(image);
    }

    public Task<Void> fetchOnlineCustomers = new Task<>() {
        @Override
        protected Void call() {
            System.out.println("Done");
            return null;
        }
    };
}
