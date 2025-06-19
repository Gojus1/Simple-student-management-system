package com.example.demo;

public class Student extends Attendance {
    private String group;

    public Student(String name, String id, String group) {
        super(name, id);
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String toString() {
        return super.toString() + ", Group: " + group;
    }
}
