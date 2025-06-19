package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceSystem {
    private Map<String, Attendance> attendanceRecords;

    public AttendanceSystem() {
        this.attendanceRecords = new HashMap<>();
    }

    public void registerEntity(Attendance entity) {
        attendanceRecords.put(entity.getId(), entity);
    }

    public void markAttendance(String entityId, String date) {
        if (attendanceRecords.containsKey(entityId)) {
            attendanceRecords.get(entityId).markAttendance(date);
        }
    }

    public List<String> viewAttendance(String entityId) {
        if (attendanceRecords.containsKey(entityId)) {
            return attendanceRecords.get(entityId).viewAttendance();
        }
        return List.of();
    }

    public void clearAttendance(String entityId) {
        if (attendanceRecords.containsKey(entityId)) {
            attendanceRecords.get(entityId).clearAttendance();
        }
    }
}
