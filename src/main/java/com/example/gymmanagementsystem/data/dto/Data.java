package com.example.gymmanagementsystem.data.dto;

import com.example.gymmanagementsystem.data.entities.main.Customers;
import javafx.collections.ObservableList;

public class Data {
    private static ObservableList<Customers> allCustomersList;
    public static ObservableList<Customers> getAllCustomersList() {
        return allCustomersList;
    }
    public static void setAllCustomersList(ObservableList<Customers> allCustomersList) {
        Data.allCustomersList = allCustomersList;
    }
}
