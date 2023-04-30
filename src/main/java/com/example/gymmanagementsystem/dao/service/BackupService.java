package com.example.gymmanagementsystem.dao.service;


import com.example.gymmanagementsystem.models.service.BackupModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class BackupService {
    private static final BackupModel backupModel = new BackupModel();
    private static ObservableList<String> paths;
    private static String lastBackupPath;
    private static String lastBackup;

    public static void backup(String path) throws SQLException {
        backupModel.backup(path);
    }

    public static void restore(String path) throws SQLException {
        try {
            backupModel.restore(path);
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }



    public static String lastBackup() throws SQLException {
        if (lastBackup == null) {
            lastBackup = backupModel.lastBackupTime();
        }
        return lastBackup == null ? " No database.db yet " : lastBackup;
    }

    public static String lastBackupPath() throws SQLException {
        if (lastBackupPath == null) {
            lastBackupPath = backupModel.lastBackupPath();
        }
        return lastBackupPath;
    }
}
