<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription Patient</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/patient.css">
    <style>
        .register-card {
            backdrop-filter: blur(10px);
            border-radius: 20px;
        }
        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        .btn-register {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border: none;
            transition: all 0.2s ease;
        }
        .btn-register:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
        }
    </style>
</head>
<body>

<div class="container py-4">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <!-- Register Card -->
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden register-card">
                <div class="card-header bg-white border-0 text-center pt-4">
                    <i class="fad fa-user-plus fa-2x text-primary mb-2"></i>
                    <h5 class="fw-bold mb-0">Créer un compte</h5>
                    <small class="text-muted">Inscrivez-vous pour prendre des rendez-vous</small>
                </div>

                <div class="card-body px-4 pt-2">
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

                    <!-- Register Form -->
                    <form method="post" action="${pageContext.request.contextPath}/patient">
                        <input type="hidden" name="action" value="register_submit">

                        <!-- last & first name -->
                        <div class="rounded border p-3 d-flex gap-2 justify-content-between">
                            <!-- Last name -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-user me-1 text-primary"></i> Nom
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="nomPat" placeholder="Votre nom" required
                                       value="<%= session.getAttribute("regNomPat") != null ?
                                session.getAttribute("regNomPat") : "" %>">
                            </div>

                            <!-- First name -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-user me-1 text-primary"></i> Prénom(s)
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="prenomPat" placeholder="Votre prénom"
                                       value="<%= session.getAttribute("regPrenomPat") != null ?
                                session.getAttribute("regPrenomPat") : "" %>">
                            </div>
                        </div>

                        <!-- birth and email-->
                        <div class="rounded border p-3 d-flex gap-2 justify-content-between mt-2">

                            <!-- Birth date -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-birthday-cake me-1 text-primary"></i> Date de naissance
                                </label>
                                <input type="date" class="form-control form-control-md rounded-3"
                                       name="dateNais" required
                                       value="<%= session.getAttribute("regDateNais") != null ?
                                session.getAttribute("regDateNais") : "" %>">
                            </div>

                            <!-- Email -->
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-envelope me-1 text-primary"></i> Adresse email
                                </label>
                                <input type="email" class="form-control form-control-md rounded-3"
                                       name="emailPat" placeholder="ex: jean.dupont@email.com" required
                                       value="<%= session.getAttribute("regEmailPat") != null ?
                                session.getAttribute("regEmailPat") : "" %>">
                            </div>
                        </div>

                        <!-- Password -->
                        <div class="mb-4 mt-2">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-lock me-1 text-primary"></i> Mot de passe
                            </label>
                            <input type="password" class="form-control form-control-md rounded-3"
                                   name="mdpPat" placeholder="Minimum 6 caractères" required>
                        </div>

                        <!-- Submit button -->
                        <button type="submit"
                                class="btn btn-success btn-md w-100 text-white py-2 rounded-3 fw-semibold">
                            <i class="fad fa-check-circle me-2"></i> S'inscrire
                        </button>
                    </form>

                    <!-- Login link -->
                    <div class="text-center mt-4">
                        <small class="text-muted">Déjà un compte ?</small>
                        <a href="${pageContext.request.contextPath}/patient?action=login"
                           class="text-decoration-none ms-1">
                            <i class="fad fa-sign-in-alt me-1"></i> Se connecter
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