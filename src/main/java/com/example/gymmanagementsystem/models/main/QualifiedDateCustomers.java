package com.example.gymmanagementsystem.models.main;

import com.example.gymmanagementsystem.dao.QualifiedPaymentsDao;
import com.example.gymmanagementsystem.entities.Customers;
import com.example.gymmanagementsystem.entities.Payments;
import com.example.gymmanagementsystem.entities.Users;
import com.example.gymmanagementsystem.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static com.example.gymmanagementsystem.models.main.CustomerModel.fetchByRoleAndGander;
import static com.example.gymmanagementsystem.models.main.CustomerModel.getCustomers;

public class QualifiedDateCustomers {
    private static final Connection connection = DbConnection.getConnection();

    public static ObservableList<Customers> fetchOnlineCustomers(Users activeUser) throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments =
                    QualifiedPaymentsDao.fetchOnlinePayments(customerPhone);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }


        return customers;
    }

    public static ObservableList<Customers> fetchOnlineCustomersWhereDate(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments =
                    QualifiedPaymentsDao.fetchAllOnlinePaymentsWhereIsBetween(customerPhone, fromDate, toDate);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }


        return customers;
    }

    public static ObservableList<Customers> fetchOfflineCustomersWhereDate(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments =
                    QualifiedPaymentsDao.fetchAllOfflinePaymentsWhereIsBetween(customerPhone, fromDate, toDate);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }


        return customers;
    }

    public static ObservableList<Customers> fetchPendCustomersWhereDate(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments =
                    QualifiedPaymentsDao.fetchAllPendPaymentsWhereIsBetween(customerPhone, fromDate, toDate);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }


        return customers;
    }
}
