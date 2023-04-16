package com.example.gymmanagementsystem.dao.service;


import com.example.gymmanagementsystem.entities.service.Users;
import com.example.gymmanagementsystem.helpers.CustomException;
import com.example.gymmanagementsystem.models.service.UserModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class UserService {
    private static int nextID = 0;
    private static final UserModel userModel;
    private static ObservableList<Users> users = null;

    static {
        userModel = new UserModel();
    }

    public static void insertUser(Users user) throws SQLException {
        try {
            if (user.getUsername().equalsIgnoreCase("ogleh")) {
                throw new CustomException("Username ka " + user.getUsername() + " horaa loo isticmalay");
            }
            userModel.insert(user);
        } catch (Exception e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: users.username")) {
                throw new CustomException("username ka " + user.getUsername() + " horaa lo isticmalay");
            } else if (e.getMessage().contains("(UNIQUE constraint failed: users.phone")) {
                throw new CustomException("lanbar ka " + user.getPhone() + " horaa lo isticmalay");
            } else {
                throw e;
            }
        }
    }

    public static void update(Users user) throws SQLException {
        try {
            userModel.update(user);
        } catch (SQLException e) {
            if (e.getMessage().contains("(UNIQUE constraint failed: users.username")) {
                throw new CustomException("username ka " + user.getUsername() + " horaa lo isticmalay");
            } else if (e.getMessage().contains("(UNIQUE constraint failed: users.phone")) {
                throw new CustomException("lanbar ka " + user.getPhone() + " horaa lo isticmalay");
            } else {
                throw e;
            }
        }
    }

    public static void delete(Users user) throws SQLException {
        try {
            userModel.delete(user);
        } catch (SQLException e) {
            throw new CustomException("Khalad ayaa ka dhacay " + e.getMessage());
        }
    }

    public static ObservableList<Users> users() throws SQLException {
        if (users == null) {
            users = userModel.fetch();
            users.add(new Users(0, "Mohamed", "Saeed", "4476619", "Male",
                    "Morning", "Ogleh", "4000", null, "super_admin"));
        }

        return users;
    }

    public static int predictNextId() throws SQLException {
        if (nextID == 0) {
            try {
                return (userModel.nextID() + 1);
            } catch (SQLException e) {
                throw new CustomException("SQL error caused predict nextId " + e.getMessage());
            }
        }
        return nextID;
    }

}
