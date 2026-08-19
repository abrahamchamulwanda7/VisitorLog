package org.example;

public class Visitor {
    private int id;
    private String fullName;
    private String phoneNumber;
    private String purpose;
    private Staff staffVisited;
    private String timeIn;
    private String timeOut;

    public Visitor(int id, String fullName, String phoneNumber, String purpose, Staff staffVisited, String timeIn) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.purpose = purpose;
        this.staffVisited = staffVisited;
        this.timeIn = timeIn;
        this.timeOut = null;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public Staff getStaffVisited() {
        return staffVisited;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        String status = (timeOut == null) ? "Still in" : "Left at " + timeOut;
        return fullName + " | Visiting: " + staffVisited.getName() + " | In: " + timeIn + " | " + status;
    }
}