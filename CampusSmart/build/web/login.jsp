<%-- 
    Document   : login
    Created on : 8 Jun 2026, 7:13:32 pm
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Smart Campus Portal - Login</title>
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
            padding: 20px;
        }

        .login-container {
            max-width: 450px;
            width: 100%;
            background: #ffffff;
            border-radius: 16px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
            overflow: hidden;
            transition: transform 0.2s;
        }

        /* Gradient header inside the card panel */
        .login-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 35px 30px;
            text-align: center;
            color: #ffffff;
        }

        .login-header h1 {
            font-size: 26px;
            font-weight: 700;
            margin-bottom: 8px;
            letter-spacing: 0.5px;
        }

        .login-header p {
            font-size: 14px;
            color: rgba(255, 255, 255, 0.85);
        }

        .login-body {
            padding: 40px 35px;
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
        .alert-success {
            background-color: #d1e7dd;
            color: #0f5132;
            border: 1px solid #badbcc;
        }

        /* Form Layout Elements */
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

        .form-control {
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

        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.15);
        }

        /* Primary Action Login Button */
        .btn-login {
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

        .btn-login:hover {
            transform: translateY(-1px);
            box-shadow: 0 6px 18px rgba(118, 75, 162, 0.35);
            opacity: 0.95;
        }

        /* Clean Secondary Link Footer Section */
        .login-footer {
            text-align: center;
            padding: 24px 35px;
            background-color: #f8fafc;
            border-top: 1px solid #e2e8f0;
            font-size: 14px;
            color: #64748b;
        }

        .login-footer a {
            color: #764ba2;
            text-decoration: none;
            font-weight: 600;
            transition: color 0.2s;
        }

        .login-footer a:hover {
            color: #667eea;
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <div class="login-container">
        <div class="login-header">
            <h1>Welcome Back</h1>
            <p>Please log in with your credentials to access your workspace</p>
        </div>

        <div class="login-body">
            <%-- Handle Invalid Authorization Feedback Alerts --%>
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="alert alert-danger">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>

            <%-- Handle Success Messages (e.g. redirected from registration step) --%>
            <% if (request.getAttribute("successMessage") != null) { %>
                <div class="alert alert-success">
                    <%= request.getAttribute("successMessage") %>
                </div>
            <% } %>

            <form action="LoginController" method="POST">
                <input type="hidden" name="action" value="login">

                <div class="form-group">
                    <label for="matricNumber">Matric Number</label>
                    <input type="text" 
                           id="matricNumber" 
                           name="matricNumber" 
                           class="form-control" 
                           placeholder="e.g., 241234"
                           value="<%= request.getAttribute("matricNumber") != null ? request.getAttribute("matricNumber") : "" %>" 
                           required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" 
                           id="password" 
                           name="password" 
                           class="form-control" 
                           placeholder="Enter your security password" 
                           required>
                </div>

                <button type="submit" class="btn-login">Sign In</button>
            </form>
        </div>

        <div class="login-footer">
            New user? <a href="signup.jsp">Create an account here</a>
        </div>
    </div>

</body>
</html>