package com.example.gymmanagementsystem.dao.main;

import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import com.example.gymmanagementsystem.helpers.CustomException;
import com.example.gymmanagementsystem.models.main.PaymentModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class PaymentService {

    private static final PaymentModel paymentModel = new PaymentModel();


    public static void insertPayment(Customers customer) throws SQLException {
        try {
            String customerGander = customer.getGander();
            paymentModel.insertPayment(customer.getPhone(), customerGander, customer.getPayments().get(0));
        } catch (SQLException e) {
            throw new SQLException("(From SQL insert payment) " + e.getMessage());
        }

    }

    public static void updatePayment(Payments payment) throws SQLException {
        try {
            paymentModel.update(payment);
        } catch (SQLException e) {
            throw new SQLException("(Form SQL update payment) " + e.getMessage());
        }

    }

    public static void deletePayment(Payments payment) throws SQLException {
        try {
            paymentModel.deletePayment(payment);
        } catch (SQLException e) {
            throw new SQLException("(From SQL delete payment) " + e.getMessage());
        }

    }

    public static void holdPayment(Payments payment, int allowedDays) throws SQLException {
        try {
            LocalDate exp = payment.getExpDate();
            LocalDate pendingDate = LocalDate.now();
            int daysRemind = Period.between(pendingDate, exp).getDays();
            if (Period.between(pendingDate, exp).getMonths() > 0) {
                daysRemind = 30;
            }
            if (daysRemind <= allowedDays) {
                throw new CustomException("Payment-kan lama xidhi karo wayoo wuxu ka hoseya" +
                        " mudada loo ogolyahy macmiilka inuu ku xidhan karo oo ah " + allowedDays + " malmood wixi ka badan");
            }
            paymentModel.holdPayment(payment, daysRemind);
        } catch (SQLException e) {
            throw new SQLException("(From SQL payment pending) " + e.getMessage());
        }
    }

    public static void unHoldPayment(Payments payment) throws SQLException {
        try {
            paymentModel.unHold(payment);
        } catch (SQLException e) {
            throw new SQLException("(From SQL un pending payment) " + e.getMessage());
        }
    }


    //----------------------------_Payment service list-----------------------


    public static ObservableList<Payments> fetchAllPayments(String customerPhone) throws SQLException {
        try {
            return paymentModel.fetchAllPayments(customerPhone);
        } catch (SQLException e) {
            throw new SQLException("SQL error fetching all payment " + e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchAllOnlinePayment(String customerPhone) throws SQLException {
        try {
            return paymentModel.fetchAllOnlinePayment(customerPhone);
        } catch (SQLException e) {
            throw new SQLException("SQL error fetching all online payment " + e.getMessage());
        }
    }


    public static ObservableList<Payments> fetchOnlinePaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {
        try {
            return paymentModel.fetchOnlinePaymentWhereDate(customerPhone, fromDate, toDate);
        } catch (SQLException e) {
            throw new SQLException("SQL error fetching online payments " + e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchOfflinePaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {

        try {
            return paymentModel.fetchOfflinePaymentWhereDate(customerPhone, fromDate, toDate);
        } catch (SQLException e) {
            throw new SQLException("SQL error fetching offline payment " + e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchPendingPaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {
        try {
            return paymentModel.fetchPendingPaymentWhereDate(customerPhone, fromDate, toDate);
        } catch (SQLException e) {
            throw new SQLException("SQL error fetching pending payment " + e.getMessage());
        }
    }


}
