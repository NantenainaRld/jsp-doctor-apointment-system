<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion Admin - Doctor Appointment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #1E293B 0%, #334155 100%);
            min-height: 100vh;
        }
        .login-card {
            backdrop-filter: blur(10px);
            border-radius: 20px;
        }
        .form-control:focus {
            border-color: #E11D48;
            box-shadow: 0 0 0 0.2rem rgba(225, 29, 72, 0.25);
        }
        .btn-login {
            background: linear-gradient(135deg, #1E293B, #E11D48);
            border: none;
            transition: all 0.2s ease;
        }
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(225, 29, 72, 0.4);
        }
        body {
            font-family: 'Poppins', 'Roboto', 'Helvetica Neue', sans-serif;
        }
    </style>
</head>
<body>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden login-card">
                <div class="card-header bg-white border-0 text-center pt-4">
                    <i class="fad fa-shield-alt fa-3x text-danger mb-2"></i>
                    <h4 class="fw-bold mb-0">Administration</h4>
                    <small class="text-muted">Accès réservé aux administrateurs</small>
                </div>

                <div class="card-body p-4">
                    <!-- Messages -->
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

                    <form method="post" action="${pageContext.request.contextPath}/admin">
                        <input type="hidden" name="action" value="login_submit">

                        <div class="mb-3">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-user me-1 text-danger"></i> Identifiant
                            </label>
                            <input type="text" class="form-control form-control-md rounded-3"
                                   name="identifiant" placeholder="id" required>
                        </div>

                        <div class="mb-4">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-lock me-1 text-danger"></i> Mot de passe
                            </label>
                            <input type="password" class="form-control form-control-md rounded-3"
                                   name="password" placeholder="••••••" required>
                        </div>

                        <button type="submit" class="btn btn-login btn-lg w-100 text-white py-2 rounded-3 fw-semibold">
                            <i class="fad fa-sign-in-alt me-2"></i> Se connecter
                        </button>
                    </form>

                    <div class="text-center mt-4">
                        <a href="${pageContext.request.contextPath}/home" class="text-decoration-none">
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