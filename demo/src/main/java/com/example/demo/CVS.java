package com.example.demo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CVS {
    private AttendanceSystem attendanceSystem;

    public CVS(AttendanceSystem attendanceSystem) {
        this.attendanceSystem = attendanceSystem;
    }

    public List<Student> importStudents(String filePath) throws IOException {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    String name = parts[0];
                    String id = parts[1];
                    String group = parts[2];
                    String attendance = parts[3];

                    Student student = new Student(name, id, group);
                    students.add(student);

                    if (!attendance.isEmpty()) {
                        String[] dates = attendance.split(";");
                        for (String date : dates) {
                            attendanceSystem.markAttendance(id, date.trim());
                        }
                    }
                }
            }
        }
        return students;
    }

    public void exportStudents(List<Student> students, AttendanceSystem attendanceSystem, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Name,ID,Group,Attendance");
            writer.newLine();
            for (Student student : students) {
                String attendance = student.getAttendanceString();
                writer.write(String.join(",", student.getName(), student.getId(), student.getGroup(), attendance));
                writer.newLine();
            }
        }
    }
}
