package com.example.gymmanagementsystem.dao;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void fetchAllCustomersPayments() throws SQLException {

        System.out.println(PaymentService.fetchAllCustomersPayments("8919450"));
    }
}