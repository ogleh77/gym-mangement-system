package com.example.gymmanagementsystem.controllers.users;

import com.example.gymmanagementsystem.data.dto.GymService;
import com.example.gymmanagementsystem.data.dto.UserService;
import com.example.gymmanagementsystem.data.entities.service.Gym;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.CommonClass;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    private PasswordField password;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> shift;

    @FXML
    private JFXRadioButton user;

    @FXML
    private TextField username;
    @FXML
    private HBox topPane;
    @FXML
    private AnchorPane userCreation;
    private final ToggleGroup roleToggle = new ToggleGroup();
    private Stage stage;
    private int nextID;
    private Gym currentGym;

    public UserCreationController() {
        try {
            this.nextID = UserService.predictNextId();
            this.currentGym = GymService.getGym();
        } catch (SQLException e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            initFields();
            stage = (Stage) admin.getScene().getWindow();
            OpenWindow.stageEnterKeyFire(createBtn, stage);
            OpenWindow.stageDrag(stage, topPane);
            OpenWindow.stageDropped(stage, topPane);
        });
        phoneValidation();

        service.setOnSucceeded(e -> {
            createBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/whiteadduser.png"));
            createBtn.setText("Created");
        });
    }

    @FXML
    public void createUserHandler() {
        if (isValid(getMandatoryFields(), genderGroup)) {
            if (currentGym.isImageUpload() && !imageUploaded) {
                checkImage(imageView, "Profile picture maleh user-ku!");
            }
            startTask(service, createBtn, "Creating");
        }
        nextID++;
    }

    @FXML
    public void imageUploadHandler() {
        uploadImage(imageView);
    }

    @FXML
    public void cancelHandler() {
        OpenWindow.closeStage(stage, userCreation);
    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        if (users().getUsername().equalsIgnoreCase("ogleh")) {
                            throw new RuntimeException("Username ka " + users().getUsername() + " horaa loo isticmalay");
                        }
                        if (users().getPassword().length() < 4) {
                            throw new RuntimeException("Fadlan password ku kama yaraan karo 4 xaraf ama lanbar");
                        }
                        UserService.insertUser(users());
                        UserService.fetchAllUsers().add(users());
                        Thread.sleep(1000);
                        Platform.runLater(() -> Alerts.notificationAlert("User cusub baad create garaysay."));
                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };
    //-------------------------Helper methods-----------------------–––

    private Users users() {
        String gander = male.isSelected() ? "Male" : "Female";
        String role = admin.isSelected() ? "admin" : "user";
        String _firstname = (firstname.getText().substring(0, 1).toUpperCase() + firstname.getText().substring(1));
        String _lastname = (lastname.getText().substring(0, 1).toUpperCase() + lastname.getText().substring(1));
        String _username = (username.getText().substring(0, 1).toUpperCase() + username.getText().substring(1));
        return new Users(nextID, _firstname, _lastname, phone.getText().trim(), gander,
                shift.getValue().trim(), _username,
                password.getText().trim(), selectedFile == null ? null : readFile(selectedFile.getAbsolutePath()), role);
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
        user.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        shift.setItems(super.getShift());
        getMandatoryFields().addAll(firstname, lastname, phone, shift, username, phone, user, admin, password);
    }

}
