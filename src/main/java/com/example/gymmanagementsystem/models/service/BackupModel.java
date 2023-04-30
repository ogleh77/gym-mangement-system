package com.example.gymmanagementsystem.models.service;


import com.example.gymmanagementsystem.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

public class BackupModel {
    private static final Connection connection = DbConnection.getConnection();
     private static final ObservableList<String> paths = FXCollections.observableArrayList();

    public void backup(String path) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            String query = "BACKUP to " + path;
            statement.executeUpdate(query);
            if (statement.executeUpdate("UPDATE backup_table SET last_backup ='" + LocalDate.now() +
                    " " + LocalTime.now() + "' WHERE location='" + path + "'") <= 0) {

                String insertPath = "INSERT INTO backup_table(location) VALUES('" + path + "')";
                statement.executeUpdate(insertPath);
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

    }



    public void restore(String path) throws SQLException {
        String query = "restore from " + path;
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    public String lastBackupTime() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT last_backup FROM backup_table;");
        String lastBackup = null;
        while (rs.next()) {
            lastBackup = rs.getString("last_backup");
        }
        rs.close();
        statement.close();
        return lastBackup;
    }

    public String lastBackupPath() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT location FROM backup_table ORDER BY backup_id DESC;");
        String backupPath = null;
        while (rs.next()) {
            backupPath = rs.getString("location");
        }
        rs.close();
        statement.close();
        return backupPath;
    }
}
