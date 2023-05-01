package com.example.gymmanagementsystem.data.dto;

import com.example.gymmanagementsystem.data.entities.service.Gym;
import com.example.gymmanagementsystem.data.models.GymModel;

import java.sql.SQLException;

public class GymService {
    private static final GymModel gymModel = new GymModel();
    private static Gym currentGym = null;
    private final static String message = "From Sqlite";

    public static void updateGym(Gym gym) throws SQLException {
        try {
            gymModel.update(gym);
        } catch (SQLException e) {
            throw new SQLException(message + " " + e.getMessage());
        }
        currentGym = gym;
    }

    public static Gym getGym() throws SQLException {
        if (currentGym == null) {
            currentGym = gymModel.currentGym();
        }
        return currentGym;
    }
}
