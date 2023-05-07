package com.example.gymmanagementsystem.dependencies;

import animatefx.animation.*;
import com.example.gymmanagementsystem.controllers.main.DashboardMenuController;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class OpenWindow {

    private static Shake shake;
    private static SlideInRight slideInRight;
    private static SlideInLeft slideInLeft;
    private static FadeIn fadeIn;
    private static SlideInUp slideInUp;
    private static FadeOut fadeOut;
    private static double xOffset = 0;
    private static double yOffset = 0;
    private final static URL icon = OpenWindow.class.getResource("/com/example/gymmanagementsystem/style/icons/app-icon.jpeg");


    public static FXMLLoader openStagedWindow(String url, Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader(OpenWindow.class.getResource(url));
        Stage stage = new Stage(StageStyle.UNDECORATED);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.initOwner(node != null ? node.getScene().getWindow() : null);
        stage.getIcons().add(new Image(String.valueOf(icon)));
        stage.show();
        return loader;
    }

    public static FXMLLoader secondWindow(String url, BorderPane borderPane) throws IOException {
        getFadeOut().setNode(borderPane.getCenter());
        getFadeOut().setSpeed(2);
        getFadeOut().play();
        FXMLLoader loader = new FXMLLoader(OpenWindow.class.getResource(url));
        AnchorPane anchorPane = loader.load();
        getFadeOut().setOnFinished(e -> {
            getFadeIn().setNode(anchorPane);
            getFadeIn().play();
            borderPane.setCenter(anchorPane);
        });
        return loader;
    }

    public static FXMLLoader openFromDashboardWindow(String url, BorderPane borderPane, HBox hBox, MenuItem logout) throws IOException {
        FXMLLoader loader = new FXMLLoader(OpenWindow.class.getResource(url));
        AnchorPane anchorPane = loader.load();
        getSlideUp().setNode(anchorPane);
        getSlideUp().play();
        borderPane.setCenter(anchorPane);
        getFadeIn().setNode(hBox);
        HBox topBox = (HBox) borderPane.getTop();
        topBox.getChildren().add(1, hBox);
        getFadeIn().play();
        // TODO: 06/05/2023  chec logiut
        logout.setVisible(logout.isVisible());
        return loader;
    }


    public static void dashboardWindow(BorderPane borderPane, HBox topPane, Users activeUser,MenuItem logout) throws Exception {
        FXMLLoader loader = new FXMLLoader(OpenWindow.class.getResource("/com/example/gymmanagementsystem/views/main/dashboard-menu.fxml"));
        AnchorPane anchorPane = loader.load();
        FadeIn fadeIn = new FadeIn(anchorPane);
        HBox hBox = (HBox) borderPane.getTop();
        hBox.getChildren().remove(topPane);
        DashboardMenuController controller = loader.getController();
        controller.setBorderPane(borderPane);
        controller.setActiveUser(activeUser);
        controller.setMenus(topPane,logout);
        borderPane.setCenter(anchorPane);
        fadeIn.setSpeed(0.4);
        fadeIn.play();
    }

    public static FXMLLoader mainWindow(String url, BorderPane borderPane) throws IOException {
        FXMLLoader loader = new FXMLLoader(OpenWindow.class.getResource(url));

        AnchorPane anchorPane = loader.load();
        getSlideUp().setNode(anchorPane);
        getSlideUp().play();
        borderPane.setCenter(anchorPane);
        return loader;
    }

    public static void reOpenLogin(Stage thisStage, String username, BorderPane borderPane) {
        boolean done = Alerts.confirmationAlert("Ma hubtaa inaad ka baxdo user ka " + username, "Maya", "Haa");
        if (done) {
            FadeOut fadeOut = OpenWindow.getFadeOut();
            fadeOut.setNode(borderPane);
            fadeOut.setSpeed(2);
            fadeOut.setOnFinished(e -> {
                thisStage.close();
                try {
                    OpenWindow.openStagedWindow("/com/example/gymmanagementsystem/views/service/login.fxml", borderPane);
                } catch (Exception ex) {
                    Alerts.errorAlert(ex.getMessage());
                    ex.printStackTrace();
                }
            });
            fadeOut.play();
        }
    }

    //-------------------Helpers---------------------------–
    public static void closeStage(Stage stage, Node node) {
        if (fadeOut == null) {
            fadeOut = new FadeOut(node);
        }
        fadeOut.setSpeed(2);
        fadeOut.setOnFinished(e -> {
            stage.close();
        });
        fadeOut.play();
    }

    public static void stageDrag(Stage stage, HBox topPane) {
        topPane.setOnMousePressed(event -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
    }

    public static void stageDropped(Stage stage, HBox topPane) {
        topPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    public static void stageEnterKeyFire(JFXButton button, Stage stage) {
        stage.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                button.fire();
                ev.consume();
            }
        });
    }

    public static SlideInLeft getSlideInLeft() {
        if (slideInLeft != null) return slideInLeft;
        slideInLeft = new SlideInLeft();
        return slideInLeft;
    }

    public static SlideInRight getSlideInRight() {
        if (slideInRight != null) return slideInRight;
        slideInRight = new SlideInRight();
        return slideInRight;
    }

    public static FadeIn getFadeIn() {
        if (fadeIn != null) return fadeIn;
        fadeIn = new FadeIn();
        return fadeIn;
    }

    public static FadeOut getFadeOut() {
        if (fadeOut == null) fadeOut = new FadeOut();
        return fadeOut;
    }

    public static SlideInUp getSlideUp() {
        if (slideInUp != null) return slideInUp;
        slideInUp = new SlideInUp();
        return slideInUp;
    }

    public static Shake getShake() {
        if (shake != null) return shake;
        shake = new Shake();
        return shake;
    }
}
