package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController extends CommonClass implements Initializable {
    @FXML
    private JFXButton loginBtn;
    @FXML
    private PasswordField password;
    @FXML
    private Label gymTitle;
    private ObservableList<Users> users;
    private Stage currentStage;
    @FXML
    private ComboBox<Users> userCombo;
    private Gym gym;
    @FXML
    private HBox topPane;

    public LoginController() {
        try {
            users = UserService.users();
            gym = GymService.getGym();
        } catch (SQLException e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            currentStage = (Stage) userCombo.getScene().getWindow();
            userCombo.setItems(users);
            gymTitle.setText(gym.getGymName());
            getMandatoryFields().addAll(userCombo, password);

            paneDrag(currentStage, topPane);
            paneDropped(currentStage, topPane);
            enterKeyFire(loginBtn, currentStage);
        });

        service.setOnSucceeded(e -> {
            loginBtn.setGraphic(null);
            loginBtn.setText("Logged");
        });
    }

    @FXML
    void loginHandler() {
        if (isValid(getMandatoryFields(), null)) {
            Users activeUser = userCombo.getValue();
            if (!password.getText().equals(activeUser.getPassword())) {
                Alerts.errorAlert("Fadlan hubi passwordka & user ka aad dorantay\nin ay isleyihiin!");
                return;
            }
            startTask(service, loginBtn, "Logging");
        }
    }

    @FXML
    void exitHandler() {
        closeStage();
    }

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);

                    Platform.runLater(() -> {
                        closeStage();
                        try {
                            openSplash();
                        } catch (IOException ex) {
                            Alerts.errorAlert(ex.getMessage());
                        }
                    });
                    return null;
                }
            };
        }
    };

    private void openSplash() throws IOException {
        FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/splash-screen.fxml", topPane);
        SplashScreenController controller = loader.getController();
        controller.setActiveUser(userCombo.getValue());
    }

    private void closeStage() {
        closeStage(currentStage, userCombo.getParent());
    }

    @FXML
    void minimizeHandler() {
        currentStage.setIconified(true);
    }
}
