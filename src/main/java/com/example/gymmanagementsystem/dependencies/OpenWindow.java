package com.example.gymmanagementsystem.dependencies;

import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import animatefx.animation.SlideInLeft;
import animatefx.animation.SlideInRight;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class OpenWindow {
    private static Shake shake;
    private static SlideInRight slideInRight;
    private static SlideInLeft slideInLeft;
    private static FadeIn fadeIn;


    public static FXMLLoader openWindow(String url, BorderPane borderPane, HBox menu, StackPane notificationsHBox) throws IOException {

        if (menu != null) {
            menu.setVisible(true);
            getFadeIn().setNode(menu);
            getSlideInLeft().playOnFinished(fadeIn);
            getFadeIn().play();
        }
        //top profile
        if (notificationsHBox != null) {
            notificationsHBox.setVisible(true);
            getFadeIn().setNode(notificationsHBox);
            getFadeIn().play();
        }
        FXMLLoader loader = new FXMLLoader(OpenWindow.class.getResource(url));
        AnchorPane anchorPane = loader.load();
        getSlideInRight().setNode(anchorPane);
        getSlideInRight().play();
        borderPane.setCenter(anchorPane);
        return loader;
    }


    public static SlideInLeft getSlideInLeft() {
        if (slideInLeft == null) {
            slideInLeft = new SlideInLeft();
        }
        return slideInLeft;
    }
    public static SlideInRight getSlideInRight() {
        if (slideInRight == null) {
            slideInRight = new SlideInRight();
        }
        return slideInRight;
    }
    public static FadeIn getFadeIn() {
        if (fadeIn == null) {
            fadeIn = new FadeIn();
        }
        return fadeIn;
    }

    public static Shake getShake() {
        if (shake == null) {
            shake = new Shake();
            System.out.println("Shake init");
        }
        return shake;
    }

}
