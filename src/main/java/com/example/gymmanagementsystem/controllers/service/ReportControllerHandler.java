package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.exporttoxls.ExportDataToExcel;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportControllerHandler extends CommonClass implements Initializable {
    @FXML
    private JFXRadioButton customersOnly;
    @FXML
    private JFXRadioButton offlineCustomers;

    @FXML
    private JFXRadioButton onlineCustomers;
    @FXML
    private JFXRadioButton pendCustomers;
    @FXML
    private DatePicker printEndDate;

    @FXML
    private DatePicker printStartDate;
    @FXML
    private ImageView loadingImage;
    @FXML
    private Label exportLabel;
    private File selectedFile;
    private final ToggleGroup choseGroup = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::init);

        exportingService.setOnSucceeded(e -> {
            System.out.println("Done");
            exportLabel.setVisible(false);
            loadingImage.setVisible(false);
        });
    }


    @FXML
    void exportToExcelHandler() {
        saveFile();

            if (validChoose() && isValid(getMandatoryFields(), null)) {
                if (selectedFile != null) {
                if (start) {
                    exportingService.restart();
                } else {
                    exportingService.start();
                    start = true;
                }
            }
        }

    }


    private void init() {
        getMandatoryFields().addAll(printEndDate, printStartDate);
        customersOnly.setToggleGroup(choseGroup);
        onlineCustomers.setToggleGroup(choseGroup);
        offlineCustomers.setToggleGroup(choseGroup);
        pendCustomers.setToggleGroup(choseGroup);
    }

    private boolean validChoose() {
        if (choseGroup.getSelectedToggle() == null) {
            ((Node) choseGroup.getToggles().get(0)).setStyle("-fx-border-color: #cb3d3d;-fx-border-width: 2");
            ((Node) choseGroup.getToggles().get(1)).setStyle("-fx-border-color: #cb3d3d;-fx-border-width: 2");
            ((Node) choseGroup.getToggles().get(2)).setStyle("-fx-border-color: #cb3d3d;-fx-border-width: 2");
            ((Node) choseGroup.getToggles().get(3)).setStyle("-fx-border-color: #cb3d3d;-fx-border-width: 2");
            getShake().setNode((Node) choseGroup.getToggles().get(0));
            getShake().play();
            getShake().setNode((Node) choseGroup.getToggles().get(1));
            getShake().play();
            getShake().setNode((Node) choseGroup.getToggles().get(2));
            getShake().play();
            getShake().setNode((Node) choseGroup.getToggles().get(3));
            getShake().play();

            return false;
        } else {
            ((Node) choseGroup.getToggles().get(0)).setStyle(null);
            ((Node) choseGroup.getToggles().get(1)).setStyle(null);
            ((Node) choseGroup.getToggles().get(2)).setStyle(null);
            ((Node) choseGroup.getToggles().get(3)).setStyle(null);
        }
        return true;
    }

    private final Service<Void> exportingService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        chooseExports(printStartDate.getValue(), printEndDate.getValue());
                        Thread.sleep(1000);
                        Platform.runLater(() -> infoAlert("Report exported successfully path: \n"
                                + selectedFile.getAbsolutePath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorMessage(e.getMessage());
                    }

                    return null;
                }
            };
        }
    };


    private void chooseExports(LocalDate start, LocalDate end) throws SQLException {
        loadingImage.setVisible(true);
        exportLabel.setVisible(true);
        if (customersOnly.isSelected()) {
            ObservableList<Customers> customers = CustomerService.fetchAllCustomer(activeUser);
            if (customers.isEmpty()) {
                throw new RuntimeException("Fadlan kuguma jiraan wax macamiil ah systemka ");
            }
            ExportDataToExcel.exportAllCustomersToXls(customers, selectedFile);

        } else if (onlineCustomers.isSelected()) {
            ObservableList<Customers> customers = CustomerService.
                    fetchOnlineCustomersWhereDateBetween(activeUser,
                            printStartDate.getValue(), printEndDate.getValue());
            if (customers.isEmpty()) {
                throw new RuntimeException("Ma jiro macamiil wakhtigu u socdo una dhaxeya " + start + " ilaa " + end);
            }
            ExportDataToExcel.exportOnlineCustomersToExcel(customers, selectedFile);
        } else if (offlineCustomers.isSelected()) {

            ObservableList<Customers> customers = CustomerService.
                    fetchOfflineCustomersWhereDateBetween(activeUser,
                            printStartDate.getValue(), printEndDate.getValue());
            if (customers.isEmpty()) {
                throw new RuntimeException("Ma jiro macamiil wakhtigu u ka dhacay una dhaxeya " + start + " ilaa " + end);
            }
            ExportDataToExcel.exportOfflineCustomersToExcel(customers, selectedFile);
        } else if (pendCustomers.isSelected()) {

            ObservableList<Customers> customers = CustomerService.
                    fetchPendCustomersWhereDateBetween(activeUser,
                            printStartDate.getValue(), printEndDate.getValue());
            if (customers.isEmpty()) {
                throw new RuntimeException("Ma jiro macamiil wakhigooda loo hakiyay una dhaxeya " + start + " ilaa " + end);
            }
            ExportDataToExcel.exportPendingCustomersToExcel(customers, selectedFile);
        }
    }

    private void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.xlsx"));
        selectedFile = fileChooser.showSaveDialog(customersOnly.getScene().getWindow());
        System.out.println(selectedFile);
    }

}
