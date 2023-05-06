package com.example.gymmanagementsystem.models;


import com.example.gymmanagementsystem.dao.BoxService;
import com.example.gymmanagementsystem.entities.Gym;
import com.example.gymmanagementsystem.helpers.DbConnection;

import java.sql.*;

public class GymModel {
    public static Connection connection = DbConnection.getConnection();

    public void update(Gym gym) throws SQLException {
        String updateQuery = "UPDATE gym SET gym_name=? ,pending_date=?,max_discount=?,zaad_merchant=?," +
                "edahab_merchant=? WHERE gym_id=" + gym.getGymId();
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        ps.setString(1, gym.getGymName());
        ps.setInt(2, gym.getPendingDate());
        ps.setDouble(3, gym.getMaxDiscount());
        ps.setInt(4, gym.getZaad());
        ps.setInt(5, gym.geteDahab());

        ps.executeUpdate();
        ps.close();
    }

    public Gym currentGym() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM gym");

        Gym currentGym = new Gym(rs.getInt("gym_id"), rs.getString("gym_name"),
                rs.getInt("zaad_merchant"), rs.getInt("edahab_merchant"),
                rs.getInt("pending_date"), rs.getDouble("max_discount"));

        currentGym.getVipBoxes().setAll(BoxService.fetchBoxes());
        rs.close();
        statement.close();
        return currentGym;

    }
}