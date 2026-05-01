<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.doctorapointment.model.RdvPatMed" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>



<div class="h-100 d-flex flex-column">
    <!-- Filter Section -->
    <div class="flex-shrink-0 mb-3 mt-2">
        <div class="card border-0 shadow-sm rounded-4">
            <div class="card-body p-3">
                <form method="post" action="${pageContext.request.contextPath}/medecin" class="row g-3 align-items-end">
                    <input type="hidden" name="action" value="filter_rdv_medecin">

                    <div class="col-md-2">
                        <label class="form-label small fw-semibold text-muted">
                            <i class="fad fa-search me-1"></i> Rechercher
                        </label>
                        <input type="text" class="form-control form-control-sm" name="filterSearch"
                               value="${sessionScope.filterSearchMedRdv != null ? sessionScope.filterSearchMedRdv : ''}"
                               placeholder="nom patient">
                    </div>

                    <div class="col-md-2">
                        <label class="form-label small fw-semibold text-muted">
                            <i class="fad fa-calendar-alt me-1"></i> Date début
                        </label>
                        <input type="date" class="form-control form-control-sm" name="filterDateDebut"
                               value="${sessionScope.filterDateDebutMed != null ? sessionScope.filterDateDebutMed : ''}">
                    </div>

                    <div class="col-md-2">
                        <label class="form-label small fw-semibold text-muted">
                            <i class="fad fa-calendar-alt me-1"></i> Date fin
                        </label>
                        <input type="date" class="form-control form-control-sm" name="filterDateFin"
                               value="${sessionScope.filterDateFinMed != null ? sessionScope.filterDateFinMed : ''}">
                    </div>

                    <div class="col-md-2">
                        <label class="form-label small fw-semibold text-muted">
                            <i class="fad fa-clock me-1"></i> Heure début
                        </label>
                        <input type="time" class="form-control form-control-sm" name="filterHeureDebut"
                               value="${sessionScope.filterHeureDebutMed != null ? sessionScope.filterHeureDebutMed : ''}">
                    </div>

                    <div class="col-md-2">
                        <label class="form-label small fw-semibold text-muted">
                            <i class="fad fa-clock me-1"></i> Heure fin
                        </label>
                        <input type="time" class="form-control form-control-sm" name="filterHeureFin"
                               value="${sessionScope.filterHeureFinMed != null ? sessionScope.filterHeureFinMed : ''}">
                    </div>

                    <div class="col-md-2">
                        <label class="form-label small fw-semibold text-muted">
                            <i class="fad fa-chart-simple me-1"></i> Statut
                        </label>
                        <select class="form-select form-select-sm" name="filterStatut">
                            <option value="all" ${sessionScope.filterStatutMed == 'all' ? 'selected' : ''}>Tous</option>
                            <option value="en attente" ${sessionScope.filterStatutMed == 'en attente' ? 'selected' : ''}>En attente</option>
                            <option value="confirmé" ${sessionScope.filterStatutMed == 'confirmé' ? 'selected' : ''}>Confirmé</option>
                            <option value="annulé" ${sessionScope.filterStatutMed == 'annulé' ? 'selected' : ''}>Annulé</option>
                            <option value="dépassé" ${sessionScope.filterStatutMed == 'dépassé' ? 'selected' : ''}>Dépassé</option>
                        </select>
                    </div>

                    <div class="col-md-12">
                        <div class="d-flex gap-2 justify-content-end">
                            <button type="submit" class="btn btn-primary btn-sm">
                                <i class="fad fa-filter me-1"></i> Filtrer
                            </button>
                            <button type="button" class="btn btn-outline-secondary btn-sm"
                                    onclick="window.location.href='${pageContext.request.contextPath}/medecin?action=clear_rdv_filters'">
                                <i class="fad fa-eraser me-1"></i> Vider
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>


    <!-- Success message from session -->
    <%
    String successMessage = (String) session.getAttribute("success_message");
    if (successMessage != null && !successMessage.isEmpty()) {
    %>
    <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
        <i class="fad fa-check-circle me-2"></i>
        <%= successMessage %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <%
    session.removeAttribute("success_message");
    }
    %>

    <!-- Error message from session -->
    <%
    String errorMessage = (String) session.getAttribute("error_message");
    if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
    <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
        <i class="fad fa-exclamation-circle me-2"></i>
        <%= errorMessage %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <%
    session.removeAttribute("error_message");
    }
    %>

    <!-- Error message from request (for direct forward) -->
    <%
    String requestError = (String) request.getAttribute("error_message");
    if (requestError != null && !requestError.isEmpty()) {
    %>
    <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
        <i class="fad fa-exclamation-circle me-2"></i>
        <%= requestError %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <%
    }
    %>

    <!-- Success message from request -->
    <%
    String requestSuccess = (String) request.getAttribute("success_message");
    if (requestSuccess != null && !requestSuccess.isEmpty()) {
    %>
    <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
        <i class="fad fa-check-circle me-2"></i>
        <%= requestSuccess %>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <%
    }
    %>
    <!-- Results count -->
    <div class="flex-shrink-0 mb-2">
        <h5 class="fw-semibold mb-0 text-primary">
            <i class="fad fa-calendar-check me-2"></i> Liste des rendez-vous
            <%

            List
            <RdvPatMed> listRdv = (List
                <RdvPatMed>) request.getAttribute("listRdv");
                    int totalCount = (listRdv != null) ? listRdv.size() : 0;
                    %>
                    <span class="badge bg-secondary rounded-pill ms-2"
                          style="font-size: 0.7rem;"><%= totalCount %></span>
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
                            <th class="small"><i class="fad fa-user me-1"></i> Patient</th>
                            <th class="small"><i class="fad fa-birthday-cake me-1"></i> Date naissance</th>
                            <th class="small"><i class="fad fa-calendar-plus me-1"></i> Date de prise</th>
                            <th class="small"><i class="fad fa-calendar-clock me-1"></i> Date & Heure</th>
                            <th class="small"><i class="fad fa-chart-simple me-1"></i> Statut</th>
                            <th class="small"><i class="fad fa-money-bill-wave me-1"></i> Montant</th>
                            <th class="small"><i class="fad fa-cogs me-1"></i> Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                        if (listRdv == null || listRdv.isEmpty()) {
                        %>
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">
                                <i class="fad fa-calendar-times me-2"></i> Aucun rendez-vous trouvé
                            </td>
                        </tr>
                        <%
                        } else {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                        com.doctorapointment.model.Medecin medecin = (com.doctorapointment.model.Medecin)
                        session.getAttribute("medecin");
                        double tauxHoraire = (medecin != null) ? medecin.getTauxHoraire() : 20000;

                        for (RdvPatMed rdv : listRdv) {
                        long minutes = java.time.temporal.ChronoUnit.MINUTES.between(rdv.getHeureDebut(),
                        rdv.getHeureFin());
                        double montant = tauxHoraire * (minutes / 60.0);
                        %>
                        <tr>
                            <td class="align-middle"><%= rdv.getIdRdv() %></td>
                            <td class="align-middle">
                                <%= rdv.getRdvNomPat() %><br>
                                <small class="text-muted"><%= rdv.getEmailPat() %></small>
                            </td>
                            <td class="align-middle"><%= rdv.getDateNais().format(dateFormatter) %></td>
                            <td class="align-middle text-secondary"><%= rdv.getDatePrisRdv().format(dateTimeFormatter)
                                %>
                            </td>
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
                                badgeIcon = "fad fa-check-circle";
                                } else if (rdv.getEtatRdv().equals("en attente")) {
                                badgeClass = "badge bg-warning text-dark";
                                badgeIcon = "fad fa-hourglass-half";
                                } else if (rdv.getEtatRdv().equals("annulé")) {
                                badgeClass = "badge bg-danger";
                                badgeIcon = "fad fa-times-circle";
                                } else if (rdv.getEtatRdv().equals("dépassé")) {
                                badgeClass = "badge bg-secondary";
                                badgeIcon = "fad fa-clock";
                                } else {
                                badgeClass = "badge bg-secondary";
                                badgeIcon = "fad fa-question-circle";
                                }
                                %>
                                <span class="<%= badgeClass %>">
                                        <i class="<%= badgeIcon %> me-1"></i> <%= rdv.getEtatRdv() %>
                                    </span>
                            </td>
                            <td class="align-middle">
                                    <span class="fw-semibold text-success">
                                        <%= String.format("%,.0f", montant) %> Ar
                                    </span>
                            </td>
                            <td class="align-middle">
                                <%
                                String etat = rdv.getEtatRdv();
                                boolean isEnAttente = "en attente".equals(etat);
                                boolean isConfirme = "confirmé".equals(etat);
                                boolean isAnnule = "annulé".equals(etat);
                                %>

                                <div class="d-flex flex-column gap-1">
                                    <button class="btn btn-sm btn-outline-danger w-100"
                                            onclick="openCancelModal(<%= rdv.getIdRdv() %>)"
                                            data-bs-toggle="tooltip" title="Annuler"
                                    <%= isEnAttente ? "" : "disabled" %>>
                                    <i class="fad fa-ban me-1"></i> Annuler
                                    </button>

                                    <button class="btn btn-sm btn-outline-success w-100"
                                            onclick="openConfirmModal(<%= rdv.getIdRdv() %>)"
                                            data-bs-toggle="tooltip" title="Confirmer"
                                    <%= isEnAttente ? "" : "disabled" %>>
                                    <i class="fad fa-check-circle me-1"></i> Confirmer
                                    </button>

                                    <button class="btn btn-sm btn-outline-primary w-100"
                                            onclick="openNotifyModal(<%= rdv.getIdRdv() %>)"
                                            data-bs-toggle="tooltip" title="Notifier le patient"
                                    <%= (isAnnule || isConfirme) ? "" : "disabled" %>>
                                    <i class="fad fa-bell me-1"></i> Notifier
                                    </button>
                                </div>
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

<!-- Cancel Confirmation Modal -->
<div class="modal fade" id="cancelRdvModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <div class="modal-header bg-danger text-white border-0">
                <div class="d-flex align-items-center">
                    <i class="fad fa-exclamation-triangle fa-2x me-2"></i>
                    <h6 class="mb-0 fw-semibold">Annulation de rendez-vous</h6>
                </div>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center py-4">
                <i class="fad fa-calendar-times fa-4x text-danger mb-3"></i>
                <p class="mb-2 fw-semibold">Êtes-vous sûr de vouloir annuler ce rendez-vous ?</p>
                <small class="text-muted">Cette action est <strong class="text-danger">irréversible</strong>. Le patient sera notifié.</small>
            </div>
            <div class="modal-footer border-0 justify-content-center gap-3 pb-4">
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

<!-- Confirm Confirmation Modal -->
<div class="modal fade" id="confirmRdvModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <div class="modal-header bg-success text-white border-0">
                <div class="d-flex align-items-center">
                    <i class="fad fa-calendar-check fa-2x me-2"></i>
                    <h6 class="mb-0 fw-semibold">Confirmation de rendez-vous</h6>
                </div>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center py-4">
                <i class="fad fa-check-circle fa-4x text-success mb-3"></i>
                <p class="mb-2 fw-semibold">Confirmer ce rendez-vous ?</p>
                <small class="text-muted">Le patient recevra un email de confirmation.</small>
            </div>
            <div class="modal-footer border-0 justify-content-center gap-3 pb-4">
                <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
                    <i class="fad fa-times me-2"></i> Non, retour
                </button>
                <a href="#" class="btn btn-success px-4" id="confirmRdvBtn">
                    <i class="fad fa-check-circle me-2"></i> Oui, confirmer
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Notify Confirmation Modal -->
<div class="modal fade" id="notifyRdvModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg rounded-4">
            <div class="modal-header bg-primary text-white border-0">
                <div class="d-flex align-items-center">
                    <i class="fad fa-bell fa-2x me-2"></i>
                    <h6 class="mb-0 fw-semibold">Notification au patient</h6>
                </div>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body text-center py-4">
                <i class="fad fa-envelope fa-4x text-primary mb-3"></i>
                <p class="mb-2 fw-semibold">Notifier le patient par email ?</p>
                <small class="text-muted">Un email de rappel sera envoyé au patient pour ce rendez-vous.</small>
            </div>
            <div class="modal-footer border-0 justify-content-center gap-3 pb-4">
                <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">
                    <i class="fad fa-times me-2"></i> Annuler
                </button>
                <a href="#" class="btn btn-primary px-4" id="confirmNotifyBtn">
                    <i class="fad fa-paper-plane me-2"></i> Oui, notifier
                </a>
            </div>
        </div>
    </div>
</div>