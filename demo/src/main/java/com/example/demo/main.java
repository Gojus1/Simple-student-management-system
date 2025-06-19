package com.example.demo;

public class main {
    public static void main(String[] args) {
        // Create a group and students
        Group groupA = new Group("Group A", "G001");

        Student student1 = new Student("Alice", "S001", "Group A");
        Student student2 = new Student("Bob", "S002", "Group A");

        groupA.addMember(student1);
        groupA.addMember(student2);

        // Use the AttendanceSystem
        AttendanceSystem attendanceSystem = new AttendanceSystem();
        attendanceSystem.registerEntity(student1);
        attendanceSystem.registerEntity(student2);
        attendanceSystem.registerEntity(groupA);

        // Mark attendance
        attendanceSystem.markAttendance("S001", "05-05");
        attendanceSystem.markAttendance("S002", "05-06");
        attendanceSystem.markAttendance("G001", "05-07"); // Group-wide attendance

        // View attendance
        System.out.println(student1 + " Attendance: " + attendanceSystem.viewAttendance("S001"));
        System.out.println(student2 + " Attendance: " + attendanceSystem.viewAttendance("S002"));
        System.out.println(groupA + " Attendance: " + attendanceSystem.viewAttendance("G001"));
    }
}
