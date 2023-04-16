package com.example.gymmanagementsystem.dao.service;

import com.example.gymmanagementsystem.entities.service.Gym;
import com.example.gymmanagementsystem.models.service.GymModel;

import java.sql.SQLException;

public class GymService {
    private static final GymModel gymModel = new GymModel();
    private static Gym currentGym = null;

    public static void updateGym(Gym gym) throws SQLException {
        gymModel.update(gym);
        currentGym = gym;
    }

    public static Gym getGym() throws SQLException {
        if (currentGym == null) {
            currentGym = gymModel.currentGym();
        }
        return currentGym;
    }

}
