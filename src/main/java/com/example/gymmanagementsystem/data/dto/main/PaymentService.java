package com.example.gymmanagementsystem.data.dto.main;

import com.example.gymmanagementsystem.data.entities.main.Customers;
import com.example.gymmanagementsystem.data.entities.main.Payments;
import com.example.gymmanagementsystem.data.models.main.PaymentsModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class PaymentService {
    private static final PaymentsModel paymentsModel = new PaymentsModel();

    public static void insertPayment(Customers customer) throws SQLException {
        try {
            String customerGander = customer.getGander();
            paymentsModel.insertPayment(customer.getPhone(), customerGander, customer.getPayments().get(0));
        } catch (SQLException e) {
            throw new SQLException("(from sqlite) " + e.getMessage());
        }

    }

    public static void updatePayment(Payments payment) throws SQLException {
        try {
            paymentsModel.update(payment);
        } catch (SQLException e) {
            throw new SQLException("(from sqlite) " + e.getMessage());
        }

    }

    public static void deletePayment(Payments payment) throws SQLException {
        try {
            paymentsModel.deletePayment(payment);
        } catch (SQLException e) {
            throw new SQLException("(from sqlite) " + e.getMessage());
        }

    }

    public static void pendPayment(Payments payment, int allowedDays) throws SQLException {
        try {
            LocalDate exp = payment.getExpDate();
            LocalDate pendingDate = LocalDate.now();
            int daysRemind = Period.between(pendingDate, exp).getDays();
            if (Period.between(pendingDate, exp).getMonths() > 0) {
                daysRemind = 30;
            }
            if (daysRemind <= allowedDays) {
                throw new SQLException("Payment-kan lama xidhi karo wayoo wuxu ka hoseya" +
                        " mudada loo ogolyahy macmiilka inuu ku xidhan karo oo ah " + allowedDays + " malmood wixi ka badan");
            }
            paymentsModel.pendPayment(payment, daysRemind);
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }
    }

    public static void unHoldPayment(Payments payment) throws SQLException {
        try {
            paymentsModel.unPendPayment(payment);
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchAllPayments(String customerPhone) throws SQLException {
        try {
            return paymentsModel.fetchAllPayments(customerPhone);
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchAllOnlinePayment(String customerPhone) throws SQLException {
        try {
            return paymentsModel.fetchAllOnlinePayment(customerPhone);
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchOnlinePaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {
        try {
            return paymentsModel.fetchOnlinePaymentWhereDate(customerPhone, fromDate.toString(), toDate.toString());
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchOfflinePaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {

        try {
            return paymentsModel.fetchOfflinePaymentWhereDate(customerPhone, fromDate.toString(), toDate.toString());
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }
    }

    public static ObservableList<Payments> fetchPendingPaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {
        try {
            return paymentsModel.fetchPendingPaymentWhereDate(customerPhone, fromDate.toString(), toDate.toString());
        } catch (SQLException e) {
            throw new SQLException("from sqlite " + e.getMessage());
        }
    }


}
