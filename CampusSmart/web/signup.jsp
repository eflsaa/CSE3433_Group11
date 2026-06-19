<%-- 
    Document   : signup
    Created on : 8 Jun 2026, 7:28:20 pm
    Author     : User
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Campus Portal - Registration</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: 'Inter', sans-serif;
        }

        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 30px 20px;
        }

        .signup-container {
            max-width: 550px;
            width: 100%;
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
            overflow: hidden;
        }

        /* Gradient header inside the card panel */
        .signup-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 35px 30px;
            text-align: center;
            color: #ffffff;
        }

        .signup-header h1 {
            font-size: 26px;
            font-weight: 700;
            margin-bottom: 8px;
            letter-spacing: 0.5px;
        }

        .signup-header p {
            font-size: 14px;
            color: rgba(255, 255, 255, 0.85);
        }

        .signup-body {
            padding: 35px;
        }

        /* Form Feedback Alerts */
        .alert {
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 24px;
            font-size: 14px;
            font-weight: 500;
            text-align: center;
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #842029;
            border: 1px solid #f5c2c7;
        }

        /* Responsive Form grid spacing adjustments */
        .form-row {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
        }

        .form-group {
            margin-bottom: 22px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #475569;
            font-size: 14px;
        }

        .form-control, select {
            width: 100%;
            padding: 13px 16px;
            border: 1px solid #cbd5e1;
            border-radius: 10px;
            font-size: 15px;
            outline: none;
            color: #334155;
            background-color: #ffffff;
            transition: all 0.2s ease;
        }

        .form-control:focus, select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
        }

        /* Create Account Submission Button layout */
        .btn-register {
            width: 100%;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #ffffff;
            border: none;
            padding: 14px;
            font-size: 16px;
            font-weight: 600;
            border-radius: 10px;
            cursor: pointer;
            box-shadow: 0 4px 12px rgba(118, 75, 162, 0.25);
            transition: all 0.2s;
            margin-top: 10px;
        }

        .btn-register:hover {
            transform: translateY(-1px);
            box-shadow: 0 6px 18px rgba(118, 75, 162, 0.35);
            opacity: 0.95;
        }

        /* Footer configuration referencing backend route views */
        .signup-footer {
            text-align: center;
            padding: 24px 35px;
            background-color: #f8fafc;
            border-top: 1px solid #e2e8f0;
            font-size: 14px;
            color: #64748b;
        }

        .signup-footer a {
            color: #764ba2;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.2s;
        }

        .signup-footer a:hover {
            color: #667eea;
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <div class="signup-container">
        <div class="signup-header">
            <h1>Create Account</h1>
            <p>Join the Smart Campus system by registering your academic credentials below</p>
        </div>

        <div class="signup-body">
            <%-- Handle validation exception notifications from Controller validation checks --%>
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-danger">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>

            <form action="SignupController" method="POST">
                <input type="hidden" name="action" value="register">

                <div class="form-row">
                    <div class="form-group">
                        <label for="matricNumber">Matric Number</label>
                        <input type="text" id="matricNumber" name="matricNumber" class="form-control" placeholder="e.g. 241234" value="<%= request.getAttribute("matricNumber") != null ? request.getAttribute("matricNumber") : "" %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="name">Full Name</label>
                        <input type="text" id="name" name="name" class="form-control" placeholder="Your official name" value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>" required>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="email">Campus Email Address</label>
                        <input type="email" id="email" name="email" class="form-control" placeholder="name@campus.edu.my" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>
                    </div>

                    <div class="form-group">
                        <label for="course">Academic Course Program</label>
                        <select id="course" name="course" required>
                            <option value="Bachelor of Computer Science" <%= "Bachelor of Computer Science".equals(request.getAttribute("course")) ? "selected" : "" %>>Bachelor of Computer Science</option>
                            <option value="Bachelor of Information Technology" <%= "Bachelor of Information Technology".equals(request.getAttribute("course")) ? "selected" : "" %>>Bachelor of Information Technology</option>
                            <option value="Diploma in Computer Science" <%= "Diploma in Computer Science".equals(request.getAttribute("course")) ? "selected" : "" %>>Diploma in Computer Science</option>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="Minimum 6 characters" required>
                    </div>

                    <div class="form-group">
                        <label for="confirmPassword">Confirm Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Retype password" required>
                    </div>
                </div>

                <button type="submit" class="btn-register">Create Account</button>
            </form>
        </div>

        <div class="signup-footer">
            Already have an account? <a href="LoginController">Login Here</a>
        </div>
    </div>

</body>
</html>