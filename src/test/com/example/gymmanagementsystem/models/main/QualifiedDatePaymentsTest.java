package com.example.gymmanagementsystem.models.main;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class QualifiedDatePaymentsTest {
    private final QualifiedDatePayments payments = new QualifiedDatePayments();

    @Test
    void fetchAllCustomersPayments() throws SQLException {
    }

    @Test
    void fetchOnlinePayment() throws SQLException {
        System.out.println(payments.fetchOnlinePayment("3791385"));
    }

    @Test
    void fetchOfflinePaymentWhereDate() throws SQLException {
        System.out.println(payments.fetchOnlinePaymentWhereDate("3791385",
                LocalDate.now().minusYears(3), LocalDate.now()));

        System.out.println(LocalDate.now().minusYears(3));
    }

    @Test
    void fetchOnlinePaymentWhereDate() {
    }

    @Test
    void fetchPendingPaymentWhereDate() {
    }

    @Test
    void testFetchPendingPaymentWhereDate()
    {
     }
}