package com.dao;

import com.model.Facility;
import com.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacilityDAO {

    private static final String MODULE_A = "A";

    public List<Facility> getAllFacilities() throws ClassNotFoundException {
        List<Facility> facilities = new ArrayList<>();
        String sql = "SELECT * FROM facilities WHERE is_active = true ORDER BY name";
        
        try (Connection conn = DBConnection.getConnection(MODULE_A);  // ← Already correct
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Facility facility = new Facility();
                facility.setId(rs.getInt("id"));
                facility.setName(rs.getString("name"));
                facility.setDescription(rs.getString("description"));
                facility.setCapacity(rs.getInt("capacity"));
                facility.setLocation(rs.getString("location"));
                facility.setActive(rs.getBoolean("is_active"));
                facility.setCreatedAt(rs.getTimestamp("created_at"));
                facilities.add(facility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facilities;
    }

    public Facility getFacilityById(int id) throws ClassNotFoundException {
        String sql = "SELECT * FROM facilities WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection(MODULE_A);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Facility facility = new Facility();
                facility.setId(rs.getInt("id"));
                facility.setName(rs.getString("name"));
                facility.setDescription(rs.getString("description"));
                facility.setCapacity(rs.getInt("capacity"));
                facility.setLocation(rs.getString("location"));
                facility.setActive(rs.getBoolean("is_active"));
                facility.setCreatedAt(rs.getTimestamp("created_at"));
                return facility;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}