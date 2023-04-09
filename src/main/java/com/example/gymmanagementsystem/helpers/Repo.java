package com.example.gymmanagementsystem.helpers;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface Repo<T> {
    default void insert(T t) {
    }

    void update(T t) throws SQLException;

    default ObservableList<T> getAll() {
        return null;
    }
}
