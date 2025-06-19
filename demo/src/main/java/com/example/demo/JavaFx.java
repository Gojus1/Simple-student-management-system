package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaFx extends Application {

    private List<Student> students = new ArrayList<>();
    private AttendanceSystem attendanceSystem = new AttendanceSystem();

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label label = new Label("Student Management System");
        Button btnAddStudent = new Button("Add Student");
        Button btnViewAttendance = new Button("View Attendance");
        Button btnExportReport = new Button("Export Report");
        Button btnImportData = new Button("Import Data");

        root.getChildren().addAll(label, btnAddStudent, btnViewAttendance, btnExportReport, btnImportData);

        btnAddStudent.setOnAction(e -> addStudent());
        btnViewAttendance.setOnAction(e -> viewAttendance());
        btnExportReport.setOnAction(e -> exportReport(stage));
        btnImportData.setOnAction(e -> importStudentData(stage));

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Student Management System");
        stage.show();
    }

    private void addStudent() {
        Stage addStudentStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter student name");
        TextField idField = new TextField();
        idField.setPromptText("Enter student ID");
        TextField groupField = new TextField();
        groupField.setPromptText("Enter student group");
        TextField dateField = new TextField();
        dateField.setPromptText("Enter attendance date (e.g., MM-DD)");

        Button saveButton = new Button("Save");

        layout.getChildren().addAll(
                new Label("Add Student"),
                nameField,
                idField,
                groupField,
                new Label("Optional Attendance Date"),
                dateField,
                saveButton
        );

        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String id = idField.getText();
            String group = groupField.getText();
            String date = dateField.getText();

            if (!name.isEmpty() && !id.isEmpty() && !group.isEmpty()) {
                students.add(new Student(name, id, group));

                if (!date.isEmpty()) {
                    attendanceSystem.markAttendance(id, date);
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully!");
                addStudentStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            }
        });

        Scene scene = new Scene(layout, 300, 300);
        addStudentStage.setScene(scene);
        addStudentStage.setTitle("Add Student");
        addStudentStage.show();
    }

    private int extractGroupNumber(String groupName) {
        try {
            return Integer.parseInt(groupName.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }


    private void viewAttendance() {
        Stage attendanceStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        ListView<String> attendanceList = new ListView<>();
        Map<String, Student> studentMap = new HashMap<>();

        // Populate the attendance list
        students.forEach(student -> {
            List<String> attendance = attendanceSystem.viewAttendance(student.getId());
            String info = "Group: " + student.getGroup() + " | " +
                    student.getName() + " (" + student.getId() + "): " +
                    (attendance.isEmpty() ? "No records" : String.join(", ", attendance));
            attendanceList.getItems().add(info);
            studentMap.put(info, student);
        });

        // Add a click event to open the edit window
        attendanceList.setOnMouseClicked(event -> {
            String selectedItem = attendanceList.getSelectionModel().getSelectedItem();
            if (selectedItem != null && studentMap.containsKey(selectedItem)) {
                Student selectedStudent = studentMap.get(selectedItem);
                openEditStudentWindow(selectedStudent);
            }
        });

        Button sortButton = new Button("Sort by Group");
        sortButton.setOnAction(e -> {
            // Sort students by group number
            students.sort((s1, s2) -> {
                int group1 = extractGroupNumber(s1.getGroup());
                int group2 = extractGroupNumber(s2.getGroup());
                return Integer.compare(group1, group2);
            });

            // Refresh the attendance list
            attendanceList.getItems().clear();
            students.forEach(student -> {
                List<String> attendance = attendanceSystem.viewAttendance(student.getId());
                String info = "Group: " + student.getGroup() + " | " +
                        student.getName() + " (" + student.getId() + "): " +
                        (attendance.isEmpty() ? "No records" : String.join(", ", attendance));
                attendanceList.getItems().add(info);
                studentMap.put(info, student);
            });
        });

        layout.getChildren().addAll(new Label("Attendance Records"), attendanceList, sortButton);

        Scene scene = new Scene(layout, 400, 400);
        attendanceStage.setScene(scene);
        attendanceStage.setTitle("View Attendance");
        attendanceStage.show();
    }


    private void openEditStudentWindow(Student student) {
        Stage editStudentStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        // Pre-filled input fields
        TextField nameField = new TextField(student.getName());
        TextField idField = new TextField(student.getId());
        TextField groupField = new TextField(student.getGroup());

        // Attendance date field (comma-separated)
        List<String> attendanceDates = attendanceSystem.viewAttendance(student.getId());
        TextField dateField = new TextField(attendanceDates.isEmpty() ? "" : String.join(", ", attendanceDates));
        dateField.setPromptText("Enter attendance dates (comma-separated)");

        Button saveButton = new Button("Save Changes");

        layout.getChildren().addAll(
                new Label("Edit Student"),
                new Label("Name:"),
                nameField,
                new Label("ID:"),
                idField,
                new Label("Group:"),
                groupField,
                new Label("Attendance Dates:"),
                dateField,
                saveButton
        );

        saveButton.setOnAction(e -> {
            String newName = nameField.getText();
            String newId = idField.getText();
            String newGroup = groupField.getText();
            String newDates = dateField.getText();

            if (!newName.isEmpty() && !newId.isEmpty() && !newGroup.isEmpty()) {
                // Update name
                student.setName(newName);
                student.setId(newId);
                student.setGroup(newGroup);

                // Update attendance
                attendanceSystem.clearAttendance(student.getId());
                if (!newDates.isEmpty()) {
                    String[] dates = newDates.split(",");
                    for (String date : dates) {
                        attendanceSystem.markAttendance(newId, date.trim());
                    }
                }

                editStudentStage.close();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Student information and attendance updated successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            }
        });

        Scene scene = new Scene(layout, 400, 400);
        editStudentStage.setScene(scene);
        editStudentStage.setTitle("Edit Student");
        editStudentStage.show();
    }


    private void importStudentData(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                CVS csvHandler = new CVS(attendanceSystem);
                students = csvHandler.importStudents(file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Students imported successfully!");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to import data.");
            }
        }
    }

    private void exportReport(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                CVS csvHandler = new CVS(attendanceSystem);
                csvHandler.exportStudents(students, attendanceSystem, file.getAbsolutePath());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Report exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export data.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
