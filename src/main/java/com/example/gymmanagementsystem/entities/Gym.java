package com.example.gymmanagementsystem.entities;

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
    private final SimpleDoubleProperty fitnessCost = new SimpleDoubleProperty();
    private final SimpleDoubleProperty poxingCost = new SimpleDoubleProperty();
    private final SimpleDoubleProperty boxCost = new SimpleDoubleProperty();
    private final SimpleIntegerProperty pendingDate = new SimpleIntegerProperty();
    private final SimpleDoubleProperty maxDiscount = new SimpleDoubleProperty();
    private final ObservableList<Box> vipBoxes;

    public Gym(int gymId, String gymName, int zaad, int eDahab, double fitnessCost, double poxingCost, double boxCost,
               int pendingDate, double maxDiscount) {
        this.gymId = gymId;
        this.setGymName(gymName);
        this.setZaad(zaad);
        this.seteDahab(eDahab);
        this.setFitnessCost(fitnessCost);
        this.setPoxingCost(poxingCost);
        this.setBoxCost(boxCost);
        this.setPendingDate(pendingDate);
        this.setMaxDiscount(maxDiscount);
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

    public double getFitnessCost() {
        return fitnessCost.get();
    }

    public void setFitnessCost(double fitnessCost) {
        this.fitnessCost.set(fitnessCost);
    }

    public double getPoxingCost() {
        return poxingCost.get();
    }

    public void setPoxingCost(double poxingCost) {
        this.poxingCost.set(poxingCost);
    }

    public double getBoxCost() {
        return boxCost.get();
    }

    public void setBoxCost(double boxCost) {
        this.boxCost.set(boxCost);
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

    public ObservableList<Box> getVipBoxes() {
        return vipBoxes;
    }

}
