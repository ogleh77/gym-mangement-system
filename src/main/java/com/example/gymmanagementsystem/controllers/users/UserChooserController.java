package com.example.gymmanagementsystem.controllers.users;

import com.example.gymmanagementsystem.data.dto.UserService;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserChooserController implements Initializable {
    @FXML
    private ListView<Users> listView;
    @FXML
    private HBox topPane;
    @FXML
    private AnchorPane chooserPane;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            stage = (Stage) listView.getScene().getWindow();
            OpenWindow.stageDrag(stage, topPane);
            OpenWindow.stageDropped(stage, topPane);
        });
    }

    @FXML
    void updateHandler() {
        Users user = listView.getSelectionModel().getSelectedItem();
        try {
            if (user == null) throw new RuntimeException("Fadlan dooro user-ka aad rabto inaad wax ka bedesho");
            stage.close();
            FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-update.fxml", topPane);
            UserUpdateController controller = loader.getController();
            controller.setUser(user);
        } catch (Exception e) {
            Alerts.waningAlert(e.getMessage());
        }
    }

    @FXML
    void deleteHandler() {
        try {
            Users user = listView.getSelectionModel().getSelectedItem();
            if (user == null) throw new RuntimeException("Fadlan dooro userka aad masixi rabto.");

            boolean done = Alerts.confirmationAlert("Ma hubtaa inaad delete garayso userka  " + user.getUsername(), "Maya", "Haa");

            if (done) {
                UserService.deleteUser(user);
                listView.getItems().remove(user);
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) Alerts.waningAlert(e.getMessage());
            else Alerts.errorAlert(e.getMessage());
        }
    }

    @FXML
    void cancelHandler() {
        OpenWindow.closeStage(stage, chooserPane);
    }

    public void setUsersWithoutActiveOne(Users tempUser) throws SQLException {
        for (Users user : UserService.fetchAllUsers()) {
            if (!user.getUsername().equals(tempUser.getUsername()) && !user.getUsername().equals("Ogleh")) {
                listView.getItems().add(user);
            }
        }
    }


}
