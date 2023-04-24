package com.example.gymmanagementsystem.controllers.users;


import animatefx.animation.FadeOut;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
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
    private HBox topPane;
    @FXML
    private JFXButton uploadBtn;
    private Stage stage;

    private final Gym currentGym = GymService.getGym();
    private boolean itsMe = false;
    Stage parentStage;

    public UpdateUserController() throws SQLException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            stage = (Stage) topText.getScene().getWindow();
            enterKeyFire(updateBtn, stage);
            paneDrag(stage, topPane);
            paneDropped(stage, topPane);
            initFields();
        });

        service.setOnSucceeded(e -> {
            updateBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/icons8-available-updates-24.png"));
            updateBtn.setText("Updated");
            if (itsMe) {
                boolean done = Alerts.confirmationAlert("Account-kaga wa la update gareyay si aad u aragto is bedelka profile kaga" +
                        " fadlan taabo HAA dib-na usoo gal.");
                if (done) {
                    openLogin();
                }
            }
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
        itsMe = true;
        if (!activeUser.getRole().equals("super_admin")) {
            superAdmin.setDisable(true);
            admin.setDisable(true);
            female.setDisable(true);
            male.setDisable(true);
            shift.setItems(super.getShift());
        }
        topText.setText("WAX KA BEDEL ACCOUNTKAGA");
        setUserData(activeUser);
    }

    @FXML
    void updateHandler() {
        if (isValid(getMandatoryFields(), null)) {
            if (currentGym.isImageUpload() && !imageUploaded) {
                checkImage(imageView, "Sawirku wuxuu ku qurxinyaa profile kaaga");
                return;
            }
            startTask(service, updateBtn, "Updating");
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
                        if (!itsMe)
                            Platform.runLater(() -> Alerts.notificationAlert(
                                    "User updated successfully"));
                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
                        e.printStackTrace();
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
        users = new Users(Integer.parseInt(idFeild.getText()), firstname.getText().trim(), lastname.getText().trim(), phone.getText().trim(), gander,
                shift.getValue(), username.getText().trim(), password.getText().trim(),
                selectedFile == null ? users.getImage() : readFile(selectedFile.getAbsolutePath()), role
        );

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
            uploadBtn.setText("Bedelka sawirka");
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

    public void setStage(Stage stage) {
        this.parentStage = stage;
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
    }

    private void openLogin() {
        FadeOut fadeOut = OpenWindow.getFadeOut();
        fadeOut.setNode(borderPane);
        fadeOut.setSpeed(2);
        fadeOut.setOnFinished(e -> {
            stage.close();
            Stage dashboard = (Stage) borderPane.getScene().getWindow();
            dashboard.close();
            try {
                OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/login.fxml", borderPane);
            } catch (Exception ex) {
                Alerts.errorAlert(ex.getMessage());
                ex.printStackTrace();
            }
        });
        fadeOut.play();
    }

}
