/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

/**
 *
 * @author User
 */
import com.dao.UserDAO;
import com.model.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession();
        if (session.getAttribute("stuMatric") != null) {
            // Redirect to dashboard if already logged in
            response.sendRedirect("CampusController");
            return;
        }

        // Show login page
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            handleLogin(request, response);
        } else if ("logout".equals(action)) {
            handleLogout(request, response);
        } else {
            response.sendRedirect("LoginController");
        }
    }

    /**
     * Handle user login process
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String matricNumber = request.getParameter("matricNumber");
        String password = request.getParameter("password");

        // Validate input
        if (matricNumber == null || matricNumber.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter matric number and password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        user user = userDAO.authenticateUser(matricNumber.trim(), password);

        if (user != null) {
    // Login successful - set session attributes
    HttpSession session = request.getSession();
    session.setAttribute("stuMatric", user.getMatricNumber());
    session.setAttribute("stuName", user.getName());
    session.setAttribute("stuEmail", user.getEmail());
    session.setAttribute("stuCourse", user.getCourse());
    session.setAttribute("stuImg", "https://ui-avatars.com/api/?name=" + 
        user.getName().replace(" ", "+") + "&background=667eea&color=fff&size=128");
    session.setMaxInactiveInterval(30 * 60);

    // FIX: Redirect straight to the CampusController servlet map, NOT dashboard.jsp directly
    response.sendRedirect("CampusController");
} else {
            // Login failed
            request.setAttribute("errorMessage", "Invalid matric number or password");
            request.setAttribute("matricNumber", matricNumber);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

/**
 * Handle user logout
 */
private void handleLogout(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    
    HttpSession session = request.getSession();
    session.invalidate(); // Clear out all student session attributes cache
    
    // CHANGE THIS: Send them back to login instead of signup.jsp
    response.sendRedirect("LoginController"); 
}
}
