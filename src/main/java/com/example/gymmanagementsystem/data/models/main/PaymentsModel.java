package com.example.gymmanagementsystem.data.models.main;

import com.example.gymmanagementsystem.data.dto.BoxService;
import com.example.gymmanagementsystem.data.entities.main.Payments;
import com.example.gymmanagementsystem.data.entities.service.Box;
import com.example.gymmanagementsystem.data.models.DailyReportModel;
import com.example.gymmanagementsystem.dependencies.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PaymentsModel {
    private static final Connection connection = DbConnection.getConnection();

    public void insertPayment(String customerPhone, String customerGender, Payments payment) throws SQLException {
        connection.setAutoCommit(false);
        String insertPaymentQuery = "INSERT INTO payments(exp_date, amount_paid, paid_by," + "discount,poxing,box_fk, customer_phone_fk,month) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(insertPaymentQuery)) {
            ps.setString(1, payment.getExpDate().toString());
            ps.setDouble(2, payment.getAmountPaid());
            ps.setString(3, payment.getPaidBy());
            ps.setDouble(4, payment.getDiscount());
            ps.setBoolean(5, payment.isPoxing());

            if (payment.getBox() != null) {
                ps.setInt(6, payment.getBox().getBoxId());
                BoxService.changeBoxState(payment.getBox());
            }
            ps.setString(7, customerPhone);
            ps.setString(8, payment.getMonth());
            ps.executeUpdate();
            makeReport(payment, customerGender);
            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void update(Payments payment) throws SQLException {
        connection.setAutoCommit(false);
        String query = "UPDATE payments SET start_date=?,exp_date=?,amount_paid=?,paid_by=?,\n" + "discount=?,poxing=?,box_fk=? WHERE payment_id=" + payment.getPaymentID();
        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, payment.getStartDate().toString());
            ps.setString(2, payment.getExpDate().toString());
            ps.setDouble(3, payment.getAmountPaid());
            ps.setString(4, payment.getPaidBy());
            ps.setDouble(5, payment.getDiscount());
            ps.setBoolean(6, payment.isPoxing());

            if (payment.getBox() != null) {
                ps.setInt(7, payment.getBox().getBoxId());
            }
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void deletePayment(Payments payment) throws SQLException {
        connection.setAutoCommit(false);
        String query = "DELETE FROM payments WHERE payment_id=" + payment.getPaymentID();
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            if (payment.getBox() != null) {
                System.out.println("yes payment had a boxand state is " + payment.getBox().isReady());
                BoxService.changeBoxState(payment.getBox());
            } else {
                System.out.println("no payment had a boxand state is ");
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void pendPayment(Payments payment, int daysRemain) throws SQLException {
        connection.setAutoCommit(false);

        String holdQuery = "INSERT INTO pending(days_remain,payment_fk)" + "VALUES (" + daysRemain + "," + payment.getPaymentID() + ")";

        try (Statement statement = connection.createStatement()) {
            String paymentQuery = "UPDATE payments SET is_online=false,pending=true WHERE payment_id=" + payment.getPaymentID();
            if (payment.getBox() != null) {
                paymentQuery = "UPDATE payments SET is_online=false,pending=true,box_fk=null WHERE payment_id=" + payment.getPaymentID();
                BoxService.changeBoxState(payment.getBox());
            }
            statement.addBatch(holdQuery);
            statement.addBatch(paymentQuery);
            statement.executeBatch();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void unPendPayment(Payments payment) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {

            int daysRemain = daysRemain(payment.getPaymentID());

            if (daysRemain != 0) {

                LocalDate remainedDays = LocalDate.now().plusDays(daysRemain);

                String deleteQuery = "DELETE FROM pending WHERE payment_fk=" + payment.getPaymentID();

                String unPendPayment = "UPDATE payments SET is_online=true, pending=false," + "exp_date='" + remainedDays + "' WHERE payment_id=" + payment.getPaymentID();

                statement.addBatch(deleteQuery);
                statement.addBatch(unPendPayment);
                statement.executeBatch();

                connection.commit();
                payment.setExpDate(LocalDate.now().plusDays(daysRemain));
            } else {
                throw new SQLException("Paymentkan lama xayarin fadlan iska hubi");
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
    //----------------------------------Payment Lists----------------------------

    public ObservableList<Payments> fetchAllPayments(String phone) throws SQLException {
        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + phone + " ORDER BY exp_date DESC ");

        return getPayments(payments, statement, rs);
    }

    public ObservableList<Payments> fetchAllOnlinePayment(String customerPhone) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk=" + customerPhone + "   AND is_online=true");

        return getPayments(payments, statement, rs);
    }

    public ObservableList<Payments> fetchOfflinePaymentWhereDate(String customerPhone, String from, String to) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk= '" + customerPhone + "' AND is_online=false AND pending=false " + "AND exp_date BETWEEN '" + from + "' AND '" + to + "' ORDER BY exp_date ASC;";

        ResultSet rs = statement.executeQuery(query);
        return PaymentsModel.getPayments(payments, statement, rs);

    }

    public ObservableList<Payments> fetchOnlinePaymentWhereDate(String customerPhone, String from, String to) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk= '" + customerPhone + "' AND is_online=true AND exp_date BETWEEN '" + from + "' AND '" + to + "';";

        ResultSet rs = statement.executeQuery(query);
        return PaymentsModel.getPayments(payments, statement, rs);

    }

    public ObservableList<Payments> fetchPendingPaymentWhereDate(String customerPhone, String from, String to) throws SQLException {

        ObservableList<Payments> payments = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM payments LEFT JOIN box b on payments.box_fk = b.box_id " + "WHERE customer_phone_fk= '" + customerPhone + "' AND pending=true AND exp_date BETWEEN '" + from + "' AND '" + to + "';";


        ResultSet rs = statement.executeQuery(query);
        return PaymentsModel.getPayments(payments, statement, rs);
    }

    public static void offPayment(Payments payment) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            String query = "UPDATE payments SET is_online=false WHERE payment_id=" + payment.getPaymentID();
            if (payment.getBox() != null) {
                BoxService.changeBoxState(payment.getBox());
            }
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

    }

    //------------------------Helpers-----------------------
    private static void makeReport(Payments payment, String customerGender) throws SQLException {
        Statement st = connection.createStatement();
        if (customerGender.equals("Male") && payment.getBox() != null) {
            DailyReportModel.dailyReportMaleWithBox(st);
        } else if (customerGender.equals("Female") && payment.getBox() != null) {
            DailyReportModel.dailyReportFemaleWithBox(st);
        } else if (payment.getBox() == null && customerGender.equals("Male")) {
            DailyReportModel.dailyReportMaleWithOutBox(st);
        } else if (payment.getBox() == null && customerGender.equals("Female")) {
            DailyReportModel.dailyReportFemaleWithOutBox(st);
        }
        st.close();
    }

    private int daysRemain(int paymentID) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM pending WHERE payment_fk=" + paymentID);
        if (rs.next()) {
            return rs.getInt("days_remain");
        }
        statement.close();
        rs.close();
        return 0;
    }

    public static ObservableList<Payments> getPayments(ObservableList<Payments> payments, Statement statement, ResultSet rs) throws SQLException {
        Payments payment;
        while (rs.next()) {

            Box box = null;
            if (rs.getString("box_fk") != null) {
                box = new Box(rs.getInt("box_id"), rs.getString("box_name"), rs.getBoolean("is_ready"));
            }
            payment = getPayments(rs);
            payment.setBox(box);
            payments.add(payment);

        }
        statement.close();
        rs.close();
        return payments;
    }

    public static Payments getPayments(ResultSet rs) throws SQLException {
        return new Payments(rs.getInt("payment_id"), LocalDate.parse(rs.getString("start_date")), LocalDate.parse(rs.getString("exp_date")), rs.getString("month"), rs.getString("year"), rs.getDouble("amount_paid"), rs.getString("paid_by"), rs.getDouble("discount"), rs.getBoolean("poxing"), rs.getString("customer_phone_fk"), rs.getBoolean("is_online"), rs.getBoolean("pending"));
    }
}
