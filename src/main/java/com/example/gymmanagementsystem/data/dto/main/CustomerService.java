package com.example.gymmanagementsystem.data.dto.main;

import com.example.gymmanagementsystem.data.dto.Data;
import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.main.Payments;
import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.data.models.main.CustomerModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;

public class CustomerService {
    private static final CustomerModel customerModel = new CustomerModel();

    public static void insertOrUpdateCustomer(Customers customer, boolean newCustomer) throws SQLException {
        try {
            if (newCustomer) {
                insertCustomer(customer);
            } else {
                updateCustomer(customer);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: customers.phone)")) {
                throw new SQLException("Lanbarka " + customer.getPhone() + " hore ayaa loo diwaan geshay fadlan dooro lanbarkale");
            } else {
                throw new SQLException("from sqlite " + e.getMessage());
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
            ObservableList<Payments> payments = PaymentService.fetchAllPayments(customer.getPhone());

            String pendPaymentMessage = "Macmiilkan waxa uu xidhay payment sasoo ay tahay ma delete garayn kartid" +
                    "Marka hore dib u fur paymentkisa marka wakhtigu u dhamadana wad masaxi kartaa insha Allah.";
            String onlinePaymentMessage = "Macmiilkan waxa uu u socda payment sasoo ay tahay ma delete garayn kartid" +
                    " ilaa wakhtigiisa uu dhamaysanyo insha Allah.";

            if (customer.getPayments().isEmpty()) {
                Data.getAllCustomersList().remove(customer);
                customerModel.delete(customer);
            } else {
                for (Payments payment : payments) {
                    if (payment.isOnline() || payment.isPending()) {
                        throw new SQLException(payment.isOnline() ? onlinePaymentMessage : pendPaymentMessage);
                    }
                    customerModel.delete(customer);
                    Data.getAllCustomersList().remove(customer);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }

    }

    public static int predictNextId() throws SQLException {
        try {
            return (1 + customerModel.nextID());
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }
    }

    //--------------------------Helpers customers list----------------------------
    public static ObservableList<Customers> fetchAllCustomer(Users activeUser) throws SQLException {
        return customerModel.fetchAllCustomers(activeUser);
    }

    public static ObservableList<Customers> fetchAllOnlineCustomer(Users activeUser) throws SQLException {
        return customerModel.fetchOnlineCustomers(activeUser);
    }

    public static ObservableList<Customers> fetchOnlineCustomersWhereDateBetween(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        return customerModel.fetchOnlineCustomersWhereDate(activeUser, fromDate, toDate);
    }

    public static ObservableList<Customers> fetchOfflineCustomersWhereDateBetween(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        return customerModel.fetchOfflineCustomersWhereDate(activeUser, fromDate, toDate);
    }

    public static ObservableList<Customers> fetchPendCustomersWhereDateBetween(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        return customerModel.fetchPendCustomersWhereDate(activeUser, fromDate, toDate);
    }

    public static ObservableList<Customers> fetchQualifiedOfflineCustomers(String customerQuery, LocalDate fromDate, LocalDate toDate) throws SQLException {
        return customerModel.fetchQualifiedOfflineCustomers(customerQuery, fromDate, toDate);
    }

}
