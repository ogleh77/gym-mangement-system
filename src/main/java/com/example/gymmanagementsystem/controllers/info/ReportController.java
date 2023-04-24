package com.example.gymmanagementsystem.controllers.info;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.DailyReport;
import com.example.gymmanagementsystem.exporttoxls.ExportDataToExcel;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.example.gymmanagementsystem.models.service.DailyReportModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportController extends CommonClass implements Initializable {

    @FXML
    private TableColumn<DailyReport, String> dailyReportDay;
    @FXML
    private TableColumn<DailyReport, Integer> totalFemale;

    @FXML
    private TableColumn<DailyReport, Integer> totalMale;

    @FXML
    private TableColumn<DailyReport, Integer> totalRegister;

    @FXML
    private TableColumn<DailyReport, Integer> totalVipBox;

    @FXML
    private TableView<DailyReport> dailyTbView;
    //---------------------Generate report-------------–
    @FXML
    private TableView<DailyReport> reportTbView;

    @FXML
    private TableColumn<DailyReport, Integer> reportTotalFemale;

    @FXML
    private TableColumn<DailyReport, Integer> reportTotalMale;

    @FXML
    private TableColumn<DailyReport, Integer> reportTotalReg;

    @FXML
    private TableColumn<DailyReport, Integer> reportTotalVip;

    @FXML
    private TableColumn<DailyReport, String> reportTotalDay;

    @FXML
    private DatePicker endDate;

    @FXML
    private ImageView imgViewSearch;


    @FXML
    private JFXButton searchBtn;

    @FXML
    private DatePicker startDate;
    private ObservableList<DailyReport> reports;
    private final ObservableList<DailyReport> weeklyReport;

    public ReportController() throws SQLException {
        weeklyReport = DailyReportModel.getWeeklyPayments(LocalDate.now());
    }

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
        Platform.runLater(() -> {
            init();
            try {
                weeklyInitTable();
            } catch (Exception e) {
                Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
            }
        });
        exportingService.setOnSucceeded(e -> {
            exportLabel.setVisible(false);
            loadingImage.setVisible(false);
        });

        service.setOnSucceeded(e -> {
            Image image = new Image(String.valueOf(url));
            imgViewSearch.setImage(image);
            searchBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/icons8-search-50.png"));
            try {
                generateTable();
            } catch (SQLException ex) {
                Alerts.errorAlert(ex.getMessage());
            }
        });
    }

    @FXML
    void searchHandler() {
        getMandatoryFields().clear();
        getMandatoryFields().addAll(startDate, endDate);
        if (isValid(getMandatoryFields(), null)) {
            startTask(service, searchBtn, "");
        }
    }

    @FXML
    void exportToExcelHandler() {
        getMandatoryFields().clear();
        if (!customersOnly.isSelected()) {
            getMandatoryFields().addAll(printStartDate, printEndDate);
        }
        if (validChoose() && isValid(getMandatoryFields(), null)) {
            saveFile();
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

    private final Service<Void> exportingService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        chooseExports(printStartDate.getValue(), printEndDate.getValue());
                        Thread.sleep(1000);
                        Platform.runLater(() -> Alerts.notificationAlert("Report exported successfully \npath: \n"
                                + selectedFile.getAbsolutePath()));
                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    reports = DailyReportModel.getPaymentsBetween(startDate.getValue(), endDate.getValue());
                    return null;
                }
            };
        }
    };

    //---------------------Helper methods---------------------–

    private void init() {
        customersOnly.setToggleGroup(choseGroup);
        onlineCustomers.setToggleGroup(choseGroup);
        offlineCustomers.setToggleGroup(choseGroup);
        pendCustomers.setToggleGroup(choseGroup);
        if (reports == null) {
            Label label = new Label("RAADI WAKHTIGA AAD U BAHANTAY");
            reportTbView.setPlaceholder(label);
        }
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

    private void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.xlsx"));
        selectedFile = fileChooser.showSaveDialog(customersOnly.getScene().getWindow());
        System.out.println(selectedFile);
    }

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

    private void weeklyInitTable() throws SQLException {
        tableFields(dailyReportDay, totalRegister, totalMale, totalFemale, totalVipBox);
        getMandatoryFields().addAll(startDate, endDate);
        if (weeklyReport.isEmpty()) {
            Label label = new Label("MA JIRO WAX REPORT AH OO U WIIGAN ");
            dailyTbView.setPlaceholder(label);
        }
        dailyTbView.setItems(DailyReportModel.getWeeklyPayments(LocalDate.now()));

    }

    private void generateTable() throws SQLException {
        tableFields(reportTotalDay, reportTotalReg, reportTotalMale, reportTotalFemale, reportTotalVip);
        if (reports.isEmpty()) {
            Label label = new Label("MA JIRO WAX REPORT AH OO U DHAXAYA " + startDate.getValue() + " ILaa " + endDate.getValue());
            reportTbView.setPlaceholder(label);
            return;
        }
        reportTbView.setItems(reports);
        reportTbView.refresh();
    }

    private void tableFields(TableColumn<DailyReport, String> reportTotalDay,
                             TableColumn<DailyReport, Integer> reportTotalReg,
                             TableColumn<DailyReport, Integer> reportTotalMale,
                             TableColumn<DailyReport, Integer> reportTotalFemale,
                             TableColumn<DailyReport, Integer> reportTotalVip) {

        reportTotalDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        reportTotalReg.setCellValueFactory(new PropertyValueFactory<>("registrations"));
        reportTotalMale.setCellValueFactory(new PropertyValueFactory<>("male"));
        reportTotalFemale.setCellValueFactory(new PropertyValueFactory<>("female"));
        reportTotalVip.setCellValueFactory(new PropertyValueFactory<>("vipBox"));
    }


}