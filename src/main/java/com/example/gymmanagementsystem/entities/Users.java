package com.example.gymmanagementsystem.entities;

import javafx.beans.property.SimpleStringProperty;

public class Users {
    private final int userId;
    private final SimpleStringProperty firstName = new SimpleStringProperty();
    private final SimpleStringProperty lastName = new SimpleStringProperty();
    private final SimpleStringProperty phone = new SimpleStringProperty();
    private final SimpleStringProperty gender = new SimpleStringProperty();
    private final SimpleStringProperty shift = new SimpleStringProperty();
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private byte[] image;
    private final SimpleStringProperty role = new SimpleStringProperty();

    public Users(int userId, String firstName, String lastName, String phone, String gender, String shift, String username, String password, byte[] image, String role) {
        this.userId = userId;
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setPhone(phone);
        this.setGender(gender);
        this.setShift(shift);
        this.setUsername(username);
        this.setPassword(password);
        this.image = image;
        this.setRole(role);
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName.get();
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

    public String getPhone() {
        return phone.get();
    }



    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getGender() {
        return gender.get();
    }



    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getShift() {
        return shift.get();
    }



    public void setShift(String shift) {
        this.shift.set(shift);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }



    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getRole() {
        return role.get();
    }


    public void setRole(String role) {
        this.role.set(role);
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "[username:- " + username.get() + " role:- " + role.getValue() + "]\n";
    }

}
