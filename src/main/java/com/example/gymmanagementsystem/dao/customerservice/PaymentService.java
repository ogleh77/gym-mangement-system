package com.example.gymmanagementsystem.dao.customerservice;

import com.example.gymmanagementsystem.entities.Customers;
import com.example.gymmanagementsystem.entities.Payments;
import com.example.gymmanagementsystem.helpers.CustomException;
import com.example.gymmanagementsystem.models.paymentmodel.PaymentModel;
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
            throw new CustomException("Khalad aya ka dhacay halkan " + e.getMessage() + " " +
                    "fadlan isku day mar danbe hadaad fahamtey nooca khaladka");
        }

    }

    public static void updatePayment(Payments payment) throws SQLException {
        try {
            paymentModel.update(payment);
        } catch (SQLException e) {
            throw new CustomException("Khalad aya ka dhacay halkan " + e.getMessage() + " " +
                    "fadlan isku day mar danbe hadaad fahamtey nooca khaladka");
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
                throw new CustomException("Fadlan lama xidhi karo payment kan waayo\n" + "wuxu ka hoseya wakhtiga loo asteyey oo ah " + allowedDays + " cisho " +
                        "Halka payment kana u hadhay " + daysRemind + " Malmood");
            }
            paymentModel.holdPayment(payment, daysRemind);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    public static void unHoldPayment(Payments payment) throws SQLException {
        try {
            paymentModel.unHold(payment);
        } catch (SQLException e) {
            throw new CustomException(e.getMessage());
        }
    }


    //----------------------------_Payment service list-----------------------


    public static ObservableList<Payments> fetchAllPayments(String customerPhone) throws CustomException {
        try {
            return paymentModel.fetchAllPayments(customerPhone);
        } catch (SQLException e) {
            throw new CustomException("Khalad aya dhacay marka lasoo akhrinayay macmilkan payments-kiisa \n" +
                    e.getMessage());
        }

    }

    public static ObservableList<Payments> fetchAllOnlinePayment(String customerPhone) throws SQLException {
        return paymentModel.fetchAllOnlinePayment(customerPhone);
    }


    public static ObservableList<Payments> fetchOnlinePaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {
        return paymentModel.fetchOnlinePaymentWhereDate(customerPhone, fromDate, toDate);
    }

    public static ObservableList<Payments> fetchOfflinePaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {
        return paymentModel.fetchOfflinePaymentWhereDate(customerPhone, fromDate, toDate);
    }

    public static ObservableList<Payments> fetchPendingPaymentBetween(String customerPhone, LocalDate fromDate
            , LocalDate toDate) throws SQLException {
        return paymentModel.fetchPendingPaymentWhereDate(customerPhone, fromDate, toDate);
    }


}
