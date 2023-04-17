package com.example.gymmanagementsystem.controllers;

import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideOutLeft;
import com.example.gymmanagementsystem.controllers.info.OutDatedController;
import com.example.gymmanagementsystem.controllers.main.HomeController;
import com.example.gymmanagementsystem.controllers.main.RegistrationController;
import com.example.gymmanagementsystem.controllers.users.UpdateUserController;
import com.example.gymmanagementsystem.controllers.users.UserChooserController;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController extends CommonClass implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label gymName;
    @FXML
    private Label warningLabel;
    @FXML
    private HBox warningParent;
    @FXML
    private VBox sidePane;
    @FXML
    private StackPane warningStack;
    @FXML
    private HBox menuHBox;
    @FXML
    private Circle activeProfile;
    @FXML
    private MenuButton activeUserName;
    @FXML
    private MenuItem addUserBtn;
    @FXML
    private MenuItem updateUserBtn;
    @FXML
    private MenuItem gymBtn;

    private final Gym currentGym;
    private ObservableList<Customers> warningList;
    private Stage dashboardStage;
    private boolean visible = false;

    @FXML
    private HBox topPane;
    private double xOffset = 0;
    private double yOffset = 0;

    public DashboardController() throws SQLException {
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            gymName.textProperty().bind(currentGym.gymNameProperty());
            // TODO: 17/04/2023 Markad bedesho user nameka haka muqdo xaga sare insha Allah
            // TODO: 17/04/2023 picture change also takes place insha Allah
            activeUserName.textProperty().bind(activeUser.usernameProperty());
            dashboardStage = (Stage) activeProfile.getScene().getWindow();
            borderPane.setLeft(null);

            borderPaneDrag();
            borderPaneDropped();
            addUserBtn.setDisable(!activeUser.getRole().equals("super_admin"));
            updateUserBtn.setDisable(!activeUser.getRole().equals("super_admin"));
            gymBtn.setDisable(!activeUser.getRole().equals("super_admin"));
        });
    }

    @FXML
    void menuClicked() {
        if (visible) {
            SlideOutLeft slideOutLeft = new SlideOutLeft();
            slideOutLeft.setNode(sidePane);
            slideOutLeft.play();
            slideOutLeft.setOnFinished(e -> borderPane.setLeft(null));
        } else {
            new SlideInLeft(sidePane).play();
            borderPane.setLeft(sidePane);
        }
        visible = !visible;
    }

    @FXML
    void minimizeHandler() {
        dashboardStage.setIconified(true);
    }

    @FXML
    void closeHandler() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ma hubtaa inad ka baxayso system ka", no, ok);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ok) {
            closeStage(dashboardStage, activeProfile.getParent());
        } else {
            alert.close();
        }
    }

    @FXML
    void homeHandler() {
        try {
            FXMLLoader loader = openWindow("/com/example/gymmanagementsystem/newviews/main/home.fxml", borderPane, null, warningStack);
            HomeController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }

    @FXML
    void registrationHandler() {
        try {
            FXMLLoader loader = openWindow("/com/example/gymmanagementsystem/newviews/main/registrations.fxml", borderPane, null, warningStack);
            RegistrationController controller = loader.getController();
            controller.setActiveUser(activeUser);
            controller.setBorderPane(borderPane);
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }

    @FXML
    void outdatedCustomersHandler() {
        try {
            FXMLLoader loader = openWindow("/com/example/gymmanagementsystem/newviews/info/outdated.fxml", borderPane, null, warningStack);
            OutDatedController controller = loader.getController();
            controller.setActiveUser(activeUser);
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }

    @FXML
    void reportHandler() throws IOException {
        openWindow("/com/example/gymmanagementsystem/newviews/info/dailyReports.fxml", borderPane, null, warningStack);

    }

    @FXML
    void updateMeHandler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/newviews/users/user-update.fxml"));
            Scene scene = new Scene(loader.load());
            UpdateUserController controller = loader.getController();
            controller.setActiveUser(activeUser);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }


    @FXML
    void addUserHandler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/newviews/users/user-creation.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(activeProfile.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }

    @FXML
    void updateUsersHandler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/newviews/users/user-chooser.fxml"));
            Scene scene = new Scene(loader.load());
            UserChooserController controller = loader.getController();
            //Users user = UserService.users().get(4);
            controller.tempActiveUser(activeUser);
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }

    @FXML
    void gymHandler() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/newviews/service/gym.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initOwner(activeProfile.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            errorMessage(e.getMessage());
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        activeUserName.setText(activeUser.getUsername() + " [" + activeUser.getRole() + "]");
        URL url;
        final String[] profileImages = {"/com/example/gymmanagementsystem/style/icons/man-profile.jpeg",
                "/com/example/gymmanagementsystem/style/icons/woman-hijap.jpeg"};

        if (activeUser.getGender().equals("Male")) {
            if (activeUser.getImage() == null) {
                url = getClass().getResource(profileImages[0]);
                activeProfile.setFill(new ImagePattern(new Image(String.valueOf(url))));
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(activeUser.getImage());
                Image image = new Image(bis);
                activeProfile.setFill(new ImagePattern(image));
            }

        } else if (activeUser.getGender().equals("Female")) {
            if (activeUser.getImage() == null) {
                url = getClass().getResource(profileImages[1]);
                activeProfile.setFill(new ImagePattern(new Image(String.valueOf(url))));
            } else {
                ByteArrayInputStream bis = new ByteArrayInputStream(activeUser.getImage());
                Image image = new Image(bis);
                activeProfile.setFill(new ImagePattern(image));
            }
        }
    }

    //--------------------__Helpers_------------------
    private void borderPaneDrag() {
        topPane.setOnMousePressed(event -> {
            xOffset = dashboardStage.getX() - event.getScreenX();
            yOffset = dashboardStage.getY() - event.getScreenY();
        });
    }

    private void borderPaneDropped() {
        topPane.setOnMouseDragged(event -> {
            dashboardStage.setX(event.getScreenX() + xOffset);
            dashboardStage.setY(event.getScreenY() + yOffset);
        });
    }
}
