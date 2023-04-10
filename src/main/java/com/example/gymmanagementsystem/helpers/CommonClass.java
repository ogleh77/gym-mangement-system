package com.example.gymmanagementsystem.helpers;

import animatefx.animation.*;
import com.example.gymmanagementsystem.entities.Customers;
import com.example.gymmanagementsystem.entities.Users;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Optional;

public abstract class CommonClass {
    protected File selectedFile;

    protected boolean start = false;
    protected boolean error = false;
    private ImageView loadingImageView;
    protected final ToggleGroup genderGroup;
    private ObservableList<Control> mandatoryFields;
    private Shake shake;
    private SlideInRight slideInRight;
    private SlideInLeft slideInLeft;
    private FadeIn fadeIn;

    protected Customers customer;

    protected Users activeUser;

    protected BorderPane borderPane;
    public boolean imageUploaded = false;

    protected final ButtonType ok = new ButtonType("Haa");
    protected final ButtonType no = new ButtonType("Maya");
    public final String[] images = {
            "/com/example/gymmanagementsystem/style/icons/loading_5.gif",
            "/com/example/gymmanagementsystem/style/icons/loading_man.gif",
            "/com/example/gymmanagementsystem/style/icons/loading_girl.gif"
    };

    public CommonClass() {
        this.genderGroup = new ToggleGroup();

    }


    protected boolean isValid(ObservableList<Control> mandatoryFields, ToggleGroup group) {

        boolean isValid = true;
        try {
            for (Control control : mandatoryFields) {
                if (control instanceof TextInputControl) {
                    if ((((TextInputControl) control).getText().isBlank() || ((TextInputControl) control).getText().isEmpty())) {
                        getShake().setNode(control);
                        control.setStyle("-fx-border-color: #cb3d3d;-fx-border-width: 2");
                        getShake().play();
                        isValid = false;
                    } else {
                        control.setStyle(null);
                    }
                } else if (control instanceof ComboBoxBase<?> && (((ComboBoxBase<?>) control).getValue() == null)) {
                    getShake().setNode(control);
                    getShake().play();
                    control.setStyle("-fx-border-color: #cb3d3d;-fx-border-width: 2");
                    isValid = false;
                } else {
                    control.setStyle(null);
                }
            }
            if (group.getSelectedToggle() == null) {
                getShake().setNode((Node) group.getToggles().get(0));
                getShake().play();
                getShake().setNode((Node) group.getToggles().get(1));
                ((Node) group.getToggles().get(0)).setStyle("-fx-border-color: #cb3d3d;-fx-border-width: 2");
                ((Node) group.getToggles().get(1)).setStyle("-fx-border-color: #cb3d3d;-fx-border-width: 2");
                getShake().play();
                isValid = false;
            } else {
                ((Node) group.getToggles().get(0)).setStyle(null);
                ((Node) group.getToggles().get(1)).setStyle(null);
            }
        } catch (NullPointerException e) {
            //  System.out.println(e.getMessage());
        }
        return isValid;
    }

    protected FXMLLoader openWindow(String url, BorderPane borderPane, HBox menu, StackPane notificationsHBox) throws IOException {

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        AnchorPane anchorPane = loader.load();
        getSlideInRight().setNode(anchorPane);
        getSlideInRight().play();
        borderPane.setCenter(anchorPane);
        return loader;
    }

    protected FXMLLoader openNormalWindow(String url, BorderPane borderPane) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        AnchorPane anchorPane = loader.load();
        getSlideInRight().setNode(anchorPane);
        getSlideInRight().play();
        borderPane.setCenter(anchorPane);
        return loader;
    }

    //Alert when error occur
    public Alert errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle("Khald ba dhacay");
        alert.show();
        return alert;
    }

    //Alert and give message
    public Alert informationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle("Hablyo!");
        return alert;
    }

    public void infoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.setTitle("War-gelin!");
        alert.showAndWait();
    }


    public File selectedFile() {
        FileChooser.ExtensionFilter imageFilter
                = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(imageFilter);
        selectedFile = chooser.showOpenDialog(null);
        return selectedFile;
    }


    //Create and load the loading gif image
    public ImageView getLoadingImageView() {
        if (loadingImageView == null) {
            URL url = getClass().getResource(images[0]);
            Image image = new Image(String.valueOf(url));
            loadingImageView = new ImageView();
            loadingImageView.setFitHeight(30);
            loadingImageView.setFitWidth(30);
            loadingImageView.setImage(image);
        }
        return loadingImageView;
    }

    public ObservableList<String> getShift() {
        ObservableList<String> shift = FXCollections.observableArrayList();
        shift.addAll("Morning", "Noon", "Afternoon", "Night");
        return shift;
    }

    public ObservableList<String> getPaidBy() {
        ObservableList<String> paidBy = FXCollections.observableArrayList();
        paidBy.addAll("eDahab", "Zaad service", "other");
        return paidBy;
    }

    public ObservableList<Control> getMandatoryFields() {
        if (mandatoryFields == null) {
            mandatoryFields = FXCollections.observableArrayList();
        }
        return mandatoryFields;
    }

    public SlideInLeft getSlideInLeft() {
        if (slideInLeft == null) {
            slideInLeft = new SlideInLeft();
        }
        return slideInLeft;
    }


    public SlideInRight getSlideInRight() {
        if (slideInRight == null) {
            slideInRight = new SlideInRight();
        }
        return slideInRight;
    }

    public FadeIn getFadeIn() {
        if (fadeIn == null) {
            fadeIn = new FadeIn();
        }
        return fadeIn;
    }

    private Shake getShake() {
        if (shake == null) {
            shake = new Shake();
            System.out.println("Shake init");
        }
        return shake;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public void setActiveUser(Users activeUser) {
        this.activeUser = activeUser;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void uploadImage(ImageView imageView) {
        try {
            if (selectedFile() != null) {
                Image image = new Image(new FileInputStream(selectedFile.getAbsolutePath()));
                imageView.setImage(image);
                imageView.setX(50);
                imageView.setY(25);
                imageUploaded = true;
            }
        } catch (FileNotFoundException e) {
            errorMessage("Fadlan sawirka lama helin isku day mar kale");
            imageUploaded = false;
        }
    }


    public void checkImage(ImageView imageView, String title) {
        ButtonType ok = new ButtonType("Hada soo upload-garee", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Ogaan baan u dhaafay.", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, title, ok, cancel);
        alert.setTitle("Sawir lama helin");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ok) {
            uploadImage(imageView);
        } else {
            imageUploaded = true;
        }
    }


    public byte[] readFile(String file) {

        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1; ) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            errorMessage("Image byte writing err" + e.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;

    }

    public void closeStage(Stage stage, Node node) {
        FadeOut fadeOut = new FadeOut(node);
        fadeOut.setOnFinished(e -> stage.close());
        fadeOut.setSpeed(2);
        fadeOut.play();
    }

    protected ImageView getFirstImage(String path) {
        URL url = getClass().getResource(path);
        Image image = new Image(String.valueOf(url));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(28);
        imageView.setFitHeight(29);
        return imageView;
    }

    public void enterKeyFire(JFXButton button, Stage stage) {
        stage.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                button.fire();
                ev.consume();
            }
        });
    }

    public void startTask(Service<Void> uploadService, JFXButton button, String message) {
        if (start) {
            uploadService.restart();
            button.setGraphic(getLoadingImageView());
            button.setText(message);
        } else {
            uploadService.start();
            button.setGraphic(getLoadingImageView());
            button.setText(message);
            start = true;
        }
    }
}
