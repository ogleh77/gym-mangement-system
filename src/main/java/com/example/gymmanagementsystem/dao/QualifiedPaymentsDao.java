package com.example.gymmanagementsystem.dao;

import com.example.gymmanagementsystem.entities.Payments;
import com.example.gymmanagementsystem.models.main.QualifiedDatePayments;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDate;

public class QualifiedPaymentsDao {

    private static final QualifiedDatePayments qualifiedDatePayments = new QualifiedDatePayments();

    public static ObservableList<Payments> fetchAllPayments(String customerPhone) throws SQLException {
        return qualifiedDatePayments.fetchAllCustomersPayments(customerPhone);
    }

    public static ObservableList<Payments> fetchOnlinePayments(String customerPhone) throws SQLException {
        return qualifiedDatePayments.fetchOnlinePayment(customerPhone);
    }


    public static ObservableList<Payments> fetchAllOnlinePaymentsWhereIsBetween(String customerPhone, LocalDate fomDate, LocalDate toDate) throws SQLException {
        return qualifiedDatePayments.fetchOnlinePaymentWhereDate(customerPhone, fomDate, toDate);
    }

    public static ObservableList<Payments> fetchAllOfflinePaymentsWhereIsBetween(String customerPhone, LocalDate fomDate, LocalDate toDate) throws SQLException {
        return qualifiedDatePayments.fetchOfflinePaymentWhereDate(customerPhone, fomDate, toDate);
    }

    public static ObservableList<Payments> fetchAllPendPaymentsWhereIsBetween(String customerPhone, LocalDate fomDate, LocalDate toDate) throws SQLException {
        return qualifiedDatePayments.fetchPendingPaymentWhereDate(customerPhone, fomDate, toDate);
    }
}
