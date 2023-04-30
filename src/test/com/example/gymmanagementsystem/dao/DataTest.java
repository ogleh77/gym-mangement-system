package com.example.gymmanagementsystem.dao;

import com.example.gymmanagementsystem.dao.service.BackupService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.service.Users;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DataTest {
    Users users = UserService.users().get(0);


    DataTest() throws SQLException {
    }

    @Test
    void setDataTest() throws SQLException {
        //boolean da = BackupService.fullBackupPaths().get(0).matches("^\\D{4}");

        // System.out.println(da ? "found" : "not found");
        //String di = BackupService.fullBackupPaths().get(0);
//        System.out.println(Arrays.toString(da));
//        System.out.println(di);

       // System.out.println(di.lastIndexOf(" "));

    }

    @Test
    void dataTest() {

        System.out.println(Data.getAllCustomersList().hashCode());

    }

    @Test
    void dataTest2() throws SQLException {
        //System.out.println(BackupService.backupPaths());

    }
}