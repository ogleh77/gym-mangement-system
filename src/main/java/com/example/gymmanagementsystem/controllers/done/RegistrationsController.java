package com.example.gymmanagementsystem.controllers.done;

import com.example.gymmanagementsystem.dao.CustomerService;
import com.example.gymmanagementsystem.dao.GymService;
import com.example.gymmanagementsystem.entities.Customers;
import com.example.gymmanagementsystem.entities.Gym;
import com.example.gymmanagementsystem.entities.Users;
import com.example.gymmanagementsystem.helpers.CommonClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationsController extends CommonClass implements Initializable {
    @FXML
    private TextField address;

    @FXML
    private JFXRadioButton female;

    @FXML
    private TextField firstName;

    @FXML
    private Label headerInfo;

    @FXML
    private ImageView imgView;

    @FXML
    private TextField lastName;

    @FXML
    private JFXRadioButton male;

    @FXML
    private TextField middleName;

    @FXML
    private TextField phone;

    @FXML
    private Label phoneValidation;

    @FXML
    private JFXButton registerBtn;

    @FXML
    private ComboBox<String> shift;
    @FXML
    private TextField weight;
    @FXML
    private TextField Forearm;

    @FXML
    private TextField chest;

    @FXML
    private TextField hips;

    @FXML
    private TextField waist;
    @FXML
    private JFXButton clearBtn;
    private boolean isCustomerNew = true;
    private final int newCustomerID;

    private ObservableList<Customers> customersList;
    private final Gym currentGym;

    public RegistrationsController() throws SQLException {
        newCustomerID = CustomerService.predictNextId();
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            // TODO: 09/04/2023 Check if can trigger enter key insha Allah enterKeyFire();
            shift.setItems(getShift());
            male.setToggleGroup(genderGroup);
            female.setToggleGroup(genderGroup);
            getMandatoryFields().addAll(firstName, middleName, lastName, phone, shift);

            enterKeyFire(registerBtn,(Stage) borderPane.getCenter().getScene().getWindow());
        });
        phoneValidation();
        weightValidation();
        chestValidation();
        waistValidation();
        hipsValidation();
        forearmValidation();

        service.setOnSucceeded(e -> {
            registerBtn.setGraphic(getFirstImage(isCustomerNew ?
                    "/com/example/gymmanagementsystem/style/icons/icons8-mobile-payment-50.png" : ""));
            registerBtn.setText(isCustomerNew ? "Go payment" : "Updated");
            System.out.println("Done");
        });

    }

    @FXML
    void customerSaveHandler() {
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7)) {
            if (!imageUploaded && currentGym.isImageUpload()) {
                checkImage(imgView, "Fadlan sawirku wuu kaa cawinayaa inaad wejiga \n" +
                        "macmiilka ka dhex garan kartid macamisha kle 😊");
            }
            clearBtn.setDisable(true);
            startTask(service, registerBtn, isCustomerNew ? "Saving" : "Updateing");
        }
    }

    @FXML
    void clearHandler() {
        clear();
    }

    @FXML
    void uploadImageHandler() {
        uploadImage(imgView);
    }

    @Override
    public void setCustomer(Customers customer) {
        super.setCustomer(customer);
        if (customer != null) {
            firstName.setText(customer.getFirstName());
            middleName.setText(customer.getMiddleName());
            lastName.setText(customer.getLastName());
            phone.setText(customer.getPhone());
            weight.setText(String.valueOf(customer.getWeight()));
            shift.setValue(customer.getShift());
            address.setText(customer.getAddress() != null ? customer.getAddress() : "No address");
            if (customer.getGander().equals("Male")) {
                male.setSelected(true);
            } else {
                female.setSelected(true);
            }
            shift.setValue(customer.getShift());
            address.setText(customer.getAddress() != null ? customer.getAddress() : "No address");
            waist.setText(String.valueOf(customer.getWaist()));
            Forearm.setText(String.valueOf(customer.getForeArm()));
            hips.setText(String.valueOf(customer.getHips()));
            chest.setText(String.valueOf(customer.getChest()));

            if (customer.getImage() != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(customer.getImage());
                Image image = new Image(bis);
                imageUploaded = true;
                imgView.setImage(image);
            }

            headerInfo.setText("CUSTOMER UPDATE PAGE");
            isCustomerNew = false;
            registerBtn.setText("Update");
        }
    }

    @Override
    public void setActiveUser(Users activeUser) {
        super.setActiveUser(activeUser);
        if (customer == null) {
            try {
                customersList = CustomerService.fetchAllCustomer(activeUser);
            } catch (SQLException e) {
                e.printStackTrace();
                errorMessage(e.getMessage());
            }
        }
    }

    @Override
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }


    private final Service<Void> service = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws InterruptedException {
                    try {
                        CustomerService.insertOrUpdateCustomer(savingCustomer(), isCustomerNew);
                        if (isCustomerNew) {
                            customersList.add(0, savingCustomer());
                            Thread.sleep(1000);
                        }

                    } catch (Exception e) {
                        if (e.getClass().isInstance(SQLException.class)) {
                            Platform.runLater(() -> errorMessage(e.getMessage() + "☹️"));
                        } else if (e.getClass().isInstance(NumberFormatException.class)) {
                            Platform.runLater(() -> errorMessage("Fadlan hubi inad si saxan u gelisay cabirada  " + " error caused " + e.getMessage()));
                        } else {
                            Platform.runLater(() -> errorMessage("error caused by" + e.getMessage()));
                        }
                    }
                    Thread.sleep(1000);
                    return null;
                }
            };
        }
    };
    //----------------_Helper methods---------------–

    private Customers savingCustomer() {

        String gander = male.isSelected() ? "Male" : "Female";
        String _address = address.getText() != null ? address.getText().trim() : "No address";
        double _weight = ((!weight.getText().isEmpty() || !weight.getText().isBlank())) ? Double.parseDouble(weight.getText().trim()) : 65.0;
        int customerId = super.customer == null ? 0 : customer.getCustomerId();
        String _shift = shift.getValue() != null ? shift.getValue() : "Morning";

        double _waist = (!waist.getText().isEmpty() || !waist.getText().isBlank() ? Double.parseDouble(waist.getText()) : 0);
        double _hips = (!hips.getText().isEmpty() || !hips.getText().isBlank() ? Double.parseDouble(hips.getText()) : 0);

        double _forearm = (!Forearm.getText().isEmpty() || !Forearm.getText().isBlank() ? Double.parseDouble(Forearm.getText()) : 0);
        double _chest = (!chest.getText().isEmpty() || !chest.getText().isBlank() ? Double.parseDouble(chest.getText()) : 0);

        if (customer == null) {
            customer = new Customers(newCustomerID, firstName.getText().trim(), lastName.getText().trim(), middleName.getText().trim(), phone.getText().trim(), gander, _shift, _address, selectedFile == null ? null : readFile(selectedFile.getAbsolutePath()), _weight, activeUser.getUsername());
        } else {
            customer.setShift(_shift);
            customer.setCustomerId(customerId);
            customer.setFirstName(firstName.getText().trim());
            customer.setGander(gander);
            customer.setWhoAdded(activeUser.getUsername());
            customer.setImage(selectedFile == null ? customer.getImage() : readFile(selectedFile.getAbsolutePath()));
            customer.setAddress(_address.trim());
            customer.setLastName(lastName.getText().trim());
            customer.setMiddleName(middleName.getText().trim());
            customer.setPhone(phone.getText().trim());
        }
        customer.setWeight(_weight);
        customer.setHips(_hips);
        customer.setChest(_chest);
        customer.setForeArm(_forearm);
        customer.setWaist(_waist);
        return customer;
    }

    private void phoneValidation() {
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone.setText(newValue.replaceAll("\\D", ""));
                phoneValidation.setText("Fadlan lanbarka xarfo looma ogola");
                phoneValidation.setVisible(true);
            } else if (!phone.getText().matches("^\\d{7}")) {
                phoneValidation.setText("Fadlan lanbarku kama yaran karo 7 digit");
                phoneValidation.setVisible(true);
            } else {
                phoneValidation.setVisible(false);
            }
        });

    }

    private void chestValidation() {
        chest.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*)")) {
                chest.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void waistValidation() {
        waist.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*)")) {
                waist.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void hipsValidation() {
        hips.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*)")) {
                hips.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void forearmValidation() {
        Forearm.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*)")) {
                Forearm.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void weightValidation() {
        weight.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d*)")) {
                weight.setText(newValue.replaceAll("[^\\d.?}]", ""));
            }
        });
    }

    private void clear() {
        ObservableList<Control> controls = FXCollections.observableArrayList(getMandatoryFields());
        controls.add(weight);
        controls.add(address);
        for (Control control : controls) {
            if (control instanceof TextField) {
                ((TextField) control).clear();
            } else if (control instanceof ComboBox<?>) {
                ((ComboBox<?>) control).setValue(null);
            }

            if (male.isSelected()) {
                male.setSelected(false);
            } else {
                female.setSelected(false);
            }
        }
    }

}
