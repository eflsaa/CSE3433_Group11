package com.dao;

import com.model.Booking;
import com.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private String generateBookingReference() {
        return "BK" + System.currentTimeMillis() + (int) (Math.random() * 1000);
    }

    public boolean insertBooking(Booking booking) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO bookings (booking_reference, student_matric_number, facility_id, "
                + "booking_date, start_time, end_time, purpose, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection("A");  // ← Explicit Module A
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            booking.setBookingReference(generateBookingReference());

            pstmt.setString(1, booking.getBookingReference());
            pstmt.setString(2, booking.getStudentMatricNumber());
            pstmt.setInt(3, booking.getFacilityId());
            pstmt.setDate(4, booking.getBookingDate());
            pstmt.setTime(5, booking.getStartTime());
            pstmt.setTime(6, booking.getEndTime());
            pstmt.setString(7, booking.getPurpose());
            pstmt.setString(8, booking.getStatus());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    public List<Booking> getAllBookings() throws ClassNotFoundException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, f.name as facility_name FROM bookings b "
                + "JOIN facilities f ON b.facility_id = f.id "
                + "ORDER BY b.booking_date DESC, b.start_time DESC";

        try (Connection conn = DBConnection.getConnection("A");  // ← Explicit Module A
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setBookingReference(rs.getString("booking_reference"));
                booking.setStudentMatricNumber(rs.getString("student_matric_number"));
                booking.setFacilityId(rs.getInt("facility_id"));
                booking.setFacilityName(rs.getString("facility_name"));
                booking.setBookingDate(rs.getDate("booking_date"));
                booking.setStartTime(rs.getTime("start_time"));
                booking.setEndTime(rs.getTime("end_time"));
                booking.setPurpose(rs.getString("purpose"));
                booking.setStatus(rs.getString("status"));
                booking.setCreatedAt(rs.getTimestamp("created_at"));
                booking.setUpdatedAt(rs.getTimestamp("updated_at"));
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public List<Booking> getBookingsByStudent(String matricNumber) throws ClassNotFoundException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, f.name as facility_name FROM bookings b "
                + "JOIN facilities f ON b.facility_id = f.id "
                + "WHERE b.student_matric_number = ? "
                + "ORDER BY b.booking_date DESC, b.start_time DESC";

        try (Connection conn = DBConnection.getConnection("A");  // ← Explicit Module A
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setBookingReference(rs.getString("booking_reference"));
                booking.setStudentMatricNumber(rs.getString("student_matric_number"));
                booking.setFacilityId(rs.getInt("facility_id"));
                booking.setFacilityName(rs.getString("facility_name"));
                booking.setBookingDate(rs.getDate("booking_date"));
                booking.setStartTime(rs.getTime("start_time"));
                booking.setEndTime(rs.getTime("end_time"));
                booking.setPurpose(rs.getString("purpose"));
                booking.setStatus(rs.getString("status"));
                booking.setCreatedAt(rs.getTimestamp("created_at"));
                booking.setUpdatedAt(rs.getTimestamp("updated_at"));
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean updateBookingStatus(int bookingId, String status) throws ClassNotFoundException {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection("A");  // ← Explicit Module A
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelBooking(int bookingId) throws ClassNotFoundException {
        return updateBookingStatus(bookingId, "cancelled");
    }

    /**
     * Simplified overlap check using standard interval logic.
     */
    public boolean isFacilityAvailable(int facilityId, Date date, Time startTime, Time endTime) throws ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE facility_id = ? "
                + "AND booking_date = ? AND status != 'cancelled' "
                + "AND start_time < ? AND end_time > ?";

        try (Connection conn = DBConnection.getConnection("A");  // ← Explicit Module A
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facilityId);
            pstmt.setDate(2, date);
            pstmt.setTime(3, endTime);    // new end time
            pstmt.setTime(4, startTime);  // new start time

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}