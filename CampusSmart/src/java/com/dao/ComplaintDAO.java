package com.dao;

import com.model.Complaint;
import com.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {
    
    private static final String MODULE_A = "A";
    
    public List<Complaint> getComplaintsByStudent(String matricNo) {
        List<Complaint> complaintList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection(MODULE_A);
            // Select only columns that actually exist in the table
            String sql = "SELECT complaint_id, matric_no, description, status FROM complaints WHERE matric_no = ? ORDER BY complaint_id DESC";
            ps = conn.prepareStatement(sql);
            ps.setString(1, matricNo);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                // Use constructor without timestamp, then set ID separately
                Complaint complaint = new Complaint(
                    rs.getString("matric_no"),
                    rs.getString("description"),
                    rs.getString("status")
                );
                complaint.setComplaintId(rs.getInt("complaint_id"));
                complaintList.add(complaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return complaintList;
    }
    
    public boolean insertComplaint(Complaint complaint) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getConnection(MODULE_A);
            String sql = "INSERT INTO complaints(matric_no, description, status) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, complaint.getMatricNo());
            ps.setString(2, complaint.getDescription());
            ps.setString(3, complaint.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, ps, conn);
        }
    }
    
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        DBConnection.closeConnection(conn);
    }
}