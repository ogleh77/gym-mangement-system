package com.example.gymmanagementsystem.dao;

import com.example.gymmanagementsystem.helpers.CustomException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

class PaymentServiceTest {

    @Test
    void fetchAllCustomersPayments() throws SQLException {

        System.out.println(PaymentService.fetchAllCustomersPayments("8919450"));
    }


    @Test
    void dateTest() throws CustomException {
        //  Payments payment = PaymentService.fetchAllCustomersPayments("8919450").get(3);
        LocalDate expDate = LocalDate.now();

        Period period = Period.between(LocalDate.now(), expDate);

        System.out.println(periodSimplify(period));

    }

    private String periodSimplify(Period period) {
        if (period.getYears() > 0) {
            return period.getYears() + "Yrs" + (period.getMonths() > 0 ? "/ " + period.getMonths() + "M " : "")
                    + (period.getDays() > 0 ? "/ " + period.getDays() + "days" : "");
        } else if (period.getMonths() > 0) {
            return period.getMonths() + " month" + (period.getDays() > 0 ? " and " + period.getDays() + " days" : "");
        } else if (period.getDays() > 0) {
            return period.getDays() == 1 ? "1 day" : period.getDays() + " days";
        }
        return "outdated";
    }

    @Test
    void insertPayment() {
    }

    @Test
    void updatePayment() {
    }

    @Test
    void holdPayment() {
    }

    @Test
    void unHoldPayment() {
    }

    @Test
    void fetchCustomersOnlinePayment() {
    }

    @Test
    void fetchCustomersOfflinePayment() {
    }

    @Test
    void fetchQualifiedOfflinePayment() throws SQLException {
        System.out.println(PaymentService.fetchQualifiedOfflinePaymentWhereDate("7916173",
                LocalDate.now().minusYears(3),
                LocalDate.now()));
    }
}