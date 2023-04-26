package com.example.gymmanagementsystem.models.service;


import com.example.gymmanagementsystem.entities.service.Box;
import com.example.gymmanagementsystem.helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BoxModel {
    private static final Connection connection = DbConnection.getConnection();

    public void insert(Box box) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String insertBox = " INSERT INTO box(box_name) " + "VALUES ('" + box.getBoxName() + "')";

            Statement statement = connection.createStatement();
            statement.executeUpdate(insertBox);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void update(Box box) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String boxQuery = "UPDATE box SET is_ready=false WHERE box_id=" + box.getBoxId();
            if (!box.isReady()) {
                boxQuery = "UPDATE box SET is_ready=true WHERE box_id=" + box.getBoxId();
            }
            Statement statement = connection.createStatement();
            statement.executeUpdate(boxQuery);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
    public void boxReadyFalse(Box box) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String boxQuery = "UPDATE box SET is_ready=false WHERE box_id=" + box.getBoxId();
            if (!box.isReady()) {
                boxQuery = "UPDATE box SET is_ready=true WHERE box_id=" + box.getBoxId();
            }
            Statement statement = connection.createStatement();
            statement.executeUpdate(boxQuery);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public ObservableList<Box> fetchBoxes() throws SQLException {
        ObservableList<Box> boxes = FXCollections.observableArrayList();
        Box box;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM box");

        while (rs.next()) {
            box = new Box(rs.getInt(1), rs.getString(2), rs.getBoolean(3));
            boxes.add(box);
        }
        rs.close();
        statement.close();
        return boxes;
    }

    public void deleteBox(Box box) throws SQLException {
        connection.setAutoCommit(false);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM box WHERE box_id=" + box.getBoxId());
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public int predictNextId() throws SQLException {
        String query = "SELECT * FROM SQLITE_SEQUENCE WHERE name='box'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        if (rs.next()) {
            return rs.getInt("seq");
        }
        return 0;
    }
}
