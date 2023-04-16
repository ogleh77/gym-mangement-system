package com.example.gymmanagementsystem.dao.main;

import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CustomException;
import com.example.gymmanagementsystem.models.main.CustomerModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;

public class CustomerService {
    private static final CustomerModel customerModel = new CustomerModel();
    private static ObservableList<Customers> allCustomersList;
    private static ObservableList<Customers> onlineCustomersByDate;
    private static ObservableList<Customers> offlineCustomersByDate;
    private static ObservableList<Customers> pendCustomersByDate;
    private static ObservableList<Customers> onlineCustomers;

    public static void insertOrUpdateCustomer(Customers customer, boolean newCustomer) throws SQLException {
        try {
            if (newCustomer) {
                insertCustomer(customer);
            } else {
                updateCustomer(customer);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: customers.phone)")) {
                throw new CustomException("Lanbarka " + customer.getPhone() + " hore ayaa loo diwaan geshay fadlan dooro lanbarkale");
            } else {
                throw new CustomException("Khalad ayaaa ka dhacay " + e.getMessage());
            }
        }
    }

    private static void insertCustomer(Customers customer) throws SQLException {
        customerModel.insert(customer);
    }

    private static void updateCustomer(Customers customer) throws SQLException {
        customerModel.update(customer);
    }

    public static void deleteCustomer(Customers customer) throws SQLException {
        try {
            if (customer == null) {
                throw new CustomException("Fadlan ka dooro table ka macmiilka aad donayso inad masaxdo");
            } else {

                ObservableList<Payments> payments = PaymentService.fetchAllPayments(customer.getPhone());

                String pendPaymentMessage = "Macmiilkan waxa uu xidhay payment sasoo ay tahay ma delete garayn kartid" +
                        "Marka hore dib u fur paymentkisa marka wakhtigu u dhamadana wad masaxi kartaa insha Allah.";
                String onlinePaymentMessage = "Macmiilkan waxa uu u socda payment sasoo ay tahay ma delete garayn kartid" +
                        " ilaa wakhtigiisa uu dhamaysanyo insha Allah.";
                if (customer.getPayments() != null) {
                    for (Payments payment : payments) {
                        if (payment.isOnline() || payment.isPending()) {
                            throw new CustomException(payment.isOnline() ? onlinePaymentMessage : pendPaymentMessage);
                        }
                        customerModel.delete(customer);
                    }
                }
                allCustomersList.remove(customer);
                customerModel.delete(customer);
            }
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }

    }

    public static int predictNextId() throws CustomException {
        try {
            return (1 + customerModel.nextID());
        } catch (SQLException e) {
            throw new CustomException("Khalad nex pridiction caused" + e.getMessage());
        }
    }
    //---------------------------Customers Lists--------------------------------


    public static ObservableList<Customers> fetchAllCustomer(Users activeUser) throws SQLException {
        return allCustomersList = customerModel.fetchAllCustomers(activeUser);
    }

    public static ObservableList<Customers> fetchAllOnlineCustomer(Users activeUser) throws SQLException {
        return customerModel.fetchOnlineCustomers(activeUser);
    }


    public static ObservableList<Customers> fetchOnlineCustomersWhereDateBetween(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        if (onlineCustomersByDate == null) {
            onlineCustomersByDate = customerModel.fetchOnlineCustomersWhereDate(activeUser, fromDate, toDate);
        }
        return onlineCustomersByDate;
    }

    public static ObservableList<Customers> fetchOfflineCustomersWhereDateBetween(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        if (offlineCustomersByDate == null) {
            offlineCustomersByDate = customerModel.fetchOfflineCustomersWhereDate(activeUser, fromDate, toDate);
        }
        return offlineCustomersByDate;
    }

    public static ObservableList<Customers> fetchPendCustomersWhereDateBetween(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        if (pendCustomersByDate == null) {
            pendCustomersByDate=customerModel.fetchPendCustomersWhereDate(activeUser, fromDate, toDate);
        }

        return pendCustomersByDate;
    }
    //-------------------------------Helpers------------------------------------


}
