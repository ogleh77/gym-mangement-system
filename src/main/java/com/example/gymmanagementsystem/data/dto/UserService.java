package com.example.gymmanagementsystem.data.dto;

import com.example.gymmanagementsystem.data.entities.service.Users;
import com.example.gymmanagementsystem.data.models.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class UserService {
    private static final UserModel userModel = new UserModel();
    private final static String message = "From sqlite ";
    private static ObservableList<Users> users;

    public static void insertUser(Users user) throws SQLException {
        try {
            userModel.insert(user);
        } catch (Exception e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: users.username"))
                throw new SQLException(message + " username ka " + user.getUsername() + " horaa lo isticmalay");
            else if (e.getMessage().contains("(UNIQUE constraint failed: users.phone"))
                throw new SQLException(message + " lanbar ka " + user.getPhone() + "horaa lo isticmalay");
            else {
                throw e;
            }
        }
    }

    public static void updateUser(Users user) throws SQLException {
        try {
            userModel.update(user);
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: users.username"))
                throw new SQLException(message + " username ka " + user.getUsername() + " horaa lo isticmalay");
            else if (e.getMessage().contains("(UNIQUE constraint failed: users.phone"))
                throw new SQLException(message + " lanbar ka" + user.getPhone() + "horaa lo isticmalay");
            else {
                throw e;
            }
        }
    }

    public static void deleteUser(Users user) throws SQLException {
        userModel.delete(user.getUserId());
    }

    public static ObservableList<Users> fetchAllUsers() throws SQLException {
        if (users == null) {
            users = FXCollections.observableArrayList();
            users = userModel.fetchAllUsers();
            users.add(new Users(0, "Mohamed", "Saeed", "4476619", "Male",
                    "Morning", "Ogleh", "4000", null, "admin"));
        }
        return users;
    }

    public static int predictNextId() throws SQLException {
        try {
            return (userModel.nextIDPrediction() + 1);
        } catch (SQLException e) {
            throw new SQLException(message + e.getMessage());
        }
    }
}
