package com.example.gymmanagementsystem.models.main;

import com.example.gymmanagementsystem.dao.BoxService;
import com.example.gymmanagementsystem.entities.Box;
import com.example.gymmanagementsystem.entities.Payments;
import com.example.gymmanagementsystem.helpers.DbConnection;
import com.example.gymmanagementsystem.helpers.Repo;
import com.example.gymmanagementsystem.models.DailyReportModel;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PaymentModel implements Repo<Payments> {
    private static final Connection connection = DbConnection.getConnection();

    public void insertPayment(String customerPhone, String customerGender, Payments payment) throws SQLException {
        connection.setAutoCommit(false);

        try {
            String insertPaymentQuery = "INSERT INTO payments(exp_date, amount_paid, paid_by," + "discount,poxing,box_fk, customer_phone_fk,month) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(insertPaymentQuery);
            ps.setString(1, payment.getExpDate().toString());
            ps.setDouble(2, payment.getAmountPaid());
            ps.setString(3, payment.getPaidBy());
            ps.setDouble(4, payment.getDiscount());
            ps.setBoolean(5, payment.isPoxing());

            if (payment.getBox() == null) {
                ps.setString(6, null);
            } else {
                ps.setInt(6, payment.getBox().getBoxId());
                BoxService.updateBox(payment.getBox());
            }

            ps.setString(7, customerPhone);
            ps.setString(8, payment.getMonth());
            ps.executeUpdate();
            makeReport(payment, customerGender);
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
    @Override
    public void update(Payments payment) throws SQLException {
        String query = "UPDATE payments SET start_date=?,exp_date=?,amount_paid=?,paid_by=?,\n" +
                "discount=?,poxing=?,box_fk=? WHERE payment_id=" + payment.getPaymentID();

        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, payment.getStartDate().toString());
        ps.setString(2, payment.getExpDate().toString());

        ps.setDouble(3, payment.getAmountPaid());
        ps.setString(4, payment.getPaidBy());
        ps.setDouble(5, payment.getDiscount());
        ps.setBoolean(6, payment.isPoxing());
        if (payment.getBox() == null) {
            ps.setString(7, null);
        } else {
            ps.setInt(7, payment.getBox().getBoxId());
            BoxService.updateBox(payment.getBox());
        }
        ps.executeUpdate();
        ps.close();
    }


    //---------------------helper methods---------------

    private ObservableList<Payments> getPayments(ObservableList<Payments> payments, Statement statement, ResultSet rs) throws SQLException {
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
    private Payments getPayments(ResultSet rs) throws SQLException {
        return new Payments(rs.getInt("payment_id"), LocalDate.parse(rs.getString("payment_date")), LocalDate.parse(rs.getString("exp_date")), rs.getString("month"), rs.getString("year"), rs.getDouble("amount_paid"), rs.getString("paid_by"), rs.getDouble("discount"), rs.getBoolean("poxing"), rs.getString("customer_phone_fk"), rs.getBoolean("is_online"), rs.getBoolean("pending"));
    }

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

}
