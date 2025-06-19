package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public abstract class Attendance {
    private String name;
    private String id;
    private List<String> attendance; // List of dates when this entity was present

    public Attendance(String name, String id) {
        this.name = name;
        this.id = id;
        this.attendance = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void markAttendance(String date) {
        if (!attendance.contains(date)) {
            attendance.add(date);
        }
    }

    public List<String> viewAttendance() {
        return new ArrayList<>(attendance);
    }

    public void clearAttendance() {
        attendance.clear();
    }

    public String getAttendanceString() {
        return String.join(";", attendance);
    }

    // Abstract method to enforce subclasses to implement this
    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + ": " + name + " (ID: " + id + ")";
    }
}
