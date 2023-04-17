package com.example.gymmanagementsystem.controllers.users;

import animatefx.animation.FadeOut;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.example.gymmanagementsystem.helpers.CustomException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserChooserController extends CommonClass implements Initializable {
    @FXML
    private ListView<Users> listView;
    private Stage thisStage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> thisStage = (Stage) listView.getScene().getWindow());
    }

    @FXML
    void cancelHandler() {
        close();
    }

    @FXML
    void updateHandler() {
        try {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                throw new CustomException("No user selected.");
            }

            FadeOut fadeOut = new FadeOut(listView.getParent());

            fadeOut.setOnFinished(e -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/newviews/users/user-update.fxml"));
                try {
                    Scene scene = new Scene(loader.load());
                    UpdateUserController controller = loader.getController();
                    controller.setUser(listView.getSelectionModel().getSelectedItem());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                } catch (IOException ex) {
                    errorMessage(ex.getMessage());
                }
                thisStage.close();
            });
            fadeOut.setSpeed(2);
            fadeOut.play();
        } catch (Exception e) {
            infoAlert(e.getMessage());
        }

    }

    @FXML
    void deleteHandler() {
        try {
            if (listView.getSelectionModel().getSelectedItem() == null) {
                throw new CustomException("Marka hore soo dooro user-ka aad rabto inaad delete-garyso.");
            }
            confirmDelete(listView.getSelectionModel().getSelectedItem().getUsername());
        } catch (Exception e) {
            if (e instanceof SQLException) {
                errorMessage(e.getMessage());
            } else {
                infoAlert(e.getMessage());
            }
        }


    }

    private void confirmDelete(String username) throws SQLException {

        ButtonType okBtn = new ButtonType("Haa", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Maya", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ma hubtaa inaad delete garayso " + "userka " + username, okBtn, cancelBtn);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get().getButtonData().isDefaultButton()) {
            UserService.delete(listView.getSelectionModel().getSelectedItem());
            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
        } else {
            alert.close();
        }
    }

    private void close() {
        FadeOut fadeOut = new FadeOut(listView.getParent());
        fadeOut.setSpeed(2);
        fadeOut.setOnFinished(e -> thisStage.close());
        fadeOut.play();
    }

    public void tempActiveUser(Users tempUser) throws SQLException {
        for (Users user : UserService.users()) {
            if (!user.getUsername().equals(tempUser.getUsername()) && !user.getUsername().equals("Ogleh**")) {
                listView.getItems().add(user);
            }
        }
    }
}
