<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Patient" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
List
<Patient> listPatients = (List
    <Patient>) request.getAttribute("listPatients");
        if (listPatients == null) {
        listPatients = new ArrayList<>();
        }
        %>

        <div class="h-100 d-flex flex-column">
            <!-- Filter Section -->
            <div class="flex-shrink-0 mb-3">
                <div class="card border-0 shadow-sm rounded-4">
                    <div class="card-body p-3">
                        <form method="post" action="${pageContext.request.contextPath}/medecin"
                              class="row g-3 align-items-end">
                            <input type="hidden" name="action" value="filter_patient_medecin">

                            <div class="col-md-4">
                                <label class="form-label small fw-semibold text-muted">
                                    <i class="fad fa-search me-1"></i> Rechercher
                                </label>
                                <input type="text" class="form-control form-control-sm" name="filterSearch"
                                       value="${sessionScope.filterSearchPatient != null ? sessionScope.filterSearchPatient : ''}"
                                       placeholder="ID, nom, prénom ou email">
                            </div>

                            <div class="col-md-3">
                                <label class="form-label small fw-semibold text-muted">
                                    <i class="fad fa-birthday-cake me-1"></i> Date naissance début
                                </label>
                                <input type="date" class="form-control form-control-sm" name="filterDateDebut"
                                       value="${sessionScope.filterDateDebutPatient != null ? sessionScope.filterDateDebutPatient : ''}">
                            </div>

                            <div class="col-md-3">
                                <label class="form-label small fw-semibold text-muted">
                                    <i class="fad fa-birthday-cake me-1"></i> Date naissance fin
                                </label>
                                <input type="date" class="form-control form-control-sm" name="filterDateFin"
                                       value="${sessionScope.filterDateFinPatient != null ? sessionScope.filterDateFinPatient : ''}">
                            </div>

                            <div class="col-md-2">
                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-primary btn-sm flex-fill">
                                        <i class="fad fa-filter me-1"></i> Filtrer
                                    </button>
                                    <button type="button" class="btn btn-outline-secondary btn-sm"
                                            onclick="window.location.href='${pageContext.request.contextPath}/medecin?action=clear_patient_filters'">
                                        <i class="fad fa-eraser me-1"></i>
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <!-- Messages section -->
            <div class="flex-shrink-0 mb-3">
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
            <!-- Header with count -->
            <div class="flex-shrink-0 d-flex justify-content-between align-items-center mb-3">
                <h5 class="fw-semibold mb-0 text-primary">
                    <i class="fad fa-users me-2"></i> Mes patients
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
                                for (Patient patient : listPatients) {
                                %>
                                <tr>
                                    <td class="align-middle"><%= patient.getIdPat() %></td>
                                    <td class="align-middle">
                                        <%= patient.getNomPat() %> <%= patient.getPrenomPat() %>
                                    </td>
                                    <td class="align-middle"><%= patient.getDateNais().format(dateFormatter) %></td>
                                    <td class="align-middle"><%= patient.getEmailPat() %></td>
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