<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctor Appointment - Accueil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">

    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Poppins', 'Roboto', 'Helvetica Neue', sans-serif;
        }
        .hero-section {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .card-role {
            transition: all 0.3s ease;
            cursor: pointer;
            border-radius: 20px;
        }
        .card-role:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 30px rgba(0, 0, 0, 0.2);
        }
        .card-role-patient:hover {
            background: linear-gradient(135deg, #0F766E, #14B8A6);
            color: white;
        }
        .card-role-medecin:hover {
            background: linear-gradient(135deg, #2563EB, #3B82F6);
            color: white;
        }
        .card-role-admin:hover {
            background: linear-gradient(135deg, #1E293B, #334155);
            color: white;
        }
        .icon-circle {
            width: 80px;
            height: 80px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
        }
    </style>
</head>
<body>

<div class="container hero-section">
    <div class="text-center mb-5 w-100">
        <h1 class="text-white fw-bold mb-3 display-4">
            <i class="fad fa-hospital-user me-2"></i> Doctor Appointment
        </h1>
        <p class="text-white-50 mb-5">Gérez vos rendez-vous médicaux en toute simplicité</p>

        <div class="row g-4 justify-content-center">
            <!-- Patient card -->
            <div class="col-md-3">
                <div class="card card-role card-role-patient border-0 shadow-lg text-center p-4 h-100">
                    <div class="icon-circle bg-white bg-opacity-25 mx-auto">
                        <i class="fad fa-user-circle fa-3x text-white"></i>
                    </div>
                    <h4 class="fw-bold mb-2">Patient</h4>
                    <p class="small mb-3">Prenez rendez-vous, consultez vos consultations</p>
                    <a href="${pageContext.request.contextPath}/patient?action=login" class="btn btn-light btn-sm">
                        <i class="fad fa-sign-in-alt me-1"></i> Se connecter
                    </a>
                    <hr class="my-3 bg-light">
                    <a href="${pageContext.request.contextPath}/patient?action=register" class="btn btn-outline-light btn-sm">
                        <i class="fad fa-user-plus me-1"></i> S'inscrire
                    </a>
                </div>
            </div>

            <!-- Medecin card -->
            <div class="col-md-3">
                <div class="card card-role card-role-medecin border-0 shadow-lg text-center p-4 h-100">
                    <div class="icon-circle bg-white bg-opacity-25 mx-auto">
                        <i class="fad fa-user-md fa-3x text-white"></i>
                    </div>
                    <h4 class="fw-bold mb-2">Médecin</h4>
                    <p class="small mb-3">Gérez vos rendez-vous et vos disponibilités</p>
                    <a href="${pageContext.request.contextPath}/medecin?action=login" class="btn btn-light btn-sm">
                        <i class="fad fa-sign-in-alt me-1"></i> Se connecter
                    </a>
                    <hr class="my-3 bg-light">
                    <a href="#" class="btn btn-outline-light btn-sm disabled">
                        <i class="fad fa-user-plus me-1"></i> Inscription réservée
                    </a>
                </div>
            </div>

            <!-- Admin card -->
            <div class="col-md-3">
                <div class="card card-role card-role-admin border-0 shadow-lg text-center p-4 h-100">
                    <div class="icon-circle bg-white bg-opacity-25 mx-auto">
                        <i class="fad fa-shield-alt fa-3x text-white"></i>
                    </div>
                    <h4 class="fw-bold mb-2">Administrateur</h4>
                    <p class="small mb-3">Gestion complète de la plateforme</p>
                    <a href="${pageContext.request.contextPath}/admin?action=login" class="btn btn-light btn-sm">
                        <i class="fad fa-sign-in-alt me-1"></i> Se connecter
                    </a>
                    <hr class="my-3 bg-light">
                    <a href="#" class="btn btn-outline-light btn-sm disabled">
                        <i class="fad fa-user-plus me-1"></i> Accès restreint
                    </a>
                </div>
            </div>
        </div>



        <!-- Footer -->
        <footer class="mt-5 pt-3">
            <small class="text-white-50">
                &copy; <%= java.time.Year.now().getValue() %> Doctor Appointment - Tous droits réservés
            </small>
        </footer>
    </div>
</div>


<script src="${pageContext.request.contextPath}/bootstrap-5.3.3/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
<script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/duotone.js"></script>N
</body>
</html>