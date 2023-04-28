package com.example.gymmanagementsystem.dao.service;


import com.example.gymmanagementsystem.models.service.BackupModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class BackupService {
    private static final BackupModel backupModel = new BackupModel();
    private static ObservableList<String> paths;

//    public static void insertPath(String path) throws SQLException {
//        if (path == null) {
//            throw new RuntimeException("Fadlan dooro meel aamin ah oo aad digan rabto backup kaga adigoo u bixinya " +
//                    "magac aad ku garan karto");
//        }
//
//        backupModel.insertPath(path);
//    }

    public static void backup(String path) throws SQLException {
        try {

                backupModel.backup(path);

//            for (String oldPath : backupPaths()) {
//                if (oldPath.contains(path)) {
//                    System.out.println("Pat already exist");
//                    backupModel.backup(oldPath, false);
//                    break;
//                } else {
//                    System.out.println("Not exist");
//                    backupModel.backup(path, true);
//                }

//                System.out.println("Found");
//                else System.out.println("Not found");
//                if (path.equals(oldPath)) {
//                    System.out.println("Pat already exist");
//                    backupModel.backup(oldPath, false);
//                } else {
//                    System.out.println("Not exist");
//                    backupModel.backup(path, true);
//                }
                System.out.println(path + " \n");

//            if (path.equals())
//                if (path == null) {
//                    throw new CustomException("Fadlan kasoo dooro listka sare path ka aad dhigayso backup kaga" +
//                            " hadii location-ku ku jirin list ka sare taabo button ka PATH si aad u samayso");
//                }
            //    backupModel.backUp(path);
        } catch (SQLException e) {
            // throw new CustomException(e.getMessage());
        }
    }

//    public static void restore(String path) throws CustomException {
//        try {
//            if (path == null) {
//                throw new CustomException("Fadlan marka ka dooro list-ka sare location-ka backup kagu kugu kaydsan yahay" +
//                        " Si aad uga soo restore garayso");
//            }
//            backupModel.restore(path);
//        } catch (SQLException e) {
//            throw new CustomException(e.getMessage());
//        }
//    }

    public static ObservableList<String> backupPaths() throws SQLException {
        if (paths == null) {
            paths = FXCollections.observableArrayList();
            paths = backupModel.backupPaths();
        }
        return paths;
    }

}
