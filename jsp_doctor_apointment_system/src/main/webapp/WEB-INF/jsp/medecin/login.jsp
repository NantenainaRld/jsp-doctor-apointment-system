<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion Médecin - Doctor Appointment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <style>
        .login-card {
            backdrop-filter: blur(10px);
            border-radius: 20px;
        }
        .form-control:focus {
            border-color: #2563EB;
            box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.25);
        }
        .btn-login {
            background: linear-gradient(135deg, #2563EB, #1E3A8A);
            border: none;
            transition: all 0.2s ease;
        }
        .btn-login:hover {
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
        <div class="col-md-5">
            <!-- Login Card -->
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden login-card">
                <div class="card-header bg-white border-0 text-center pt-4">
                    <i class="fad fa-user-md fa-3x text-primary mb-2"></i>
                    <h4 class="fw-bold mb-0">Espace Médecin</h4>
                    <small class="text-muted">Connectez-vous à votre compte professionnel</small>
                </div>

                <div class="card-body p-4">
                    <!-- Messages section -->
                    <%
                    String successMessage = (String) session.getAttribute("success_message");
                    if (successMessage != null && !successMessage.isEmpty()) {
                    %>
                    <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
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
                    <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
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
                    <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
                        <i class="fad fa-exclamation-circle me-2"></i>
                        <%= requestError %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <%
                    }
                    %>

                    <!-- Login Form -->
                    <form method="post" action="${pageContext.request.contextPath}/medecin">
                        <input type="hidden" name="action" value="login_submit">

                        <!-- ID Médecin -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-id-card me-1 text-primary"></i> Identifiant médecin
                            </label>
                            <input type="text" class="form-control form-control-md rounded-3"
                                   name="idMed" placeholder="ex: M001"
                                   value="<%= session.getAttribute("loginIdMed") != null ? session.getAttribute("loginIdMed") : "" %>"
                            required>
                        </div>

                        <!-- Password -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-lock me-1 text-primary"></i> Mot de passe
                            </label>
                            <input type="password" class="form-control form-control-md rounded-3"
                                   name="mdpMed" placeholder="••••••" required>
                        </div>

                        <!-- Submit button -->
                        <button type="submit" class="btn btn-login btn-lg w-100 text-white py-2 rounded-3 fw-semibold">
                            <i class="fad fa-sign-in-alt me-2"></i> Se connecter
                        </button>
                    </form>

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