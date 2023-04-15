package com.example.gymmanagementsystem.models.main;

import com.example.gymmanagementsystem.dao.main.PaymentService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.main.Payments;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class CustomerModel {
    private static final Connection connection = DbConnection.getConnection();

    public void insert(Customers customer) throws SQLException {
        String insertQuery = "INSERT INTO customers(FIRST_NAME, MIDDLE_NAME, LAST_NAME, PHONE, GANDER, SHIFT, ADDRESS, IMAGE, WEIGHT, WHO_ADDED, WAIST, HIPS, CHEST, FORE_ARM)\n" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
        insertOrUpdateStatement(customer, insertQuery, true);
    }

    public void update(Customers customer) throws SQLException {
        String updateQuery = "UPDATE customers SET first_name=?,middle_name=?,last_name=?,phone=?,gander=?,shift=?, " + "address=?,image=?,weight=?,waist=?,hips=?,chest=?,fore_arm=? WHERE customer_id=" + customer.getCustomerId();
        insertOrUpdateStatement(customer, updateQuery, false);
    }

    public void delete(Customers customer) throws SQLException {
        connection.setAutoCommit(false);

        try {
            String deleteCustomerQuery = "DELETE FROM customers WHERE phone=" + customer.getPhone();
            String deletePaymentsQuery = "DELETE FROM payments WHERE customer_phone_fk=" + customer.getPhone();

            Statement statement = connection.createStatement();

            statement.addBatch(deleteCustomerQuery);
            if (customer.getPayments() != null) statement.addBatch(deletePaymentsQuery);

            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
    }

    public int nextID() throws SQLException {
        String query = "SELECT * FROM SQLITE_SEQUENCE WHERE name='customers'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        if (rs.next()) {
            return rs.getInt("seq");
        }

        return 0;
    }


    //--------------------------List of customers----------------------------––


    public ObservableList<Customers> fetchAllCustomers(Users activeUser) throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            getCustomers(customers, rs, null);
        }

        rs.close();
        statement.close();
        return customers;
    }

    public ObservableList<Customers> fetchOnlineCustomers(Users activeUser) throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments =
                    PaymentService.fetchAllOnlinePayment(customerPhone);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }


        return customers;
    }

    public ObservableList<Customers> fetchOnlineCustomersWhereDate(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments =
                    PaymentService.fetchOnlinePaymentBetween(customerPhone, fromDate, toDate);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }


        return customers;
    }

    public ObservableList<Customers> fetchOfflineCustomersWhereDate(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException, SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments =
                    PaymentService.fetchOfflinePaymentBetween(customerPhone, fromDate, toDate);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }


        return customers;
    }

    public ObservableList<Customers> fetchPendCustomersWhereDate(Users activeUser, LocalDate fromDate, LocalDate toDate) throws SQLException {
        ObservableList<Customers> customers = FXCollections.observableArrayList();
        String fetchCustomers = fetchByRoleAndGander(activeUser.getGender(), activeUser.getRole());

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(fetchCustomers);

        while (rs.next()) {
            String customerPhone = rs.getString("phone");
            ObservableList<Payments> payments =
                    PaymentService.fetchPendingPaymentBetween(customerPhone, fromDate, toDate);

            if (payments == null || payments.isEmpty()) {
                continue;
            }
            getCustomers(customers, rs, payments);
        }


        return customers;
    }


    //-------------------------------Helpers------------------------------------
    private static void getCustomers(ObservableList<Customers> customers, ResultSet rs, ObservableList<Payments> payment) throws SQLException {
        Customers customer = new Customers(rs.getInt("customer_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("middle_name"), rs.getString("phone"), rs.getString("gander"), rs.getString("shift"), rs.getString("address"), rs.getBytes("image"), rs.getDouble("weight"), rs.getString("who_added"));
        customer.setChest(rs.getDouble("chest"));
        customer.setForeArm(rs.getDouble("fore_arm"));
        customer.setWaist(rs.getDouble("waist"));
        customer.setHips(rs.getDouble("hips"));

        if (payment != null) {
            customer.setPayments(payment);
        }
        customers.add(customer);
    }

    private void insertOrUpdateStatement(Customers customer, String query, boolean insert) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, customer.getFirstName());
        ps.setString(2, customer.getMiddleName());
        ps.setString(3, customer.getLastName());
        ps.setString(4, customer.getPhone());
        ps.setString(5, customer.getGander());
        ps.setString(6, customer.getShift());
        ps.setString(7, customer.getAddress());
        ps.setBytes(8, customer.getImage());
        ps.setDouble(9, customer.getWeight());
        if (insert) {
            ps.setString(10, customer.getWhoAdded());
            ps.setDouble(11, customer.getWaist());
            ps.setDouble(12, customer.getHips());
            ps.setDouble(13, customer.getChest());
            ps.setDouble(14, customer.getForeArm());
        } else {
            ps.setDouble(10, customer.getWaist());
            ps.setDouble(11, customer.getHips());
            ps.setDouble(12, customer.getChest());
            ps.setDouble(13, customer.getForeArm());
        }


        ps.executeUpdate();
        ps.close();
    }

    private String fetchByRoleAndGander(String gander, String role) {
        String fetchQuery = "SELECT * FROM customers WHERE gander='" + gander + "' ORDER BY customer_id ";
        if (role.equals("super_admin")) {
            fetchQuery = "SELECT * FROM customers ORDER BY customer_id ";
        }
        return fetchQuery;
    }

}
