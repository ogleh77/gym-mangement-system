package com.example.gymmanagementsystem.temcont;

import com.example.gymmanagementsystem.data.entities.service.Users;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class homeContr implements Initializable {
    @FXML
    private Label data;
    private Users users;
    @FXML
    private ImageView imgView;
    private Image image;

    public homeContr() {
        this.users = new Users(1, "Mohamed", "Saeeed", "333", "dddd", "Noon",
                "Moha", "222", null, "ddd");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            init();
            data.textProperty().bind(users.usernameProperty());
            imgView.setImage(image);
         });
    }

    @FXML
    void nextHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/next.fxml"));
        Scene scene = new Scene(loader.load());
        NextCont cont = loader.getController();
        cont.setUsers(users);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();
    }


    private void init() {
        ByteArrayInputStream bis = new ByteArrayInputStream(users.getImage());
        image = new Image(bis);

        imgView.setImage(image);

    }
}
