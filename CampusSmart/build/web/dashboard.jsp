<%-- 
    Document   : dashboard
    Created on : Jun 8, 2026, 6:31:12 PM
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.Booking" %>
<%@ page import="com.model.Complaint" %>
<%@ page import="com.model.Facility" %>
<%
    // Session Guard Check
    if (session.getAttribute("stuMatric") == null) {
        response.sendRedirect("LoginController");
        return;
    }

    // Determine active layout tab context based on request attribute flags or fallbacks
    String currentTab = (request.getAttribute("activeTab") != null) ? (String) request.getAttribute("activeTab") : "dashboard";

    // Get lists with null safety
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookingsList");
    List<Complaint> complaints = (List<Complaint>) request.getAttribute("complaintsList");
    List<Facility> facilities = (List<Facility>) request.getAttribute("facilitiesList");

    // Ensure lists are never null in the JSP
    if (bookings == null) {
        bookings = new java.util.ArrayList<>();
    }
    if (complaints == null) {
        complaints = new java.util.ArrayList<>();
    }
    if (facilities == null)
        facilities = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Smart Campus Dashboard</title>
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
                font-family: 'Inter', sans-serif;
            }

            body {
                background-color: #f3f4f6;
                color: #333;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }

            /* --- GRADIENT HEADER NAVIGATION NAVBAR --- */
            header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                box-shadow: 0 4px 12px rgba(118, 75, 162, 0.2);
                position: sticky;
                top: 0;
                z-index: 1000;
            }

            .nav-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                height: 75px;
            }

            .logo {
                font-size: 24px;
                font-weight: 700;
                color: #ffffff;
                text-decoration: none;
                display: flex;
                align-items: center;
                gap: 8px;
                letter-spacing: 0.5px;
            }

            .nav-right-container {
                display: flex;
                align-items: center;
                gap: 20px;
            }

            .nav-tabs {
                display: flex;
                list-style: none;
                gap: 6px;
                align-items: center;
                background: rgba(255, 255, 255, 0.1);
                padding: 5px;
                border-radius: 30px;
                border: 1px solid rgba(255, 255, 255, 0.15);
            }

            .nav-tabs a {
                text-decoration: none;
                font-size: 14px;
                color: rgba(255, 255, 255, 0.85);
                font-weight: 500;
                padding: 8px 18px;
                border-radius: 20px;
                transition: all 0.25s ease;
            }

            .nav-tabs a:hover {
                color: #ffffff;
                background: rgba(255, 255, 255, 0.1);
            }

            .nav-tabs a.active {
                background-color: #ffffff;
                color: #764ba2;
                font-weight: 600;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
            }

            /* Logout Utility */
            .logout-form {
                display: inline;
                border-left: 1px solid rgba(255, 255, 255, 0.2);
                padding-left: 15px;
            }

            .btn-logout {
                background: rgba(255, 255, 255, 0.15);
                border: 1px solid rgba(255, 255, 255, 0.25);
                color: #ffffff;
                padding: 7px 16px;
                border-radius: 20px;
                cursor: pointer;
                font-size: 13px;
                font-weight: 500;
                transition: all 0.2s;
            }

            .btn-logout:hover {
                background-color: #ffffff;
                color: #d93838;
                border-color: #ffffff;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            /* --- MAIN WORKSPACE --- */
            main {
                flex: 1;
                max-width: 1000px;
                width: 100%;
                margin: 40px auto;
                padding: 0 20px;
            }

            .dashboard-title {
                text-align: center;
                font-size: 32px;
                font-weight: 700;
                margin-bottom: 30px;
                color: #212529;
            }

            /* Message Banner Box alerts */
            .alert {
                padding: 12px 16px;
                border-radius: 8px;
                margin-bottom: 24px;
                font-size: 15px;
                font-weight: 500;
                text-align: center;
            }
            .alert-danger {
                background-color: #f8d7da;
                color: #842029;
                border: 1px solid #f5c2c7;
            }
            .alert-success {
                background-color: #d1e7dd;
                color: #0f5132;
                border: 1px solid #badbcc;
            }

            /* Card Frame Styling */
            .card {
                background: #ffffff;
                border-radius: 16px;
                box-shadow: 0 10px 25px rgba(118, 75, 162, 0.06);
                overflow: hidden;
                border: 1px solid #e2e8f0;
            }

            .card-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 20px 26px;
                font-size: 20px;
                font-weight: 600;
                letter-spacing: 0.3px;
            }

            .card-body {
                padding: 28px;
            }

            /* PROFILE SPECIFIC STYLES */
            .profile-container {
                display: flex;
                align-items: center;
                gap: 35px;
                padding: 10px 0;
            }

            @media (max-width: 600px) {
                .profile-container {
                    flex-direction: column;
                    text-align: center;
                }
            }

            .profile-avatar-large {
                width: 130px;
                height: 130px;
                border-radius: 50%;
                border: 4px solid #f0f4f8;
                box-shadow: 0 4px 15px rgba(118, 75, 162, 0.1);
            }

            .profile-details {
                flex: 1;
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
                gap: 20px;
            }

            .info-field {
                background: #f8fafc;
                padding: 16px 20px;
                border-radius: 10px;
                border: 1px solid #e2e8f0;
                transition: all 0.2s;
            }

            .info-field:hover {
                border-color: #667eea;
                background: #f1f5f9;
            }

            .info-label {
                font-size: 11px;
                text-transform: uppercase;
                letter-spacing: 0.8px;
                color: #64748b;
                font-weight: 700;
                margin-bottom: 5px;
            }

            .info-value {
                font-size: 16px;
                font-weight: 600;
                color: #1e293b;
            }

            /* Layout Forms Form Elements */
            .form-group {
                margin-bottom: 22px;
            }

            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: 600;
                color: #475569;
                font-size: 15px;
            }

            textarea, select, input[type="date"], input[type="time"] {
                width: 100%;
                padding: 13px;
                border: 1px solid #cbd5e1;
                border-radius: 10px;
                font-size: 15px;
                outline: none;
                background-color: #ffffff;
                color: #334155;
                transition: all 0.2s;
            }

            textarea {
                height: 130px;
                resize: vertical;
            }

            textarea:focus, select:focus, input:focus {
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
            }

            .btn-submit {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border: none;
                padding: 13px 28px;
                font-size: 15px;
                font-weight: 600;
                border-radius: 8px;
                cursor: pointer;
                box-shadow: 0 4px 10px rgba(118, 75, 162, 0.2);
                transition: all 0.2s;
            }

            .btn-submit:hover {
                transform: translateY(-1px);
                box-shadow: 0 6px 14px rgba(118, 75, 162, 0.3);
                opacity: 0.95;
            }

            /* Records History Elements */
            .records-section {
                margin-top: 35px;
                border-top: 1px solid #e2e8f0;
                padding-top: 26px;
            }

            .records-title {
                font-size: 19px;
                font-weight: 600;
                margin-bottom: 18px;
                color: #1e293b;
            }

            .records-list {
                display: flex;
                flex-direction: column;
                gap: 12px;
            }

            .record-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 16px 20px;
                background-color: #f8fafc;
                border: 1px solid #e2e8f0;
                border-radius: 10px;
            }

            .record-info {
                display: flex;
                flex-direction: column;
                gap: 4px;
            }

            .record-meta {
                font-size: 13px;
                color: #64748b;
            }

            .record-facility {
                font-weight: 600;
                color: #1e293b;
            }

            .status-badge {
                padding: 5px 14px;
                border-radius: 20px;
                font-size: 13px;
                font-weight: 600;
            }

            .status-pending {
                background-color: #fef3c7;
                color: #d97706;
            }

            .status-approved {
                background-color: #dcfce7;
                color: #15803d;
            }

            .status-rejected {
                background-color: #fee2e2;
                color: #dc2626;
            }

            .status-cancelled {
                background-color: #e5e7eb;
                color: #6b7280;
            }

            .status-completed {
                background-color: #dbeafe;
                color: #2563eb;
            }

            .empty-message {
                color: #64748b;
                font-style: italic;
                padding: 10px 5px;
                text-align: center;
            }

            /* Standard Empty Layout Container Placeholders */
            .placeholder-box {
                background: white;
                padding: 60px 40px;
                text-align: center;
                border-radius: 16px;
                border: 2px dashed #cbd5e1;
                color: #64748b;
                box-shadow: 0 10px 25px rgba(118, 75, 162, 0.03);
            }

            .placeholder-box h2 {
                color: #1e293b;
                margin-bottom: 8px;
            }

            /* Grid layout for forms */
            .form-row {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
            }

            @media (max-width: 600px) {
                .form-row {
                    grid-template-columns: 1fr;
                }
            }

            /* --- FOOTER REGION --- */
            footer {
                background-color: #1e1b4b;
                color: #94a3b8;
                padding: 26px 0;
                font-size: 14px;
                text-align: center;
                border-top: 4px solid #764ba2;
                margin-top: auto;
            }

            footer a {
                color: #ffffff;
                text-decoration: none;
                margin: 0 12px;
                transition: color 0.2s;
            }

            footer a:hover {
                color: #667eea;
                text-decoration: underline;
            }
        </style>
    </head>
    <body>

        <header>
            <div class="nav-container">
                <a href="CampusController" class="logo">🏫 SmartCampus</a>

                <div class="nav-right-container">
                    <nav>
                        <ul class="nav-tabs">
                            <li><a href="CampusController?viewTab=dashboard" class="<%= "dashboard".equals(currentTab) ? "active" : ""%>">Dashboard</a></li>
                            <li><a href="CampusController?viewTab=booking" class="<%= "booking".equals(currentTab) ? "active" : ""%>">Booking</a></li>
                            <li><a href="CampusController?viewTab=report" class="<%= "report".equals(currentTab) ? "active" : ""%>">Report</a></li>
                            <li><a href="CampusController?viewTab=announcement" class="<%= "announcement".equals(currentTab) ? "active" : ""%>">Announcement</a></li>
                            <li><a href="CampusController?viewTab=profile" class="<%= "profile".equals(currentTab) ? "active" : ""%>">Profile</a></li>
                        </ul>
                    </nav>

                    <form action="LoginController" method="POST" class="logout-form">
                        <input type="hidden" name="action" value="logout">
                        <button type="submit" class="btn-logout">Logout</button>
                    </form>
                </div>
            </div>
        </header>

        <main>
            <h1 class="dashboard-title">Smart Campus Portal</h1>

            <%-- Display success or error messages --%>
            <% if (request.getAttribute("successMessage") != null) {%>
            <div class="alert alert-success">
                <%= request.getAttribute("successMessage")%>
            </div>
            <% } %>
            <% if (request.getAttribute("errorMessage") != null) {%>
            <div class="alert alert-danger">
                <%= request.getAttribute("errorMessage")%>
            </div>
            <% } %>

            <%-- VIEW MODE 1: DASHBOARD CONTEXT --%>
            <% if ("dashboard".equals(currentTab)) {%>
            <div class="placeholder-box">
                <h2>Welcome back, <%= session.getAttribute("stuName")%>!</h2>
                <p>Use the top-right navbar menu tabs to configure your campus services workspace.</p>
            </div>
            <% } %>

            <%-- VIEW MODE 2: BOOKING WORKSPACE LAYOUT PANELS --%>
            <% if ("booking".equals(currentTab)) { %>
            <div class="card">
                <div class="card-header">Facility Booking Reservation</div>
                <div class="card-body">
                    <form action="CampusController" method="POST">
                        <input type="hidden" name="formAction" value="submitBooking">

                        <div class="form-group">
                            <label for="facilityId">Select Facility</label>
                            <select name="facilityId" id="facilityId" required>
                                <option value="">-- Select a facility --</option>
                                <%
                                    if (facilities != null && !facilities.isEmpty()) {
                                        for (Facility f : facilities) {
                                %>
                                <option value="<%= f.getId()%>"><%= f.getName()%> - <%= f.getLocation()%> (Capacity: <%= f.getCapacity()%>)</option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="date">Reservation Date</label>
                                <input type="date" name="date" id="date" required>
                            </div>
                            <div class="form-group">
                                <label for="startTime">Start Time</label>
                                <input type="time" name="startTime" id="startTime" required step="1800">
                            </div>
                            <div class="form-group">
                                <label for="endTime">End Time</label>
                                <input type="time" name="endTime" id="endTime" required step="1800">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="purpose">Purpose of Booking</label>
                            <textarea id="purpose" name="purpose" placeholder="Describe the purpose of this booking..." required></textarea>
                        </div>

                        <button type="submit" class="btn-submit">Confirm Booking</button>
                    </form>

                    <div class="records-section">
                        <h2 class="records-title">Your Confirmed Bookings</h2>
                        <div class="records-list">
                            <%
                                String userMatric = (String) session.getAttribute("stuMatric");
                                boolean hasUserBookings = false;

                                if (bookings != null && !bookings.isEmpty()) {
                                    for (Booking b : bookings) {
                                        if (b.getStudentMatricNumber() != null && b.getStudentMatricNumber().equals(userMatric)) {
                                            hasUserBookings = true;
                            %>
                            <div class="record-item">
                                <div class="record-info">
                                    <span class="record-facility"><%= b.getFacilityName()%></span>
                                    <span class="record-meta">
                                        Ref: <%= b.getBookingReference()%> | 
                                        Date: <%= b.getBookingDate()%> | 
                                        Time: <%= b.getStartTime()%> - <%= b.getEndTime()%>
                                        <% if (b.getPurpose() != null && !b.getPurpose().isEmpty()) {%>
                                        | Purpose: <%= b.getPurpose()%>
                                        <% }%>
                                    </span>
                                </div>
                                <span class="status-badge status-<%= b.getStatus() != null ? b.getStatus().toLowerCase() : "pending"%>">
                                    <%= b.getStatus() != null ? b.getStatus() : "Pending"%>
                                </span>
                            </div>
                            <%
                                        }
                                    }
                                }

                                if (!hasUserBookings) {
                            %>
                            <p class="empty-message">No active facility booking records found.</p>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>

            <%-- VIEW MODE 3: REPORT MANAGEMENT --%>
            <% if ("report".equals(currentTab)) { %>
            <div class="card">
                <div class="card-header">Complaint Management</div>
                <div class="card-body">
                    <form action="CampusController" method="POST">
                        <input type="hidden" name="formAction" value="submitComplaint">
                        <div class="form-group">
                            <label for="complaintDescription">Complaint Description</label>
                            <textarea id="complaintDescription" name="description" placeholder="Describe the maintenance issue or concern in detail..." required></textarea>
                        </div>
                        <button type="submit" class="btn-submit">Submit Complaint</button>
                    </form>

                    <div class="records-section">
                        <h2 class="records-title">Your Complaint Records</h2>
                        <div class="records-list">
                            <%
                                if (complaints != null && !complaints.isEmpty()) {
                                    for (Complaint c : complaints) {
                            %>
                            <div class="record-item">
                                <div class="record-info">
                                    <span class="record-facility"><%= c.getDescription()%></span>
                                    <span class="record-meta">Status: <%= c.getStatus() != null ? c.getStatus() : "Pending"%></span>
                                </div>
                                <span class="status-badge status-<%= c.getStatus() != null ? c.getStatus().toLowerCase() : "pending"%>">
                                    <%= c.getStatus() != null ? c.getStatus() : "Pending"%>
                                </span>
                            </div>
                            <%
                                }
                            } else {
                            %>
                            <p class="empty-message">No complaint records found.</p>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>

            <%-- VIEW MODE 4: ANNOUNCEMENTS WORKSPACE PANELS --%>
            <% if ("announcement".equals(currentTab)) { %>
            <div class="placeholder-box">
                <h2>Campus Notices & Announcements</h2>
                <p style="margin-top: 12px;">No urgent administrative broadcast announcements posted at this time.</p>
            </div>
            <% } %>

            <%-- VIEW MODE 5: STUDENT PROFILE --%>
            <% if ("profile".equals(currentTab)) {%>
            <div class="card">
                <div class="card-header">Student Profile Account</div>
                <div class="card-body">
                    <div class="profile-container">
                        <div>
                            <img src="<%= session.getAttribute("stuImg") != null ? session.getAttribute("stuImg") : "https://ui-avatars.com/api/?name=" + session.getAttribute("stuName") + "&background=667eea&color=fff"%>" 
                                 alt="Student Profile Avatar" class="profile-avatar-large">
                        </div>
                        <div class="profile-details">
                            <div class="info-field">
                                <div class="info-label">Full Name</div>
                                <div class="info-value"><%= session.getAttribute("stuName") != null ? session.getAttribute("stuName") : "Not set"%></div>
                            </div>
                            <div class="info-field">
                                <div class="info-label">Matric Number</div>
                                <div class="info-value"><%= session.getAttribute("stuMatric") != null ? session.getAttribute("stuMatric") : "Not set"%></div>
                            </div>
                            <div class="info-field">
                                <div class="info-label">Registered Email</div>
                                <div class="info-value"><%= session.getAttribute("stuEmail") != null ? session.getAttribute("stuEmail") : "Not set"%></div>
                            </div>
                            <div class="info-field">
                                <div class="info-label">Academic Course</div>
                                <div class="info-value"><%= session.getAttribute("stuCourse") != null ? session.getAttribute("stuCourse") : "Not set"%></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <% }%>
        </main>

        <footer>
            <p>&copy; 2026 Smart Campus Management System. All Rights Reserved.</p>
            <p style="margin-top: 8px; font-size: 13px;">
                <a href="#privacy">Privacy Policy</a> | 
                <a href="#terms">Terms of Service</a> | 
                <a href="#support">IT Central Helpdesk Support</a>
            </p>
        </footer>

    </body>
</html>