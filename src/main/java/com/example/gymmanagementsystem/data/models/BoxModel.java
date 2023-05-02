package com.example.gymmanagementsystem.data.models;

import com.example.gymmanagementsystem.data.entities.service.Box;
import com.example.gymmanagementsystem.dependencies.DbConnection;
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
        try (Statement statement = connection.createStatement()) {
            String insertBox = " INSERT INTO box(box_name) " + "VALUES ('" + box.getBoxName() + "')";

            statement.executeUpdate(insertBox);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void setBoxOnline(int boxId) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            String boxQuery = "UPDATE box SET is_ready=true WHERE box_id=" + boxId;
            statement.executeUpdate(boxQuery);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }


    public void setBoxOff(int boxId) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement statement = connection.createStatement()) {
            String boxQuery = "UPDATE box SET is_ready=false WHERE box_id=" + boxId;
            statement.executeUpdate(boxQuery);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
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

    public int predictNextId() throws SQLException {
        String query = "SELECT * FROM SQLITE_SEQUENCE WHERE name='box'";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        if (rs.next()) {
            return rs.getInt("seq");
        }
        rs.close();
        statement.close();
        return 0;
    }
}
