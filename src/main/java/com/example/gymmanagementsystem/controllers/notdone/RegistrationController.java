package com.example.gymmanagementsystem.controllers.notdone;

import com.example.gymmanagementsystem.data.dto.main.CustomerService;
import com.example.gymmanagementsystem.data.dto.Data;
import com.example.gymmanagementsystem.data.dto.GymService;
import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.service.Gym;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.dependencies.Alerts;
import com.example.gymmanagementsystem.dependencies.CommonClass;
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
    private double _waist = 0;
    private double _hips = 0;
    private double _forearm = 0;
    private double _chest = 0;
    private double _weight = 0;
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
    void clearHandler() {
        clear();
    }

    // TODO: 02/05/2023 Check this insha Allah whay compianing unique phone 
    @FXML
    void saveHandler() {
        if (isValid(getMandatoryFields(), genderGroup) && (phone.getText().length() == 7 || phone.getText().length() == 10)) {
            if (currentGym.isImageUpload() && !imageUploaded) {
                checkImage(imgView, "Fadlan sawirku wuu kaa cawinayaa inaad wejiga \n" + "macmiilka ka dhex garan kartid macamisha kle ");
            }
            try {
                _weight = ((!weight.getText().isEmpty() || !weight.getText().isBlank())) ? Double.parseDouble(weight.getText().trim()) : 65.0;
                _waist = (!waist.getText().isEmpty() || !waist.getText().isBlank() ? Double.parseDouble(waist.getText()) : 0);
                _hips = (!hips.getText().isEmpty() || !hips.getText().isBlank() ? Double.parseDouble(hips.getText()) : 0);
                _forearm = (!Forearm.getText().isEmpty() || !Forearm.getText().isBlank() ? Double.parseDouble(Forearm.getText()) : 0);
                _chest = (!chest.getText().isEmpty() || !chest.getText().isBlank() ? Double.parseDouble(chest.getText()) : 0);

                startTask(service, registerBtn, isCustomerNew ? "Saving" : "Updating");
                clearBtn.setDisable(true);
            } catch (Exception e) {
                if (e instanceof NumberFormatException)
                    Alerts.waningAlert("Fadlan ka saar cabirada (Measurements) ka points ka dherigaa" +
                            "\n caused by " + e.getMessage() + " ☹️");
                else Alerts.errorAlert(e.getMessage());
            }
        }
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
            waist.setText(String.valueOf(customer.getWaist()));
            Forearm.setText(String.valueOf(customer.getForeArm()));
            hips.setText(String.valueOf(customer.getHips()));
            chest.setText(String.valueOf(customer.getChest()));
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
                        CustomerService.insertOrUpdateCustomer(savingCustomer(), isCustomerNew);
                        if (isCustomerNew) {
                            customersList.add(savingCustomer());
                        }
                        CustomerService.insertOrUpdateCustomer(customer, isCustomerNew);
                        //Thread.sleep(1000);
                        Platform.runLater(() -> {
                            if (isCustomerNew) {
                                boolean data = Alerts.singleConfirmationAlert("" + "Waxaad samaysay macmiil cusub u samee macmiilka payment?", "Go to payments");
                                if (data) {
                                    openPayment();
                                }
                            } else {
                                boolean data = Alerts.singleConfirmationAlert("" + "Waad ku gulaystay update-ka macmiilka ku noqo home ka?", "Back to Home");
                                if (data) {
                                    openHome();
                                }
                            }
                        });
                    } catch (SQLException e) {
                        Platform.runLater(() -> Alerts.errorAlert(e.getMessage() + "☹️"));
                    }

                    return null;
                }
            };
        }
    };

    //------------------Helper methods------------------–

    private Customers savingCustomer() {

        String gander = male.isSelected() ? "Male" : "Female";
        int customerId = super.customer == null ? 0 : customer.getCustomerId();
        String _shift = shift.getValue() != null ? shift.getValue() : "Morning";

        if (customer == null) {
            customer = new Customers(newCustomerID, firstName.getText().trim(), lastName.getText().trim(), middleName.getText().trim(), phone.getText().trim(), gander, _shift, address.getText().trim(), selectedFile == null ? null : readFile(selectedFile.getAbsolutePath()), _weight, activeUser.getUsername());
        } else {
            customer.setShift(_shift);
            customer.setCustomerId(customerId);
            customer.setFirstName(firstName.getText().trim());
            customer.setGander(gander);
            customer.setWhoAdded(activeUser.getUsername());
            customer.setImage(selectedFile == null ? customer.getImage() : readFile(selectedFile.getAbsolutePath()));
            customer.setAddress(address.getText().trim());
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
        System.out.println("Payments");
//        try {
//            FXMLLoader loader = OpenWindow.secondWindow("/com/example/gymmanagementsystem/newviews/main/payments.fxml",
//                    borderPane);
//            PaymentController controller = loader.getController();
//            controller.setBorderPane(borderPane);
//            controller.setCustomer(customer);
//        } catch (Exception e) {
//            Alerts.errorAlert(e.getMessage());
//        }
    }

    private void openHome() {
        System.out.println("Home");

//        try {
//            FXMLLoader loader = OpenWindow.secondWindow("/com/example/gymmanagementsystem/newviews/main/home.fxml",
//                    borderPane);
//            HomeController controller = loader.getController();
//            controller.setActiveUser(activeUser);
//            controller.setBorderPane(borderPane);
//        } catch (Exception e) {
//            Alerts.errorAlert(e.getMessage());
//        }
    }


}
