package com.example.gymmanagementsystem.dependencies;

import animatefx.animation.Shake;
import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.scene.Node;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;

public abstract class CommonClass {
    protected boolean start = false;
    protected File selectedFile;
    private ImageView loadingImageView;
    private ObservableList<Control> mandatoryFields;

    protected Customers customer;
    protected Users activeUser;
    protected BorderPane borderPane;
    public boolean imageUploaded = false;
    protected final ToggleGroup genderGroup;
    private Shake shake;
    public final String[] images = {"/com/example/gymmanagementsystem/style/icons/loading_5.gif", "/com/example/gymmanagementsystem/style/icons/loading_man.gif", "/com/example/gymmanagementsystem/style/icons/loading_girl.gif"};

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
        }
        return isValid;
    }

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
        shift.addAll("Subaxnimo", "Duhur", "Galabnimo", "Habeenimo");
        return shift;
    }

    public ObservableList<String> getPaidBy() {
        ObservableList<String> paidBy = FXCollections.observableArrayList();
        paidBy.addAll("eDahab", "Zaad service", "Hab kale");
        return paidBy;
    }

    public ObservableList<Control> getMandatoryFields() {
        if (mandatoryFields == null) {
            mandatoryFields = FXCollections.observableArrayList();
        }
        return mandatoryFields;
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
            Alerts.waningAlert("Fadlan sawirka lama helin isku day mar kale");
            imageUploaded = false;
        }
    }

    public void checkImage(ImageView imageView, String title) {
        boolean done = Alerts.confirmationAlert(title, "Iska dhaaf", "Hada soo qaad");
        if (done) uploadImage(imageView);
        else imageUploaded = true;
    }


    public File selectedFile() {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(imageFilter);
        selectedFile = chooser.showOpenDialog(null);
        return selectedFile;
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
            Alerts.errorAlert("Image byte writing err" + e.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;

    }

    protected ImageView getFirstImage(String path) {
        URL url = getClass().getResource(path);
        Image image = new Image(String.valueOf(url));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(28);
        imageView.setFitHeight(29);
        return imageView;
    }

    public void startTask(Service<Void> uploadService, JFXButton button, String message) {
        if (start) {
            uploadService.restart();
            button.setGraphic(getLoadingImageView());
            button.setText(message);
        } else {
            uploadService.start();
            if (button != null) {
                button.setGraphic(getLoadingImageView());
                button.setText(message);
            }
            start = true;
        }
    }

    public Shake getShake() {
        if (shake == null) {
            shake = new Shake();
        }
        return shake;
    }
}
