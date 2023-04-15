package com.example.gymmanagementsystem.dao.customerservice;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.service.Users;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class CustomerServiceTest {
    private final Users user = UserService.users().get(1);

    CustomerServiceTest() throws SQLException {
    }

    @Test
    void fetchAllCustomer() throws SQLException {
        System.out.println(CustomerService.fetchAllCustomer(user));
    }

    @Test
    void fetchAllOnlineCustomer() throws SQLException {
        System.out.println(CustomerService.fetchAllOnlineCustomer(user).size());
    }

    @Test
    void fetchOnlineCustomersWhereDateBetween() throws SQLException {
        System.out.println(CustomerService.fetchOnlineCustomersWhereDateBetween(user,
                LocalDate.of(2022, 12, 10),
                LocalDate.of(2023, 7, 5)));
    }

    @Test
    void fetchOfflineCustomersWhereDateBetween() throws SQLException {
        System.out.println(CustomerService.fetchOfflineCustomersWhereDateBetween(user,
                LocalDate.of(2022, 12, 10),
                LocalDate.of(2023, 7, 5)));
    }


    @Test
    void fetchPendCustomersWhereDateBetween() throws SQLException {
        System.out.println(CustomerService.fetchPendCustomersWhereDateBetween(user,
                LocalDate.of(2022, 12, 10),
                LocalDate.of(2023, 7, 5)));
    }
}