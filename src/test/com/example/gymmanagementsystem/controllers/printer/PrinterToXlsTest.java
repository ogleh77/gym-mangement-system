package com.example.gymmanagementsystem.controllers.printer;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Users;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

class PrinterToXlsTest {
    private final Users user = UserService.users().get(0);
    private final ObservableList<Customers> customers = CustomerService.fetchAllCustomer(user);
    private final ObservableList<Customers> onlineCustomers = CustomerService.fetchAllOnlineCustomer(user);

    LocalDate frm = LocalDate.now().minusYears(2);

    LocalDate to = LocalDate.now().plusYears(2);

    private final ObservableList<Customers> pendingCustomers =
            CustomerService.fetchPendCustomersWhereDateBetween(user,frm,to);
    private final ObservableList<Customers> offlineCustomers =
            CustomerService.fetchOfflineCustomersWhereDateBetween(user,frm,
                    to);
    PrinterToXlsTest() throws SQLException {
    }


    @Test
    void exportOnlineCustomersToExcel() {
        PrinterToXls.exportOnlineCustomersToExcel(onlineCustomers, new File("online_customers.xlsx"));
    }

    @Test
    void exportAllCustomersToXls() {
        File file = new File("customers.xlsx");
        PrinterToXls.exportAllCustomersToXls(customers, file);
    }

    @Test
    void exportOfflineCustomersToExcel() {
        File file = new File("offline.xlsx");
        PrinterToXls.exportOfflineCustomersToExcel(offlineCustomers, file);
    }

    @Test
    void exportPendingCustomersToXls() {
        File file = new File("pending.xlsx");
        PrinterToXls.exportOfflineCustomersToExcel(pendingCustomers, file);
    }
}