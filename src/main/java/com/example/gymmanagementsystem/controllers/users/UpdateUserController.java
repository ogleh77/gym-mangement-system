package com.example.gymmanagementsystem.controllers.users;


import com.example.gymmanagementsystem.dao.service.UserService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateUserController extends CommonClass implements Initializable {
    @FXML
    private JFXRadioButton admin;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstname;

    @FXML
    private TextField idFeild;

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
    private JFXRadioButton superAdmin;

    @FXML
    private JFXButton updateBtn;

    @FXML
    private TextField username;
    @FXML
    private Label warningMessage;

    @FXML
    private Label topText;

    private Users users;
    private final ToggleGroup roleToggle = new ToggleGroup();

    @FXML
    private JFXButton uploadBtn;
    private Stage stage;

    private boolean done = true;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) topText.getScene().getWindow();
            enterKeyFire(updateBtn,stage);
            initFields();
        });

        service.setOnSucceeded(e -> {
            updateBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/icons8-available-updates-24.png"));
            updateBtn.setText("Updated");
        });
    }

    @FXML
    void cancelHandler() {
        closeStage(stage, topText.getParent());
    }

    @FXML
    void uploadImageHandler() {
        uploadImage(imageView);
    }

    @Override
    public void setActiveUser(Users activeUser) {
        this.users = activeUser;
        if (!activeUser.getRole().equals("super_admin")) {
            superAdmin.setDisable(true);
            admin.setDisable(true);
            female.setDisable(true);
            male.setDisable(true);
            shift.setItems(super.getShift());
        }
        topText.setText("UPDATE ME");
        setUserData(activeUser);
    }

    @FXML
    void updateHandler() {
        if (isValid(getMandatoryFields(), null)) {
            if (!imageUploaded) {
                checkImage(imageView, "Sawirku wuxuu ku qurxinyaa profile kaaga");
                return;
            }

            if (start) {
                service.restart();
                updateBtn.setGraphic(getLoadingImageView());
                updateBtn.setText("Updating");
            } else {
                service.start();
                updateBtn.setGraphic(getLoadingImageView());
                updateBtn.setText("Updating");
                start = true;
            }
        }
    }

    public void setUser(Users user) {
        this.users = user;
        setUserData(user);
        firstname.setEditable(false);
        lastname.setEditable(false);
        phone.setEditable(false);
        username.setEditable(false);
        password.setEditable(false);
        warningMessage.setVisible(true);
        uploadBtn.setVisible(false);
        imageUploaded = true;
        shift.setValue(user.getShift());
        shift.setEditable(false);
    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        UserService.update(users());
                        Platform.runLater(() -> infoAlert("User updated successfully"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> errorMessage(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };

    //----------------------helper methods-------------------
    private Users users() {

        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";

        users.setFirstName(firstname.getText().trim());
        users.setPhone(phone.getText().trim());
        users.setLastName(lastname.getText().trim());
        users.setUsername(username.getText().trim());
        users.setPassword(password.getText().trim());
        users.setImage(selectedFile == null ? users.getImage() : readFile(selectedFile.getAbsolutePath()));
        users.setGender(gander);
        users.setRole(role);
        users.setShift(shift.getValue().trim());
        imageUploaded = true;
        return users;
    }

    private void setUserData(Users user) {
        idFeild.setText(String.valueOf(user.getUserId()));
        firstname.setText(user.getFirstName());
        lastname.setText(user.getLastName());
        phone.setText(user.getPhone());
        shift.setValue(user.getShift());
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        if (user.getGender().equals("Male")) {
            male.setSelected(true);
        } else if (user.getGender().equals("Female")) {
            female.setSelected(true);
        }

        if (user.getRole().equals("super_admin")) {
            superAdmin.setSelected(true);
        } else if (user.getRole().equals("admin")) {
            admin.setSelected(true);
        }


        if (user.getImage() != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(user.getImage());
            Image image = new Image(bis);
            if (user.getImage() != null) {
                imageView.setImage(image);
                imageUploaded = true;
            }
        }
    }

    private void initFields() {
        admin.setToggleGroup(roleToggle);
        superAdmin.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        shift.setItems(super.getShift());

        getMandatoryFields().addAll(firstname, lastname, phone, shift, username, password, superAdmin, admin);

    }


}
