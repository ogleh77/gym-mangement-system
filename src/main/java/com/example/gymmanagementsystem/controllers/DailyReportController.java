package com.example.gymmanagementsystem.controllers;

import com.example.gymmanagementsystem.controllers.printers.CustomersPrinter;
import com.example.gymmanagementsystem.dao.CustomerService;
import com.example.gymmanagementsystem.entities.Customers;
import com.example.gymmanagementsystem.entities.DailyReport;
import com.example.gymmanagementsystem.entities.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DailyReportController extends CommonClass implements Initializable {
    @FXML
    private JFXButton printBtn;
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

    @FXML
    private DatePicker printEndDate;

    @FXML
    private DatePicker printStartDate;
    @FXML
    private JFXCheckBox allCustomers;
    @FXML
    private ProgressBar progress;

    private ObservableList<DailyReport> reports;

    private Stage stage;
    // private final ObservableList<DailyReport> weeklyReport;
    private final URL url = getClass().getResource("/com/example/shipable/style/icons/icons8-search-50.png");

    public DailyReportController() throws SQLException {
        // weeklyReport = DailyReportModel.getWeeklyPayments(LocalDate.now());
    }

    private File selectedFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) progress.getScene().getWindow();
        });
    }

    @FXML
    void searchHandler() {

    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    saveTo();

                    return null;
                }
            };
        }
    };

    public void printHandler() throws SQLException {
       saveTo();


        if (selectedFile != null) {
            ObservableList<Customers> customers = CustomerService.fetchAllCustomer(activeUser);
            CustomersPrinter printer = new CustomersPrinter();

            printer.printCustomers(customers, selectedFile);
        }
    }


    private void saveTo() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        selectedFile = fileChooser.showSaveDialog(stage);
    }


    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
    }
}
