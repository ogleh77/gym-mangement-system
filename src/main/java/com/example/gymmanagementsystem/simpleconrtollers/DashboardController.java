package com.example.gymmanagementsystem.simpleconrtollers;

import animatefx.animation.FadeIn;
import com.example.gymmanagementsystem.controllers.info.OutDatedController;
import com.example.gymmanagementsystem.controllers.info.ReportController;
import com.example.gymmanagementsystem.controllers.info.WarningController;
import com.example.gymmanagementsystem.controllers.main.HomeController;
import com.example.gymmanagementsystem.controllers.main.RegistrationController;
import com.example.gymmanagementsystem.controllers.users.UpdateUserController;
import com.example.gymmanagementsystem.controllers.users.UserChooserController;
import com.example.gymmanagementsystem.dao.service.BackupService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
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
    private HBox topPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private HBox menuHBox;
    private Stage dashboardStage;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Circle activeProfile;
    @FXML
    private MenuButton username;
    @FXML
    private Label warningLabel;
    @FXML
    private HBox warningParent;

    @FXML
    private Label gymName;
    @FXML
    private AnchorPane loadingPane;
    private ObservableList<Customers> warningList;
    private final Gym currentGym;

    public DashboardController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            this.dashboardStage = (Stage) topPane.getScene().getWindow();
            paneDrag(dashboardStage, topPane);
            paneDropped(dashboardStage, topPane);
            stackPane.getChildren().remove(loadingPane);
            username.textProperty().bind(activeUser.usernameProperty());
            gymName.textProperty().bind(currentGym.gymNameProperty());
            try {
                OpenWindow.dashboardWindow(borderPane, menuHBox);
            } catch (Exception e) {
                Alerts.errorAlert(e.getMessage());
            }
        });
        backupCloseService.setOnSucceeded(e -> {
            System.out.println("Done");
            closeStage(dashboardStage, topPane);
        });
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
    void dashboardHandler() {
        try {
            OpenWindow.dashboardWindow(borderPane, menuHBox);
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
    void updateMeHandler() {
        try {
            FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-update.fxml", activeProfile);
            UpdateUserController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setStage(dashboardStage);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    void updateUserHandler() {
        try {
            FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-chooser.fxml", menuHBox);
            UserChooserController controller = loader.getController();
            controller.tempActiveUser(activeUser);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void outdatedHandler() {
        try {
            FXMLLoader loader = OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/info/outdated.fxml", borderPane);
            OutDatedController controller = loader.getController();
            controller.setActiveUser(activeUser);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void closeHandler() {
        boolean data = Alerts.confirmationAlert("Ma hubtaa inaad ka baxdo systemka");
        if (data) {
            try {
                if (BackupService.lastBackupPath() == null) {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle("Dooro mesha backupka dhiganayso");
                    selectedFile = chooser.showSaveDialog(null);
                    BackupService.backup(selectedFile.getAbsolutePath());
                } else {
                    BackupService.backup(BackupService.lastBackupPath());
                }
                startTask(backupCloseService, null, "");
            } catch (Exception e) {
                Alerts.errorAlert(e.getMessage());
            }

        }
    }

    @FXML
    void minimizeHandler() {
        dashboardStage.setIconified(true);
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
    void backupHandler() {
        try {
            OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/backup.fxml", activeProfile);
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

    @FXML
    void notificationHandler() {
        try {
            if (!warningList.isEmpty()) {
                warningParent.setVisible(false);
            }
            FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/info/warning.fxml", topPane);
            WarningController controller = loader.getController();
            controller.setOutdatedCustomers(warningList);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    void logOutMenuHandler() {
        OpenWindow.reOpenLogin(dashboardStage, activeUser.getUsername(), borderPane);
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


    //-------------------–helper methods------------------------
    public void setWarningList(ObservableList<Customers> warningList) {
        this.warningList = warningList;
        if (warningList.isEmpty()) {
            warningParent.setVisible(false);
        } else {
            warningLabel.setText(warningList.size() <= 9 ? String.valueOf(warningList.size()) : "9+");
            FadeIn fadeIn = new FadeIn(warningParent);
            fadeIn.setCycleCount(20);
            fadeIn.play();
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
}
