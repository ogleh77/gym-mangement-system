package com.example.gymmanagementsystem.controllers;

import com.example.gymmanagementsystem.dao.CustomerService;
import com.example.gymmanagementsystem.dao.UserService;
import com.example.gymmanagementsystem.entities.Customers;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class PrintCustomerClassTest {

    @Test
    void printCustomersOnly() throws SQLException {
        ObservableList<Customers>customers= CustomerService.fetchAllCustomer(UserService.users().get(2));
        PrintCustomerClass.printCustomersOnly(customers);
    }

    @Test
    void printOnlineCustomersOnly() throws SQLException {
        ObservableList<Customers>customers= CustomerService.fetchOnlineCustomer(UserService.users().get(2));
        PrintCustomerClass.printOnlineCustomersOnly(customers);
    }
}