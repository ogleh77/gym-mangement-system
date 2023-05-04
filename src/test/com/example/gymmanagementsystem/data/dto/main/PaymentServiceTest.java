package com.example.gymmanagementsystem.data.dto.main;

import com.example.gymmanagementsystem.data.dto.UserService;
import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.main.Payments;
import com.example.gymmanagementsystem.data.entities.service.Box;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class PaymentServiceTest {
    Payments payment = new Payments(15, LocalDate.now(), LocalDate.now().plusDays(30), LocalDate.now().getMonth().toString(),
            null, 11, "Edahab", 2, true, "4303921", true, false);
    Box box = new Box(8, "tata", false);

    @Test
    void insertPayment() throws SQLException {
        Customers customer = CustomerService.fetchAllCustomer(UserService.fetchAllUsers().get(0)).get(0);
        customer.getPayments().add(payment);

        payment.setBox(box);
        PaymentService.insertPayment(customer);
    }

    @Test
    void updatePayment() throws SQLException {
        payment.setBox(box);
        PaymentService.updatePayment(payment);
    }

    @Test
    void deletePayment() throws SQLException {
        payment.setBox(box);
        PaymentService.deletePayment(payment);
    }


}