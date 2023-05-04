package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.controllers.notdone.SplashScreenController;
import com.example.gymmanagementsystem.data.dto.GymService;
import com.example.gymmanagementsystem.data.dto.UserService;
import com.example.gymmanagementsystem.data.entities.service.Gym;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
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
    @FXML
    private HBox topPane;
    private Gym gym;
    @FXML
    private ComboBox<Users> userCombo;
    private ObservableList<Users> users;
    private Stage stage;

    public LoginController() {
        try {
            users = UserService.fetchAllUsers();
            gym = GymService.getGym();
        } catch (SQLException e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) userCombo.getScene().getWindow();
            OpenWindow.stageDropped(stage, topPane);
            OpenWindow.stageDrag(stage, topPane);
            OpenWindow.stageEnterKeyFire(loginBtn, stage);

            userCombo.setItems(users);
            gymTitle.setText(gym.getGymName());
            getMandatoryFields().addAll(userCombo, password);

        });

        service.setOnSucceeded(e -> {
            loginBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/icons8-login-50.png"));
            loginBtn.setText("Logged");
        });
    }

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        stage.close();
                        try {
                            openSplash();
                        } catch (IOException ex) {
                            Alerts.errorAlert(ex.getMessage());
                            ex.printStackTrace();
                        }
                    });
                    return null;
                }
            };
        }
    };

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
    void minimizeHandler() {
        stage.setIconified(true);
    }

    @FXML
    void exitHandler() {
        OpenWindow.closeStage(stage, topPane);
    }


    private void openSplash() throws IOException {
        FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/splash-screen.fxml", topPane);
        SplashScreenController controller = loader.getController();
        controller.setActiveUser(userCombo.getValue());
    }

}
