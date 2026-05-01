<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Patient" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
List<Patient> listPatients = (List<Patient>) request.getAttribute("listPatients");
    if (listPatients == null) {
    listPatients = new ArrayList<>();
    }
    %>

    <div class="h-100 d-flex flex-column">
        <!-- Messages section -->
        <div class="flex-shrink-0 mb-3">
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

            <%
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
        </div>

        <!-- Filter Section -->
        <div class="flex-shrink-0 mb-3">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-3">
                    <form method="post" action="${pageContext.request.contextPath}/admin" class="row g-3 align-items-end">
                        <input type="hidden" name="action" value="filter_patient_admin">

                        <div class="col-md-5">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-search me-1"></i> Rechercher
                            </label>
                            <input type="text" class="form-control form-control-sm" name="filterSearch"
                                   value="${sessionScope.filterSearchPatientAdmin != null ? sessionScope.filterSearchPatientAdmin : ''}"
                                   placeholder="ID, nom, prénom ou email">
                        </div>

                        <div class="col-md-3">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-birthday-cake me-1"></i> Date naissance début
                            </label>
                            <input type="date" class="form-control form-control-sm" name="filterDateNaisDebut"
                                   value="${sessionScope.filterDateNaisDebut != null ? sessionScope.filterDateNaisDebut : ''}">
                        </div>

                        <div class="col-md-2">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-birthday-cake me-1"></i> Date naissance fin
                            </label>
                            <input type="date" class="form-control form-control-sm" name="filterDateNaisFin"
                                   value="${sessionScope.filterDateNaisFin != null ? sessionScope.filterDateNaisFin : ''}">
                        </div>

                        <div class="col-md-2">
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary btn-sm flex-fill">
                                    <i class="fad fa-filter me-1"></i> Filtrer
                                </button>
                                <button type="button" class="btn btn-outline-secondary btn-sm"
                                        onclick="window.location.href='${pageContext.request.contextPath}/admin?action=clear_patient_filters'">
                                    <i class="fad fa-eraser me-1"></i>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Header with count -->
        <div class="flex-shrink-0 d-flex justify-content-between align-items-center mb-3">
            <h5 class="fw-semibold mb-0 text-primary">
                <i class="fad fa-users me-2"></i> Liste des patients
                <span class="badge bg-secondary rounded-pill ms-2"><%= listPatients.size() %></span>
            </h5>
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
                                <th class="small"><i class="fad fa-user me-1"></i> Nom complet</th>
                                <th class="small"><i class="fad fa-birthday-cake me-1"></i> Date naissance</th>
                                <th class="small"><i class="fad fa-envelope me-1"></i> Email</th>
                                <th class="small"><i class="fad fa-cogs me-1"></i> Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                            if (listPatients.isEmpty()) {
                            %>
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">
                                    <i class="fad fa-user-slash me-2"></i> Aucun patient trouvé
                                </td>
                            </tr>
                            <%
                            } else {
                            java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            for (Patient patient : listPatients) {
                            %>
                            <tr>
                                <td class="align-middle"><%= patient.getIdPat() %></td>
                                <td class="align-middle"><%= patient.getNomPat() %> <%= patient.getPrenomPat() != null ? patient.getPrenomPat() : "" %></td>
                                <td class="align-middle"><%= patient.getDateNais().format(dateFormatter) %></td>
                                <td class="align-middle"><%= patient.getEmailPat() %></td>
                                <td class="align-middle">
                                    <button class="btn btn-sm btn-outline-primary me-1"
                                            onclick="window.location.href='${pageContext.request.contextPath}/admin?action=edit_patient&id=<%= patient.getIdPat() %>'"
                                            data-bs-toggle="tooltip" title="Modifier">
                                        <i class="fad fa-edit"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger"
                                            onclick="openDeleteModal('<%= patient.getIdPat() %>')"
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
            </div>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deletePatientModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-danger text-white border-0">
                    <div class="d-flex align-items-center">
                        <i class="fad fa-trash-alt fa-2x me-2"></i>
                        <h6 class="mb-0 fw-semibold">Supprimer le patient</h6>
                    </div>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body text-center py-4">
                    <i class="fad fa-user-slash fa-4x text-danger mb-3"></i>
                    <p class="mb-2 fw-semibold">Êtes-vous sûr de vouloir supprimer ce patient ?</p>
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