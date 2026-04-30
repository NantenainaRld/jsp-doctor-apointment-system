<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Medecin" %>
<%@ page import="java.util.List" %>

<div class="row h-100">
    <div class="col-12 py-1">
        <!-- Filter Section -->
        <div class="row align-items-center gx-2 px-1 py-1 rounded xbg-secondary">
            <form method="post" action="${pageContext.request.contextPath}/patient">
                <input type="hidden" name="action" value="filter_med">
                <div class="row align-items-center gx-2">

                    <!-- Search by name / ID / location -->
                    <div class="col-md-4">
                        <div class="border rounded p-2 bg-white">
                            <label class="form-label small text-muted mb-1">
                                <i class="fad fa-search me-1 text-primary"></i> Rechercher
                            </label>
                            <input type="text" class="form-control form-control-sm" name="filterSearchMed"
                                   value="${sessionScope.filterSearchMed}"
                                   placeholder="ID, nom, prénom ou lieu...">
                        </div>
                    </div>

                    <!-- Filter by specialty -->
                    <div class="col-md-3">
                        <div class="border rounded p-2 bg-white">
                            <label class="form-label small text-muted mb-1">
                                <i class="fad fa-stethoscope me-1 text-primary"></i> Spécialité
                            </label>
                            <input type="text" class="form-control form-control-sm" name="filterSpecialite"
                                   value="${sessionScope.filterSpecialite}" placeholder="Cardiologue, ...">
                        </div>
                    </div>

                    <!-- Filter by hourly rate -->
                    <div class="col-md-3">
                        <div class="border rounded p-2 bg-white">
                            <label class="form-label small text-muted mb-1">
                                <i class="fad fa-chart-line me-1 text-primary"></i> Taux horaire
                            </label>
                            <select class="form-select form-select-sm" name="filterTaux">
                                <option value="" ${sessionScope.filterTaux== null ?
                                'selected' : ''}>Par défaut</option>
                                <option value="asc" ${sessionScope.filterTaux==
                                'asc' ? 'selected' : ''}>Moins cher d'abord</option>
                                <option value="desc" ${sessionScope.filterTaux==
                                'desc' ? 'selected' : ''}>Plus cher d'abord</option>
                            </select>
                        </div>
                    </div>

                    <!-- Buttons -->
                    <div class="col-md-2">
                        <div class="d-flex gap-2 bg-light rounded p-2">
                            <button type="submit" class="btn btn-primary btn-sm flex-fill"
                                    data-bs-toggle="tooltip" title="Appliquer les filtres">
                                <i class="fad fa-filter me-1"></i> Filtrer
                            </button>
                            <button type="button" class="btn btn-outline-secondary btn-sm flex-fill"
                                    onclick="window.location.href='${pageContext.request.contextPath}/patient?action=empty_filter_med'"
                                    data-bs-toggle="tooltip" title="Effacer tous les filtres">
                                <i class="fad fa-eraser me-1"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <!-- Alert messages -->
        <%
        String errorMessage = (String) request.getAttribute("error_message");
        if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
        <div class="alert alert-danger alert-dismissible fade show my-1" role="alert">
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
        <div class="alert alert-success alert-dismissible fade show my-1" role="alert">
            <i class="fad fa-check-circle me-2"></i>
            <%= successMessage %>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <%
        }
        %>

        <!-- Table title -->
        <div class="mt-4 mb-2">
            <h5 class="fw-semibold mb-2">
                <i class="fad fa-user-md me-2"></i> Liste des médecins
                <%
                List
                <Medecin> listMed = (List
                    <Medecin>) request.getAttribute("listMed");
                        int totalCount = (listMed != null) ? listMed.size() : 0;
                        %>
                        <span class="badge xbg-secondary rounded-pill ms-2 text-secondary" style="font-size: 0.7rem;"><%= totalCount %></span>
            </h5>
            <hr class="mt-1 mb-2">
        </div>

        <!-- Scrollable table body -->
        <div class="border rounded overflow-auto">
            <table class="table table-hover table-striped mb-0">
                <thead class="position-sticky top-0 bg-dark text-white" style="z-index: 1;">
                <tr>
                    <th class="small xbg-primary text-white"><i class="fad fa-hashtag me-1"></i> ID</th>
                    <th class="small xbg-primary text-white"><i class="fad fa-user-md me-1"></i> Médecin</th>
                    <th class="small xbg-primary text-white"><i class="fad fa-stethoscope me-1"></i> Spécialité</th>
                    <th class="small xbg-primary text-white"><i class="fad fa-map-marker-alt me-1"></i> Lieu</th>
                    <th class="small xbg-primary text-white"><i class="fad fa-chart-line me-1"></i> Taux horaire</th>
                    <th class="small xbg-primary text-white text-center"><i class="fad fa-cogs me-1"></i> Action</th>
                </tr>
                </thead>
                <tbody>
                <%
                if (listMed == null || listMed.isEmpty()) {
                %>
                <tr>
                    <td colspan="5" class="text-center text-muted py-4">
                        <i class="fad fa-user-slash me-2"></i> Aucun médecin trouvé
                    </td>
                </tr>
                <%
                } else {
                for (Medecin med : listMed) {
                %>
                <tr>
                    <td class="align-middle"><%= med.getIdMed() %></td>
                    <td class="align-middle">
                        <strong>Dr <%= med.getNomMed() %> <%= med.getPrenomMed() %></strong>
                    </td>
                    <td class="align-middle"><%= med.getSpecialite() %></td>
                    <td class="align-middle"><%= med.getLieu() %></td>
                    <td class="align-middle">
                        <%= String.format("%,.2f", med.getTauxHoraire()) %> Ar/h
                    </td>
                    <td class="align-middle text-center">
                        <button class="btn btn-sm btn-outline-primary"
                                onclick="window.location.href='${pageContext.request.contextPath}/patient?action=filter_dispo&idMed=<%= med.getIdMed() %>'"
                                data-bs-toggle="tooltip"
                                title="Voir les horaires">
                            <i class="fad fa-calendar-alt"></i>
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


<style>
    .hover-shadow {
        transition: all 0.2s ease;
    }
    .hover-shadow:hover {
        transform: translateY(-4px);
        box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
    }
</style>