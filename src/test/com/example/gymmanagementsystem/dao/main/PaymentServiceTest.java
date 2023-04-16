package com.example.gymmanagementsystem.dao.main;

import com.example.gymmanagementsystem.entities.main.Payments;
import com.example.gymmanagementsystem.helpers.CustomException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void deletePayment() throws SQLException {
        Payments payment=PaymentService.fetchAllPayments("8435290").get(0);

        PaymentService.deletePayment(payment);

    }
}