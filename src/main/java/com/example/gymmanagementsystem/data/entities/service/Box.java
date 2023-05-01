package com.example.gymmanagementsystem.data.entities.service;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Box {
    private int boxId;
    private final SimpleStringProperty boxName = new SimpleStringProperty();
    private final SimpleBooleanProperty ready = new SimpleBooleanProperty();

    public Box(int boxId, String boxName, boolean ready) {
        this.boxId = boxId;
        this.setBoxName(boxName);
        this.setReady(ready);
    }

    public int getBoxId() {
        return boxId;
    }
    public String getBoxName() {
        return boxName.get();
    }
    public void setBoxName(String boxName) {
        this.boxName.set(boxName);
    }

    public boolean isReady() {
        return ready.get();
    }
    public void setReady(boolean ready) {
        this.ready.set(ready);
    }

    @Override
    public String toString() {
        return boxName.get();
    }
}
