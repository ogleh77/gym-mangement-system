package com.example.gymmanagementsystem.exporttoxls;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Users;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

class ExportDataToExcelTest {
    private final Users user = UserService.users().get(0);
    private final ObservableList<Customers> customers = CustomerService.fetchAllCustomer(user);
    private final ObservableList<Customers> onlineCustomers = CustomerService.fetchAllOnlineCustomer(user);

    LocalDate frm = LocalDate.now().minusYears(2);

    LocalDate to = LocalDate.now().plusYears(2);

    private final ObservableList<Customers> offlineCustomers =
            CustomerService.fetchOfflineCustomersWhereDateBetween(user, frm, to);
    private final ObservableList<Customers> pendingCustomers
            = CustomerService.fetchPendCustomersWhereDateBetween(user, frm, to);

    ExportDataToExcelTest() throws SQLException {

    }

    @Test
    void exportAllCustomersToXls() {
        ExportDataToExcel.exportAllCustomersToXls(customers, new File("customers.xlsx"));
    }

    @Test
    void exportOnlineCustomersToExcel() {
        ExportDataToExcel.exportOnlineCustomersToExcel(onlineCustomers, new File("online_customers.xlsx"));

    }

    @Test
    void exportOfflineCustomersToExcel() {
        ExportDataToExcel.exportOfflineCustomersToExcel(offlineCustomers, new File("offline_customers.xlsx"));

    }

    @Test
    void exportPendingCustomersToXls() {
        ExportDataToExcel.exportPendingCustomersToExcel(pendingCustomers, new File("pending_customers.xlsx"));

    }
}
