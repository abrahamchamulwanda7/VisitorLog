package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitorLog {

    public void signIn(String fullName, String phoneNumber, String purpose, Staff staffVisited, String timeIn) {
        String sql = "INSERT INTO visitors (full_name, phone_number, purpose, staff_id, time_in) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fullName);
            stmt.setString(2, phoneNumber);
            stmt.setString(3, purpose);
            stmt.setInt(4, staffVisited.getId());
            stmt.setString(5, timeIn);
            int rows = stmt.executeUpdate();
            System.out.println("Signed in: " + fullName + " (rows inserted: " + rows + ")");
            System.out.println("Connected to: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            System.out.println("Error signing in visitor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void listCurrentVisitors() {
        String sql = "SELECT v.id, v.full_name, v.time_in, s.name AS staff_name " +
                "FROM visitors v JOIN staff s ON v.staff_id = s.id " +
                "WHERE v.time_out IS NULL";
        System.out.println("--- Visitors Currently In ---");
        boolean anyIn = false;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + ". " + rs.getString("full_name") +
                        " | Visiting: " + rs.getString("staff_name") +
                        " | In: " + rs.getString("time_in"));
                anyIn = true;
            }
            if (!anyIn) {
                System.out.println("No visitors currently in the building.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching visitors: " + e.getMessage());
        }
    }

    public void signOut(int visitorId, String timeOut) {
        String sql = "UPDATE visitors SET time_out = ? WHERE id = ? AND time_out IS NULL";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, timeOut);
            stmt.setInt(2, visitorId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Signed out visitor ID: " + visitorId);
            } else {
                System.out.println("Visitor not found or already signed out.");
            }
        } catch (SQLException e) {
            System.out.println("Error signing out visitor: " + e.getMessage());
        }
    }

    public void searchByName(String name) {
        String sql = "SELECT v.id, v.full_name, v.time_in, v.time_out, s.name AS staff_name " +
                "FROM visitors v JOIN staff s ON v.staff_id = s.id " +
                "WHERE v.full_name LIKE ?";
        System.out.println("--- Search Results for '" + name + "' ---");
        boolean found = false;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String status = rs.getString("time_out") == null ? "Still in" : "Left at " + rs.getString("time_out");
                System.out.println(rs.getString("full_name") + " | Visiting: " + rs.getString("staff_name") +
                        " | In: " + rs.getString("time_in") + " | " + status);
                found = true;
            }
            if (!found) {
                System.out.println("No matching visitors found.");
            }
        } catch (SQLException e) {
            System.out.println("Error searching visitors: " + e.getMessage());
        }
    }
}