package com.example.gymmanagementsystem.simpleconrtollers;

import com.example.gymmanagementsystem.controllers.info.OutDatedController;
import com.example.gymmanagementsystem.controllers.users.UpdateUserController;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DashbaordController extends CommonClass implements Initializable {

    @FXML
    private HBox topPane;
    private double xOffset = 0;
    private double yOffset = 0;
    @FXML
    private HBox menuHBox;
    private Stage dashboardStage;
    HBox hBox;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Circle activeProfile;
    @FXML
    private MenuButton username;
    private final Users user;

    public DashbaordController() throws SQLException {
        this.user = UserService.users().get(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            this.dashboardStage = (Stage) topPane.getScene().getWindow();
            borderPaneDrag();
            borderPaneDropped();

            try {
                OpenWindow.dashboardWindow(borderPane, menuHBox);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    @FXML
    void homeHandler() throws IOException, SQLException {
        FXMLLoader loader = OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/main/home.fxml", borderPane);
        System.out.println(borderPane);
        HomeController controller = loader.getController();
        controller.setActiveUser(UserService.users().get(0));
        controller.setBorderPane(borderPane);
    }

    @FXML
    void dashboardHandler() throws Exception {
        OpenWindow.dashboardWindow(borderPane, menuHBox);
    }

    @FXML
    void registrationHandler() throws Exception {
        OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/main/registrations.fxml", borderPane);
    }

    @FXML
    void reportHandler() throws IOException {
        OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/info/dailyReports.fxml", borderPane);
    }

    @FXML
    void updateMeHandler() throws IOException {
        FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-update.fxml", menuHBox);

        UpdateUserController controller = loader.getController();
        controller.setActiveUser(user);
        controller.setStage(dashboardStage);

    }

    @FXML
    void updateUserHandler() throws IOException {
        OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-chooser.fxml", menuHBox);
    }

    @FXML
    void notificationHandler() throws IOException {
        OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/info/warning.fxml", topPane);
    }

    @FXML
    void outdatedHandler() throws IOException {
        FXMLLoader loader = OpenWindow.mainWindow("/com/example/gymmanagementsystem/newviews/info/outdated.fxml", borderPane);
        OutDatedController controller = loader.getController();
        controller.setActiveUser(activeUser);
    }

    @FXML
    void closeHandler() {
        boolean data = Alerts.confirmationAlert("Ma hubtaa inaad ka baxdo systemka", "Doorasho");
        if (data) {
            OpenWindow.closeStage(dashboardStage, activeProfile);
        }
        System.out.println(data);
    }

    @FXML
    void minimizeHandler() {
        dashboardStage.setIconified(true);
    }

    @FXML
    void addUserHandler() throws IOException {
        OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-creation.fxml", activeProfile);
    }

    @FXML
    void backupHandler() throws IOException {
        OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/backup.fxml", activeProfile);

    }

    @FXML
    void gymHandler() throws IOException {
        OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/service/gym.fxml", activeProfile);

    }

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

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        System.out.println("active user is " + activeUser.getUsername());
        HBox hBox = (HBox) topPane.getChildren().get(1);
        topPane.getChildren().remove(hBox);

        URL url;
        final String[] profileImages = {"/com/example/gymmanagementsystem/style/icons/man-profile.jpeg",
                "/com/example/gymmanagementsystem/style/icons/woman-hijap.jpeg"};
        username.setText(activeUser.getUsername() + " [" + activeUser.getRole() + "]");
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
}
