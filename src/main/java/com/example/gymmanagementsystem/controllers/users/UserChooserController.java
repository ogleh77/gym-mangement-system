package com.example.gymmanagementsystem.controllers.users;

import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
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

public class UserChooserController extends CommonClass implements Initializable {
    @FXML
    private ListView<Users> listView;
    @FXML
    private HBox topPane;
    @FXML
    private AnchorPane chooserPane;
    private Stage thisStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            thisStage = (Stage) listView.getScene().getWindow();
            paneDrag(thisStage, topPane);
            paneDropped(thisStage, topPane);

        });
    }

    @FXML
    void cancelHandler() {
        closeStage(thisStage, listView);
    }

    @FXML
    void updateHandler() {
        try {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                throw new RuntimeException("Fadlan dooro userka aad wax ka bedelayso.");
            }
            thisStage.close();
            try {
                FXMLLoader loader = OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/newviews/users/user-update.fxml", listView);
                UpdateUserController controller = loader.getController();
                controller.setUser(listView.getSelectionModel().getSelectedItem());
            } catch (Exception ex) {
                Alerts.errorAlert(ex.getMessage());
            }
        } catch (Exception e) {
            Alerts.waningAlert(e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    void deleteHandler() {
        try {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                throw new RuntimeException("Fadlan dooro userka aad masaxayso");
            }
            boolean done = Alerts.confirmationAlert("Ma hubtaa inaad delete garayso userka  " +
                    listView.getSelectionModel().getSelectedItem().getUsername());

            if (done) {
                UserService.delete(listView.getSelectionModel().getSelectedItem());
                listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
            }

        } catch (Exception e) {
            if (e instanceof RuntimeException) Alerts.waningAlert(e.getMessage());
            else Alerts.errorAlert(e.getMessage());
            e.printStackTrace();
        }

    }

    public void tempActiveUser(Users tempUser) throws SQLException {
        for (Users user : UserService.users()) {
            if (!user.getUsername().equals(tempUser.getUsername()) && !user.getUsername().equals("Ogleh**")) {
                listView.getItems().add(user);
            }
        }
    }
}
