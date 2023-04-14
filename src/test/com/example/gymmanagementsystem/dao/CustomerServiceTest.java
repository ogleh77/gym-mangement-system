package com.example.gymmanagementsystem.dao;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class CustomerServiceTest {

    @Test
    void fetchQualifiedOfflineCustomersWhere() throws SQLException {
        System.out.println(CustomerService.fetchQualifiedOfflineCustomersWhere("SELECT * FROM customers ",
                LocalDate.now().minusMonths(8), LocalDate.now()).size());
    }
}