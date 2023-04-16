package com.example.gymmanagementsystem.controllers.service;

import com.example.gymmanagementsystem.controllers.SplashScreenController;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
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
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private double xOffset = 0;
    private double yOffset = 0;

    public LoginController() {
        try {
            users = UserService.users();
            gym = GymService.getGym();
        } catch (SQLException e) {
            errorMessage("Khalad ba ka dhacay " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            currentStage = (Stage) userCombo.getScene().getWindow();
            userCombo.setItems(users);
            gymTitle.setText(gym.getGymName());
            getMandatoryFields().addAll(userCombo, password);

            topPane.setOnMousePressed(event -> {
                xOffset = currentStage.getX() - event.getScreenX();
                yOffset = currentStage.getY() - event.getScreenY();
            });

            topPane.setOnMouseDragged(event -> {
                currentStage.setX(event.getScreenX() + xOffset);
                currentStage.setY(event.getScreenY() + yOffset);
            });

            enterKeyFire(loginBtn, currentStage);
        });

        service.setOnSucceeded(e -> {
            loginBtn.setGraphic(null);

            if (error) {
                errorMessage("Fadlan hubi username ka ama passwordka aad gelisay");
            } else {
                closeStage();
                try {
                    openSplash();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @FXML
    void loginHandler() {
        if (isValid(getMandatoryFields(), null)) {
            if (start) {
                service.restart();
                loginBtn.setGraphic(getLoadingImageView());
            } else {
                service.start();
                loginBtn.setGraphic(getLoadingImageView());
                start = true;
            }
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
                    Users activeUser = userCombo.getValue();
                    error = !password.getText().equals(activeUser.getPassword());
                    return null;
                }
            };
        }
    };

    private void openSplash() throws IOException {
        System.out.println("Welcome");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/newviews/service/splash-screen.fxml"));
        Scene scene = new Scene(loader.load());
        SplashScreenController controller = loader.getController();
        controller.setActiveUser(userCombo.getValue());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    private void closeStage() {
        closeStage(currentStage, userCombo.getParent());
    }
}
