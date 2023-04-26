package com.example.gymmanagementsystem.controllers.main;

import com.example.gymmanagementsystem.dao.Data;
import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.OpenWindow;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.entities.service.Users;
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

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController extends CommonClass implements Initializable {
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
    @FXML
    private JFXButton imageUploadBtn;

    private boolean isCustomerNew = true;
    private final int newCustomerID;
    private final Gym currentGym;
    private ObservableList<Customers> customersList;

    public RegistrationController() throws SQLException {
        this.newCustomerID = CustomerService.predictNextId();
        this.currentGym = GymService.getGym();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::init);
        phoneValidation();
        chestValidation();
        waistValidation();
        hipsValidation();
        chestValidation();
        forearmValidation();
        weightValidation();
        service.setOnSucceeded(e -> {
            registerBtn.setGraphic(getFirstImage("/com/example/gymmanagementsystem/style/icons/icons8-save-30.png"));
            registerBtn.setText(isCustomerNew ? "Payment" : "Updated");
            clearBtn.setDisable(false);
        });
    }

    @FXML
    void saveHandler() {
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7 || phone.getText().length() == 10)) {
            if (!currentGym.isImageUpload() && !imageUploaded) {
                checkImage(imgView, "Fadlan sawirku wuu kaa cawinayaa inaad wejiga \n" + "macmiilka ka dhex garan kartid macamisha kle 😊");
                return;
            }
            clearBtn.setDisable(true);
            startTask(service, registerBtn, isCustomerNew ? "Saving" : "Updating");
        }
    }

    @FXML
    void uploadImageHandler() {
        uploadImage(imgView);
    }

    @FXML
    void clearHandler() {
        clear();
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
// TODO: 17/04/2023 check why is redudent alert is occuring insha Allah
            if (customer.getImage() != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(customer.getImage());
                Image image = new Image(bis);
                imageUploaded = true;
                imgView.setImage(image);
                imageUploadBtn.setText("Change image");
            }

            headerInfo.setText("CUSTOMER UPDATE PAGE");
            isCustomerNew = false;
            registerBtn.setText("Update");
        }
    }


    @Override
    public void setActiveUser(Users activeUser) {
        try {
            Data.setAllCustomersList(CustomerService.fetchAllOnlineCustomer(activeUser));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.setActiveUser(activeUser);
        this.customersList = Data.getAllCustomersList();
        System.out.println(customersList);
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
                protected Void call() {
                    try {
                   //      CustomerService.insertOrUpdateCustomer(savingCustomer(), isCustomerNew);
                        if (isCustomerNew) {
                            customersList.add(savingCustomer());
                            System.out.println(customersList);
                        }
                        Thread.sleep(1000);
                        Platform.runLater(() -> {
                            if (isCustomerNew) {
                                boolean data = Alerts.singleConfirmationAlert("" +
                                        "Waxaad samaysay macmiil cusub u samee macmiilka payment?", "Go to payments");
                                if (data) {
                                    openPayment();
                                }
                            } else {
                                boolean data = Alerts.singleConfirmationAlert("" +
                                        "Waad ku gulaystay update-ka macmiilka ku noqo home ka?", "Back to Home");
                                if (data) {
                                    openHome();
                                }
//                                Alerts.notificationAlert("Waad ku gulaystay update-ka macmiilka", "Updated");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getClass().isInstance(SQLException.class)) {
                            Platform.runLater(() -> errorMessage(e.getMessage() + "☹️"));
                        } else if (e.getMessage().matches("multiple points")) {
                            Platform.runLater(() -> errorMessage("Hubi inaad points racisay markii aad gelinasay cabirada (Measurements)" +
                                    "\ncaused by " + e.getMessage() + " ☹️"));
                        } else {
                            Platform.runLater(() -> errorMessage("error caused by" + e.getMessage()));
                        }
                    }

                    return null;
                }
            };
        }
    };

    //------------------------------Helper methods------------------------
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
            }
        });

        phone.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!phone.isFocused()) {
                if (phone.getText().length() < 7) {
                    phoneValidation.setText("Fadlan lanbarku waa inu noqdaa 7 ama 10 lanbar");
                    phoneValidation.setVisible(true);
                } else if (phone.getText().length() > 7 && phone.getText().length() < 10) {
                    phoneValidation.setText("Fadlan lanbarku waa inu noqdaa 7 ama 10 lanbar");
                    phoneValidation.setVisible(true);
                } else if (phone.getText().length() > 10) {
                    phoneValidation.setText("Fadlan lanbarku kama badan karo 10 lanbar");
                    phoneValidation.setVisible(true);
                } else {
                    phoneValidation.setVisible(false);
                }
            }
//        phone.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
//            if (!phone.isFocused()) {
////                if (!phone.getText().matches("^\\d{7}")) {
//                if (phone.getText().length() < 7) {
//                    phoneValidation.setText("Fadlan lanbarku kama yaran karo 7 digit");
//                } else if (phone.getText().length() > 7 && phone.getText().length() < 10) {
//                    phoneValidation.setText("Fadlan lanbarku kama badan karo 10 digit");
//                }else {
//                    phoneValidation.setVisible(false);
//
//                }
//                phoneValidation.setVisible(true);
//            } else {
//                phoneValidation.setVisible(false);
//            }
            //}
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
        controls.addAll(address, waist, hips, weight, chest, Forearm);
        for (Control control : controls) {
            if (control instanceof TextField) {
                ((TextField) control).clear();
            } else if (control instanceof ComboBox<?>) {
                ((ComboBox<?>) control).setValue(null);
            }
        }
        if (male.isSelected()) {
            male.setSelected(false);
        } else {
            female.setSelected(false);
        }
    }

    private void init() {
        shift.setItems(getShift());
        male.setToggleGroup(genderGroup);
        female.setToggleGroup(genderGroup);
        getMandatoryFields().addAll(firstName, middleName, lastName, phone, shift);
    }

    private void openPayment() {
        try {
            OpenWindow.secondWindow("/com/example/gymmanagementsystem/newviews/main/payments.fxml",
                    borderPane);
        } catch (Exception e) {
            Alerts.errorAlert(e.getMessage(), "Khalad aya dhacay");
        }
        //  System.out.println("Payment called");
    }

    private void openHome() {
//        try {
//            OpenWindow.secondWindow("/com/example/gymmanagementsystem/newviews/main/payments.fxml",
//                    borderPane);
//        } catch (Exception e) {
//            Alerts.errorAlert(e.getMessage(), "Khalad aya dhacay");
//        }
        System.out.println("Back to  Home");
    }

}
