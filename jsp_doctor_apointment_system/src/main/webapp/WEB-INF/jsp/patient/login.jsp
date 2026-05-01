<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion Patient - Doctor Appointment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/patient.css">
    <style>
        .login-card {
            backdrop-filter: blur(10px);
            border-radius: 20px;
        }
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
    </style>
</head>
<body>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <!-- Login Card -->
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden login-card">
                <div class="card-header bg-white border-0 text-center pt-4">
                    <i class="fad fa-user-circle fa-3x text-primary mb-2"></i>
                    <h4 class="fw-bold mb-0">Espace Patient</h4>
                    <small class="text-muted">Connectez-vous à votre compte</small>
                </div>

                <div class="card-body p-4">
                    <!-- Error message -->
                    <%
                    String errorMessage = (String) request.getAttribute("error_message");
                    if (errorMessage != null && !errorMessage.isEmpty()) {
                    %>
                    <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
                        <i class="fad fa-exclamation-circle me-2"></i>
                        <%= errorMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>

                    <!-- Success message (after account deletion or registration) -->
                    <%
                    String successMessage = (String) request.getAttribute("success_message");
                    if (successMessage != null && !successMessage.isEmpty()) {
                    %>
                    <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
                        <i class="fad fa-check-circle me-2"></i>
                        <%= successMessage %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>

                    <!-- Account deleted message -->
                    <%
                    String deleted = request.getParameter("deleted");
                    if ("true".equals(deleted)) {
                    %>
                    <div class="alert alert-info alert-dismissible fade show mb-3" role="alert">
                        <i class="fad fa-info-circle me-2"></i>
                        Votre compte a été supprimé avec succès.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>

                    <!-- Account registered message -->
                    <%
                    String registered = request.getParameter("registered");
                    if ("true".equals(registered)) {
                    %>
                    <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
                        <i class="fad fa-check-circle me-2"></i>
                        Inscription réussie ! Veuillez vous connecter avec vos identifiants.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>


                    <!-- Login Form -->
                    <form method="post" action="${pageContext.request.contextPath}/patient">
                        <input type="hidden" name="action" value="login_submit">

                        <!-- Email -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-envelope me-1 text-primary"></i> Adresse email
                            </label>
                            <input type="email" class="form-control form-control-md rounded-3"
                                   name="emailPat" placeholder="ex: jean.dupont@email.com"
                                   value="<%= session.getAttribute("loginEmail") != null ?
                            session.getAttribute("loginEmail") : "" %>" required>
                        </div>

                        <!-- Password -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-lock me-1 text-primary"></i> Mot de passe
                            </label>
                            <input type="password" class="form-control form-control-md rounded-3"
                                   name="mdpPat" placeholder="••••••" required>
                        </div>

                        <!-- Submit button -->
                        <div class="w-100 text-center">
                            <button type="submit" class="btn btn-login btn-outline-primary btn-md py-2 rounded-3 fw-semibold">
                                <i class="fad fa-sign-in-alt me-2"></i> Se connecter
                            </button>
                        </div>
                    </form>

                    <!-- Register link -->
                    <div class="text-center mt-4">
                        <small class="text-muted">Pas encore de compte ?</small>
                        <a href="${pageContext.request.contextPath}/patient?action=register"
                           class="text-decoration-none ms-1">
                            <i class="fad fa-user-plus me-1"></i> Créer un compte
                        </a>
                    </div>
                    <!-- Back to home link -->
                    <div class="text-center mt-4">
                        <a href="${pageContext.request.contextPath}/" class="text-decoration-none">
                            <i class="fad fa-home me-1"></i> Retour à l'accueil
                        </a>
                    </div>
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