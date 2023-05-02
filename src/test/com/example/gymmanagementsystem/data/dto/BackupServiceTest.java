package com.example.gymmanagementsystem.data.dto;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class BackupServiceTest {

    @Test
    void backupTo() throws SQLException {
        BackupService.backupTo("/Users/mrd/Desktop/mybackup");
    }

    @Test
    void lastBackupPath() throws SQLException {
        System.out.println(BackupService.lastBackupPath());
    }

    @Test
    void lastBackupDate() throws SQLException {
        System.out.println(BackupService.lastBackupDate());

    }
}