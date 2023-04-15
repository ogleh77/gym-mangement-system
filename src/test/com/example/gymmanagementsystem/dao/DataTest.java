package com.example.gymmanagementsystem.dao;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Users;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DataTest {
    Users users = UserService.users().get(0);


    DataTest() throws SQLException {
    }

    @Test
    void setDataTest() throws SQLException {
        ObservableList<Customers> customers = CustomerService.fetchAllCustomer(users);
        Data.setAllCustomersList(customers);

    }

    @Test
    void dataTest() {

        System.out.println(Data.getAllCustomersList().hashCode());

    }

    @Test
    void dataTest2() {
        System.out.println(Data.getAllCustomersList().hashCode());


    }
}