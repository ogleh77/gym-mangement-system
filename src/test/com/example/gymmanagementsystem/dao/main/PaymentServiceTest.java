package com.example.gymmanagementsystem.dao.main;

import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class PaymentServiceTest {

    @Test
    void deletePayment() throws SQLException {
        //Payments payment = PaymentService.fetchAllPayments("8435290").get(0);
        Customers customer = CustomerService.fetchAllCustomer(UserService.users().get(0)).get(0);
      //  System.out.println(customer);
         PaymentService.insertPayment(customer);

    }
}