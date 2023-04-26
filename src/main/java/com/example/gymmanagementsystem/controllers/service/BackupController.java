package com.example.gymmanagementsystem.controllers.service;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class BackupController implements Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void cancelHandler(MouseEvent mouseEvent) {
    }

    public void backupHandler(ActionEvent actionEvent) {
    }

    public void pathHandler(ActionEvent actionEvent) {
    }

    public void restoreHandler(ActionEvent actionEvent) {
    }
}
