<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administration - Doctor Appointment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <style>
        /* Admin color palette */
        :root {
            --admin-primary: #1E293B;
            --admin-secondary: #334155;
            --admin-accent: #E11D48;
            --admin-bg-light: #F8FAFC;
            --admin-text-dark: #0F172A;
        }

        body {
            font-family: 'Poppins', 'Roboto', 'Helvetica Neue', sans-serif;
            background-color: var(--admin-bg-light);
        }

        /* Header background */
        .navbar-custom {
            background: linear-gradient(135deg, var(--admin-primary), var(--admin-secondary));
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        /* Welcome text */
        .welcome-text {
            font-size: 1rem;
            font-weight: 500;
            color: white;
        }

        /* Navigation buttons */
        .nav-btn {
            background: rgba(255, 255, 255, 0.1);
            border: none;
            color: white;
            padding: 8px 20px;
            border-radius: 30px;
            font-weight: 500;
            margin: 0 5px;
            transition: all 0.2s ease;
            text-decoration: none;
            display: inline-block;
            font-size: 0.9rem;
        }

        .nav-btn i {
            margin-right: 8px;
        }

        .nav-btn:hover {
            background: rgba(255, 255, 255, 0.25);
            transform: translateY(-2px);
        }

        .nav-btn-active {
            background: var(--admin-accent);
            color: white;
        }

        /* Profile button */
        .profile-btn {
            background: rgba(255, 255, 255, 0.2);
            border: none;
            color: white;
            padding: 8px 16px;
            border-radius: 30px;
            font-size: 0.85rem;
            transition: all 0.2s ease;
        }

        .profile-btn:hover {
            background: rgba(255, 255, 255, 0.35);
        }
    </style>
</head>
<body class="vh-100 d-flex flex-column overflow-hidden">

<nav class="navbar navbar-expand-lg flex-shrink-0 navbar-custom">
    <div class="container-fluid">
        <!-- Left side: welcome message -->
        <div class="navbar-brand">
            <span class="welcome-text">
                <i class="fad fa-shield-alt me-2"></i>
                Administration
            </span>
        </div>

        <!-- Center: navigation buttons -->
        <div class="mx-auto">
            <%
            String currentPage = (String) session.getAttribute("page_admin");
            if (currentPage == null) currentPage = "medecin";
            %>
            <a href="${pageContext.request.contextPath}/admin?action=medecin" class="nav-btn <%= currentPage.equals("medecin") ? "nav-btn-active" : "" %>">
            <i class="fad fa-user-md me-2"></i> Médecins
            </a>
            <a href="${pageContext.request.contextPath}/admin?action=patient" class="nav-btn <%= currentPage.equals("patient") ? "nav-btn-active" : "" %>">
            <i class="fad fa-users me-2"></i> Patients
            </a>
            <a href="${pageContext.request.contextPath}/admin?action=rdv" class="nav-btn <%= currentPage.equals("rdv") ? "nav-btn-active" : "" %>">
            <i class="fad fa-calendar-alt me-2"></i> Rendez-vous
            </a>
        </div>

        <!-- Right side: logout button -->
        <div>
            <a href="${pageContext.request.contextPath}/admin?action=logout" class="profile-btn">
                <i class="fad fa-sign-out-alt me-1"></i> Déconnexion
            </a>
        </div>
    </div>
</nav>

<div class="flex-grow-1 overflow-auto">