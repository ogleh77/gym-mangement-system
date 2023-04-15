package com.example.gymmanagementsystem.controllers.done;

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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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
    @FXML
    private JFXButton uploadImageBtn;
    private Stage stage;
    private final ToggleGroup roleToggle = new ToggleGroup();

    public UserCreationController() {
        // this.currentGym=GymS;
        // TODO: 08/04/2023 Insha Allah gym ka setting kiisa ku dar inu qofku furan karo xidhana karo
        //image upload notificationka
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            initFields();
            stage = (Stage) username.getScene().getWindow();
            enterKeyFire(createBtn, stage);
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
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7 || !phoneValidation.isVisible())) {
            if (!imageUploaded) {
                checkImage(imageView, "Profile picture maleh userku!");
            }
            startTask(service, createBtn, "Creating");
        }
    }

    @FXML
    void imageUploadHandler() {
        uploadImage(imageView);
    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Platform.runLater(() -> {
                            try {
                                UserService.insertUser(users());
                                UserService.users().add(users());
                                Platform.runLater(() -> {
                                    Optional<ButtonType> result = informationAlert("New user created successfully 👍🏾")
                                            .showAndWait();
                                    if (result.isPresent() && result.get().getButtonData().isDefaultButton()) {
                                        closeStage(stage, username.getParent());
                                    }
                                });
                            } catch (SQLException e) {
                                Platform.runLater(() -> errorMessage(e.getMessage() + " ☹️"));
                            }

                        });
                    } catch (Exception e) {

                        Platform.runLater(() -> errorMessage(e.getMessage()));
                    }
                    return null;
                }
            };
        }
    };


    //-------------------------helpers----------------
    private Users users() throws SQLException {
        int nextId = UserService.predictNextId();
        String gander = male.isSelected() ? "Male" : "Female";
        String role = superAdmin.isSelected() ? "super_admin" : "admin";
        return new Users(nextId, firstname.getText().trim(), lastname.getText().trim(), phone.getText().trim(), gander, shift.getValue().trim(), username.getText().trim(), oldPassword.getText().trim(), selectedFile == null ? null : readFile(selectedFile.getAbsolutePath()), role);
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

    private void phoneValidation() {
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone.setText(newValue.replaceAll("\\D", ""));
            } else if (!phone.getText().matches("^\\d{7}")) {
                phoneValidation.setText("Fadlan lanbarku kama yaran karo 7 digit");
                phoneValidation.setVisible(true);

            } else {
                phoneValidation.setVisible(false);
            }
        });

    }
}
