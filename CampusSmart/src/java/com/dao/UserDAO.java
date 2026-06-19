package com.dao;

import com.model.user; 
import com.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDAO {
    
    private static final String MODULE_B = "B";
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }
    
    public user authenticateUser(String matricNumber, String password) {
        user userObj = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection(MODULE_B);
            String hashedPassword = hashPassword(password);
            String sql = "SELECT * FROM users WHERE matric_number=? AND password=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, matricNumber);
            ps.setString(2, hashedPassword);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                userObj = new user(
                    rs.getString("matric_number"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("course")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return userObj;
    }
    
    public boolean registerUser(user userObj) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getConnection(MODULE_B);
            String hashedPassword = hashPassword(userObj.getPassword());
            String sql = "INSERT INTO users (matric_number, password, name, email, course) VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userObj.getMatricNumber());
            ps.setString(2, hashedPassword);
            ps.setString(3, userObj.getName());
            ps.setString(4, userObj.getEmail());
            ps.setString(5, userObj.getCourse());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(null, ps, conn);
        }
    }
    
    public user getUserByMatric(String matricNumber) {
        user userObj = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection(MODULE_B);
            String sql = "SELECT * FROM users WHERE matric_number=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, matricNumber);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                userObj = new user(
                    rs.getString("matric_number"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("course")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return userObj;
    }
    
    public boolean matricExists(String matricNumber) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection(MODULE_B);
            String sql = "SELECT matric_number FROM users WHERE matric_number=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, matricNumber);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    public boolean emailExists(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection(MODULE_B);
            String sql = "SELECT email FROM users WHERE email=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(rs, ps, conn);
        }
    }
    
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        DBConnection.closeConnection(conn);
    }
}