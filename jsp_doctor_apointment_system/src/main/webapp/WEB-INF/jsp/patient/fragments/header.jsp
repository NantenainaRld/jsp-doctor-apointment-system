<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Patient" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord - Patient</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/patient.css">
</head>
<% String currentPage = (String) session.getAttribute("page_patient"); %>

<body class="vh-100 d-flex flex-column overflow-hidden gap-1">
<nav class="navbar navbar-expand-lg xbg-primary flex-shrink-0">
    <div class="container-fluid">
        <!-- Left side: welcome message -->
        <div class="navbar-brand text-white">
            <%
            // Retrieve patient from session
            Patient patient = (Patient) session.getAttribute("patient");
            String patientName = "";
            if (patient != null && patient.getNomPat() != null) {
            patientName = patient.getNomPat();
            if (patient.getPrenomPat() != null) {
            patientName += " " + patient.getPrenomPat();
            }
            }
            if (patientName.isEmpty()) {
            patientName = "Patient";
            }
            // Truncate if longer than 35 characters
            if (patientName.length() > 35) {
            patientName = patientName.substring(0, 35) + "...";
            }
            %>
            Bonjour, <strong><%= patientName %></strong>
        </div>

        <!-- Center: navigation buttons -->
        <div class="mx-auto">
            <%
            if (currentPage == null) currentPage = "";
            %>
            <a href="${pageContext.request.contextPath}/patient?action=rdv"
               class="btn btn-sm me-2 <%= currentPage.equals("rdv") ? "btn-light xbtn-selected" : "btn-outline-light" %>"
            data-bs-toggle="tooltip"
            title="Voir tous vos rendez-vous">
            <i class="fad fa-calendar-alt me-2"></i> Mes rendez-vous
            </a>

            <a href="${pageContext.request.contextPath}/patient?action=med"
               class="btn btn-sm <%= currentPage.equals("med") ? "btn-light xbtn-selected" : "btn-outline-light" %>"
            data-bs-toggle="tooltip"
            title="Consulter la liste des médecins">
            <i class="fad fa-user-md me-2"></i> Médecins
            </a>
        </div>

        <!-- Right side: user dropdown menu -->
        <div class="dropdown">
            <button class="btn btn-light btn-sm dropdown-toggle" type="button" id="userMenu" data-bs-toggle="dropdown" aria-expanded="false">
                <i class="fad fa-user-circle me-1"></i> Mon compte
            </button>
            <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0" aria-labelledby="userMenu">
                <li>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/patient?action=update_patient">
                        <i class="fad fa-edit me-2 "></i> Modifier le profil
                    </a>
                </li>
                <li>
                    <a class="dropdown-item text-danger" href="#" data-bs-toggle="modal" data-bs-target="#deleteAccountModal">
                        <i class="fad fa-trash-alt me-2"></i> Supprimer le compte
                    </a>
                </li>
                <li><hr class="dropdown-divider"></li>
                <li>
                    <a class="dropdown-item" href="${pageContext.request.contextPath}/patient?action=logout">
                        <i class="fad fa-sign-out-alt me-2"></i> Déconnexion
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Delete Account Confirmation Modal -->
<div class="modal fade" id="deleteAccountModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <div class="modal-header bg-danger text-white border-0">
                <div class="d-flex align-items-center">
                    <i class="fad fa-exclamation-triangle fa-2x me-2"></i>
                    <h6 class="mb-0 fw-semibold">Suppression de compte</h6>
                </div>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center py-4">
                <i class="fad fa-user-slash fa-4x text-danger mb-3"></i>
                <p class="mb-2 fw-semibold">Êtes-vous vraiment sûr de vouloir supprimer votre compte ?</p>
                <small class="text-muted">Cette action est <strong class="text-danger">irréversible</strong>. Toutes vos données (rendez-vous, historique) seront définitivement supprimées.</small>
            </div>
            <div class="modal-footer border-0 justify-content-center gap-3 pb-4">
                <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
                    <i class="fad fa-times me-2"></i> Annuler
                </button>
                <a href="${pageContext.request.contextPath}/patient?action=delete_account" class="btn btn-danger px-4">
                    <i class="fad fa-trash-alt me-2"></i> Oui, supprimer
                </a>
            </div>
        </div>
    </div>
</div>



