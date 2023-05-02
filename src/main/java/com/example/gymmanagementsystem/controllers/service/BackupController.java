package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.data.dto.BackupService;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BackupController extends CommonClass implements Initializable {
    @FXML
    private AnchorPane backupPane;
    @FXML
    private JFXButton backupBtn;
    @FXML
    private ListView<String> listView;
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
            init();
            stage = (Stage) backupPane.getScene().getWindow();
            OpenWindow.stageDropped(stage, topPane);
            OpenWindow.stageDrag(stage, topPane);
        });

        backupService.setOnSucceeded(e -> {
            backupBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/backup.png"));
            backupBtn.setText("Backup");
        });

        restoreService.setOnSucceeded(e -> {
            restoreBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/icons8-reset-48.png"));
            restoreBtn.setText("Restore");

        });
    }

    @FXML
    void backupHandler() {
        try {
            if (listView.getSelectionModel().getSelectedItem() == null && !listView.getItems().isEmpty())
                throw new RuntimeException("Fadlan marka hore dooro location-ka backup kagu " +
                        "kugu kaydsanaa" + " Kana dooro liiska sare");
            String path = listView.getSelectionModel().getSelectedItem();

            if (!listView.getItems().isEmpty()) BackupService.backupTo(path);
            else pathSelector();
            startTask(backupService, backupBtn, "Backupping");
            backupTime.setText("Date: " + BackupService.lastBackupDate());

        } catch (Exception e) {
            if (e instanceof RuntimeException) Alerts.waningAlert(e.getMessage());
            else Alerts.errorAlert(e.getMessage());
        }

    }

    @FXML
    void restoreHandler() {
        try {
            if (listView.getSelectionModel().getSelectedItem() == null && !listView.getItems().isEmpty())
                throw new RuntimeException("Fadlan marka hore dooro location-ka backup kagu " +
                        "kugu kaydsanaa" + " Kana dooro liiska sare");
            startTask(restoreService, restoreBtn, "Restoring");
        } catch (RuntimeException e) {
            Alerts.waningAlert(e.getMessage());
        }
    }

    @FXML
    void cancelHandler() {
        OpenWindow.closeStage(stage, backupPane);
    }

    private final Service<Void> backupService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(1000);
                        String message = "Waxaad samasay backup waxadna dhigatay ";
                        Platform.runLater(() -> {
                            Alerts.notificationAlert(!listView.getItems().isEmpty() ?
                                    message + " " + listView.getSelectionModel().getSelectedItem() : message + " " + selectedFile.getAbsolutePath());
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert("FROM SQL " + e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };

    private final Service<Void> restoreService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(1000);
                        BackupService.restoreFrom();
                        // TODO: 02/05/2023 Logout
                        Platform.runLater(() -> {
                            Alerts.notificationAlert("Waxaad ku gulaysatay inaad kasoo restore garayso " +
                                    listView.getSelectionModel().getSelectedItem());
                        });

                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            Alerts.errorAlert(e.getMessage());
                            e.printStackTrace();
                        });
                    }
                    return null;
                }
            };
        }
    };


    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        if (!activeUser.getRole().equals("admin")) {
            restoreBtn.setDisable(true);
        }
    }

    private void init() {
        try {
            if (BackupService.lastBackupPath() == null) {
                listView.setPlaceholder(new Label("Halkan waxaad ka arki dontaa" + " position-ka\n" + "Backup-kaga kadib markaad samayso!"));
            } else {
                listView.getItems().add(BackupService.lastBackupPath());
                backupTime.setText(BackupService.lastBackupDate());
            }
        } catch (SQLException e) {
            Alerts.errorAlert("FROM sqlite :_ " + e.getMessage());
        }
    }


    private void pathSelector() throws SQLException {
        FileChooser chooser = new FileChooser();
        selectedFile = chooser.showSaveDialog(stage);
        BackupService.backupTo(selectedFile.getAbsolutePath());
        listView.getItems().add(selectedFile.getAbsolutePath());
    }


}
