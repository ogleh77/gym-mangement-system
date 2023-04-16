package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.dao.service.BoxService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.entities.service.Box;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.example.gymmanagementsystem.helpers.CustomException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    private JFXButton updateBtn;
    @FXML
    private TextField zaad;
    @FXML
    private JFXCheckBox uploadImageCheck;
    private final Gym currentGym;
    private Stage thisStage;
    int nextId;


    public GymController() throws SQLException {
        currentGym = GymService.getGym();
        nextId = BoxService.nextBoxID();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initData();
            thisStage = (Stage) gymName.getScene().getWindow();
            enterKeyFire(updateBtn, thisStage);
        });

        discountValidation();
        pendValidation();
        zaadValidation();
        eDahabValidation();
    }

    @FXML
    void cancelHandler() {
        closeStage(thisStage, listView.getParent());
    }

    @FXML
    void createBoxHandler() throws SQLException {
        getMandatoryFields().clear();
        getMandatoryFields().add(addBox);

        if (isValid(getMandatoryFields(), null)) {
            if (!addBox.getText().isBlank() && !addBox.getText().isEmpty()) {
                Box box = new Box(nextId, addBox.getText(), true);
                System.out.println(box.getBoxId());
                try {
                    BoxService.insertBox(box);
                    listView.getItems().add(box);
                    BoxService.fetchBoxes().add(box);
                    informationAlert("New box added successfully").showAndWait();
                } catch (CustomException e) {
                    errorMessage(e.getMessage());
                }
                nextId++;
            }
        }
    }

    @FXML
    void deleteBoxHandler() {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            Box box = listView.getSelectionModel().getSelectedItem();
            try {
                BoxService.deleteBox(box);
                listView.getItems().remove(box);
                informationAlert("Box deleted successfully").showAndWait();
            } catch (SQLException e) {
                errorMessage(e.getMessage());
            }
        }
    }

    @FXML
    void updateHandler() {
        getMandatoryFields().clear();
        getMandatoryFields().addAll(gymName, zaad, eDahab, maxDiscount, pendDate);
        if (isValid(getMandatoryFields(), null)) {
            try {
                double max_discount = Double.parseDouble(maxDiscount.getText());
                int zad_number = Integer.parseInt(zaad.getText());
                int eDab_number = Integer.parseInt(eDahab.getText());
                int pend_date = Integer.parseInt(pendDate.getText());

                currentGym.setGymName(gymName.getText());
                currentGym.seteDahab(zad_number);
                currentGym.setMaxDiscount(max_discount);
                currentGym.seteDahab(eDab_number);
                currentGym.setZaad(zad_number);

                currentGym.setPendingDate(pend_date);

                currentGym.setMaxDiscount(max_discount);
                currentGym.setImageUpload(uploadImageCheck.isSelected());
                GymService.updateGym(currentGym);
                infoAlert("Gym updated successfully");
            } catch (Exception e) {
                errorMessage(e.getMessage());
            }
        }

    }

    //----------------------Helper methods-------------------
    private void initData() {
        System.out.println(currentGym);
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
}