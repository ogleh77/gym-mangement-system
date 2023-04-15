package com.example.gymmanagementsystem.dao;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Users;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Data {
    private static ObservableList<Customers> allCustomersList;
    private static ObservableList<Customers> onlineCustomersByDate;
    private static ObservableList<Customers> offlineCustomersByDate;
    private static ObservableList<Customers> pendCustomersByDate;
    private static ObservableList<Customers> onlineCustomers;


//    private static  Users activeUser;
//
//
//    public static void setActiveUser(Users activeUser) {
//        Data.activeUser = activeUser;
//    }
//
//    public  Users getActiveUser() {
//        return activeUser;
//    }

    public static ObservableList<Customers> getAllCustomersList() {
        return allCustomersList;
    }

    public static void setAllCustomersList(ObservableList<Customers> allCustomersList) {
        Data.allCustomersList = allCustomersList;
    }

    public static ObservableList<Customers> getOnlineCustomersByDate() {
        return onlineCustomersByDate;
    }

    public static void setOnlineCustomersByDate(ObservableList<Customers> onlineCustomersByDate) {
        Data.onlineCustomersByDate = onlineCustomersByDate;
    }

    public static ObservableList<Customers> getOfflineCustomersByDate() {
        return offlineCustomersByDate;
    }

    public static void setOfflineCustomersByDate(ObservableList<Customers> offlineCustomersByDate) {
        Data.offlineCustomersByDate = offlineCustomersByDate;
    }

    public static ObservableList<Customers> getPendCustomersByDate() {
        return pendCustomersByDate;
    }

    public static void setPendCustomersByDate(ObservableList<Customers> pendCustomersByDate) {
        Data.pendCustomersByDate = pendCustomersByDate;
    }

    public static ObservableList<Customers> getOnlineCustomers() {
        return onlineCustomers;
    }

    public static void setOnlineCustomers(ObservableList<Customers> onlineCustomers) {
        Data.onlineCustomers = onlineCustomers;
    }
}
