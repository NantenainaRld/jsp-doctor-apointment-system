<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Patient" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
// Check session and get appropriate user
Patient patient = null;
String userRole = null;

if (session.getAttribute("patient") != null) {
userRole = "patient";
patient = (Patient) request.getAttribute("patient");

// If patient not in request, try to get from session
if (patient == null) {
patient = (Patient) session.getAttribute("patient");
}
} else if (session.getAttribute("admin") != null) {
userRole = "admin";
patient = (Patient) request.getAttribute("patient");

// If patient not in request, try to get from session attribute
if (patient == null) {
patient = (Patient) session.getAttribute("patientToEdit");
}
}

DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

if (patient == null) {
if ("patient".equals(userRole)) {
response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
} else if ("admin".equals(userRole)) {
response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
} else {
response.sendRedirect(request.getContextPath() + "/");
}
return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modification profil patient</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/patient.css">
    <style>
        .profile-card {
            backdrop-filter: blur(10px);
        }
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        .btn-primary {
            transition: all 0.2s ease;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
    </style>
</head>
<body>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <!-- Profile Card -->
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden profile-card">
                <div class="card-header bg-primary py-3 border-0">
                    <div class="d-flex align-items-center text-light">
                        <i class="fad fa-user-edit fa-2x me-3  "></i>
                        <div>
                            <h5 class="mb-0 fw-semibold">Modification du profil</h5>
                        </div>
                    </div>
                </div>

                <div class="card-body p-4">
                    <div class="text-center mb-2">ID: <b><%= patient.getIdPat() %></b></div>
                    <hr>
                    <!-- Error message -->
                    <%
                    String errorMessage = (String) request.getAttribute("error_message");
                    if (errorMessage != null && !errorMessage.isEmpty()) {
                    %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fad fa-exclamation-circle me-2"></i>
                        <%= errorMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>

                    <!-- Success message -->
                    <%
                    String successMessage = (String) request.getAttribute("success_message");
                    if (successMessage != null && !successMessage.isEmpty()) {
                    %>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fad fa-check-circle me-2"></i>
                        <%= successMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>

                    <!-- Update form -->
                    <form method="post" action="${pageContext.request.contextPath}/patient">
                        <input type="hidden" name="action" value="update_patient">
                        <input type="hidden" name="idPat" value="<%= patient.getIdPat() %>">

                        <!-- Last % first name-->
                        <div class="d-flex mb-3 gap-2 justify-content-between xbg-secondary rounded-4 p-2">
                            <!-- Last name -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-user me-1 text-primary"></i> Nom
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="nomPat" value="<%= patient.getNomPat() %>" required>
                            </div>

                            <!-- First name -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-user me-1 text-primary"></i> Prénom(s)
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="prenomPat" value="<%= patient.getPrenomPat() %>">
                            </div>
                        </div>

                        <!-- birth and email-->
                        <div class="d-flex mb-3 gap-2 justify-content-between xbg-secondary rounded-4 p-2">
                            <!-- Birth date -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-birthday-cake me-1 text-primary"></i> Date de naissance
                                </label>
                                <input type="date" class="form-control form-control-md rounded-3"
                                       name="dateNais" value="<%= patient.getDateNais().format(dateFormatter) %>"
                                       required>
                            </div>

                            <!-- Email -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-envelope me-1 text-primary"></i> Adresse email
                                </label>
                                <input type="email" class="form-control form-control-md rounded-3"
                                       name="emailPat" value="<%= patient.getEmailPat() %>" placeholder="exemple@domain.com" required>
                            </div>
                        </div>

                        <!-- Password (optional) -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-lock me-1 text-primary"></i> Nouveau mot de passe
                            </label>
                            <input type="password" class="form-control form-control-md rounded-3"
                                   name="mdpPat" placeholder="Laissez vide pour ne pas changer">
                            <small class="text-muted">Minimum 6 caractères</small>
                        </div>

                        <!-- Action buttons -->
                        <div class="d-flex gap-3">
                            <button type="submit" class="btn btn-primary flex-fill py-2 rounded-3 fw-semibold">
                                <i class="fad fa-save me-2"></i> Enregistrer
                            </button>
                            <button type="button" class="btn btn-outline-secondary flex-fill py-2 rounded-3 fw-semibold"
                                    onclick="window.location.href='${pageContext.request.contextPath}/patient?action=dashboard'">
                                <i class="fad fa-times me-2"></i> Annuler
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.3/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/duotone.js"></script>
</body>
</html>