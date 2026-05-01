<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Medecin" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
List
<Medecin> topMedecins = (List
    <Medecin>) request.getAttribute("topMedecins");
        %>
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Top Médecins - Doctor Appointment</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
            <style>
                body {
                    background: linear-gradient(135deg, #AC63CC 0%, #CA3A01 100%);
                }
                .card-top {
                    transition: transform 0.2s ease;
                }
                .card-top:hover {
                    transform: translateY(-5px);
                }
                .rank-badge {
                    position: absolute;
                    top: -10px;
                    left: -10px;
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-weight: bold;
                    font-size: 1.2rem;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.2);
                }
                .rank-1 { background: linear-gradient(135deg, #FFD700, #FFA500); color: white; }
                .rank-2 { background: linear-gradient(135deg, #C0C0C0, #A9A9A9); color: white; }
                .rank-3 { background: linear-gradient(135deg, #CD7F32, #A0522D); color: white; }
                .rank-other { background: #2c3e50; color: white; }
                /* Font family */
                body {
                    font-family: 'Poppins', 'Roboto', 'Helvetica Neue', sans-serif;
                    color: var(--text-dark);
                }
            </style>
        </head>
        <body>

        <div class="container py-5">

            <!-- Messages section -->
            <div class="mb-4">
                <%
                // Success message from session
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
                // Error message from session
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
                // Error message from request
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
            </div>

            <!-- Page header -->
            <div class="text-center mb-4">
                <i class="fad fa-trophy fa-3x text-warning mb-3"></i>
                <h2 class="fw-bold text-white mb-2">Top 5 Médecins</h2>
                <p class="text-white-50">Les médecins les plus consultés</p>
            </div>

            <%
            if (topMedecins == null || topMedecins.isEmpty()) {
            %>
            <div class="alert alert-info text-center" role="alert">
                <i class="fad fa-info-circle me-2"></i>
                Aucune donnée disponible pour le moment.
            </div>
            <%
            } else {
            %>
            <div class="row g-4 justify-content-center">
                <%
                int rank = 1;
                for (Medecin med : topMedecins) {
                String rankClass = rank == 1 ? "rank-1" : (rank == 2 ? "rank-2" : (rank == 3 ? "rank-3" :
                "rank-other"));
                String medalIcon = rank == 1 ? "fa-crown" : (rank == 2 ? "fa-medal" : (rank == 3 ? "fa-medal" :
                "fa-star"));
                %>
                <div class="col-4">
                    <div class="card border-0 shadow-lg rounded-4 card-top position-relative h-100">
                        <div class="rank-badge <%= rankClass %>">
                            <%= rank %>
                        </div>
                        <div class="card-body text-center p-4">
                            <div class="bg-primary bg-opacity-10 rounded-circle p-3 d-inline-block mb-3">
                                <i class="fad <%= medalIcon %> fa-2x text-primary"></i>
                            </div>
                            <h5 class="fw-bold mb-1">Dr <%= med.getNomMed() %> <%= med.getPrenomMed() %></h5>
                            <p class="text-muted small mb-2"><%= med.getSpecialite() %></p>
                            <div class="d-flex justify-content-center gap-3 mb-3">
                                    <span class="badge bg-light text-dark">
                                    <i class="fad fa-hashtag me-1 text-dark"></i> <%= med.getIdMed() %>
                                </span>
                                <span class="badge bg-light text-dark">
                                    <i class="fad fa-map-marker-alt me-1 text-primary"></i> <%= med.getLieu() %>
                                </span>
                                <span class="badge bg-light text-dark">
                                    <i class="fad fa-chart-line me-1 text-success"></i> <%= String.format("%,.2f", med.getTauxHoraire()) %> Ar/h
                                </span>

                            </div>
                        </div>
                    </div>
                </div>
                <%
                rank++;
                }
                %>
            </div>
            <%
            }
            %>

            <!-- Back button -->
            <div class="text-center mt-5">
                <a href="${pageContext.request.contextPath}/" class="btn btn-light px-4">
                    <i class="fad fa-arrow-left me-2"></i> Retour à l'accueil
                </a>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/bootstrap-5.3.3/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
        <script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/duotone.js"></script>
        </body>
        </html>