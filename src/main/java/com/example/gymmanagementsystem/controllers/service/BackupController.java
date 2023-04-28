package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    private TextField name;

    @FXML
    private JFXButton pathBtn;

    @FXML
    private JFXButton restoreBtn;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) listView.getScene().getWindow();
            enterKeyFire(backupBtn, stage);
        });
//        backupService.setOnSucceeded(e -> {
//            backupBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/backup.png"));
//            backupBtn.setText("Backup");
//        });

    }

    @FXML
    void cancelHandler() {
        closeStage(stage, listView.getParent());
    }

    @FXML
    void backupHandler() throws SQLException {
//        System.out.println("Hello");
        pathSelector();
//
//        startTask(backupService, backupBtn, "backuping");

    }

    @FXML
    void pathHandler() throws SQLException {
        System.out.println("Hello");
//
    }

    public void restoreHandler() {
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        if (!activeUser.getRole().equals("super_admin")) {
            restoreBtn.setDisable(true);
        }
    }


    private void pathSelector() {
        FileChooser chooser = new FileChooser();
        File selectedPath = chooser.showSaveDialog(null);

        File fileDir = new File(selectedPath.getAbsolutePath() + "/backups");
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        chooser.setInitialDirectory(fileDir);
        // chooser.setInitialDirectory(selec);
//        if (selectedPath.exists()) {
//            System.out.println("Path exist");
//        } else {
//            System.out.println("not exist");
//        }
//        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", ""));

        //BackupService.insertPath(selectedPath.getAbsolutePath() + "/" + name.getText());
        listView.getItems().add(selectedPath.getAbsolutePath());
        System.out.println(selectedPath);
    }
//    private final Service<Void> backupService = new Service<>() {
//        @Override
//        protected Task<Void> createTask() {
//            return new Task<>() {
//                @Override
//                protected Void call() {
//                    try {
//                        Thread.sleep(1000);
//                        BackupService.backup(listView.getSelectionModel().getSelectedItem());
//                        Platform.runLater(() -> informationAlert("Backup successfully."));
//                    } catch (Exception e) {
//                        Platform.runLater(() -> infoAlert(e.getMessage()));
//                    }
//                    return null;
//                }
//            };
//        }
//    };

}
