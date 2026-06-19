package com.model;

import java.sql.Timestamp;

public class Facility {
    private int id;
    private String name;
    private String description;
    private int capacity;
    private String location;
    private boolean isActive;
    private Timestamp createdAt;

    public Facility() {}

    public Facility(String name, String description, int capacity, String location) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.location = location;
        this.isActive = true;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}