<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Medecin" %>
<%@ page import="com.doctorapointment.model.Disponibilite" %>
<%@ page import="com.doctorapointment.model.Rdv" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalTime" %>
<%
// Get data from request
Medecin medecin = (Medecin) request.getAttribute("medecin");
Disponibilite disponibilite = (Disponibilite) request.getAttribute("disponibilite");
List
<Rdv> rdvList = (List
    <Rdv>) request.getAttribute("rdvList");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (medecin == null || disponibilite == null) {
        response.sendRedirect(request.getContextPath() + "/patient?action=med");
        return;
        }

        LocalTime minTime = disponibilite.getDebutDispo();
        LocalTime maxTime = disponibilite.getFinDispo();
        %>
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Prendre rendez-vous - Dr <%= medecin.getNomMed() %></title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.3/css/bootstrap.min.css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/patient.css">
            <script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
            <style>
                .time-slot-taken {
                    background-color: #f8d7da;
                    text-decoration: line-through;
                    color: #721c24;
                }
                .time-slot-free {
                    background-color: #d4edda;
                }
            </style>
        </head>
        <body class="bg-light ">

        <div class="container py-4">
            <div class="row g-4">
                <!-- LEFT COLUMN: Form -->
                <div class="col-md-5">
                    <div class="card xbg-secondary p-2 border-0 rounded-4 mb-2">
                        <!-- Doctor info -->
                        <div class="card border-0 shadow-sm rounded-4 mb-2">
                            <div class="card-body p-2">
                                <div class="d-flex align-items-center">
                                    <div class="bg-primary bg-opacity-10 rounded-circle p-2 me-2">
                                        <i class="fad fa-user-md fa-1x text-primary"></i>
                                    </div>
                                    <div>
                                        <div class="d-flex justify-content-start ">
                                            <h6 class="mb-0 fw-bold me-2">Dr <%= medecin.getNomMed() %> <%=
                                                medecin.getPrenomMed() %></h6>
                                            <span class="text-secondary" style="font-size: 0.9rem">(<%= medecin.getIdMed()%>)</span>
                                        </div>
                                        <div class="text-muted small">
                                        <span><i
                                                class="fad fa-stethoscope me-1"></i> <%= medecin.getSpecialite() %></span>
                                            <span class="mx-2">•</span>
                                            <span><i
                                                    class="fad fa-map-marker-alt me-1"></i> <%= medecin.getLieu() %></span>
                                            <span class="mx-2">•</span>
                                            <span><i class="fad fa-chart-line me-1"></i> <%= String.format("%,.2f", medecin.getTauxHoraire()) %> Ar/h</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Disponibility info -->
                        <div class="card border-0 shadow-sm rounded-4">
                            <div class="card-body p-3">
                                <div class="d-flex align-items-center">
                                    <i class="fad fa-calendar-alt text-primary me-2"></i>
                                    <div>
                                        <small class="text-muted">Disponibilité sélectionnée (n⁰ <b><%=
                                            disponibilite.getIdDispo()%></b>)</small>
                                        <h6 class="mb-0">
                                            <%= disponibilite.getDateDispo().format(dateFormatter) %>
                                            de <strong><%= disponibilite.getDebutDispo().format(timeFormatter)
                                            %></strong>
                                            à <strong><%= disponibilite.getFinDispo().format(timeFormatter) %></strong>
                                        </h6>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Alert messages -->
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

                    <!-- Take RDV Form -->
                    <div class="card border-0 shadow-sm rounded-4">
                        <div class="card-header xbg-primary text-light border-0">
                            <h6 class="mb-0 fw-semibold">
                                <i class="fad fa-calendar-plus me-2 "></i> Prendre un rendez-vous
                            </h6>
                        </div>
                        <div class="card-body p-4">
                            <form method="post" action="${pageContext.request.contextPath}/patient" id="rdvForm">
                                <input type="hidden" name="action" value="add_rdv">
                                <input type="hidden" name="idDispo" value="<%= disponibilite.getIdDispo() %>">
                                <input type="hidden" name="idMed" value="<%= medecin.getIdMed() %>">
                                <input type="hidden" name="dateRdv" value="<%= disponibilite.getDateDispo() %>">

                                <!-- Heure debut -->
                                <div class="mb-3">
                                    <label class="form-label fw-semibold">
                                        <i class="fad fa-play-circle me-1 text-primary"></i> Heure de début
                                    </label>
                                    <input type="time" class="form-control form-control-lg rounded-3"
                                           name="heureDebut"
                                           value="${sessionScope.heureDebutAddRdv}"
                                           required>
                                    <small class="text-muted">Disponible entre <%= minTime.format(timeFormatter) %> et
                                        <%= maxTime.format(timeFormatter) %></small>
                                </div>

                                <!-- Heure fin -->
                                <div class="mb-4">
                                    <label class="form-label fw-semibold">
                                        <i class="fad fa-stop-circle me-1 text-primary"></i> Heure de fin
                                    </label>
                                    <input type="time" class="form-control form-control-lg rounded-3"
                                           name="heureFin"
                                           value="${sessionScope.heureFinAddRdv}"
                                           required>
                                </div>

                                <!-- Buttons -->
                                <div class="d-flex gap-3">
                                    <button type="submit" class="btn btn-primary flex-fill py-2 rounded-3 fw-semibold">
                                        <i class="fad fa-check-circle me-2"></i> Confirmer
                                    </button>
                                    <button type="button"
                                            class="btn btn-outline-secondary flex-fill py-2 rounded-3 fw-semibold"
                                            onclick="window.location.href='${pageContext.request.contextPath}/patient?action=rdv'">
                                        <i class="fad fa-times me-2"></i> Annuler
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- RIGHT COLUMN: Existing RDV table -->
                <div class="col-md-7">
                    <div class="card border-0 shadow-sm rounded-4">
                        <div class="card-header xbg-primary text-light border-0 pt-3 px-4">
                            <h6 class="mb-0 fw-semibold">
                                <i class="fad fa-calendar-day me-2 "></i> Rendez-vous existants le <%=
                                disponibilite.getDateDispo().format(dateFormatter) %>
                            </h6>
                            <small class="text-muted mt-2 d-block "><i class="fad fa-exclamation-triangle text-warning"></i> Les créneaux déjà réservés ne peuvent pas être
                                sélectionnés.</small>
                        </div>
                        <div class="card-body p-4">
                            <!-- Filter form -->
                            <form method="post" action="${pageContext.request.contextPath}/patient" class="row g-2 mb-1 ">
                                <input type="hidden" name="action" value="filter_rdv_dispo">
                                <input type="hidden" name="idDispo" value="<%= disponibilite.getIdDispo() %>">

                                <div class="col-md-4">
                                    <label class="form-label small text-muted">Heure début min</label>
                                    <input type="time" class="form-control form-control-sm" name="filterHeureDebut"
                                           value="${sessionScope.filterHeureDebut}">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small text-muted">Heure fin max</label>
                                    <input type="time" class="form-control form-control-sm" name="filterHeureFin"
                                           value="${sessionScope.filterHeureFin}">
                                </div>
                                <div class="col-md-4 d-flex gap-2 py-4">
                                    <button type="submit" class="btn btn-primary btn-sm">
                                        <i class="fad fa-filter me-1"></i> Filtrer
                                    </button>
                                    <button type="button" class="btn btn-outline-secondary btn-sm flex-fill"
                                            onclick="window.location.href='${pageContext.request.contextPath}/patient?action=clear_filter_rdv_dispo&idDispo=${param.idDispo}'">
                                        <i class="fad fa-eraser me-1"></i> Vider
                                    </button>
                                </div>
                            </form>
                            <hr>

                            <!-- RDV Table -->
                            <div class="table-responsive" style="max-height: 400px; overflow-y: auto;">
                                <table class="table table-sm table-hover mb-0">
                                    <thead class="bg-primary text-white position-sticky top-0">
                                    <tr>
                                        <th><i class="fad fa-hashtag me-1"></i> ID</th>
                                        <th><i class="fad fa-clock me-1"></i> Début</th>
                                        <th><i class="fad fa-clock me-1"></i> Fin</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <%
                                    if (rdvList == null || rdvList.isEmpty()) {
                                    %>
                                    <tr>
                                        <td colspan="3" class="text-center text-muted py-3">
                                            <i class="fad fa-calendar-times me-1"></i> Aucun rendez-vous existant
                                        </td>
                                    </tr>
                                    <%
                                    } else {
                                    for (Rdv rdvExist : rdvList) {
                                    String rowClass = "";
                                    if (rdvExist.getHeureDebut().equals(request.getParameter("heureDebut"))
                                    && rdvExist.getHeureFin().equals(request.getParameter("heureFin"))) {
                                    rowClass = "table-warning";
                                    }
                                    %>
                                    <tr class="<%= rowClass %>">
                                        <td class="small"><%= rdvExist.getIdRdv()%></td>
                                        <td class="small"><%= rdvExist.getHeureDebut().format(timeFormatter) %></td>
                                        <td class="small"><%= rdvExist.getHeureFin().format(timeFormatter) %></td>
                                    </tr>
                                    <%
                                    }
                                    }
                                    %>
                                    </tbody>
                                </table>
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