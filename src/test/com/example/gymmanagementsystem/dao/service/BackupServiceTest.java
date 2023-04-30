package com.example.gymmanagementsystem.dao.service;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class BackupServiceTest {

    @Test
    void restore() throws SQLException {
        BackupService.restore("/Users/mrd/Desktop/mybackup");
    }
}