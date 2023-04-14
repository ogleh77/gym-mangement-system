package com.example.gymmanagementsystem.models.main;

import com.example.gymmanagementsystem.entities.Payments;
import com.example.gymmanagementsystem.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class QualifiedDatePayments {

    private static final Connection connection = DbConnection.getConnection();


    public ObservableList<Payments> fetchAllCustomersPayments(String phone) throws SQLException {
        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + phone + " ORDER BY exp_date DESC ");

        return PaymentModel.getPayments(payments, statement, rs);
    }

    public ObservableList<Payments> fetchOnlinePayment(String customerPhone) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " +
                "WHERE customer_phone_fk=" + customerPhone + " AND is_online=true");

        return PaymentModel.getPayments(payments, statement, rs);
    }

    public ObservableList<Payments> fetchOfflinePaymentWhereDate(String customerPhone, LocalDate from, LocalDate to) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " +
                "WHERE customer_phone_fk= '" + customerPhone + "' AND pending=false " +
                "AND is_online=false AND exp_date BETWEEN '" + from + "' AND '" + to + "';";


        ResultSet rs = statement.executeQuery(query);
        return PaymentModel.getPayments(payments, statement, rs);

    }

    public ObservableList<Payments> fetchOnlinePaymentWhereDate(String customerPhone, LocalDate from, LocalDate to) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " +
                "WHERE customer_phone_fk= '" + customerPhone + "' AND is_online=true AND exp_date BETWEEN '" + from + "' AND '" + to + "';";


        ResultSet rs = statement.executeQuery(query);
        return PaymentModel.getPayments(payments, statement, rs);

    }

    public ObservableList<Payments> fetchPendingPaymentWhereDate(String customerPhone, LocalDate from, LocalDate to) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " +
                "WHERE customer_phone_fk= '" + customerPhone + "' AND pending=true AND exp_date BETWEEN '" + from + "' AND '" + to + "';";


        ResultSet rs = statement.executeQuery(query);
        return PaymentModel.getPayments(payments, statement, rs);

    }
}
