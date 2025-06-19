package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Group extends Attendance {
    private List<Student> members;

    public Group(String name, String id) {
        super(name, id);
        this.members = new ArrayList<>();
    }

    public List<Student> getMembers() {
        return members;
    }

    public void addMember(Student student) {
        members.add(student);
    }

    @Override
    public String getRole() {
        return "Group";
    }

    @Override
    public String toString() {
        return getRole() + ": " + getName() + " (" + members.size() + " members)";
    }
}
