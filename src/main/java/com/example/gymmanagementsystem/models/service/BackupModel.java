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
    //    private static final ObservableList<String> fullBackupInfo = FXCollections.observableArrayList();
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
            e.printStackTrace();
            connection.rollback();
            throw e;
        }

    }


    public ObservableList<String> backupPaths() throws SQLException {

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM backup_table;");

        while (rs.next()) {
            paths.add(rs.getString("location"));
        }
        rs.close();
        statement.close();
        return paths;
    }
}
