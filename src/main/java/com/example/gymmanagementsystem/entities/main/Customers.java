package com.example.gymmanagementsystem.entities.main;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customers implements Comparable<Customers> {
    private int customerId;
    private final SimpleStringProperty firstName = new SimpleStringProperty();
    private final SimpleStringProperty lastName = new SimpleStringProperty();
    private final SimpleStringProperty middleName = new SimpleStringProperty();
    private final SimpleStringProperty phone = new SimpleStringProperty();
    private final SimpleStringProperty gander = new SimpleStringProperty();
    private final SimpleStringProperty shift = new SimpleStringProperty();
    private final SimpleStringProperty address = new SimpleStringProperty();
    private byte[] image;
    private final SimpleDoubleProperty weight = new SimpleDoubleProperty();
    private final SimpleStringProperty whoAdded = new SimpleStringProperty();
    private SimpleDoubleProperty waist = new SimpleDoubleProperty();
    private SimpleDoubleProperty foreArm = new SimpleDoubleProperty();
    private SimpleDoubleProperty hips = new SimpleDoubleProperty();
    private SimpleDoubleProperty chest = new SimpleDoubleProperty();
    private ObservableList<Payments> payments;


    public Customers(int customerId, String firstName, String lastName, String middleName, String phone, String gander, String shift, String address, byte[] image, double weight, String whoAdded) {
        this.customerId = customerId;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setMiddleName(middleName);
        this.setPhone(phone);
        this.setGander(gander);
        this.setShift(shift);
        this.setAddress(address);
        this.image = image;
        this.setWeight(weight);
        this.setWhoAdded(whoAdded);

        this.payments = FXCollections.observableArrayList();
    }



    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getGander() {
        return gander.get();
    }

    public void setGander(String gander) {
        this.gander.set(gander);
    }

    public String getShift() {
        return shift.get();
    }

    public void setShift(String shift) {
        this.shift.set(shift);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getWeight() {
        return weight.get();
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    public String getWhoAdded() {
        return whoAdded.get();
    }

    public void setWhoAdded(String whoAdded) {
        this.whoAdded.set(whoAdded);
    }

    public ObservableList<Payments> getPayments() {
        return payments;
    }

    public void setPayments(ObservableList<Payments> payments) {
        this.payments = payments;
    }

    public SimpleDoubleProperty weightProperty() {
        return weight;
    }

    public double getWaist() {
        return waist.get();
    }

    public SimpleDoubleProperty waistProperty() {
        return waist;
    }

    public double getForeArm() {
        return foreArm.get();
    }

    public SimpleDoubleProperty foreArmProperty() {
        return foreArm;
    }

    public double getHips() {
        return hips.get();
    }

    public SimpleDoubleProperty hipsProperty() {
        return hips;
    }

    public double getChest() {
        return chest.get();
    }

    public SimpleDoubleProperty chestProperty() {
        return chest;
    }

    public void setWaist(double waist) {
        this.waist.set(waist);
    }

    public void setForeArm(double foreArm) {
        this.foreArm.set(foreArm);
    }

    public void setHips(double hips) {
        this.hips.set(hips);
    }

    public void setChest(double chest) {
        this.chest.set(chest);
    }

    @Override
    public int compareTo(Customers o) {
        if (this.payments.get(0).getExpDate().isAfter(o.payments.get(0).getExpDate())) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return firstName.get()+" "+phone.get()+" "+payments+"\n";
    }
}
