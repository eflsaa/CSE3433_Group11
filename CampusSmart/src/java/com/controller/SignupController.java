/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.controller;

/**
 *
 * @author User
 */
/*
 * Registration Controller - Handles user registration
 */

import com.dao.UserDAO;
import com.model.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SignupController", urlPatterns = {"/SignupController"})
public class SignupController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Show registration page
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("register".equals(action)) {
            handleSignup(request, response);
        } else {
            response.sendRedirect("SignupController");
        }
    }

    /**
     * Handle user registration
     */
    private void handleSignup(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String matricNumber = request.getParameter("matricNumber");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String course = request.getParameter("course");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate all inputs
        if (matricNumber == null || matricNumber.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your matric number");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your full name");
            request.setAttribute("matricNumber", matricNumber);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter your email address");
            request.setAttribute("matricNumber", matricNumber);
            request.setAttribute("name", name);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("errorMessage", "Please enter a valid email address");
            request.setAttribute("matricNumber", matricNumber);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (course == null || course.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please select your course");
            request.setAttribute("matricNumber", matricNumber);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Please enter a password");
            request.setAttribute("matricNumber", matricNumber);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("course", course);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("errorMessage", "Password must be at least 6 characters");
            request.setAttribute("matricNumber", matricNumber);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("course", course);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match");
            request.setAttribute("matricNumber", matricNumber);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("course", course);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Check if matric number already exists
        if (userDAO.matricExists(matricNumber.trim())) {
            request.setAttribute("errorMessage", "Matric number already registered");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("course", course);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Check if email already exists
        if (userDAO.emailExists(email.trim())) {
            request.setAttribute("errorMessage", "Email already registered");
            request.setAttribute("matricNumber", matricNumber);
            request.setAttribute("name", name);
            request.setAttribute("course", course);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Create new user
        user newUser = new user(matricNumber.trim(), password, name.trim(), email.trim(), course);
        
        if (userDAO.registerUser(newUser)) {
            // Registration successful
            request.setAttribute("successMessage", "Registration successful! Please login with your credentials.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Registration failed. Please try again.");
            request.setAttribute("matricNumber", matricNumber);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("course", course);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }
}
