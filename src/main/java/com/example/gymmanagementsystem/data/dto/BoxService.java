package com.example.gymmanagementsystem.data.dto;

import com.example.gymmanagementsystem.data.entities.service.Box;
import com.example.gymmanagementsystem.data.models.BoxModel;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class BoxService {
    private final static BoxModel boxModel = new BoxModel();
    private final static String message = "FROM sqlite ";

    public static void insertBox(Box box) throws SQLException {
        try {
            boxModel.insert(box);
        } catch (SQLException e) {
            throw new SQLException(message + e.getMessage());
        }
    }

    public static void changeBoxState(Box box) throws SQLException {
        try {
            if (!box.isReady()) {
                boxModel.setBoxOnline(box.getBoxId());
            } else {
                boxModel.setBoxOff(box.getBoxId());
            }
        } catch (SQLException e) {
            throw new SQLException(message + e.getMessage());
        }
    }

    public static void deleteBox(Box box) throws SQLException {
        try {
//            if (!box.isReady()) {
//                throw new RuntimeException(box.getBoxName() + " Ma masaxi kartid sababtoo ah " +
//                        "Macmiil ayaa isticmalaya iminka.");
//            }
            boxModel.deleteBox(box);
        } catch (SQLException e) {
            throw new SQLException(message + e.getMessage());
        }
    }

    public static ObservableList<Box> fetchBoxes() throws SQLException {
        try {
            return boxModel.fetchBoxes();
        } catch (SQLException e) {
            throw new SQLException(message + e.getMessage());
        }
    }

    public static int nextId() throws SQLException {
        try {
            return (1 + boxModel.predictNextId());
        } catch (SQLException e) {
            throw new SQLException(message + e.getMessage());
        }
    }
}
