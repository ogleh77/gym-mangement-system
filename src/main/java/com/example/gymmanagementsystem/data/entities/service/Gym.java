package com.example.gymmanagementsystem.data.entities.service;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Gym {
    private final int gymId;
    private final SimpleStringProperty gymName = new SimpleStringProperty();
    private final SimpleIntegerProperty zaad = new SimpleIntegerProperty();
    private final SimpleIntegerProperty eDahab = new SimpleIntegerProperty();
    private final SimpleIntegerProperty pendingDate = new SimpleIntegerProperty();
    private final SimpleDoubleProperty maxDiscount = new SimpleDoubleProperty();

    private final SimpleBooleanProperty imageUpload = new SimpleBooleanProperty();
    private final ObservableList<Box> vipBoxes;

    public Gym(int gymId, String gymName, int zaad, int eDahab,
               int pendingDate, double maxDiscount, boolean imageUploadCheck) {
        this.gymId = gymId;
        this.setGymName(gymName);
        this.setZaad(zaad);
        this.seteDahab(eDahab);
        this.setPendingDate(pendingDate);
        this.setMaxDiscount(maxDiscount);
        this.setImageUpload(imageUploadCheck);
        this.vipBoxes = FXCollections.observableArrayList();
    }

    public int getGymId() {
        return gymId;
    }

    public String getGymName() {
        return gymName.get();
    }

    public SimpleStringProperty gymNameProperty() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName.set(gymName);
    }

    public int getZaad() {
        return zaad.get();
    }

    public void setZaad(int zaad) {
        this.zaad.set(zaad);
    }

    public int geteDahab() {
        return eDahab.get();
    }

    public void seteDahab(int eDahab) {
        this.eDahab.set(eDahab);
    }


    public int getPendingDate() {
        return pendingDate.get();
    }

    public void setPendingDate(int pendingDate) {
        this.pendingDate.set(pendingDate);
    }

    public double getMaxDiscount() {
        return maxDiscount.get();
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount.set(maxDiscount);
    }

    public boolean isImageUpload() {
        return imageUpload.get();
    }

    public void setImageUpload(boolean imageUpload) {
        this.imageUpload.set(imageUpload);
    }

    public ObservableList<Box> getVipBoxes() {
        return vipBoxes;
    }

    @Override
    public String toString() {
        return "Gym{" +
                "gymId=" + gymId +
                ", gymName=" + gymName +
                ", zaad=" + zaad +
                ", eDahab=" + eDahab +
                ", pendingDate=" + pendingDate +
                ", maxDiscount=" + maxDiscount +
                ", imageUpload=" + imageUpload +
                ", vipBoxes=" + vipBoxes +
                '}';
    }

}
