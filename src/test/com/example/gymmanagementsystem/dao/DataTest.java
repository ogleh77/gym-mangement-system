package com.example.gymmanagementsystem.dao;

import com.example.gymmanagementsystem.dao.main.CustomerService;
import com.example.gymmanagementsystem.dao.service.BackupService;
import com.example.gymmanagementsystem.dao.service.GymService;
import com.example.gymmanagementsystem.dao.service.UserService;
import com.example.gymmanagementsystem.entities.main.Customers;
import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.models.service.BackupModel;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DataTest {
    Users users = UserService.users().get(0);


    DataTest() throws SQLException {
    }

    @Test
    void setDataTest() throws SQLException {
//        Gym g1= new Gym(2,"add",222,33,2,23,true);
//        Gym g2= GymService.getGym();
//       // Gym g1= GymService.getGym();
////        System.out.println(g2.getGymName());
//  g2.setGymName("tataaa");
//
//        System.out.println(g1.getGymName());
//        System.out.println(g2.getGymName());
//        System.out.println(g1.equals(g2));

        //BackupModel model=new BackupModel();

        BackupService.backup("/Users/mrd/Desktop/data");
    }

    @Test
    void dataTest() {

        System.out.println(Data.getAllCustomersList().hashCode());

    }

    @Test
    void dataTest2() throws SQLException {
        System.out.println(BackupService.backupPaths());

    }
}