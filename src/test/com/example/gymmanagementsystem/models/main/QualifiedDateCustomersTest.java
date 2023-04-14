package com.example.gymmanagementsystem.models.main;

import com.example.gymmanagementsystem.dao.UserService;
import com.example.gymmanagementsystem.entities.Users;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

class QualifiedDateCustomersTest {
    Users users = UserService.users().get(0);

    QualifiedDateCustomersTest() throws SQLException {
    }

    @Test
    void fetchOnlineCustomersWhereDate() throws SQLException {
        Users users = UserService.users().get(0);

        LocalDate start = LocalDate.of(2022, 12, 14);
        LocalDate end = LocalDate.of(2023, 1, 22);
        System.out.println(start);
        System.out.println(end);
        System.out.println(QualifiedDateCustomers.fetchOnlineCustomersWhereDate(users, start, end));
    }

    @Test
    void fetchOfflineCustomersWhereDate() {
    }


    @Test
    void fetchPendCustomersWhereDate() throws SQLException {
        LocalDate start = LocalDate.of(2022, 12, 14);
        LocalDate end = LocalDate.of(2023, 7, 5);
        System.out.println(start);
        System.out.println(end);
        System.out.println(QualifiedDateCustomers.fetchPendCustomersWhereDate(users, start, end));
    }
}