package com.example.gymmanagementsystem.data.dto;

import com.example.gymmanagementsystem.data.models.BackupModel;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupService {
    private static final BackupModel backupModel = new BackupModel();
    private static String lastBackupPath;
    private static String lastBackup;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void backupTo(String path) throws SQLException {
        if (lastBackupPath() != null) {
            LocalDateTime now = LocalDateTime.now();
            String formattedDate = now.format(formatter);
            backupModel.backup(path, formattedDate);
        } else {
            backupModel.insertBackupPath(path);
        }
    }

    public static String lastBackupPath() throws SQLException {
        if (lastBackupPath == null) {
            lastBackupPath = backupModel.lastBackupPath();
        }
        return lastBackupPath;
    }

    public static String lastBackupDate() throws SQLException {
        lastBackup = backupModel.lastBackupTime();
        return lastBackup;
    }

    public static void restoreFrom() throws SQLException {
        backupModel.restore(lastBackupPath);
    }
}
