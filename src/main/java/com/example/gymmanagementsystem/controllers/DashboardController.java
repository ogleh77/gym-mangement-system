package com.example.gymmanagementsystem.controllers;

import animatefx.animation.FadeIn;
import com.example.gymmanagementsystem.controllers.informations.OutdatedController;
import com.example.gymmanagementsystem.controllers.informations.ReportController;
import com.example.gymmanagementsystem.controllers.informations.WarningController;
import com.example.gymmanagementsystem.controllers.main.HomeController;
import com.example.gymmanagementsystem.controllers.main.RegistrationController;
import com.example.gymmanagementsystem.controllers.users.UserChooserController;
import com.example.gymmanagementsystem.controllers.users.UserUpdateController;
import com.example.gymmanagementsystem.data.dto.BackupService;
import com.example.gymmanagementsystem.data.dto.GymService;
import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.service.Gym;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashboardController extends CommonClass implements Initializable {
    @FXML
    private Circle activeProfile;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Label gymTitle;

    @FXML
    private AnchorPane loadingPane;

    @FXML
    private HBox menuHBox;

    @FXML
    private Label outdatedCounter;

    @FXML
    private StackPane stackPane;

    @FXML
    private HBox topPane;

    @FXML
    private MenuButton username;

    @FXML
    private HBox warningParent;
    @FXML
    private MenuItem logout;
    private ObservableList<Customers> warningList;
    private final Gym currentGym;
    private Stage stage;

    public DashboardController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::init);
    }


    //-----------------Menu controllers----------------------
    @FXML
    void dashboardHandler() {
        try {
            OpenWindow.dashboardWindow(borderPane, menuHBox, activeUser,logout);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void homeHandler() {
        try {
            FXMLLoader loader = OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/main/home.fxml", borderPane);
            HomeController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void registrationHandler() {
        try {
            FXMLLoader loader = OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/main/registrations.fxml", borderPane);
            RegistrationController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void reportHandler() {
        try {
            FXMLLoader loader = OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/info/dailyReports.fxml", borderPane);
            ReportController controller = loader.getController();
            controller.setActiveUser(activeUser);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void outdatedHandler() {
        try {
            FXMLLoader loader = OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/info/outdated.fxml", borderPane);
            OutdatedController controller = loader.getController();
            controller.setActiveUser(activeUser);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void warningHandler() {
        try {
            if (!warningList.isEmpty()) {
                warningParent.setVisible(false);
            }
            FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/info/warning.fxml", topPane);
            WarningController controller = loader.getController();
            controller.setOutdatedCustomers(warningList);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    //--------------------Setting controllers----------------
    @FXML
    void backupHandler() {
        try {
            OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/backup.fxml", activeProfile);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void addUserHandler() {
        try {
            OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-creation.fxml", activeProfile);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void updateUserHandler() {
        try {
            FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-chooser.fxml", menuHBox);
            UserChooserController controller = loader.getController();
            controller.setUsersWithoutActiveOne(activeUser);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void gymHandler() {
        try {
            OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/gym.fxml", activeProfile);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    //----------------------------Close and minimize---------
    @FXML
    void minimizeHandler() {
        stage.setIconified(true);
    }

    @FXML
    void closeHandler() {
        boolean data = Alerts.confirmationAlert("Ma hubtaa inaad ka baxdo systemka"
                , "Maya", "Haa");
        if (data) {
            stage.close();
            try {
                if (BackupService.lastBackupPath() == null) {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Dooro mesha backupka dhiganayso");
                    selectedFile = chooser.showSaveDialog(null);
                    BackupService.backupTo(selectedFile.getAbsolutePath());
                } else {
                    BackupService.backupTo(BackupService.lastBackupPath());
                }
                startTask(backupCloseService, null, "");
            } catch (Exception e) {
                Alerts.errorAlert(e.getMessage());
            }

        }
    }

    //---------------------user profile---------------------
    @FXML
    void updateMeHandler() {
        try {
            FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-update.fxml", activeProfile);
            UserUpdateController controller = loader.getController();
            controller.setActiveUser(activeUser);
            //controller.setStage(dashboardStage);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void logOutMenuHandler() {
        OpenWindow.reOpenLogin(stage, activeUser.getUsername(), borderPane);
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        HBox hBox = (HBox) topPane.getChildren().get(1);
        topPane.getChildren().remove(hBox);
        URL url;
        final String[] profileImages = {"/com/example/gymmanagementsystem/style/icons/man-profile.jpeg", "/com/example/gymmanagementsystem/style/icons/woman-hijap.jpeg"};
        username.setText(activeUser.getUsername() + " [" + activeUser.getRole() + "]");
        if (activeUser.getGender().equals("Male")) {
            if (activeUser.getImage() == null) {
                url = getClass().getResource(profileImages[0]);
                activeProfile.setFill(new ImagePattern(new Image(String.valueOf(url))));
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(activeUser.getImage());
                Image image = new Image(bis);
                activeProfile.setFill(new ImagePattern(image));
            }

        } else if (activeUser.getGender().equals("Female")) {
            if (activeUser.getImage() == null) {
                url = getClass().getResource(profileImages[1]);
                activeProfile.setFill(new ImagePattern(new Image(String.valueOf(url))));
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(activeUser.getImage());
                Image image = new Image(bis);
                activeProfile.setFill(new ImagePattern(image));
            }
        }
    }

    private final Service<Void> backupCloseService = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Platform.runLater(() -> {
                            stackPane.getChildren().add(1, loadingPane);
                            borderPane.setDisable(true);
                        });
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Alerts.errorAlert(e.getMessage());
                    }
                    return null;
                }
            };
        }
    };

    //---------------------------_Helper methods---------------------------
    private void init() {
        this.stage = (Stage) username.getScene().getWindow();
        gymTitle.textProperty().bind(currentGym.gymNameProperty());
        OpenWindow.stageDropped(stage, topPane);
        OpenWindow.stageDrag(stage, topPane);
        stackPane.getChildren().remove(loadingPane);
        try {
            OpenWindow.dashboardWindow(borderPane, menuHBox, activeUser,logout);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }

    }

    public void setWarningList(ObservableList<Customers> warningList) {
        this.warningList = warningList;
        if (warningList.isEmpty()) {
            warningParent.setVisible(false);
        } else {
            outdatedCounter.setText(warningList.size() <= 9 ? String.valueOf(warningList.size()) : "9+");
            FadeIn fadeIn = new FadeIn(warningParent);
            fadeIn.setCycleCount(20);
            fadeIn.play();
        }
    }
}
