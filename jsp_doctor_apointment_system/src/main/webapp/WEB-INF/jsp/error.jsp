<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erreur - Doctor Appointment</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/patient.css">
    <script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
    <style>
        .error-card {
            backdrop-filter: blur(10px);
            border-radius: 20px;
        }
    </style>
</head>
<body>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <!-- Error Card -->
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden error-card text-center">
                <div class="card-body p-5">
                    <i class="fad fa-exclamation-triangle fa-4x text-warning mb-3"></i>
                    <h3 class="fw-bold mb-3">Oups !</h3>

                    <%
                    String message = (String) request.getAttribute("message");
                    if (message == null || message.isEmpty()) {
                    message = "Une erreur inattendue s'est produite.";
                    }
                    %>
                    <p class="text-muted mb-4"><%= message %></p>

                    <div class="d-flex gap-3 justify-content-center">
                        <button class="btn btn-primary px-4" onclick="window.history.back()">
                            <i class="fad fa-arrow-left me-2"></i> Retour
                        </button>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary px-4">
                            <i class="fad fa-home me-2"></i> Accueil
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