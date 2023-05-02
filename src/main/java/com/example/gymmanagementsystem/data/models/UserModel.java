package com.example.gymmanagementsystem.data.models;

import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.dependencies.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UserModel {
    private final static Connection connection = DbConnection.getConnection();

    public void insert(Users users) throws SQLException {
        String insertUserQuery = "INSERT INTO users(first_name, last_name, phone, gender, shift, username, password, image, role) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        insertOrUpdateUser(users, insertUserQuery);
    }

    public void update(Users users) throws SQLException {
        String updateUser = "UPDATE users SET first_name=?,last_name=?,phone=?,gender=?,shift=?,username=?,password=?,image=?,role=? \n" +
                "WHERE user_id=" + users.getUserId();
        insertOrUpdateUser(users, updateUser);
    }

    public void delete(int userID) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String deleteUser = "DELETE FROM users " +
                    "WHERE user_id=" + userID;
            Statement statement = connection.createStatement();
            statement.execute(deleteUser);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public ObservableList<Users> fetchAllUsers() throws SQLException {
        String fetchQuery = "SELECT * FROM users";
        ObservableList<Users> users = FXCollections.observableArrayList();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery(fetchQuery);

        Users user;
        while (rs.next()) {
            user = new Users(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),
                    rs.getString(7), rs.getString(8), rs.getBytes(9), rs.getString(10));
            users.add(user);
        }
        statement.close();
        rs.close();
        return users;
    }

    //---------------Helpers--------------––
    private void insertOrUpdateUser(Users users, String updateUser) throws SQLException {
        connection.setAutoCommit(false);
        try {
            PreparedStatement ps = connection.prepareStatement(updateUser);
            ps.setString(1, users.getFirstName());
            ps.setString(2, users.getLastName());
            ps.setString(3, users.getPhone());
            ps.setString(4, users.getGender());
            ps.setString(5, users.getShift());
            ps.setString(6, users.getUsername());
            ps.setString(7, users.getPassword());
            ps.setBytes(8, users.getImage());
            ps.setString(9, users.getRole());
            ps.executeUpdate();
            ps.close();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }

    }

    public int nextIDPrediction() throws SQLException {
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM SQLITE_SEQUENCE WHERE name = 'users';");
        if (rs.next()) {
            return rs.getInt("seq");
        }
        return 0;
    }


}
