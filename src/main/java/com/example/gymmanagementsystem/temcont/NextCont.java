package com.example.gymmanagementsystem.temcont;

import com.example.gymmanagementsystem.data.entities.service.Users;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NextCont implements Initializable {
    @FXML
    private TextField input;

    private Users users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    void changeHandler() {
        users.setUsername(input.getText());
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
