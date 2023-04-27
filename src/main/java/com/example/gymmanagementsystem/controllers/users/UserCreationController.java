package com.example.gymmanagementsystem.controllers.users;

import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserCreationController extends CommonClass implements Initializable {
    @FXML
    private JFXRadioButton admin;

    @FXML
    private JFXButton createBtn;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstname;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField lastname;

    @FXML
    private JFXRadioButton male;

    @FXML
    private PasswordField oldPassword;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> shift;

    @FXML
    private JFXRadioButton superAdmin;

    @FXML
    private TextField username;
    @FXML
    private HBox topPane;
    private final ToggleGroup roleToggle = new ToggleGroup();
    private final Gym currentGym;

    private Stage stage;

    public UserCreationController() throws SQLException {
        currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initFields();
            stage = (Stage) superAdmin.getScene().getWindow();
            enterKeyFire(createBtn, stage);
            paneDrag(stage, topPane);
            paneDropped(stage, topPane);
        });
        phoneValidation();

        service.setOnSucceeded(e -> {
            createBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/whiteadduser.png"));
            createBtn.setText("Created");
        });
    }

    @FXML
    void cancelHandler() {
        closeStage(stage, username.getParent());
    }

    @FXML
    void createUserHandler() {
        if (isValid(getMandatoryFields(), genderGroup)) {
            if (currentGym.isImageUpload() && !imageUploaded) {
                checkImage(imageView, "Profile picture maleh userku!");
            }
            startTask(service, createBtn, "Creating");
        }
    }

    @FXML
    void imageUploadHandler() {
        uploadImage(imageView);
    }

    //-------------------------Helper methods------------------------

    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        UserService.insertUser(users());
                        UserService.users().add(users());
                        Thread.sleep(1000);
                        Platform.runLater(() -> Alerts.notificationAlert("Waxaad samaysay user cusub"));
                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };

    private Users users() throws SQLException {
        int nextId = UserService.predictNextId();
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";
        return new Users(nextId, firstname.getText().trim(), lastname.getText().trim(), phone.getText().trim(), gander,
                shift.getValue().trim(), username.getText().trim(),
                oldPassword.getText().trim(), selectedFile == null ? null : readFile(selectedFile.getAbsolutePath()), role);
    }

    private void phoneValidation() {
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    private void initFields() {
        admin.setSelected(true);
        admin.setToggleGroup(roleToggle);
        superAdmin.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        shift.setItems(super.getShift());

        getMandatoryFields().addAll(firstname, lastname, phone, shift, username, oldPassword, superAdmin, admin);
    }
}
