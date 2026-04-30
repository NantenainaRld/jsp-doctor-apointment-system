<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Disponibilite" %>
<%@ page import="com.doctorapointment.model.Medecin" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
// Get doctor and disponibilities from request
Medecin medecin = (Medecin) request.getAttribute("medecin");
List
<Disponibilite> listDispo = (List
    <Disponibilite>) request.getAttribute("listDispo");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (medecin == null) {
        response.sendRedirect(request.getContextPath() + "/patient?action=med");
        return;
        }
        %>
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Disponibilités - Dr <%= medecin.getNomMed() %></title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/list_dispo.css">
        </head>
        <body class="bg-light">

        <div class="container py-4">
            <!-- Back button -->
            <div class="mb-3">
                <button class="btn btn-outline-secondary btn-sm"
                        onclick="handleBackNavigation()">
                    <i class="fad fa-arrow-left me-1"></i> Retourner
                </button>
            </div>

            <!-- Doctor info + Filters row -->
            <div class="row g-4 mb-4">
                <!-- Doctor info card -->
                <div class="col-md-5">
                    <div class="card border-0 shadow-sm rounded-4 h-100">
                        <div class="card-body p-4">
                            <div class="d-flex align-items-center">
                                <div class="bg-primary bg-opacity-10 rounded-circle p-3 me-3">
                                    <i class="fad fa-user-md fa-2x text-primary"></i>
                                </div>
                                <div>
                                    <div class="d-flex ">
                                        <h5 class="fw-bold">Dr <%= medecin.getNomMed() %> <%=
                                            medecin.getPrenomMed()
                                            %></h5>
                                        <span class=" text-secondary ms-2" style="font-size:0.9rem">(<%= medecin.getIdMed() %>)</span>
                                    </div>
                                    <div class="text-muted small">
                                        <span><i
                                                class="fad fa-stethoscope me-1"></i> <%= medecin.getSpecialite() %></span>
                                        <span class="mx-2">•</span>
                                        <span><i class="fad fa-map-marker-alt me-1"></i> <%= medecin.getLieu() %></span>
                                        <span class="mx-2">•</span>
                                        <span><i class="fad fa-chart-line me-1"></i> <%= String.format("%.2f", medecin.getTauxHoraire()) %> Ar/h</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Filters card -->
                <div class="col-md-7">
                    <div class="card border-0 shadow-sm rounded-4 h-100">
                        <div class="card-body p-3">
                            <form method="post"
                                  action="${pageContext.request.contextPath}/patient?action=filter_dispo&idMed=<%= medecin.getIdMed() %>">
                                <div class="row mb-3">
                                    <!-- Date filters row -->
                                    <div class="col-6">
                                        <label class="form-label small fw-semibold text-muted mb-1">
                                            <i class="fad fa-calendar-alt me-1 text-primary"></i> Par date
                                        </label>
                                        <div class="d-flex gap-2">
                                            <input type="date" class="form-control form-control-sm"
                                                   name="filterDateDebut"
                                                   value="${param.filterDateDebut != null ? param.filterDateDebut : sessionScope.dispoFilterDateDebut}">
                                            <input type="date" class="form-control form-control-sm" name="filterDateFin"
                                                   value="${param.filterDateFin != null ? param.filterDateFin : sessionScope.dispoFilterDateFin}">
                                        </div>
                                    </div>

                                    <!-- Time filters row -->
                                    <div class="col-6">
                                        <label class="form-label small fw-semibold text-muted mb-1">
                                            <i class="fad fa-clock me-1 text-primary"></i> Par heure
                                        </label>
                                        <div class="d-flex gap-2">
                                            <input type="time" class="form-control form-control-sm"
                                                   name="filterHeureDebut"
                                                   value="${param.filterHeureDebut != null ? param.filterHeureDebut : sessionScope.dispoFilterHeureDebut}">
                                            <input type="time" class="form-control form-control-sm"
                                                   name="filterHeureFin"
                                                   value="${param.filterHeureFin != null ? param.filterHeureFin : sessionScope.dispoFilterHeureFin}">
                                        </div>
                                    </div>
                                </div>

                                <!-- Buttons row -->
                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-primary btn-sm flex-fill">
                                        <i class="fad fa-filter me-1"></i> Filtrer
                                    </button>
                                    <button type="button" class="btn btn-outline-secondary btn-sm flex-fill"
                                            onclick="window.location.href='${pageContext.request.contextPath}/patient?action=clear_dispo_filters&idMed=<%= medecin.getIdMed() %>'">
                                        <i class="fad fa-eraser me-1"></i> Vider
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Alert messages -->
            <%
            String errorMessage = (String) request.getAttribute("error_message");
            if (errorMessage != null && !errorMessage.isEmpty()) {
            %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fad fa-exclamation-triangle me-2"></i>
                <%= errorMessage %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <%
            }
            %>

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

            <!-- Disponibilities title -->
            <div class="d-flex justify-content-between align-items-center mb-0">
                <h5 class="fw-semibold mb-0">
                    <i class="fad fa-calendar-alt me-2"></i> Disponibilités du médecin
                    <%
                    int totalCount = (listDispo != null) ? listDispo.size() : 0;
                    %>
                    <span class="badge xbg-secondary rounded-pill ms-2 text-secondary small" style="font-size: 0.7rem;"><%= totalCount %></span>
                </h5>
            </div>
            <hr>

            <!-- Disponibilities table -->
            <div class="border rounded overflow-auto bg-white">
                <table class="table table-hover table-striped mb-0">
                    <thead class="position-sticky top-0 bg-primary text-white" style="z-index: 1;">
                    <tr>
                        <th class="small xbg-primary text-light"><i class="fad fa-calendar-day me-1"></i> Date</th>
                        <th class="small xbg-primary text-light"><i class="fad fa-clock me-1"></i> Heure début</th>
                        <th class="small xbg-primary text-light"><i class="fad fa-clock me-1"></i> Heure fin</th>
                        <th class="small xbg-primary text-light text-center"><i class="fad fa-cogs me-1"></i> Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                    if (listDispo == null || listDispo.isEmpty()) {
                    %>
                    <tr>
                        <td colspan="4" class="text-center text-muted py-4">
                            <i class="fad fa-calendar-times me-2"></i> Aucune disponibilité trouvée
                        </td>
                    </tr>
                    <%
                    } else {
                    for (Disponibilite dispo : listDispo) {
                    %>
                    <tr>
                        <td class="align-middle"><%= dispo.getDateDispo().format(dateFormatter) %></td>
                        <td class="align-middle"><%= dispo.getDebutDispo().format(timeFormatter) %></td>
                        <td class="align-middle"><%= dispo.getFinDispo().format(timeFormatter) %></td>
                        <td class="align-middle text-center">
                            <button class="btn btn-sm btn-outline-success"
                                    onclick="window.location.href='${pageContext.request.contextPath}/patient?action=add_rdv&idDispo=<%= dispo.getIdDispo() %>'"
                                    data-bs-toggle="tooltip"
                                    title="Prendre rendez-vous à ce créneau">
                                <i class="fad fa-calendar-plus"></i> Prendre rendez-vous
                            </button>
                        </td>
                    </tr>
                    <%
                    }
                    }
                    %>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/bootstrap-5.3.3/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
        <script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/duotone.js"></script>
        <script src="${pageContext.request.contextPath}/js/list_dispo.js"></script>
        <script>
            // Initialize tooltips
            document.addEventListener('DOMContentLoaded', function() {
                var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
                tooltipTriggerList.forEach(function (tooltipTriggerEl) {
                    new bootstrap.Tooltip(tooltipTriggerEl);
                });
            });
            function handleBackNavigation() {
            <%
                String userRole = null;
                if (session.getAttribute("patient") != null) {
                    userRole = "patient";
                } else if (session.getAttribute("admin") != null) {
                    userRole = "admin";
                }
            %>
            var userRole = "<%= userRole %>";

            if (userRole === "patient") {
                window.location.href = "${pageContext.request.contextPath}/patient?action=med";
            } else if (userRole === "admin") {
                window.location.href = "${pageContext.request.contextPath}/admin?action=med";
            } else {
                window.history.back();
            }
        }
        </script>
        </body>
        </html>