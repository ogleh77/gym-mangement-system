package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.dao.service.BackupService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BackupController extends CommonClass implements Initializable {
    @FXML
    private JFXButton backupBtn;
    @FXML
    private Label lastBackup;
    @FXML
    private ListView<String> listView;

    @FXML
    private JFXButton pathBtn;
    @FXML
    private JFXButton restoreBtn;
    @FXML
    private Label backupTime;
    @FXML
    private HBox topPane;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) listView.getScene().getWindow();
            enterKeyFire(backupBtn, stage);
            paneDrag(stage, topPane);
            paneDropped(stage, topPane);
        });
        init();
    }

    @FXML
    void cancelHandler() {
        closeStage(stage, listView.getParent());
    }

    @FXML
    void backupHandler() {
        String location = listView.getSelectionModel().getSelectedItem();
        try {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                throw new RuntimeException("Fadlan marka hore dooro location-ka backup kagu kugu kaydsanaa"
                        + " Kana dooro liiska sare");
            }

            if (listView.getItems().isEmpty()) {
                backup(location);
                listView.getItems().add(BackupService.lastBackupPath());
            } else {
                backup(location);
            }
            backupTime.setText("Date: " + BackupService.lastBackup());
            Alerts.notificationAlert("Successfully Backup to " + location, "Good job");
        } catch (Exception e) {
            if (e instanceof SQLException) {
                Alerts.errorAlert("FROM SQL " + e.getMessage(), "Khalad ayaa dhacay");
            } else {
                Alerts.waningAlert(e.getMessage(), "Ogow!");
            }
        }

    }

    @FXML
    void restoreHandler() {
        String location = listView.getSelectionModel().getSelectedItem();
        try {
            if (location == null) {
                throw new RuntimeException("Fadlan marka hore dooro location-ka backup kagu kugu kaydsan yahay"
                        + " Kana dooro liiska sare");
            }
            BackupService.restore(location);
            Alerts.notificationAlert("Successfully restored from " + location, "good");
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                Alerts.waningAlert(e.getMessage(), "Hubso");
            } else {
                Alerts.errorAlert(e.getMessage(), "Khalad baa dhacay");
            }
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        if (!activeUser.getRole().equals("super_admin")) {
            restoreBtn.setDisable(true);
        }
    }
    private void init() {
        try {
            if (BackupService.lastBackupPath() == null) {
                listView.setPlaceholder(new Label("Halkan waxaad ka arki dontaa" + " position-ka\n" + "Backup-kaga kadib markaad samayso!"));
            } else {
                listView.getItems().add(BackupService.lastBackupPath());
                backupTime.setText(BackupService.lastBackup());
            }
        } catch (SQLException e) {
            Alerts.errorAlert("FROM SQL:_ " + e.getMessage(), "Khaladbaa dhacay");
        }
    }
}
