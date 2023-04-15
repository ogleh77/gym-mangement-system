package com.example.gymmanagementsystem.xlsfiles;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Users;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

class PrintToExcelTest {
    private final Users user = UserService.users().get(0);
    private final ObservableList<Customers> customers = CustomerService.fetchAllCustomer(user);
    private final ObservableList<Customers> onlineCustomers = CustomerService.fetchAllOnlineCustomer(user);
    LocalDate frm = LocalDate.now().minusYears(2);

    LocalDate to = LocalDate.now().plusYears(2);

    private final ObservableList<Customers> offlineCustomers =
            CustomerService.fetchOfflineCustomersWhereDateBetween(user,frm,
                    to);

    PrintToExcelTest() throws SQLException {
    }

    @Test
    void exportCustomersToExcel() {
        File file = new File("customers.xls");
        try {
            PrintToExcel.exportCustomersToExcel(customers, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void exportOnlineCustomersToExcel() {
        File file = new File("onlineCustomers.xls");
        try {
            PrintOnlineCustomersXls.exportOnlineCustomersToExcel(onlineCustomers, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void exportOfflineCustomersToExcel() {
        File file = new File("fy.xls");
        try {
            PrintOfflineCustomersXls.exportOnlineCustomersToExcel(offlineCustomers, file);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(offlineCustomers.get(1).getPayments());
        System.out.println(frm+"    "+to);
    }


}