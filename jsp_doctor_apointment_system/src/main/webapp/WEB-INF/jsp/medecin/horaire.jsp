<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Disponibilite" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
// Declare formatters BEFORE using them
DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

List<Disponibilite> listDispo = (List<Disponibilite>) request.getAttribute("listDispo");

    // Ensure listDispo is never null
    if (listDispo == null) {
    listDispo = new java.util.ArrayList<>();
    }
    %>

    <div class="h-100 d-flex flex-column">

        <!-- Filter Section -->
        <div class="flex-shrink-0 mt-2">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-3">
                    <form method="post" action="${pageContext.request.contextPath}/medecin" class="row g-3 align-items-end">
                        <input type="hidden" name="action" value="filter_dispo_medecin">

                        <div class="col-md-3">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-calendar-alt me-1"></i> Date début
                            </label>
                            <input type="date" class="form-control form-control-sm" name="filterDateDebut"
                                   value="${sessionScope.filterDateDebutDispo != null ? sessionScope.filterDateDebutDispo : ''}">
                        </div>

                        <div class="col-md-3">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-calendar-alt me-1"></i> Date fin
                            </label>
                            <input type="date" class="form-control form-control-sm" name="filterDateFin"
                                   value="${sessionScope.filterDateFinDispo != null ? sessionScope.filterDateFinDispo : ''}">
                        </div>

                        <div class="col-md-2">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-clock me-1"></i> Heure début
                            </label>
                            <input type="time" class="form-control form-control-sm" name="filterHeureDebut"
                                   value="${sessionScope.filterHeureDebutDispo != null ? sessionScope.filterHeureDebutDispo : ''}">
                        </div>

                        <div class="col-md-2">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-clock me-1"></i> Heure fin
                            </label>
                            <input type="time" class="form-control form-control-sm" name="filterHeureFin"
                                   value="${sessionScope.filterHeureFinDispo != null ? sessionScope.filterHeureFinDispo : ''}">
                        </div>

                        <div class="col-md-2">
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary btn-sm flex-fill">
                                    <i class="fad fa-filter me-1"></i> Filtrer
                                </button>
                                <button type="button" class="btn btn-outline-secondary btn-sm"
                                        onclick="window.location.href='${pageContext.request.contextPath}/medecin?action=clear_dispo_filters'">
                                    <i class="fad fa-eraser me-1"></i>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Messages section -->
        <div class="flex-shrink-0 my-2">
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

        <!-- Header with add button -->
        <div class="flex-shrink-0 d-flex justify-content-between align-items-center mb-3">
            <h5 class="fw-semibold mb-0 text-primary">
                <i class="fad fa-clock me-2"></i> Mes horaires de travail
            </h5>
            <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addDispoModal">
                <i class="fad fa-plus me-1"></i> Ajouter un créneau
            </button>
        </div>

        <!-- Disponibilities count -->
        <div class="flex-shrink-0 mb-2">
        <span class="badge bg-secondary rounded-pill">
            <i class="fad fa-calendar-alt me-1"></i> <%= listDispo.size() %> créneaux
        </span>
        </div>

        <!-- Scrollable Table -->
        <div class="flex-grow-1 overflow-auto">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead class="bg-primary text-white position-sticky top-0">
                            <tr>
                                <th class="small"><i class="fad fa-hashtag me-1"></i> ID</th>
                                <th class="small"><i class="fad fa-calendar-day me-1"></i> Date</th>
                                <th class="small"><i class="fad fa-clock me-1"></i> Heure début</th>
                                <th class="small"><i class="fad fa-clock me-1"></i> Heure fin</th>
                                <th class="small text-center"><i class="fad fa-cogs me-1"></i> Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                            if (listDispo.isEmpty()) {
                            %>
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">
                                    <i class="fad fa-calendar-times me-2"></i> Aucun créneau disponible
                                </td>
                            </tr>
                            <%
                            } else {
                            for (Disponibilite dispo : listDispo) {
                            %>
                            <tr>
                                <td class="align-middle"><%= dispo.getIdDispo() %></td>
                                <td class="align-middle"><%= dispo.getDateDispo().format(dateFormatter) %></td>
                                <td class="align-middle"><%= dispo.getDebutDispo().format(timeFormatter) %></td>
                                <td class="align-middle"><%= dispo.getFinDispo().format(timeFormatter) %></td>
                                <td class="align-middle text-center">
                                    <button class="btn btn-sm btn-outline-primary me-1"
                                            onclick='openEditModal(<%= dispo.getIdDispo() %>, "<%= dispo.getDateDispo() %>", "<%= dispo.getDebutDispo() %>", "<%= dispo.getFinDispo() %>")'
                                            data-bs-toggle="tooltip" title="Modifier">
                                        <i class="fad fa-edit me-2"></i>Modifier
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger"
                                            onclick="openDeleteModal(<%= dispo.getIdDispo() %>)"
                                            data-bs-toggle="tooltip" title="Supprimer">
                                        <i class="fad fa-trash-alt me-2"></i>Supprimer
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
            </div>
        </div>
    </div>

    <!-- Add Disponibility Modal -->
    <div class="modal fade" id="addDispoModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-primary text-white border-0">
                    <div class="d-flex align-items-center">
                        <i class="fad fa-plus-circle fa-2x me-2"></i>
                        <h6 class="mb-0 fw-semibold">Ajouter un créneau</h6>
                    </div>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/medecin">
                    <input type="hidden" name="action" value="add_dispo">
                    <div class="modal-body p-4">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Date</label>
                            <input type="date" class="form-control" name="dateDispo"
                                   value="<%= session.getAttribute("addDispoDate") != null ? session.getAttribute("addDispoDate") : "" %>"
                            required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Heure début</label>
                            <input type="time" class="form-control" name="heureDebut"
                                   value="<%= session.getAttribute("addDispoHeureDebut") != null ? session.getAttribute("addDispoHeureDebut") : "" %>"
                            required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Heure fin</label>
                            <input type="time" class="form-control" name="heureFin"
                                   value="<%= session.getAttribute("addDispoHeureFin") != null ? session.getAttribute("addDispoHeureFin") : "" %>"
                            required>
                        </div>
                        <div class="alert alert-warning mt-2 py-2">
                            <small>
                                <i class="fad fa-exclamation-triangle me-1"></i>
                                Durée minimale : 10 minutes
                            </small>
                        </div>
                    </div>
                    <div class="modal-footer border-0 justify-content-center gap-3 pb-4">
                        <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
                            <i class="fad fa-times me-2"></i> Annuler
                        </button>
                        <button type="submit" class="btn btn-primary px-4">
                            <i class="fad fa-save me-2"></i> Enregistrer
                        </button>
                    </div>
                </form>
            </div>
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
                <form method="post" action="${pageContext.request.contextPath}/medecin">
                    <input type="hidden" name="action" value="update_dispo">
                    <input type="hidden" name="idDispo" id="editIdDispo">
                    <input type="hidden" name="dateDispo" id="editDateDispo">

                    <div class="modal-body p-4">
                        <!-- Subtitle with current info -->
                        <div class="alert alert-info bg-light border-0 mb-3 py-2 text-center">
                            <i class="fad fa-calendar-alt me-1"></i>
                            Créneau <strong>#<span id="editIdDispoLabel"></span></strong><br>
                            Date : <strong><span id="editDateLabel"></span></strong>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-clock me-1"></i> Heure début
                            </label>
                            <input type="time" class="form-control" name="heureDebut" id="editHeureDebut" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label fw-semibold">
                                <i class="fad fa-clock me-1"></i> Heure fin
                            </label>
                            <input type="time" class="form-control" name="heureFin" id="editHeureFin" required>
                        </div>
                        <div class="alert alert-warning mt-2 py-2">
                            <small>
                                <i class="fad fa-exclamation-triangle me-1"></i>
                                Durée minimale : 10 minutes
                            </small>
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