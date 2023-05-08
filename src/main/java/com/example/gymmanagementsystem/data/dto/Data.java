package com.example.gymmanagementsystem.data.dto;

import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.service.Users;
import javafx.collections.ObservableList;

public class Data {
    private static ObservableList<Customers> allCustomersList;
    private static ObservableList<Users> allUsers;

    public static ObservableList<Customers> getAllCustomersList() {
        return allCustomersList;
    }

    public static ObservableList<Users> getAllUsers() {
        return allUsers;
    }

    public static void setAllCustomersList(ObservableList<Customers> allCustomersList) {
        Data.allCustomersList = allCustomersList;
    }

    public static void setAllUsers(ObservableList<Users> allUsers) {
        Data.allUsers = allUsers;
    }
}
