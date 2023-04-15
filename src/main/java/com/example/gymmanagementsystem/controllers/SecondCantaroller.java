package com.example.gymmanagementsystem.controllers;

import com.example.gymmanagementsystem.dao.Data;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SecondCantaroller implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    void hashHandelr() {
        System.out.println("Second customers " + Data.getAllCustomersList().hashCode());
        System.out.println("Second online customers " + Data.getOnlineCustomers().hashCode());
    }

    @FXML
    void logoutHandler() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gymmanagementsystem/views/test-vies.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
}
