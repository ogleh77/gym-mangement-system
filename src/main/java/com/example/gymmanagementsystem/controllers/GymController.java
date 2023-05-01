package com.example.gymmanagementsystem.controllers;

import com.example.gymmanagementsystem.data.dto.BoxService;
import com.example.gymmanagementsystem.data.dto.GymService;
import com.example.gymmanagementsystem.data.entities.service.Box;
import com.example.gymmanagementsystem.data.entities.service.Gym;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GymController extends CommonClass implements Initializable {
    @FXML
    private TextField addBox;

    @FXML
    private TextField eDahab;

    @FXML
    private TextField gymName;

    @FXML
    private ListView<Box> listView;

    @FXML
    private TextField maxDiscount;

    @FXML
    private TextField pendDate;

    @FXML
    private HBox topPane;
    @FXML
    private AnchorPane gymPane;
    @FXML
    private JFXButton updateBtn;

    @FXML
    private JFXCheckBox uploadImageCheck;

    @FXML
    private TextField zaad;

    private Stage stage;
    private final Gym currentGym;
    private int nextId;

    public GymController() throws SQLException {
        this.currentGym = GymService.getGym();
        this.nextId = BoxService.nextId();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) topPane.getScene().getWindow();
            OpenWindow.stageDrag(stage, topPane);
            OpenWindow.stageDropped(stage, topPane);
            OpenWindow.stageEnterKeyFire(updateBtn, stage);
        });
        initData();
        pendValidation();
        discountValidation();
        zaadValidation();
        eDahabValidation();
    }

    @FXML
    void cancelHandler() {
        OpenWindow.closeStage(stage, gymPane);
    }

    @FXML
    void createBoxHandler() {
        getMandatoryFields().clear();
        getMandatoryFields().add(addBox);

        if (isValid(getMandatoryFields(), null)) {
            if (!addBox.getText().isBlank() && !addBox.getText().isEmpty()) {
                Box box = new Box(nextId, addBox.getText(), true);
                try {
                    BoxService.insertBox(box);
                    listView.getItems().add(box);
                    BoxService.fetchBoxes().add(box);
                    Alerts.notificationAlert("New box has been added successfully");
                    nextId++;
                } catch (Exception e) {
                    if (e instanceof SQLException) Alerts.errorAlert(e.getMessage());
                    else Alerts.waningAlert(e.getMessage());
                }
            }
        }
    }

    @FXML
    void deleteBoxHandler() {
        Box box = listView.getSelectionModel().getSelectedItem();
        try {
            if (box == null) {
                throw new RuntimeException("Fadlan liiska sare ka doooro khanada aad masixi rabto.");
            }
            if (!box.isReady()) {
                throw new RuntimeException(box.getBoxName()+" ma masaxi kartid sababto ah waxa isticmalaya macmiil.");
            }
            BoxService.deleteBox(box);
            listView.getItems().remove(box);
            Alerts.notificationAlert("Box deleted successfully");
        } catch (Exception e) {
            if (e instanceof SQLException) Alerts.errorAlert(e.getMessage());
            else Alerts.waningAlert(e.getMessage());
        }
    }

    @FXML
    void updateHandler() {
        getMandatoryFields().clear();
        getMandatoryFields().addAll(gymName, zaad, eDahab, maxDiscount, pendDate);
        if (isValid(getMandatoryFields(), null)) {
            try {
                updateGym(currentGym);
                Alerts.notificationAlert("Gym updated successfully..");
            } catch (Exception e) {
                if (e instanceof NumberFormatException)
                    Alerts.waningAlert("Fadlan ka saar points ka badan qaybta discount-ka");
                else Alerts.errorAlert(e.getMessage());
            }
        }
    }

    //------------------------Helper methods-----------------

    private void initData() {
        gymName.setText(currentGym.getGymName());
        maxDiscount.setText(String.valueOf(currentGym.getMaxDiscount()));
        eDahab.setText((String.valueOf(currentGym.geteDahab())));
        zaad.setText((String.valueOf(currentGym.getZaad())));
        uploadImageCheck.setSelected(currentGym.isImageUpload());
        listView.setItems(currentGym.getVipBoxes());
        pendDate.setText(String.valueOf(currentGym.getPendingDate()));
    }

    private void pendValidation() {
        pendDate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                pendDate.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    private void discountValidation() {
        maxDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxDiscount.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void zaadValidation() {
        zaad.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                zaad.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    private void eDahabValidation() {
        eDahab.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                eDahab.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }


    private void updateGym(Gym gym) throws Exception {
        double max_discount = Double.parseDouble(maxDiscount.getText());
        int zad_number = Integer.parseInt(zaad.getText());
        int eDab_number = Integer.parseInt(eDahab.getText());
        int pend_date = Integer.parseInt(pendDate.getText());
        gym.setGymName(gymName.getText());
        gym.seteDahab(zad_number);
        gym.setMaxDiscount(max_discount);
        gym.seteDahab(eDab_number);
        gym.setZaad(zad_number);

        gym.setPendingDate(pend_date);

        gym.setMaxDiscount(max_discount);
        gym.setImageUpload(uploadImageCheck.isSelected());
        GymService.updateGym(currentGym);
    }
}
