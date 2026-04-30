<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.RdvPatMed" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<div class="row h-100 ">
    <div class="col-12 py-1">
        <!-- Filter Section -->
        <div class="row align-items-center gx-2 px-1 py-1 rounded xbg-secondary">
            <form method="post" action="${pageContext.request.contextPath}/patient">
                <div class="row align-items-center gx-2 px-1 py-1 rounded">
                    <!-- Filter by date -->
                    <div class="col-md-3">
                        <div class="border rounded p-2 bg-white">
                            <label class="form-label small text-muted mb-1">
                                <i class="fad fa-calendar-alt me-1 text-primary"></i> Par date
                            </label>
                            <div class="d-flex gap-1">
                                <input type="date" class="form-control form-control-sm" name="filterDateDebut"
                                       id="filterDateDebut"
                                       value="${sessionScope.filterDateDebut}" placeholder="Début">
                                <input type="date" class="form-control form-control-sm" name="filterDateFin"
                                       id="filterDateFin"
                                       value="${sessionScope.filterDateFin}" placeholder="Fin">
                            </div>
                        </div>
                    </div>

                    <!-- Filter by time -->
                    <div class="col-md-3">
                        <div class="border rounded p-2 bg-white">
                            <label class="form-label small text-muted mb-1">
                                <i class="fad fa-clock me-1 text-primary"></i> Par heure
                            </label>
                            <div class="d-flex gap-1">
                                <input type="time" class="form-control form-control-sm" name="filterHeureDebut"
                                       id="filterHeureDebut"
                                       value="${sessionScope.filterHeureDebut}" placeholder="Début">
                                <input type="time" class="form-control form-control-sm" name="filterHeureFin"
                                       id="filterHeureFin"
                                       value="${sessionScope.filterHeureFin}" placeholder="Fin">
                            </div>
                        </div>
                    </div>

                    <!-- Filter by type -->
                    <div class="col-md-2">
                        <div class="border rounded p-2 bg-white">
                            <label class="form-label small text-muted mb-1">
                                <i class="fad fa-tag me-1 text-primary"></i> Par statut
                            </label>
                            <select class="form-select form-select-sm" name="filterStatut" id="filterStatut">
                                <option value="all" ${sessionScope.filterStatut==
                                'all' ? 'selected' : ''}>Tous</option>
                                <option value="en attente" ${sessionScope.filterStatut==
                                'en attente' ? 'selected' : ''}>En attente</option>
                                <option value="confirmé" ${sessionScope.filterStatut==
                                'confirmé' ? 'selected' : ''}>Confirmé</option>
                                <option value="annulé" ${sessionScope.filterStatut==
                                'annulé' ? 'selected' : ''}>Annulé</option>
                                <option value="dépassé" ${sessionScope.filterStatut==
                                'dépassé' ? 'selected' : ''}>Dépassé</option>
                            </select>
                        </div>
                    </div>

                    <!-- Filter by doctor name -->
                    <div class="col-md-2">
                        <div class="border rounded p-2 bg-white">
                            <label class="form-label small text-muted mb-1">
                                <i class="fad fa-user-md me-1 text-primary"></i> Par médecin
                            </label>
                            <input type="text" class="form-control form-control-sm" name="filterSearch"
                                   id="filterSearch"
                                   value="${sessionScope.filterSearch}" placeholder="Nom du médecin">
                        </div>
                    </div>

                    <!-- Buttons -->
                    <div class="col-md-2">
                        <div class="d-flex gap-2 bg-light rounded p-2">
                            <button type="submit" name="action" value="filter_rdv"
                                    class="btn btn-primary btn-sm flex-fill"
                                    id="btnFiltrer"
                                    data-bs-toggle="tooltip"
                                    title="Appliquer les filtres sélectionnés">
                                <i class="fad fa-filter me-1"></i> Filtrer
                            </button>

                            <button type="submit" name="action" value="empty_rdv_filter"
                                    class="btn btn-outline-secondary btn-sm flex-fill"
                                    id="btnVider"
                                    data-bs-toggle="tooltip"
                                    title="Effacer tous les filtres">
                                <i class="fad fa-eraser me-1"></i> Vider
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <!-- Alert -->
        <%
        String errorMessage = (String) request.getAttribute("error_message");
        if (errorMessage != null && !errorMessage.isEmpty()) {
        %>

        <div class="alert alert-danger alert-dismissible fade show my-1" role="alert">
            <i class="fad fa-exclamation-triangle me-2"></i>
            <%= errorMessage %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <%
        }
        %>

        <!-- Success message-->
        <%
        String successMessage = (String) request.getAttribute("success_message");
        if (successMessage != null && !successMessage.isEmpty()) {
        %>
        <div class="alert alert-success alert-dismissible fade show my-1" role="alert">
            <i class="fad fa-check-circle me-2"></i>
            <%= successMessage %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <%
        }
        %>

        <!-- Table title -->
        <div class="mt-4 mb-2">
            <h5 class="fw-semibold mb-2">
                <i class="fad fa-list me-2"></i> Liste des rendez-vous
                <%
                List
                <RdvPatMed> listRdv = (List
                    <RdvPatMed>) request.getAttribute("listRdv");
                        int totalCount = (listRdv != null) ? listRdv.size() : 0;
                        %>
                        <span class="badge xbg-secondary rounded-pill ms-2 text-secondary" style="font-size: 0.7rem;"><%= totalCount %></span>
            </h5>
            <hr class="mt-1 mb-2">
        </div>

        <!-- Scrollable table body -->
        <div class="border rounded overflow-auto">
            <table class="table table-hover table-striped mb-0">
                <thead class="position-sticky top-0 bg-dark text-success" style="z-index: 1;">
                <tr>
                    <th class="small xbg-primary text-white"><i class="fad fa-hashtag me-1"></i> ID</th>
                    <th class="small xbg-primary text-white"><i class="fad fa-user-md me-1"></i> Médecin (Spécialité)
                    </th>
                    <th class="small xbg-primary text-white"><i class="fad fa-calendar-plus me-1"></i> Date de prise
                    </th>
                    <th class="small xbg-primary text-white"><i class="fad fa-calendar-clock me-1"></i> Date & Heure
                    </th>
                    <th class="small xbg-primary text-white"><i class="fad fa-chart-simple me-1"></i> Statut</th>
                    <th class="small xbg-primary text-white"><i class="fad fa-money-bill-wave me-1"></i> Montant</th>
                    <th class="small xbg-primary text-white"><i class="fad fa-cogs me-1"></i> Actions</th>
                </tr>
                </thead>
                <tbody>
                <%
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                if (listRdv == null || listRdv.isEmpty()) {
                %>
                <tr>
                    <td colspan="7" class="text-center text-muted py-4">
                        <i class="fad fa-calendar-times me-2"></i> Aucun rendez-vous trouvé
                    </td>
                </tr>
                <%
                } else {
                for (RdvPatMed rdv : listRdv) {
                %>
                <tr>
                    <td class="align-middle"><%= rdv.getIdRdv() %></td>
                    <td class="align-middle">
                        <strong><%= rdv.getRdvNomMed() %></strong>
                        <small class="text-muted">(<%= rdv.getSpecialite() %>)</small>
                    </td>
                    <td class="align-middle text-secondary"><%= rdv.getDatePrisRdv().format(dateFormatter) %></td>
                    <td class="align-middle">
                        <%= rdv.getDateRdv().format(dateFormatter) %> à
                        <%= rdv.getHeureDebut().format(timeFormatter) %> -
                        <%= rdv.getHeureFin().format(timeFormatter) %>
                    </td>
                    <td class="align-middle">
                        <%
                        String badgeClass = "";
                        String badgeIcon = "";
                        if (rdv.getEtatRdv().equals("confirmé")) {
                        badgeClass = "badge bg-success";
                        badgeIcon = "fad fa-check-circle me-1";
                        } else if (rdv.getEtatRdv().equals("en attente")) {
                        badgeClass = "badge bg-warning text-dark";
                        badgeIcon = "fad fa-hourglass-half me-1";
                        } else if (rdv.getEtatRdv().equals("annulé")) {
                        badgeClass = "badge bg-danger";
                        badgeIcon = "fad fa-times-circle me-1";
                        } else if (rdv.getEtatRdv().equals("dépassé")) {
                        badgeClass = "badge bg-secondary";
                        badgeIcon = "fad fa-clock me-1";
                        } else {
                        badgeClass = "badge bg-secondary";
                        badgeIcon = "fad fa-question-circle me-1";
                        }
                        %>
                        <span class="<%= badgeClass %>">
                                <i class="<%= badgeIcon %>"></i> <%= rdv.getEtatRdv() %>
                            </span>
                    </td>
                    <td class="align-middle">
                        <%
                        double tauxHoraire = rdv.getTauxHoraire();
                        long minutes = java.time.temporal.ChronoUnit.MINUTES.between(rdv.getHeureDebut(),
                        rdv.getHeureFin());
                        double montant = tauxHoraire * (minutes / 60.0);
                        %>
                        <span class="fw-semibold text-success">
                            <%= String.format("%,.2f", montant) %> Ar
                        </span>
                    </td>
                    <td class="align-middle">
                        <%
                        boolean isEnAttente = "en attente".equals(rdv.getEtatRdv());
                        %>
                        <button class="btn btn-sm btn-outline-primary me-1"
                                onclick="updateRdv(<%= rdv.getIdRdv() %>)"
                        <%= isEnAttente ? "" : "disabled" %>
                        data-bs-toggle="tooltip"
                        title="Modifier le créneau du rendez-vous">
                        <i class="fad fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger"
                                onclick="cancelRdv(<%= rdv.getIdRdv() %>)"
                        <%= isEnAttente ? "" : "disabled" %>
                        data-bs-toggle="tooltip"
                        title="Annuler ce rendez-vous">
                        <i class="fad fa-ban"></i>
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

        <!-- Update Confirmation Modal -->
        <div class="modal fade" id="updateModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-primary text-white border-0">
                        <i class="fad fa-edit me-2"></i>
                        <h6 class="modal-title">Modification de rendez-vous</h6>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="text-center py-3">
                            <i class="fad fa-calendar-edit fa-3x text-primary mb-3"></i>
                            <p class="mb-0">Êtes-vous sûr de vouloir modifier ce rendez-vous ?</p>
                            <small class="text-muted">Vous pourrez changer seulement le créneau si disponible.</small>
                        </div>
                    </div>
                    <div class="modal-footer border-0 justify-content-center gap-3">
                        <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
                            <i class="fad fa-times me-2"></i> Non, retour
                        </button>
                        <a href="#" class="btn btn-primary px-4" id="confirmUpdateBtn">
                            <i class="fad fa-check-circle me-2"></i> Oui, modifier
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal cancel rdv-->
        <div class="modal fade" id="cancelModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg">
                    <div class="modal-header bg-danger text-white border-0">
                        <i class="fad fa-exclamation-triangle me-2"></i>
                        <h6 class="modal-title">Annulation de rendez-vous</h6>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="text-center py-3">
                            <i class="fad fa-calendar-times fa-3x text-danger mb-3"></i>
                            <p class="mb-0">Êtes-vous sûr de vouloir annuler ce rendez-vous ?</p>
                            <small class="text-muted">Cette action est irréversible.</small>
                        </div>
                    </div>
                    <div class="modal-footer border-0 justify-content-center gap-3">
                        <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
                            <i class="fad fa-times me-2"></i> Non, retour
                        </button>
                        <a href="#" class="btn btn-danger px-4" id="confirmCancelBtn">
                            <i class="fad fa-check-circle me-2"></i> Oui, annuler
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>