package org.example;

import java.util.ArrayList;
import java.util.List;

public class VisitorLog {
    private List<Visitor> visitors;
    private int nextId;

    public VisitorLog() {
        this.visitors = new ArrayList<>();
        this.nextId = 1;
    }

    public void signIn(String fullName, String phoneNumber, String purpose, Staff staffVisited, String timeIn) {
        Visitor visitor = new Visitor(nextId, fullName, phoneNumber, purpose, staffVisited, timeIn);
        visitors.add(visitor);
        nextId++;
        System.out.println("Signed in: " + fullName);
    }

    public void listCurrentVisitors() {
        System.out.println("--- Visitors Currently In ---");
        boolean anyIn = false;
        for (Visitor v : visitors) {
            if (v.getTimeOut() == null) {
                System.out.println(v);
                anyIn = true;
            }
        }
        if (!anyIn) {
            System.out.println("No visitors currently in the building.");
        }
    }

    public void signOut(int visitorId, String timeOut) {
        for (Visitor v : visitors) {
            if (v.getId() == visitorId && v.getTimeOut() == null) {
                v.setTimeOut(timeOut);
                System.out.println("Signed out: " + v.getFullName());
                return;
            }
        }
        System.out.println("Visitor not found or already signed out.");
    }

    public void searchByName(String name) {
        System.out.println("--- Search Results for '" + name + "' ---");
        boolean found = false;
        for (Visitor v : visitors) {
            if (v.getFullName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println(v);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No matching visitors found.");
        }
    }
}