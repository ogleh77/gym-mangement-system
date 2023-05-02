package com.example.gymmanagementsystem.controllers;

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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserUpdateController extends CommonClass implements Initializable {
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
    private JFXRadioButton user;

    @FXML
    private JFXButton updateBtn;

    @FXML
    private TextField username;
    @FXML
    private Label warningMessage;

    @FXML
    private Label topText;

    @FXML
    private HBox topPane;
    @FXML
    private JFXButton uploadBtn;
    @FXML
    private AnchorPane updatePane;
    private Stage stage;
    private Users users;
    private final ToggleGroup roleToggle = new ToggleGroup();
    private Gym currentGym;
    private boolean itsMe = false;

    public UserUpdateController() {
        try {
            this.currentGym = GymService.getGym();
        } catch (SQLException e) {
            Alerts.errorAlert(e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            initFields();
            stage = (Stage) topPane.getScene().getWindow();
        });

        service.setOnSucceeded(e -> {
            updateBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/icons8-available-updates-24.png"));
            updateBtn.setText("Updated");
        });
    }

    @FXML
    void cancelHandler() {
        OpenWindow.closeStage(stage, updatePane);
    }

    @FXML
    void updateHandler() {
        if (isValid(getMandatoryFields(), genderGroup)) {
            if (currentGym.isImageUpload() && !imageUploaded) {
                checkImage(imageView, "Profile picture maleh user-ku!");
            }
            startTask(service, updateBtn, "Updating");
        }
    }

    @FXML
    void uploadImageHandler() {
        uploadImage(imageView);
    }

    @Override
    public void setActiveUser(Users activeUser) {
        this.users = activeUser;
        itsMe = true;
        if (!activeUser.getRole().equals("admin")) {
            admin.setDisable(true);
            user.setDisable(true);
            female.setDisable(true);
            male.setDisable(true);
            shift.setItems(super.getShift());
        }
        topText.setText("WAX KA BEDEL ACCOUNTKAGA");
        setUserData(activeUser);
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        super.setBorderPane(borderPane);
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
                        UserService.updateUser(users());
                        Platform.runLater(() -> {
                            if (itsMe) {
                                boolean done = Alerts.singleConfirmationAlert("Account-kaga wa la update gareyay si aad u aragto is bedelka profile kaga" +
                                        " fadlan taabo logout dib-na usoo gal.", "Logout");
                                if (done) {
                                    openLogin();
                                }
                            } else {
                                Alerts.notificationAlert("Waxaad update garaysay user ka " + users().getUsername());
                            }
                        });

                    } catch (Exception e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };

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

        if (user.getRole().equals("admin")) {
            admin.setSelected(true);
        } else if (user.getRole().equals("user")) {
            this.user.setSelected(true);
        }


        if (user.getImage() != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(user.getImage());
            Image image = new Image(bis);
            if (user.getImage() != null) {
                imageView.setImage(image);
                imageUploaded = true;
            }
            uploadBtn.setText("Bedel sawirka");
        }
    }

    private Users users() {
        String gander = male.isSelected() ? "Male" : "Female";
        String role = this.admin.isSelected() ? "admin" : "user";
        users = new Users(Integer.parseInt(idFeild.getText()), firstname.getText().trim(), lastname.getText().trim(), phone.getText().trim(), gander,
                shift.getValue(), username.getText().trim(), password.getText().trim(),
                selectedFile == null ? users.getImage() : readFile(selectedFile.getAbsolutePath()), role
        );

        imageUploaded = true;
        return users;
    }

    private void initFields() {
        admin.setToggleGroup(roleToggle);
        this.user.setToggleGroup(roleToggle);
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        shift.setItems(super.getShift());

        getMandatoryFields().addAll(firstname, lastname, phone, shift, username, password, this.user, admin);

    }

    private void openLogin() {
        System.out.println("logouted..");
//        FadeOut fadeOut = OpenWindow.getFadeOut();
//        fadeOut.setNode(borderPane);
//        fadeOut.setSpeed(2);
//        fadeOut.setOnFinished(e -> {
//            stage.close();
//            Stage dashboard = (Stage) borderPane.getScene().getWindow();
//            dashboard.close();
//            try {
//                OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/login.fxml", borderPane);
//            } catch (Exception ex) {
//                Alerts.errorAlert(ex.getMessage());
//                ex.printStackTrace();
//            }
//        });
//        fadeOut.play();
    }
}
