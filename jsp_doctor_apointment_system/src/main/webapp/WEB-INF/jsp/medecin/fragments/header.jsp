<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Medecin" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord - Médecin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <style>
        /* Medecin color palette */
        :root {
            --med-primary: #2563EB;
            --med-secondary: #3B82F6;
            --med-accent: #8B5CF6;
            --med-bg-light: #EFF6FF;
            --med-text-dark: #1E3A8A;
        }

        body {
            font-family: 'Poppins', 'Roboto', 'Helvetica Neue', sans-serif;
            background-color: var(--med-bg-light);
        }

        /* Header background */
        .navbar-custom {
            background: linear-gradient(135deg, var(--med-primary), var(--med-secondary));
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        /* Welcome text */
        .welcome-text {
            font-size: 1.1rem;
            font-weight: 500;
            color: white;
        }

        /* Navigation buttons */
        .nav-btn {
            background: rgba(255, 255, 255, 0.15);
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
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
        }

        .nav-btn-active {
            background: white;
            color: var(--med-primary);
        }

        .nav-btn-active i {
            color: var(--med-primary);
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

        /* Dropdown menu */
        .dropdown-menu-custom {
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border: none;
            margin-top: 8px;
        }

        .dropdown-item i {
            width: 20px;
            margin-right: 8px;
        }
    </style>
</head>
<body class="vh-100 d-flex flex-column overflow-hidden">

<nav class="navbar navbar-expand-lg flex-shrink-0 navbar-custom">
    <div class="container-fluid">
        <!-- Left side: welcome message -->
        <div class="navbar-brand">
            <%
            Medecin medecin = (Medecin) session.getAttribute("medecin");
            String medecinName = "";
            if (medecin != null && medecin.getNomMed() != null) {
            medecinName = medecin.getNomMed();
            if (medecin.getPrenomMed() != null) {
            medecinName += " " + medecin.getPrenomMed();
            }
            }
            if (medecinName.isEmpty()) {
            medecinName = "Médecin";
            }
            // Truncate if longer than 35 characters
            if (medecinName.length() > 35) {
            medecinName = medecinName.substring(0, 35) + "...";
            }
            %>
            <span class="welcome-text">
                <i class="fad fa-user-md me-2"></i>
                Bonjour, <strong><%= medecinName %></strong>
            </span>
        </div>

        <!-- Center: navigation buttons -->
        <div class="mx-auto">
            <%
            String currentPage = (String) session.getAttribute("page_medecin");
            if (currentPage == null) currentPage = "rdv";
            %>
            <a href="${pageContext.request.contextPath}/medecin?action=rdv" class="nav-btn <%= currentPage.equals("rdv") ? "nav-btn-active" : "" %>">
            <i class="fad fa-calendar-alt me-2"></i> Rendez-vous
            </a>
            <a href="${pageContext.request.contextPath}/medecin?action=horaire" class="nav-btn <%= currentPage.equals("horaire") ? "nav-btn-active" : "" %>">
            <i class="fad fa-clock me-2"></i> Mes horaires
            </a>
            <a href="${pageContext.request.contextPath}/medecin?action=patient" class="nav-btn <%= currentPage.equals("patient") ? "nav-btn-active" : "" %>">
            <i class="fad fa-users me-2"></i> Mes patients
            </a>
        </div>

        <!-- Right side: user dropdown menu -->
        <div class="dropdown">
            <button class="profile-btn dropdown-toggle" type="button" id="userMenu" data-bs-toggle="dropdown" aria-expanded="false">
                <i class="fad fa-user-circle me-1"></i> Mon compte
            </button>
            <ul class="dropdown-menu dropdown-menu-end dropdown-menu-custom" aria-labelledby="userMenu">
                <li>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/medecin?action=profile">
                        <i class="fad fa-edit text-primary"></i> Modifier profil
                    </a>
                </li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/medecin?action=logout">
                        <i class="fad fa-sign-out-alt"></i> Déconnexion
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="flex-grow-1 overflow-auto">