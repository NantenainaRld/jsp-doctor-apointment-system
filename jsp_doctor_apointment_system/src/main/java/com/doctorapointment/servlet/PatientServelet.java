package com.doctorapointment.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import com.doctorapointment.dao.MedecinDAO;
import com.doctorapointment.model.Disponibilite;
import com.doctorapointment.model.Medecin;
import com.doctorapointment.model.Rdv;
import com.doctorapointment.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doctorapointment.model.Patient;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class PatientServelet
 */
@WebServlet("/patient")
public class PatientServelet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(PatientServelet.class);
    private PatientService patSer = new PatientService();

    /**
     * Default constructor.
     */
    public PatientServelet() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/patient?action=login");
            return;
        }

        switch (action) {
            // cancel rdv
            case "cancel_rdv": {
                // Check if patient is logged in
                if (session.getAttribute("patient") == null
                        || ((Patient) session.getAttribute("patient")).getIdPat() == null
                        || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/patient?action=login");
                    return;
                }

                // Get RDV ID from request
                String idRdvStr = request.getParameter("id");
                if (idRdvStr == null || idRdvStr.isEmpty()) {
                    request.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                int idRdv;
                try {
                    idRdv = Integer.parseInt(idRdvStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                // Update RDV status to "annulé"
                RdvService rdvSer = new RdvService();
                Rdv rdv = new Rdv();
                rdv.setIdRdv(idRdv);
                rdv.setRdvIdPat(((Patient) session.getAttribute("patient")).getIdPat());
                ServiceResult serRes = rdvSer.cancelRdvPatient(rdv);

                if (serRes.isSuccess()) {
                    request.setAttribute("success_message", "Rendez-vous annulé avec succès et " +
                            "un email d'annulation a été envoyé à votre adresse email" +
                            ".");
                } else {
                    request.setAttribute("error_message", serRes.getErrorMessage());
                }

                // Preserve current filters in session (no need to remove them)
                request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                break;
            }
            // Page: update rdv
            case "update_rdv": {
                // Verify patient is logged in
                if (session.getAttribute("patient") == null
                        || ((Patient) session.getAttribute("patient")).getIdPat() == null
                        || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/patient?action=login");
                    return;
                }

                // Get RDV ID from request
                String idRdvStr = request.getParameter("id");
                if (idRdvStr == null || idRdvStr.isEmpty()) {
                    request.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                int idRdv;
                try {
                    idRdv = Integer.parseInt(idRdvStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                // Fetch RDV details
                RdvService rdvSer = new RdvService();
                Rdv rdv = new Rdv();
                rdv.setIdRdv(idRdv);
                rdv.setRdvIdPat(((Patient) session.getAttribute("patient")).getIdPat());
                ServiceResult serRes = rdvSer.getRdvPatientById(rdv);

                if (serRes.isSuccess()) {
                    request.setAttribute("rdv", serRes.getData());
                    request.getRequestDispatcher("/WEB-INF/jsp/rdv/update_rdv.jsp").forward(request, response);
                } else {
                    request.setAttribute("error_message", serRes.getErrorMessage());
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                }
                break;
            }
            // rdv session
            case "rdv": {
                // User wants to see RDV page
                session.setAttribute("page_patient", "rdv");
                response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                break;
            }
            // medecin session
            case "med": {
                // User wants to see MED page
                session.setAttribute("page_patient", "med");
                response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                break;
            }
            // Page: dashboard
            case "dashboard": {
                // Verify patient is logged in
                if (session.getAttribute("patient") == null
                        || ((Patient) session.getAttribute("patient")).getIdPat() == null
                        || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/patient?action=login");
                    return;
                }

                // Get current page from session or default to "rdv"
                String currentPage = (String) session.getAttribute("page_patient");
                if (currentPage == null) {
                    currentPage = "rdv";
                    session.setAttribute("page_patient", "rdv");
                }

                // Get patient ID
                Patient patient = (Patient) session.getAttribute("patient");
                String rdvIdPat = patient.getIdPat();

                // Load data based on current page
                RdvService rdvSer = new RdvService();

                if (currentPage.equals("rdv")) {
                    // Load appointments for RDV page
                    // Retrieve filters from session
                    String search = (String) session.getAttribute("filterSearch");
                    String dateDebutStr = (String) session.getAttribute("filterDateDebut");
                    String dateFinStr = (String) session.getAttribute("filterDateFin");
                    String heureDebutStr = (String) session.getAttribute("filterHeureDebut");
                    String heureFinStr = (String) session.getAttribute("filterHeureFin");
                    String statut = (String) session.getAttribute("filterStatut");

                    // Convert date and time values
                    LocalDate dateDebut = (dateDebutStr != null && !dateDebutStr.isEmpty()) ? LocalDate.parse(dateDebutStr) : null;
                    LocalDate dateFin = (dateFinStr != null && !dateFinStr.isEmpty()) ? LocalDate.parse(dateFinStr) : null;
                    LocalTime heureDebut = (heureDebutStr != null && !heureDebutStr.isEmpty()) ? LocalTime.parse(heureDebutStr) : null;
                    LocalTime heureFin = (heureFinStr != null && !heureFinStr.isEmpty()) ? LocalTime.parse(heureFinStr) : null;

                    // Filter appointments with stored filters
                    ServiceResult serRes = rdvSer.filterRdvPatient(search, rdvIdPat, dateDebut, dateFin, statut, heureDebut, heureFin);

                    if (serRes.isSuccess()) {
                        request.setAttribute("listRdv", serRes.getData());
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        request.setAttribute("listRdv", new ArrayList<>());
                    }

                } else if (currentPage.equals("med")) {
                    // Load doctors list for MED page with filters
                    // Retrieve filters from session
                    String searchMed = (String) session.getAttribute("filterSearchMed");
                    String specialite = (String) session.getAttribute("filterSpecialite");
                    String taux = (String) session.getAttribute("filterTaux");

                    // Set default values if null
                    if (searchMed == null) searchMed = "";
                    if (specialite == null) specialite = "";
                    if (taux == null) taux = "";

                    // Filter doctors with stored filters
                    MedecinService medSer = new MedecinService();
                    ServiceResult medRes = medSer.filterMedecin(searchMed, specialite, taux);

                    if (medRes.isSuccess()) {
                        request.setAttribute("listMed", medRes.getData());
                    } else {
                        request.setAttribute("error_message", medRes.getErrorMessage());
                        request.setAttribute("listMed", new ArrayList<>());
                    }
                } else {
                    // Default to RDV page
                    session.setAttribute("page_patient", "rdv");
                    request.setAttribute("listRdv", new ArrayList<>());
                }

                // Forward to index page
                request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                break;
            }
            // empty filter med
            case "empty_filter_med": {
                // Remove filter attributes from session
                session.removeAttribute("filterSearchMed");
                session.removeAttribute("filterSpecialite");
                session.removeAttribute("filterTaux");

                // Keep current page as MED (stay on same page)
                session.setAttribute("page_patient", "med");

                // Redirect to dashboard with empty filters
                response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                break;
            }
            // Page: filter dispo
            case "filter_dispo": {
                // Verify patient is logged in
                if (session.getAttribute("patient") == null
                        || ((Patient) session.getAttribute("patient")).getIdPat() == null
                        || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/patient?action=login");
                    return;
                }

                // Get doctor ID from request
                String idMed = request.getParameter("idMed");
                if (idMed == null || idMed.isEmpty()) {
                    request.setAttribute("error_message", "Médecin non spécifié.");
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                // find medecin
                MedecinService medSer = new MedecinService();
                ServiceResult serRes = medSer.findById(idMed);
                if (serRes.isSuccess()) {
                    request.setAttribute("medecin", serRes.getData());
                } else {
                    request.setAttribute("error_message", serRes.getErrorMessage());
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                // get session values
                String dateDebutStr = (String) session.getAttribute("dispoFilterDateDebut"),
                        dateFinStr = (String) session.getAttribute("dispoFilterDateFin"),
                        heureDebutStr = (String) session.getAttribute("dispoFilterHeureDebut"),
                        heureFinStr = (String) session.getAttribute("dispoFilterHeureFin");

                // date/time parse
                LocalDate dateDebut, dateFin;
                LocalTime heureDebut, heureFin;
                try {
                    // Convert date and time values
                    dateDebut = (dateDebutStr != null && !dateDebutStr.trim().isEmpty()) ? LocalDate.parse(dateDebutStr.trim()) : null;
                    dateFin = (dateFinStr != null && !dateFinStr.trim().isEmpty()) ? LocalDate.parse(dateFinStr.trim()) : null;
                    heureDebut = (heureDebutStr != null && !heureDebutStr.trim().isEmpty()) ? LocalTime.parse(heureDebutStr.trim()) : null;
                    heureFin = (heureFinStr != null && !heureFinStr.trim().isEmpty()) ? LocalTime.parse(heureFinStr.trim()) : null;
                } catch (DateTimeParseException e) {
                    log.error("Error parsing datetim in filter dispo (get)", e);
                    request.setAttribute("error_message", "La date ou heure choisie est invalide.");
                    request.getRequestDispatcher("/WEB-INF/jsp/disponibilite/list_dispo.jsp").forward(request, response);
                    return;
                }

                // Filter disponibilities
                DisponibiliteService dispoSer = new DisponibiliteService();
                ServiceResult dispoRes = dispoSer.filterDispo(idMed, dateDebut, dateFin, heureDebut, heureFin);

                if (dispoRes.isSuccess()) {
                    request.setAttribute("listDispo", dispoRes.getData());
                } else {
                    request.setAttribute("error_message", dispoRes.getErrorMessage());
                    request.setAttribute("listDispo", new ArrayList<>());
                }

                request.getRequestDispatcher("/WEB-INF/jsp/disponibilite/list_dispo.jsp").forward(request, response);
                break;
            }
            // Clear disponibility filters
            case "clear_dispo_filters": {
                // Remove filter attributes from session
                session.removeAttribute("dispoFilterDateDebut");
                session.removeAttribute("dispoFilterDateFin");
                session.removeAttribute("dispoFilterHeureDebut");
                session.removeAttribute("dispoFilterHeureFin");

                // Get doctor ID from request
                String clearIdMed = request.getParameter("idMed");

                // Redirect to disponibility page with empty filters
                response.sendRedirect(request.getContextPath() + "/patient?action=filter_dispo&idMed=" + clearIdMed);
                break;
            }
            // Page: add rdv
            case "add_rdv": {
                // Verify patient is logged in
                if (session.getAttribute("patient") == null
                        || ((Patient) session.getAttribute("patient")).getIdPat() == null
                        || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/patient?action=login");
                    return;
                }

                // Get disponibility ID from request
                String idDispoStr = request.getParameter("idDispo");
                if (idDispoStr == null || idDispoStr.isEmpty()) {
                    request.setAttribute("error_message", "Disponibilité non spécifiée.");
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                int idDispo;
                try {
                    idDispo = Integer.parseInt(idDispoStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error_message", "Disponibilité invalide.");
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                // Fetch disponibility details
                DisponibiliteService dispoSer = new DisponibiliteService();
                ServiceResult dispoRes = dispoSer.findById(idDispo);

                if (!dispoRes.isSuccess()) {
                    request.setAttribute("error_message", dispoRes.getErrorMessage());
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                Disponibilite disponibilite = (Disponibilite) dispoRes.getData();

                // Fetch doctor details
                MedecinService medSer = new MedecinService();
                ServiceResult medRes = medSer.findById(disponibilite.getDispoIdMed());

                if (!medRes.isSuccess()) {
                    request.setAttribute("error_message", medRes.getErrorMessage());
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    return;
                }

                Medecin medecin = (Medecin) medRes.getData();

                // Get filter parameters for existing RDV table
                String filterHeureDebutStr = (String) session.getAttribute("filterHeureDebut");
                String filterHeureFinStr = (String) session.getAttribute("filterHeureFin");

                LocalTime filterHeureDebut = (filterHeureDebutStr != null && !filterHeureDebutStr.trim().isEmpty())
                        ? LocalTime.parse(filterHeureDebutStr.trim()) : null;
                LocalTime filterHeureFin = (filterHeureFinStr != null && !filterHeureFinStr.trim().isEmpty())
                        ? LocalTime.parse(filterHeureFinStr.trim()) : null;

                // Fetch existing RDV for this doctor on this date
                RdvService rdvSer = new RdvService();
                Rdv rdvSearch = new Rdv();
                rdvSearch.setDateRdv(disponibilite.getDateDispo());
                rdvSearch.setRdvIdMed(medecin.getIdMed());

                ServiceResult rdvListRes = rdvSer.filterRdvDispo(rdvSearch, filterHeureDebut, filterHeureFin);

                if (rdvListRes.isSuccess()) {
                    request.setAttribute("rdvList", rdvListRes.getData());
                } else {
                    request.setAttribute("rdvList", new ArrayList<>());
                    request.setAttribute("error_message", rdvListRes.getErrorMessage());
                }

                // Pass data to JSP
                request.setAttribute("medecin", medecin);
                request.setAttribute("disponibilite", disponibilite);

                request.getRequestDispatcher("/WEB-INF/jsp/rdv/add_rdv.jsp").forward(request, response);
                break;
            }
            // clear filter rdv dispo
            case "clear_filter_rdv_dispo": {
                session.removeAttribute("filterHeureDebut");
                session.removeAttribute("filterHeureFin");

                response.sendRedirect(request.getContextPath()
                        + "/patient?action=add_rdv&idDispo=" + request.getParameter("idDispo"));
                break;
            }
            // Page: update patient profile
            case "update_patient": {
                // Verify patient is logged in
                if (session.getAttribute("patient") == null
                        || ((Patient) session.getAttribute("patient")).getIdPat() == null
                        || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/patient?action=login");
                    return;
                }

                request.setAttribute("patient", (Patient) session.getAttribute("patient"));
                request.getRequestDispatcher("/WEB-INF/jsp/patient/update_patient.jsp").forward(request, response);
                break;
            }
            // Logout patient
            case "logout": {
                // Invalidate the session (remove all attributes)
                session.invalidate();

                // Redirect to login page
                response.sendRedirect(request.getContextPath() + "/patient?action=login");
                break;
            }
            // Delete patient account
            case "delete_account": {
                // Verify patient is logged in
                if (session.getAttribute("patient") == null) {
                    response.sendRedirect(request.getContextPath() + "/patient?action=login");
                    return;
                }

                Patient patient = (Patient) session.getAttribute("patient");
                String patientId = patient.getIdPat();

                // Delete patient via service
                PatientService patSer = new PatientService();
                ServiceResult serRes = patSer.deletePatient(patient);

                if (serRes.isSuccess()) {
                    session.invalidate();
                    response.sendRedirect(request.getContextPath() + "/patient?action=login&deleted=true");
                } else {
                    // Use request attribute instead of session
                    request.setAttribute("error_message", serRes.getErrorMessage());

                    // Refresh patient data in session (if still exists)
                    ServiceResult freshPatient = patSer.findById(patientId);
                    if (freshPatient.isSuccess()) {
                        session.setAttribute("patient", freshPatient.getData());
                    }

                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                }
                break;
            }
            // Page: login
            case "login": {
                // Check if patient is already logged in
                if (session.getAttribute("patient") != null) {
                    // Already logged in, redirect to dashboard
                    response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                    return;
                }

                // Forward to login page
                request.getRequestDispatcher("/WEB-INF/jsp/patient/login.jsp").forward(request, response);
                break;
            }
            // Page: register - GET handler
            case "register": {
                // Check if patient is already logged in
                if (session.getAttribute("patient") != null) {
                    // Already logged in, redirect to dashboard
                    response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                    return;
                }
                // Forward to register page
                request.getRequestDispatcher("/WEB-INF/jsp/patient/register.jsp").forward(request, response);
                break;
            }
            default:
                request.setAttribute("message", "Page non trouvée");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                break;
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // validate action
        if (action == null || action.trim().isEmpty()) {
            request.setAttribute("message", "Aucune action spécifiée");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                // filter rdv
                case "filter_rdv": {
                    // Session validation - check if patient is logged in
                    if (session.getAttribute("patient") == null
                            || ((Patient) session.getAttribute("patient")).getIdPat() == null
                            || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/patient?action=login");
                        return;
                    }

                    // Retrieve form values from request
                    String dateDebutStr = request.getParameter("filterDateDebut");
                    String dateFinStr = request.getParameter("filterDateFin");
                    String heureDebutStr = request.getParameter("filterHeureDebut");
                    String heureFinStr = request.getParameter("filterHeureFin");
                    String statut = request.getParameter("filterStatut");
                    String search = request.getParameter("filterSearch");

                    // Persist filter values in session for form pre-filling
                    session.setAttribute("filterDateDebut", dateDebutStr);
                    session.setAttribute("filterDateFin", dateFinStr);
                    session.setAttribute("filterHeureDebut", heureDebutStr);
                    session.setAttribute("filterHeureFin", heureFinStr);
                    session.setAttribute("filterStatut", statut);
                    session.setAttribute("filterSearch", search);

                    // Convert date and time values (null if empty)
                    LocalDate dateDebut = (dateDebutStr != null && !dateDebutStr.isEmpty()) ? LocalDate.parse(dateDebutStr) : null;
                    LocalDate dateFin = (dateFinStr != null && !dateFinStr.isEmpty()) ? LocalDate.parse(dateFinStr) : null;
                    LocalTime heureDebut = (heureDebutStr != null && !heureDebutStr.isEmpty()) ? LocalTime.parse(heureDebutStr) : null;
                    LocalTime heureFin = (heureFinStr != null && !heureFinStr.isEmpty()) ? LocalTime.parse(heureFinStr) : null;

                    // Get patient ID from session
                    Patient patient = (Patient) session.getAttribute("patient");
                    String rdvIdPat = patient.getIdPat();

                    // Apply filters
                    RdvService rdvSer = new RdvService();
                    ServiceResult serRes = rdvSer.filterRdvPatient(search, rdvIdPat, dateDebut, dateFin, statut, heureDebut, heureFin);

                    // Set response attributes
                    if (serRes.isSuccess()) {
                        request.setAttribute("listRdv", serRes.getData());
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                    }

                    // Forward to patient index page
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    break;
                }
                // empty filter rdv
                case "empty_rdv_filter": {
                    // Remove filter attributes from session
                    session.removeAttribute("filterDateDebut");
                    session.removeAttribute("filterDateFin");
                    session.removeAttribute("filterHeureDebut");
                    session.removeAttribute("filterHeureFin");
                    session.removeAttribute("filterStatut");
                    session.removeAttribute("filterSearch");

                    // Redirect to RDV page with empty filters
                    response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                    break;
                }
                // update rdv
                case "update_rdv": {
                    // Verify patient is logged in
                    if (session.getAttribute("patient") == null
                            || ((Patient) session.getAttribute("patient")).getIdPat() == null
                            || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/patient?action=login");
                        return;
                    }

                    // Get form parameters
                    String rdvIdStr = request.getParameter("id");
                    String heureDebutStr = request.getParameter("heureDebut");
                    String heureFinStr = request.getParameter("heureFin");

                    // Validate parameters
                    if (rdvIdStr == null || rdvIdStr.isEmpty()) {
                        request.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                        request.getRequestDispatcher("/WEB-INF/jsp/rdv/update_rdv.jsp").forward(request, response);
                        return;
                    }

                    // Parse values
                    int rdvId;
                    LocalDate dateRdv;
                    LocalTime heureDebut;
                    LocalTime heureFin;

                    try {
                        rdvId = Integer.parseInt(rdvIdStr);
                        heureDebut = LocalTime.parse(heureDebutStr);
                        heureFin = LocalTime.parse(heureFinStr);
                    } catch (Exception e) {
                        request.setAttribute("error_message", "Format de l'heure invalide.");
                        request.getRequestDispatcher("/WEB-INF/jsp/rdv/update_rdv.jsp").forward(request, response);
                        return;
                    }

                    // Get patient from session
                    Patient patient = (Patient) session.getAttribute("patient");
                    String rdvIdPat = patient.getIdPat();

                    // Build RDV object
                    Rdv rdv = new Rdv();
                    rdv.setIdRdv(rdvId);
                    rdv.setHeureDebut(heureDebut);
                    rdv.setHeureFin(heureFin);
                    rdv.setRdvIdPat(rdvIdPat);

                    // Update RDV
                    RdvService rdvSer = new RdvService();
                    ServiceResult serRes = rdvSer.updateRdvPat(rdv);

                    if (serRes.isSuccess()) {
                        request.setAttribute("success_message", "Rendez-vous modifié avec succès.");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());

                        // Re-fetch RDV to display in form
                        Rdv rdvFind = new Rdv();
                        rdvFind.setIdRdv(rdvId);
                        rdvFind.setRdvIdPat(((Patient) session.getAttribute("patient")).getIdPat());
                        ServiceResult serResFind = rdvSer.getRdvPatientById(rdv);

                        if (serResFind.isSuccess()) {
                            request.setAttribute("rdv", serResFind.getData());
                            request.getRequestDispatcher("/WEB-INF/jsp/rdv/update_rdv.jsp").forward(request, response);
                        } else {
                            request.setAttribute("error_message", serResFind.getErrorMessage());
                            request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                        }
                    }
                    break;
                }
                // filter doctors
                case "filter_med": {
                    // Get form values from request
                    String searchMed = request.getParameter("filterSearchMed");
                    String specialite = request.getParameter("filterSpecialite");
                    String taux = request.getParameter("filterTaux");

                    // Set default values if null
                    if (searchMed == null) searchMed = "";
                    if (specialite == null) specialite = "";
                    if (taux == null) taux = "";

                    // Save filters to session
                    session.setAttribute("filterSearchMed", searchMed);
                    session.setAttribute("filterSpecialite", specialite);
                    session.setAttribute("filterTaux", taux);

                    // Set current page to MED
                    session.setAttribute("page_patient", "med");

                    // Redirect to dashboard to apply filters and refresh
                    response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                    break;
                }
                // filter dispo
                case "filter_dispo": {
                    // Retrieve form values from request
                    String idMed = request.getParameter("idMed");
                    String dateDebutStr = request.getParameter("filterDateDebut");
                    String dateFinStr = request.getParameter("filterDateFin");
                    String heureDebutStr = request.getParameter("filterHeureDebut");
                    String heureFinStr = request.getParameter("filterHeureFin");

                    // Save filter values to session for persistence after page reload
                    session.setAttribute("dispoFilterDateDebut", dateDebutStr);
                    session.setAttribute("dispoFilterDateFin", dateFinStr);
                    session.setAttribute("dispoFilterHeureDebut", heureDebutStr);
                    session.setAttribute("dispoFilterHeureFin", heureFinStr);

                    // Redirect to GET handler (idMed must be in URL for proper display)
                    response.sendRedirect(request.getContextPath() +
                            "/patient?action=filter_dispo&idMed=" + idMed);
                    break;
                }
                // filter rdv dispo
                case "filter_rdv_dispo": {
                    // get parametters
                    String heureDebut = request.getParameter("filterHeureDebut");
                    String heureFin = request.getParameter("filterHeureFin");
                    String idDispo = request.getParameter("idDispo");

                    session.setAttribute("filterHeureDebut", heureDebut == null ? "" : heureDebut);
                    session.setAttribute("filterHeureFin", heureFin == null ? "" : heureFin);

                    response.sendRedirect(request.getContextPath() + "/patient?action=add_rdv&idDispo=" + idDispo);
                    break;
                }
                // add rdv
                case "add_rdv": {
                    // Verify patient is logged in
                    if (session.getAttribute("patient") == null
                            || ((Patient) session.getAttribute("patient")).getIdPat() == null
                            || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/patient?action=login");
                        return;
                    }

                    // Get form parameters
                    String idDispoStr = request.getParameter("idDispo");
                    String heureDebutStr = request.getParameter("heureDebut");
                    String heureFinStr = request.getParameter("heureFin");

                    if (heureDebutStr == null || heureDebutStr.isEmpty() || heureFinStr == null || heureFinStr.isEmpty()) {
                        request.setAttribute("error_message", "Heure de début et heure de fin requises.");
                        request.getRequestDispatcher("/WEB-INF/jsp/rdv/add_rdv.jsp").forward(request, response);
                        return;
                    }

                    // Parse date and time values
                    LocalTime heureDebut;
                    LocalTime heureFin;

                    try {
                        heureDebut = LocalTime.parse(heureDebutStr);
                        heureFin = LocalTime.parse(heureFinStr);
                    } catch (Exception e) {
                        request.setAttribute("error_message", "Format de date ou d'heure invalide.");
                        request.getRequestDispatcher("/WEB-INF/jsp/rdv/add_rdv.jsp").forward(request, response);
                        return;
                    }

                    // Validation: disponibilite
                    if (idDispoStr == null || idDispoStr.isEmpty()) {
                        request.setAttribute("error_message", "Disponibilité non spécifiée.");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                        return;
                    }

                    int idDispo;
                    try {
                        idDispo = Integer.parseInt(idDispoStr);
                    } catch (NumberFormatException e) {
                        request.setAttribute("error_message", "Disponibilité invalide.");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                        return;
                    }

                    // Fetch disponibility details
                    DisponibiliteService dispoSer = new DisponibiliteService();
                    ServiceResult dispoRes = dispoSer.findById(idDispo);
                    if (!dispoRes.isSuccess()) {
                        request.setAttribute("error_message", dispoRes.getErrorMessage());
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                        return;
                    }
                    Disponibilite disponibilite = (Disponibilite) dispoRes.getData();
                    request.setAttribute("disponibilite", disponibilite);

                    // fetch medecin details
                    MedecinService medSer = new MedecinService();
                    ServiceResult medSerRes = medSer.findById(disponibilite.getDispoIdMed());
                    if (!medSerRes.isSuccess()) {
                        request.setAttribute("error_message", medSerRes.getErrorMessage());
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                        return;
                    }
                    request.setAttribute("medecin", medSerRes.getData());

                    // set param to session
                    session.setAttribute("heureDebutAddRdv", heureDebutStr);
                    session.setAttribute("heureFinAddRdv", heureFinStr);

                    // Get patient from session
                    Patient patient = (Patient) session.getAttribute("patient");
                    String rdvIdPat = patient.getIdPat();

                    // Build RDV object
                    Rdv rdv = new Rdv();
                    rdv.setRdvIdMed(disponibilite.getDispoIdMed());
                    rdv.setRdvIdPat(rdvIdPat);
                    rdv.setDateRdv(disponibilite.getDateDispo());
                    rdv.setHeureDebut(heureDebut);
                    rdv.setHeureFin(heureFin);

                    // Call service to add RDV
                    RdvService rdvSer = new RdvService();
                    ServiceResult serRes = rdvSer.addRdv(rdv);

                    if (serRes.isSuccess()) {
                        request.setAttribute("success_message", "Rendez-vous ajouté avec succès !");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/index.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        request.getRequestDispatcher("/WEB-INF/jsp/rdv/add_rdv.jsp").forward(request, response);
                    }
                    break;
                }
                // Update patient profile
                case "update_patient": {
                    // Verify patient is logged in
                    if (session.getAttribute("patient") == null
                            || ((Patient) session.getAttribute("patient")).getIdPat() == null
                            || ((Patient) session.getAttribute("patient")).getIdPat().isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/patient?action=login");
                        return;
                    }

                    // Get form parameters
                    String idPat = request.getParameter("idPat");
                    String nomPat = request.getParameter("nomPat");
                    String prenomPat = request.getParameter("prenomPat");
                    String dateNaisStr = request.getParameter("dateNais");
                    String emailPat = request.getParameter("emailPat");
                    String mdpPat = request.getParameter("mdpPat");

                    // Validate required fields
                    if (idPat == null || idPat.isEmpty()) {
                        request.setAttribute("error_message", "Identifiant patient manquant.");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/update_patient.jsp").forward(request, response);
                        return;
                    }

                    // Parse date of birth
                    LocalDate dateNais;
                    try {
                        dateNais = LocalDate.parse(dateNaisStr);
                    } catch (Exception e) {
                        request.setAttribute("error_message", "Format de date de naissance invalide.");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/update_patient.jsp").forward(request, response);
                        return;
                    }

                    // Build Patient object
                    Patient patient = new Patient();
                    patient.setIdPat(idPat);
                    patient.setNomPat(nomPat);
                    patient.setPrenomPat(prenomPat);
                    patient.setDateNais(dateNais);
                    patient.setEmailPat(emailPat);
                    patient.setMdpPat(mdpPat);

                    // Update patient via service
                    PatientService patSer = new PatientService();
                    ServiceResult serRes = patSer.updatePatient(patient);

                    if (serRes.isSuccess()) {
                        // Refresh session patient data
                        ServiceResult freshPatient = patSer.findById(idPat);
                        if (freshPatient.isSuccess()) {
                            session.setAttribute("patient", freshPatient.getData());
                        }
                        request.setAttribute("success_message", "Votre profil a été mis à jour avec succès.");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/update_patient.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        // Fetch current patient data to preserve form values
                        ServiceResult currentPatient = patSer.findById(idPat);
                        if (currentPatient.isSuccess()) {
                            request.setAttribute("patient", currentPatient.getData());
                        }
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/update_patient.jsp").forward(request, response);
                    }
                    break;
                }
                // Login patient
                case "login_submit": {
                    // Check if already logged in
                    if (session.getAttribute("patient") != null) {
                        response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                        return;
                    }

                    // Get form parameters
                    String emailPat = request.getParameter("emailPat");
                    String mdpPat = request.getParameter("mdpPat");

                    // Validate input
                    if (emailPat == null || emailPat.isEmpty() || mdpPat == null || mdpPat.isEmpty()) {
                        request.setAttribute("error_message", "Veuillez remplir tous les champs.");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/login.jsp").forward(request, response);
                        return;
                    }

                    // Authenticate patient
                    PatientService patSer = new PatientService();
                    ServiceResult serRes = patSer.loginPatient(emailPat, mdpPat);

                    if (serRes.isSuccess()) {
                        // Login successful
                        Patient patient = (Patient) serRes.getData();
                        session.setAttribute("patient", patient);
                        session.setAttribute("page_patient", "rdv");

                        // Remove temporary login email from session
                        session.removeAttribute("loginEmail");

                        // Redirect to dashboard
                        response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                    } else {
                        // Login failed
                        session.setAttribute("loginEmail", emailPat); // Save email for pre-filling
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/login.jsp").forward(request, response);
                    }
                    break;
                }
                // Register patient - POST handler
                case "register_submit": {
                    // Check if already logged in
                    if (session.getAttribute("patient") != null) {
                        response.sendRedirect(request.getContextPath() + "/patient?action=dashboard");
                        return;
                    }

                    // Get form parameters
                    String nomPat = request.getParameter("nomPat");
                    String prenomPat = request.getParameter("prenomPat");
                    String dateNaisStr = request.getParameter("dateNais");
                    String emailPat = request.getParameter("emailPat");
                    String mdpPat = request.getParameter("mdpPat");

                    // Save form values to session for pre-filling (in case of error)
                    session.setAttribute("regNomPat", nomPat);
                    session.setAttribute("regPrenomPat", prenomPat);
                    session.setAttribute("regDateNais", dateNaisStr);
                    session.setAttribute("regEmailPat", emailPat);

                    // Parse date of birth
                    LocalDate dateNais;
                    try {
                        dateNais = LocalDate.parse(dateNaisStr);
                    } catch (Exception e) {
                        request.setAttribute("error_message", "Format de date de naissance invalide.");
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/register.jsp").forward(request, response);
                        return;
                    }

                    // Build Patient object
                    Patient patient = new Patient();
                    patient.setNomPat(nomPat);
                    patient.setPrenomPat(prenomPat);
                    patient.setDateNais(dateNais);
                    patient.setEmailPat(emailPat);
                    patient.setMdpPat(mdpPat);

                    // Register patient via service
                    PatientService patSer = new PatientService();
                    ServiceResult serRes = patSer.registerPatient(patient);

                    if (serRes.isSuccess()) {
                        // Clear temporary session attributes
                        session.removeAttribute("regNomPat");
                        session.removeAttribute("regPrenomPat");
                        session.removeAttribute("regDateNais");
                        session.removeAttribute("regEmailPat");

                        // Redirect to login page with success message
                        response.sendRedirect(request.getContextPath() + "/patient?action=login&registered=true");
                    } else {
                        // Registration failed
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        request.getRequestDispatcher("/WEB-INF/jsp/patient/register.jsp").forward(request, response);
                    }
                    break;
                }
                default:
                    request.setAttribute("message", "L'action demandée n'est pas trouvée");
                    request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("message", "Une erreur technique est survenue.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            log.error("Error at patient post route", e);
        }
    }

}
