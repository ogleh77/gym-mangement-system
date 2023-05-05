package com.example.gymmanagementsystem.data.dto;

import com.example.gymmanagementsystem.data.entities.service.Box;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class BoxServiceTest {

    @Test
    void insertBox() throws SQLException {
        Box box = new Box(2, "Khanada Shanaad", true);
        BoxService.insertBox(box);
    }

    @Test
    void changeBoxState() throws SQLException {
        Box box = new Box(7, "Khanada kobaaad", false);
        BoxService.changeBoxState(box);
    }

    @Test
    void fetchBoxes() throws SQLException {
        System.out.println(BoxService.fetchBoxes());
    }


    @Test
    void deleteBox() throws SQLException {
        Box box = new Box(8, "Khanada Shanaad", false);
        BoxService.deleteBox(box);
    }

    @Test
    void nextId() throws SQLException {
        System.out.println(BoxService.nextId());
    }

    @Test
    void testInsertBox() throws SQLException {
        Box box = new Box("Khanad 5aad");
        BoxService.insertBox(box);
    }
}