package com.example.gymmanagementsystem.data.dto;

import com.example.gymmanagementsystem.data.dto.main.CustomerService;
import com.example.gymmanagementsystem.data.entities.main.Customers;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class CustomerServiceTest {

    @Test
    void insertOrUpdateCustomer() throws SQLException {
        Customers customer = new Customers(1, "ee", "ww", "rr",
                "4303921", "male", "noon", "huj", null, 1,
                "Og");
        CustomerService.insertOrUpdateCustomer(customer, true);
    }
}