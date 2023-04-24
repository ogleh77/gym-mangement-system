package com.example.gymmanagementsystem.dao.main;

import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class CustomerServiceTest {

    @Test
    void deleteCustomer() throws SQLException {

        Customers customer = CustomerService.fetchAllCustomer(UserService.users().get(0)).get(0);
        System.out.println(customer);
        CustomerService.deleteCustomer(customer);
    }

    @Test
    void insertOrUpdateCustomer() {
    }

    @Test
    void predictNextId() {
    }

    @Test
    void fetchAllCustomer() {
    }

    @Test
    void fetchAllOnlineCustomer() {
    }

    @Test
    void fetchOnlineCustomersWhereDateBetween() {
        //System.out.println(CustomerService.fetchOnlineCustomersWhereDateBetween(""));
    }

    @Test
    void fetchOfflineCustomersWhereDateBetween() {
    }

    @Test
    void fetchPendCustomersWhereDateBetween() {
    }

    @Test
    void fetchQualifiedOfflineCustomers() {
    }
}