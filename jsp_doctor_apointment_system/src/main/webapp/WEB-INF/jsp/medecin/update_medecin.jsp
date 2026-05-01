<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Medecin" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
// Get medecin from request
Medecin medecin = (Medecin) request.getAttribute("medecin");

if (medecin == null) {
response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
return;
}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier du profil médecin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <style>
        .profile-card {
            backdrop-filter: blur(10px);
        }
        .form-control:focus, .form-select:focus {
            border-color: #2563EB;
            box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.25);
        }
        .btn-primary {
            transition: all 0.2s ease;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(37, 99, 235, 0.4);
        }
        /* Font family */
        body {
            font-family: 'Poppins', 'Roboto', 'Helvetica Neue', sans-serif;
        }
    </style>
</head>
<body>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <!-- Profile Card -->
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden profile-card">
                <div class="card-header bg-white py-3 border-0">
                    <div class="d-flex align-items-center">
                        <i class="fad fa-user-md fa-2x me-3 text-primary"></i>
                        <div>
                            <h5 class="mb-0 fw-semibold">Modifier le profil</h5>
                            <small class="text-muted">Mettez à jour les informations personnelles</small>
                        </div>
                    </div>
                </div>

                <div class="card-body p-4">
                    <!-- Messages section -->
                    <%
                    String successMessage = (String) session.getAttribute("success_message");
                    if (successMessage != null && !successMessage.isEmpty()) {
                    %>
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fad fa-check-circle me-2"></i>
                        <%= successMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    session.removeAttribute("success_message");
                    }
                    %>

                    <%
                    String errorMessage = (String) session.getAttribute("error_message");
                    if (errorMessage != null && !errorMessage.isEmpty()) {
                    %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fad fa-exclamation-circle me-2"></i>
                        <%= errorMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    session.removeAttribute("error_message");
                    }
                    %>

                    <%
                    String requestError = (String) request.getAttribute("error_message");
                    if (requestError != null && !requestError.isEmpty()) {
                    %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fad fa-exclamation-circle me-2"></i>
                        <%= requestError %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>

                    <!-- Update form -->
                    <form method="post" action="${pageContext.request.contextPath}/medecin">
                        <input type="hidden" name="action" value="update_medecin_submit">
                        <input type="hidden" name="idMed" value="<%= medecin.getIdMed() %>">

                        <!-- ID (readonly) -->
                        <div class="mb-3 text-center">
                            <span class="text-secondary">ID: </span><b><%= medecin.getIdMed() %></b>
                        </div>
                        <hr>
                        <!--// name and first name-->
                        <div class="border rounded-4 gap-2 d-flex p-2 mb-3">
                            <!-- Last name -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-user me-1 text-primary"></i> Nom
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="nomMed" value="<%= medecin.getNomMed() %>" required>
                            </div>

                            <!-- First name -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-user me-1 text-primary"></i> Prénom
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="prenomMed" value="<%= medecin.getPrenomMed() %>">
                            </div>
                        </div>

                        <!-- // speciality and lieu and taux -->
                        <div class="border rounded-4 gap-2 d-flex p-2 mb-3">
                            <!-- Specialty -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-stethoscope me-1 text-primary"></i> Spécialité
                                </label>
                                <input type="text" class="form-control form-control-lg rounded-3"
                                       name="specialite" value="<%= medecin.getSpecialite() %>" required>
                            </div>

                            <!-- Location -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-map-marker-alt me-1 text-primary"></i> Lieu d'exercice
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="lieu" value="<%= medecin.getLieu() %>" required>
                            </div>

                            <!-- Hourly rate -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-chart-line me-1 text-primary"></i> Taux horaire (Ar/h)
                                </label>
                                <input type="number" step="0.01" class="form-control form-control-md rounded-3"
                                       name="tauxHoraire" value="<%= medecin.getTauxHoraire() %>" required>
                            </div>
                        </div>

                        <!-- Password (optional) -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-lock me-1 text-primary"></i> Nouveau mot de passe
                            </label>
                            <input type="password" class="form-control form-control-md rounded-3"
                                   name="mdpMed" placeholder="Laissez vide pour ne pas changer">
                            <small class="text-muted">Minimum 6 caractères</small>
                        </div>

                        <!-- Action buttons -->
                        <div class="d-flex gap-3">
                            <button type="submit" class="btn btn-primary flex-fill py-2 rounded-3 fw-semibold">
                                <i class="fad fa-save me-2"></i> Enregistrer
                            </button>
                            <button type="button" class="btn btn-outline-secondary flex-fill py-2 rounded-3 fw-semibold"
                                    onclick="window.history.back()">
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