package com.controller;

import com.dao.BookingDAO;
import com.dao.ComplaintDAO;
import com.dao.FacilityDAO;
import com.model.Booking;
import com.model.Complaint;
import com.model.Facility;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CampusController", urlPatterns = {"/CampusController"})
public class CampusController extends HttpServlet {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final FacilityDAO facilityDAO = new FacilityDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("bookingsList", bookingDAO.getAllBookings());
            request.setAttribute("facilitiesList", facilityDAO.getAllFacilities());

            HttpSession session = request.getSession();
            String matricNo = (String) session.getAttribute("stuMatric");

            if (matricNo != null && !matricNo.isEmpty()) {
                request.setAttribute(
                        "complaintsList",
                        complaintDAO.getComplaintsByStudent(matricNo)
                );
            }

            String viewTab = request.getParameter("viewTab");
            if (viewTab != null && !viewTab.trim().isEmpty()) {
                request.setAttribute("activeTab", viewTab);
            } else {
                String existingTab = (String) request.getAttribute("activeTab");
                if (existingTab == null || existingTab.trim().isEmpty()) {
                    request.setAttribute("activeTab", "dashboard");
                }
            }

            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            
        } catch (ClassNotFoundException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String formAction = request.getParameter("formAction");

        if (null == formAction) {
            doGet(request, response);
        } else switch (formAction) {
            case "submitBooking" -> {
                try {
                    handleBookingSubmission(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(CampusController.class.getName()).log(Level.SEVERE, null, ex);
                    // Set user-friendly message and forward back
                    request.setAttribute("errorMessage", "Database error while submitting booking: " + ex.getMessage());
                    request.setAttribute("activeTab", "booking");
                    doGet(request, response);
                }
            }
            case "submitComplaint" -> handleComplaintSubmission(request, response);
            default -> doGet(request, response);
        }
    }

    private void handleBookingSubmission(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        try {
            HttpSession session = request.getSession();
            String matricNo = (String) session.getAttribute("stuMatric");

            if (matricNo == null || matricNo.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Please login to make a booking.");
                request.setAttribute("activeTab", "booking");
                doGet(request, response);
                return;
            }

            String facilityIdStr = request.getParameter("facilityId");
            String dateStr = request.getParameter("date");
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            String purpose = request.getParameter("purpose");

            if (facilityIdStr == null || dateStr == null || startTimeStr == null || 
                endTimeStr == null || purpose == null || purpose.trim().isEmpty()) {
                request.setAttribute("errorMessage", "All fields are required.");
                request.setAttribute("activeTab", "booking");
                doGet(request, response);
                return;
            }

            try {
                int facilityId = Integer.parseInt(facilityIdStr);
                Date bookingDate = Date.valueOf(dateStr);
                Time startTime = Time.valueOf(startTimeStr + ":00");
                Time endTime = Time.valueOf(endTimeStr + ":00");

                if (endTime.before(startTime) || endTime.equals(startTime)) {
                    request.setAttribute("errorMessage", "End time must be after start time.");
                    request.setAttribute("activeTab", "booking");
                    doGet(request, response);
                    return;
                }

                if (!bookingDAO.isFacilityAvailable(facilityId, bookingDate, startTime, endTime)) {
                    request.setAttribute("errorMessage", "This facility is already booked for the selected time slot.");
                    request.setAttribute("activeTab", "booking");
                    doGet(request, response);
                    return;
                }

                Booking newBooking = new Booking();
                newBooking.setStudentMatricNumber(matricNo);
                newBooking.setFacilityId(facilityId);
                newBooking.setBookingDate(bookingDate);
                newBooking.setStartTime(startTime);
                newBooking.setEndTime(endTime);
                newBooking.setPurpose(purpose);
                newBooking.setStatus("pending");

                boolean isSaved = bookingDAO.insertBooking(newBooking);

                if (isSaved) {
                    request.setAttribute("successMessage", "Booking submitted successfully!");
                } else {
                    request.setAttribute("errorMessage", "Failed to submit booking. Please try again.");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid facility selection.");
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorMessage", "Invalid date or time format.");
            } catch (SQLException e) {
                // Catch SQLException and rethrow to be handled by doPost
                throw e;
            }

            request.setAttribute("activeTab", "booking");
            doGet(request, response);

        } catch (ClassNotFoundException e) {
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void handleComplaintSubmission(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String matricNo = (String) session.getAttribute("stuMatric");
        if (matricNo == null || matricNo.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please login to submit a complaint.");
            request.setAttribute("activeTab", "report");
            doGet(request, response);
            return;
        }
        String description = request.getParameter("description");
        if (description != null && !description.trim().isEmpty()) {
            Complaint newComplaintObj = new Complaint(
                    matricNo,
                    description.trim(),
                    "Pending"
            );
            
            boolean isSaved = complaintDAO.insertComplaint(newComplaintObj);
            
            if (isSaved) {
                request.setAttribute("successMessage", "Complaint submitted successfully!");
            } else {
                request.setAttribute("errorMessage", "Failed to submit complaint. Please try again.");
            }
        } else {
            request.setAttribute("errorMessage", "Please provide a description for your complaint.");
        }
        request.setAttribute("activeTab", "report");
        doGet(request, response);
    }
}