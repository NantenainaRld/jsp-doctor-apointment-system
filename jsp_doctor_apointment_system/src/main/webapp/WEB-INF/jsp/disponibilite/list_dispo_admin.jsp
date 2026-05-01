<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Disponibilite" %>
<%@ page import="com.doctorapointment.model.Medecin" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
Medecin medecin = (Medecin) request.getAttribute("medecin");
List<Disponibilite> listDispo = (List<Disponibilite>) request.getAttribute("listDispo");

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    if (medecin == null) {
    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
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
        <style>
            body { background-color: #f0f2f5; font-family: 'Poppins', 'Roboto', 'Helvetica Neue', sans-serif; }
        </style>
    </head>
    <body>

    <div class="container py-4">
        <!-- Back button -->
        <div class="mb-3">
            <button class="btn btn-outline-secondary btn-sm" onclick="window.location.href='${pageContext.request.contextPath}/admin?action=dashboard'">
                <i class="fad fa-arrow-left me-1"></i> Retour
            </button>
        </div>

        <!-- Doctor info + Filters row -->
        <div class="row g-4 mb-4">
            <!-- Doctor info card -->
            <div class="col-md-5">
                <div class="card border-0 shadow-sm rounded-4">
                    <div class="card-body p-4">
                        <div class="d-flex align-items-center">
                            <div class="bg-primary bg-opacity-10 rounded-circle p-3 me-3">
                                <i class="fad fa-user-md fa-2x text-primary"></i>
                            </div>
                            <div>
                                <div class="d-flex">
                                    <h5 class="fw-bold">Dr <%= medecin.getNomMed() %> <%= medecin.getPrenomMed() %></h5>
                                    <span class="text-secondary ms-2" style="font-size:0.9rem">(<%= medecin.getIdMed() %>)</span>
                                </div>
                                <div class="text-muted small">
                                    <span><i class="fad fa-stethoscope me-1"></i> <%= medecin.getSpecialite() %></span>
                                    <span class="mx-2">•</span>
                                    <span><i class="fad fa-map-marker-alt me-1"></i> <%= medecin.getLieu() %></span>
                                    <span class="mx-2">•</span>
                                    <span><i class="fad fa-chart-line me-1"></i> <%= String.format("%,.2f", medecin.getTauxHoraire()) %> Ar/h</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Filters card -->
            <div class="col-md-7">
                <div class="card border-0 shadow-sm rounded-4">
                    <div class="card-body p-3">
                        <form method="post" action="${pageContext.request.contextPath}/admin">
                            <input type="hidden" name="action" value="filter_dispo_admin">
                            <input type="hidden" name="idMed" value="<%= medecin.getIdMed() %>">
                            <div class="row mb-3">
                                <div class="col-6">
                                    <label class="form-label small fw-semibold text-muted mb-1">
                                        <i class="fad fa-calendar-alt me-1 text-primary"></i> Date début
                                    </label>
                                    <input type="date" class="form-control form-control-sm" name="filterDateDebut"
                                           value="${sessionScope.dispoAdminFilterDateDebut != null ? sessionScope.dispoAdminFilterDateDebut : ''}">
                                </div>
                                <div class="col-6">
                                    <label class="form-label small fw-semibold text-muted mb-1">
                                        <i class="fad fa-calendar-alt me-1 text-primary"></i> Date fin
                                    </label>
                                    <input type="date" class="form-control form-control-sm" name="filterDateFin"
                                           value="${sessionScope.dispoAdminFilterDateFin != null ? sessionScope.dispoAdminFilterDateFin : ''}">
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-6">
                                    <label class="form-label small fw-semibold text-muted mb-1">
                                        <i class="fad fa-clock me-1 text-primary"></i> Heure début
                                    </label>
                                    <input type="time" class="form-control form-control-sm" name="filterHeureDebut"
                                           value="${sessionScope.dispoAdminFilterHeureDebut != null ? sessionScope.dispoAdminFilterHeureDebut : ''}">
                                </div>
                                <div class="col-6">
                                    <label class="form-label small fw-semibold text-muted mb-1">
                                        <i class="fad fa-clock me-1 text-primary"></i> Heure fin
                                    </label>
                                    <input type="time" class="form-control form-control-sm" name="filterHeureFin"
                                           value="${sessionScope.dispoAdminFilterHeureFin != null ? sessionScope.dispoAdminFilterHeureFin : ''}">
                                </div>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary btn-sm flex-fill">
                                    <i class="fad fa-filter me-1"></i> Filtrer
                                </button>
                                <button type="button" class="btn btn-outline-secondary btn-sm flex-fill"
                                        onclick="window.location.href='${pageContext.request.contextPath}/admin?action=clear_dispo_admin_filters&idMed=<%= medecin.getIdMed() %>'">
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
        // Error message from request
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
        // Success message from request
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

        <%
        // Error message from session
        String sessionError = (String) session.getAttribute("error_message");
        if (sessionError != null && !sessionError.isEmpty()) {
        %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fad fa-exclamation-triangle me-2"></i>
            <%= sessionError %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <%
        session.removeAttribute("error_message");
        }
        %>

        <%
        // Success message from session
        String sessionSuccess = (String) session.getAttribute("success_message");
        if (sessionSuccess != null && !sessionSuccess.isEmpty()) {
        %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fad fa-check-circle me-2"></i>
            <%= sessionSuccess %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <%
        session.removeAttribute("success_message");
        }
        %>

        <!-- Disponibilities title -->
        <div class="d-flex justify-content-between align-items-center mb-0">
            <h5 class="fw-semibold mb-0">
                <i class="fad fa-calendar-alt me-2"></i> Disponibilités du médecin
                <%
                int totalCount = (listDispo != null) ? listDispo.size() : 0;
                %>
                <span class="badge bg-secondary rounded-pill ms-2" style="font-size: 0.7rem;"><%= totalCount %></span>
            </h5>
        </div>
        <hr>

        <!-- Disponibilities table -->
        <div class="border rounded overflow-auto bg-white">
            <table class="table table-hover table-striped mb-0">
                <thead class="position-sticky top-0 bg-primary text-white" style="z-index: 1;">
                <tr>
                    <th class="small"><i class="fad fa-calendar-day me-1"></i> Date</th>
                    <th class="small"><i class="fad fa-clock me-1"></i> Heure début</th>
                    <th class="small"><i class="fad fa-clock me-1"></i> Heure fin</th>
                    <th class="small text-center"><i class="fad fa-cogs me-1"></i> Actions</th>
                </tr>
                </thead>
                <tbody>
                <%
                if (listDispo == null || listDispo.isEmpty()) {
                %>
                <tr>
                    <td colspan="4" class="text-center text-muted py-4">
                        <i class="fad fa-calendar-times me-2"></i> Aucune disponibilité trouvée
                        \n</td>
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
                        <button class="btn btn-sm btn-outline-primary me-1"
                                onclick='openEditModal(<%= dispo.getIdDispo() %>, "<%= dispo.getDateDispo() %>", "<%= dispo.getDebutDispo() %>", "<%= dispo.getFinDispo() %>")'
                                data-bs-toggle="tooltip" title="Modifier">
                            <i class="fad fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger"
                                onclick="openDeleteModal(<%= dispo.getIdDispo() %>)"
                                data-bs-toggle="tooltip" title="Supprimer">
                            <i class="fad fa-trash-alt"></i>
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

    <!-- Edit Disponibility Modal -->
    <div class="modal fade" id="editDispoModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-warning text-white border-0">
                    <div class="d-flex align-items-center">
                        <i class="fad fa-edit fa-2x me-2"></i>
                        <h6 class="mb-0 fw-semibold">Modifier le créneau</h6>
                    </div>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="action" value="update_dispo_admin">
                    <input type="hidden" name="idDispo" id="editIdDispo">
                    <input type="hidden" name="idMed" value="<%= medecin.getIdMed() %>">
                    <div class="modal-body p-4">
                        <div class="alert alert-info bg-light border-0 mb-3 py-2 text-center">
                            <i class="fad fa-calendar-alt me-1"></i>
                            Créneau <strong><span id="editIdDispoLabel"></span></strong>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Heure début</label>
                            <input type="time" class="form-control" name="heureDebut" id="editHeureDebut" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Heure fin</label>
                            <input type="time" class="form-control" name="heureFin" id="editHeureFin" required>
                        </div>
                    </div>
                    <div class="modal-footer border-0 justify-content-center gap-3 pb-4">
                        <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
                            <i class="fad fa-times me-2"></i> Annuler
                        </button>
                        <button type="submit" class="btn btn-warning px-4">
                            <i class="fad fa-save me-2"></i> Modifier
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteDispoModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-danger text-white border-0">
                    <div class="d-flex align-items-center">
                        <i class="fad fa-trash-alt fa-2x me-2"></i>
                        <h6 class="mb-0 fw-semibold">Supprimer le créneau</h6>
                    </div>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body text-center py-4">
                    <i class="fad fa-calendar-times fa-4x text-danger mb-3"></i>
                    <p class="mb-2 fw-semibold">Êtes-vous sûr de vouloir supprimer ce créneau ?</p>
                    <small class="text-muted">Cette action est irréversible.</small>
                </div>
                <div class="modal-footer border-0 justify-content-center gap-3 pb-4">
                    <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
                        <i class="fad fa-times me-2"></i> Annuler
                    </button>
                    <a href="#" class="btn btn-danger px-4" id="confirmDeleteBtn">
                        <i class="fad fa-check-circle me-2"></i> Oui, supprimer
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/bootstrap-5.3.3/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/fontawesome.js"></script>
    <script src="${pageContext.request.contextPath}/fontawesome-pro-7.1.0-web/js/duotone.js"></script>
    <script>
        // Edit modal handler
        function openEditModal(id, date, debut, fin) {
            document.getElementById('editIdDispo').value = id;
            document.getElementById('editIdDispoLabel').innerText = id;
            document.getElementById('editHeureDebut').value = debut;
            document.getElementById('editHeureFin').value = fin;
            var modal = new bootstrap.Modal(document.getElementById('editDispoModal'));
            modal.show();
        }

        // Delete modal handler
        function openDeleteModal(id) {
            var confirmBtn = document.getElementById('confirmDeleteBtn');
            if (confirmBtn) {
                confirmBtn.href = '${pageContext.request.contextPath}/admin?action=delete_dispo_admin&id=' + id + '&idMed=<%= medecin.getIdMed() %>';
            }
            var modal = new bootstrap.Modal(document.getElementById('deleteDispoModal'));
            modal.show();
        }
    </script>
    </body>
    </html>