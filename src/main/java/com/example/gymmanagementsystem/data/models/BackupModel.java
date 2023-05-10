package com.example.gymmanagementsystem.data.models;

import com.example.gymmanagementsystem.dependencies.DbConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BackupModel {
    private static final Connection connection = DbConnection.getConnection();

    public void backup(String path, String date) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            String backupQuery = "BACKUP to " + path;
            String updateTimeQuery = "UPDATE backup_table SET last_backup ='" + date + "' WHERE location='" + path + "'";

            statement.executeUpdate(backupQuery);
            statement.executeUpdate(updateTimeQuery);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

    }

    public void insertBackupPath(String path) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            String backupQuery = "BACKUP to " + path;
            String insertPath = "INSERT INTO backup_table(location) VALUES('" + path + "')";

            statement.executeUpdate(backupQuery);
            statement.executeUpdate(insertPath);
            connection.commit();
         } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

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

    public void restore(String path) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            String query = "restore from " + path;
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}
