<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.Medecin" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
List<Medecin> listMedecins = (List<Medecin>) request.getAttribute("listMedecins");
    if (listMedecins == null) {
    listMedecins = new ArrayList<>();
    }
    %>

    <div class="h-100 d-flex flex-column">
        <!-- Messages section -->
        <div class="flex-shrink-0 mb-3">
            <%
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

        <!-- Filter Section -->
        <div class="flex-shrink-0 mb-3">
            <div class="card border-0 shadow-sm rounded-4">
                <div class="card-body p-3">
                    <form method="post" action="${pageContext.request.contextPath}/admin" class="row g-3 align-items-end">
                        <input type="hidden" name="action" value="filter_medecin">

                        <div class="col-md-4">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-search me-1"></i> Rechercher
                            </label>
                            <input type="text" class="form-control form-control-sm" name="filterSearch"
                                   value="${sessionScope.filterSearchMedecin != null ? sessionScope.filterSearchMedecin : ''}"
                                   placeholder="ID, nom, prénom ou lieu">
                        </div>

                        <div class="col-md-3">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-stethoscope me-1"></i> Spécialité
                            </label>
                            <input type="text" class="form-control form-control-sm" name="filterSpecialite"
                                   value="${sessionScope.filterSpecialiteMedecin != null ? sessionScope.filterSpecialiteMedecin : ''}"
                                   placeholder="Cardiologue, ...">
                        </div>

                        <div class="col-md-2">
                            <label class="form-label small fw-semibold text-muted">
                                <i class="fad fa-chart-line me-1"></i> Taux horaire
                            </label>
                            <select class="form-select form-select-sm" name="filterTaux">
                                <option value="" ${sessionScope.filterTauxMedecin == null ? 'selected' : ''}>Par défaut</option>
                                <option value="asc" ${sessionScope.filterTauxMedecin == 'asc' ? 'selected' : ''}>Moins cher</option>
                                <option value="desc" ${sessionScope.filterTauxMedecin == 'desc' ? 'selected' : ''}>Plus cher</option>
                            </select>
                        </div>

                        <div class="col-md-3">
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary btn-sm flex-fill">
                                    <i class="fad fa-filter me-1"></i> Filtrer
                                </button>
                                <button type="button" class="btn btn-outline-secondary btn-sm"
                                        onclick="window.location.href='${pageContext.request.contextPath}/admin?action=clear_medecin_filters'">
                                    <i class="fad fa-eraser me-1"></i>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Header with buttons -->
        <div class="flex-shrink-0 d-flex justify-content-between align-items-center mb-3">
            <h5 class="fw-semibold mb-0 text-primary">
                <i class="fad fa-user-md me-2"></i> Liste des médecins
                <span class="badge bg-secondary rounded-pill ms-2"><%= listMedecins.size() %></span>
            </h5>
            <div class="d-flex gap-2">
                <button class="btn btn-outline-warning btn-sm"
                        onclick="window.location.href='${pageContext.request.contextPath}/medecin?action=top_medecin'">
                    <i class="fad fa-trophy me-1"></i> Top Médecins
                </button>
                <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addMedecinModal">
                    <i class="fad fa-plus me-1"></i> Ajouter un médecin
                </button>
            </div>
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
                                <th class="small"><i class="fad fa-stethoscope me-1"></i> Spécialité</th>
                                <th class="small"><i class="fad fa-map-marker-alt me-1"></i> Lieu</th>
                                <th class="small"><i class="fad fa-chart-line me-1"></i> Taux horaire</th>
                                <th class="small"><i class="fad fa-cogs me-1"></i> Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                            if (listMedecins.isEmpty()) {
                            %>
                            <tr>
                                <td colspan="6" class="text-center text-muted py-4">
                                    <i class="fad fa-user-slash me-2"></i> Aucun médecin trouvé
                                </td>
                            </tr>
                            <%
                            } else {
                            for (Medecin med : listMedecins) {
                            %>
                            <tr>
                                <td class="align-middle"><%= med.getIdMed() %></td>
                                <td class="align-middle">
                                    <%= med.getNomMed() %> <%= med.getPrenomMed() != null ? med.getPrenomMed() : "" %>
                                </td>
                                <td class="align-middle"><%= med.getSpecialite() %></td>
                                <td class="align-middle"><%= med.getLieu() %></td>
                                <td class="align-middle"><%= String.format("%,.0f", med.getTauxHoraire()) %> Ar/h</td>
                                <td class="align-middle">
                                    <button class="btn btn-sm btn-outline-info me-1"
                                            onclick="window.location.href='${pageContext.request.contextPath}/admin?action=disponibilite_medecin&id=<%= med.getIdMed() %>'"
                                            data-bs-toggle="tooltip" title="Voir les disponibilités">
                                        <i class="fad fa-clock"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-primary me-1"
                                            onclick="window.location.href='${pageContext.request.contextPath}/admin?action=edit_medecin&id=<%= med.getIdMed() %>'"
                                            data-bs-toggle="tooltip" title="Modifier">
                                        <i class="fad fa-edit"></i>
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger"
                                            onclick="openDeleteModal('<%= med.getIdMed() %>')"
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

    <!-- Add Medecin Modal -->
    <div class="modal fade" id="addMedecinModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-primary text-white border-0">
                    <div class="d-flex align-items-center">
                        <i class="fad fa-user-md fa-2x me-2"></i>
                        <h6 class="mb-0 fw-semibold">Ajouter un médecin</h6>
                    </div>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form method="post" action="${pageContext.request.contextPath}/admin">
                    <input type="hidden" name="action" value="add_medecin">
                    <div class="modal-body p-4">
                        <!-- ID is auto-generated, no input needed -->

                        <!-- Name and First name -->
                        <div class="border rounded-4 gap-2 d-flex p-3 mb-3">
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-user me-1 text-primary"></i> Nom
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="nomMed" placeholder="ex: RAKOTO"
                                       value="<%= session.getAttribute("addMedNom") != null ? session.getAttribute("addMedNom") : "" %>"
                                required>
                            </div>
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-user me-1 text-primary"></i> Prénom
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="prenomMed" placeholder="ex: Jean"
                                       value="<%= session.getAttribute("addMedPrenom") != null ? session.getAttribute("addMedPrenom") : "" %>">
                            </div>
                        </div>

                        <!-- Specialty and Location -->
                        <div class="border rounded-4 gap-2 d-flex p-3 mb-3">
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-stethoscope me-1 text-primary"></i> Spécialité
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="specialite" placeholder="ex: Cardiologue"
                                       value="<%= session.getAttribute("addMedSpecialite") != null ? session.getAttribute("addMedSpecialite") : "" %>"
                                required>
                            </div>
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-map-marker-alt me-1 text-primary"></i> Lieu d'exercice
                                </label>
                                <input type="text" class="form-control form-control-md rounded-3"
                                       name="lieu" placeholder="ex: Antananarivo"
                                       value="<%= session.getAttribute("addMedLieu") != null ? session.getAttribute("addMedLieu") : "" %>"
                                required>
                            </div>
                        </div>

                        <!-- Hourly rate and Password -->
                        <div class="border rounded-4 gap-2 d-flex p-3 mb-3">
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-chart-line me-1 text-primary"></i> Taux horaire (Ar/h)
                                </label>
                                <input type="number" step="0.01" class="form-control form-control-md rounded-3"
                                       name="tauxHoraire" placeholder="ex: 25000"
                                       value="<%= session.getAttribute("addMedTaux") != null ? session.getAttribute("addMedTaux") : "" %>"
                                required>
                            </div>
                            <div class="w-100">
                                <label class="form-label fw-semibold">
                                    <i class="fad fa-lock me-1 text-primary"></i> Mot de passe
                                </label>
                                <input type="password" class="form-control form-control-md rounded-3"
                                       name="mdpMed" placeholder="Minimum 6 caractères" required>
                                <small class="text-muted">Minimum 6 caractères</small>
                            </div>
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
    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteMedecinModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg rounded-4">
                <div class="modal-header bg-danger text-white border-0">
                    <div class="d-flex align-items-center">
                        <i class="fad fa-trash-alt fa-2x me-2"></i>
                        <h6 class="mb-0 fw-semibold">Supprimer le médecin</h6>
                    </div>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body text-center py-4">
                    <i class="fad fa-user-slash fa-4x text-danger mb-3"></i>
                    <p class="mb-2 fw-semibold">Êtes-vous sûr de vouloir supprimer ce médecin ?</p>
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