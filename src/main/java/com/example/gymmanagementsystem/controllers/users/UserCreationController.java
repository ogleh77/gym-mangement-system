package com.example.gymmanagementsystem.controllers.users;

import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
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
    private Label phoneValidation;

    @FXML
    private ComboBox<String> shift;

    @FXML
    private JFXRadioButton superAdmin;

    @FXML
    private TextField username;
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
        });
        phoneValidation();

        service.setOnSucceeded(e -> {
            createBtn.setGraphic(null);
            createBtn.setText("Created");

//            if (done) {
//                createBtn.setGraphic(null);
//                createBtn.setText("Created");
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "User Created successfully",
//                        new ButtonType("cancel"));
//
//                Optional<ButtonType> result = alert.showAndWait();
//
//                if (result.isPresent() && !result.get().equals(ButtonType.YES)) {
//                    closeStage(stage, firstname.getParent());
//                }
            // }
        });
    }

    @FXML
    void cancelHandler() {
        closeStage(stage, username.getParent());
    }

    @FXML
    void createUserHandler() {
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7 || !phoneValidation.isVisible())) {
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

                        Platform.runLater(() -> infoAlert("New user has been created successfully"));

                    } catch (Exception e) {
//                        e.printStackTrace();
                        Platform.runLater(() -> errorMessage(e.getMessage()));
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
                phoneValidation.setText("Fadlan lanbarka xarfo looma ogola");
                phoneValidation.setVisible(true);
            } else if (!phone.getText().matches("^\\d{7}")) {
                phoneValidation.setText("Fadlan lanbarku kama yaran karo 7 digit");
                phoneValidation.setVisible(true);

            } else {
                phoneValidation.setVisible(false);
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
