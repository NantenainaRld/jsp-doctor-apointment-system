package com.doctorapointment.servlet;

import com.doctorapointment.model.*;
import com.doctorapointment.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Servlet implementation class AdminServelet
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
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
            response.sendRedirect(request.getContextPath() + "/admin?action=login");
            return;
        }

        switch (action) {
            case "dashboard": {
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                String pageAdmin = (String) session.getAttribute("page_admin");
                if (pageAdmin == null || pageAdmin.isEmpty()) {
                    pageAdmin = "medecin";
                    session.setAttribute("page_admin", "medecin");
                }

                // Load data based on current page
                MedecinService medSer = new MedecinService();
                PatientService patSer = new PatientService();
                RdvService rdvSer = new RdvService();

                if (pageAdmin.equals("medecin")) {
                    // Retrieve filters from session
                    String search = (String) session.getAttribute("filterSearchMedecin");
                    String specialite = (String) session.getAttribute("filterSpecialiteMedecin");
                    String taux = (String) session.getAttribute("filterTauxMedecin");

                    // Set default values
                    if (search == null) search = "";
                    if (specialite == null) specialite = "";
                    if (taux == null) taux = "";

                    // Filter medecins
                    ServiceResult serRes = medSer.filterMedecin(search, specialite, taux);

                    if (serRes.isSuccess()) {
                        request.setAttribute("listMedecins", serRes.getData());
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        request.setAttribute("listMedecins", new ArrayList<>());
                    }

                } else if (pageAdmin.equals("patient")) {
                    // Retrieve filters from session
                    String search = (String) session.getAttribute("filterSearchPatientAdmin");
                    String dateNaisDebut = (String) session.getAttribute("filterDateNaisDebut");
                    String dateNaisFin = (String) session.getAttribute("filterDateNaisFin");

                    // Set default values
                    if (search == null) search = "";

                    // Filter patients
                    ServiceResult serRes = patSer.filterPatient(search, dateNaisDebut, dateNaisFin);

                    if (serRes.isSuccess()) {
                        request.setAttribute("listPatients", serRes.getData());
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        request.setAttribute("listPatients", new ArrayList<>());
                    }

                    // Empty other lists
                    request.setAttribute("listMedecins", new ArrayList<>());
                    request.setAttribute("listRdv", new ArrayList<>());
                } else if (pageAdmin.equals("rdv")) {
                    // Retrieve filters from session
                    String search = (String) session.getAttribute("filterSearchAdminRdv");
                    String dateDebutStr = (String) session.getAttribute("filterDateDebutAdmin");
                    String dateFinStr = (String) session.getAttribute("filterDateFinAdmin");
                    String heureDebutStr = (String) session.getAttribute("filterHeureDebutAdmin");
                    String heureFinStr = (String) session.getAttribute("filterHeureFinAdmin");
                    String statut = (String) session.getAttribute("filterStatutAdmin");

                    // Set default values
                    if (search == null) search = "";
                    if (statut == null) statut = "all";

                    // Convert values
                    LocalDate dateDebut = (dateDebutStr != null && !dateDebutStr.isEmpty()) ? LocalDate.parse(dateDebutStr) : null;
                    LocalDate dateFin = (dateFinStr != null && !dateFinStr.isEmpty()) ? LocalDate.parse(dateFinStr) : null;
                    LocalTime heureDebut = (heureDebutStr != null && !heureDebutStr.isEmpty()) ? LocalTime.parse(heureDebutStr) : null;
                    LocalTime heureFin = (heureFinStr != null && !heureFinStr.isEmpty()) ? LocalTime.parse(heureFinStr) : null;

                    // Filter RDV
                    ServiceResult serRes = rdvSer.filterRdvAdmin(search, dateDebut, dateFin, statut, heureDebut, heureFin);

                    if (serRes.isSuccess()) {
                        request.setAttribute("listRdv", serRes.getData());
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        request.setAttribute("listRdv", new ArrayList<>());
                    }

                    // Empty other lists
                    request.setAttribute("listMedecins", new ArrayList<>());
                    request.setAttribute("listPatients", new ArrayList<>());
                }

                request.getRequestDispatcher("/WEB-INF/jsp/admin/index.jsp").forward(request, response);
                break;
            }
            // Page: login - GET handler
            case "login": {
                // Check if already logged in
                if (session.getAttribute("admin") != null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }
                request.getRequestDispatcher("/WEB-INF/jsp/admin/login.jsp").forward(request, response);
                break;
            }
            // Logout admin
            case "logout": {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/admin?action=login");
                break;
            }
            // Clear medecin filters - GET handler
            case "clear_medecin_filters": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Remove all filter attributes from session
                session.removeAttribute("filterSearchMedecin");
                session.removeAttribute("filterSpecialiteMedecin");
                session.removeAttribute("filterTauxMedecin");

                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Edit medecin - show update form
            case "edit_medecin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get medecin ID from request
                String idMed = request.getParameter("id");
                if (idMed == null || idMed.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du médecin manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Fetch medecin details
                MedecinService medSer = new MedecinService();
                ServiceResult serRes = medSer.findById(idMed);

                if (serRes.isSuccess()) {
                    request.setAttribute("medecin", serRes.getData());
                    request.getRequestDispatcher("/WEB-INF/jsp/medecin/update_medecin.jsp").forward(request, response);
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                }
                break;
            }
            // Delete medecin - GET handler
            case "delete_medecin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get medecin ID from request
                String idMed = request.getParameter("id");
                if (idMed == null || idMed.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du médecin manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Call service to delete medecin
                MedecinService medSer = new MedecinService();
                ServiceResult serRes = medSer.deleteMed(idMed);

                if (serRes.isSuccess()) {
                    session.setAttribute("success_message", "Médecin supprimé avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // View disponibilities for a specific medecin
            case "disponibilite_medecin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                String idMed = request.getParameter("id");
                if (idMed == null || idMed.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du médecin manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Fetch medecin details
                MedecinService medSer = new MedecinService();
                ServiceResult medRes = medSer.findById(idMed);

                if (!medRes.isSuccess()) {
                    session.setAttribute("error_message", medRes.getErrorMessage());
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Fetch filters from session
                String dateDebutStr = (String) session.getAttribute("dispoAdminFilterDateDebut");
                String dateFinStr = (String) session.getAttribute("dispoAdminFilterDateFin");
                String heureDebutStr = (String) session.getAttribute("dispoAdminFilterHeureDebut");
                String heureFinStr = (String) session.getAttribute("dispoAdminFilterHeureFin");

                // Convert values
                LocalDate dateDebut = (dateDebutStr != null && !dateDebutStr.isEmpty()) ? LocalDate.parse(dateDebutStr) : null;
                LocalDate dateFin = (dateFinStr != null && !dateFinStr.isEmpty()) ? LocalDate.parse(dateFinStr) : null;
                LocalTime heureDebut = (heureDebutStr != null && !heureDebutStr.isEmpty()) ? LocalTime.parse(heureDebutStr) : null;
                LocalTime heureFin = (heureFinStr != null && !heureFinStr.isEmpty()) ? LocalTime.parse(heureFinStr) : null;

                // Filter disponibilities
                DisponibiliteService dispoSer = new DisponibiliteService();
                ServiceResult dispoRes = dispoSer.filterDispoAdmin(idMed, dateDebut, dateFin, heureDebut, heureFin);

                request.setAttribute("medecin", medRes.getData());
                if (dispoRes.isSuccess()) {
                    request.setAttribute("listDispo", dispoRes.getData());
                } else {
                    request.setAttribute("error_message", dispoRes.getErrorMessage());
                    request.setAttribute("listDispo", new ArrayList<>());
                }

                request.getRequestDispatcher("/WEB-INF/jsp/disponibilite/list_dispo_admin.jsp").forward(request, response);
                break;
            }
            // Clear disponibility filters for admin - GET handler
            case "clear_dispo_admin_filters": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                String idMed = request.getParameter("idMed");
                if (idMed == null || idMed.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du médecin manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Remove filter attributes from session
                session.removeAttribute("dispoAdminFilterDateDebut");
                session.removeAttribute("dispoAdminFilterDateFin");
                session.removeAttribute("dispoAdminFilterHeureDebut");
                session.removeAttribute("dispoAdminFilterHeureFin");

                // Redirect to disponibilite page with cleared filters
                response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                break;
            }
            // Delete disponibility - Admin POST handler
            case "delete_dispo_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                String idDispoStr = request.getParameter("id");
                String idMed = request.getParameter("idMed");

                if (idDispoStr == null || idDispoStr.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du créneau manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                    return;
                }

                int idDispo;
                try {
                    idDispo = Integer.parseInt(idDispoStr);
                } catch (NumberFormatException e) {
                    session.setAttribute("error_message", "Identifiant invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                    return;
                }

                DisponibiliteService dispoSer = new DisponibiliteService();
                ServiceResult serRes = dispoSer.deleteDispo(idDispo);

                if (serRes.isSuccess()) {
                    session.setAttribute("success_message", "Créneau supprimé avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                break;
            }
            // Clear patient filters - GET handler
            case "clear_patient_filters": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Remove filter attributes from session
                session.removeAttribute("filterSearchPatientAdmin");
                session.removeAttribute("filterDateNaisDebut");
                session.removeAttribute("filterDateNaisFin");

                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Page: redirect to medecin list
            case "medecin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                session.setAttribute("page_admin", "medecin");
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Page: redirect to patient list
            case "patient": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                session.setAttribute("page_admin", "patient");
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Page: redirect to rdv list
            case "rdv": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                session.setAttribute("page_admin", "rdv");
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Edit patient - show update form (admin)
            case "edit_patient": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get patient ID from request
                String idPat = request.getParameter("id");
                if (idPat == null || idPat.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du patient manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Fetch patient details
                PatientService patSer = new PatientService();
                ServiceResult serRes = patSer.findById(idPat);

                if (serRes.isSuccess()) {
                    request.setAttribute("patient", serRes.getData());
                    request.getRequestDispatcher("/WEB-INF/jsp/patient/update_patient.jsp").forward(request, response);
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                }
                break;
            }
            // Delete patient - GET handler
            case "delete_patient": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get patient ID from request
                String idPat = request.getParameter("id");
                if (idPat == null || idPat.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du patient manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Build Patient object
                Patient patient = new Patient();
                patient.setIdPat(idPat);

                // Call service to delete patient
                PatientService patSer = new PatientService();
                ServiceResult serRes = patSer.deletePatient(patient);

                if (serRes.isSuccess()) {
                    session.setAttribute("success_message", "Patient supprimé avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Clear RDV filters for admin - GET handler
            case "clear_rdv_filters": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Remove filter attributes from session
                session.removeAttribute("filterSearchAdminRdv");
                session.removeAttribute("filterDateDebutAdmin");
                session.removeAttribute("filterDateFinAdmin");
                session.removeAttribute("filterHeureDebutAdmin");
                session.removeAttribute("filterHeureFinAdmin");
                session.removeAttribute("filterStatutAdmin");

                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Cancel RDV by admin - GET handler
            case "cancel_rdv_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get RDV ID from request
                String rdvIdStr = request.getParameter("id");
                if (rdvIdStr == null || rdvIdStr.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                int rdvId;
                try {
                    rdvId = Integer.parseInt(rdvIdStr);
                } catch (NumberFormatException e) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Build RDV object
                Rdv rdv = new Rdv();
                rdv.setIdRdv(rdvId);

                // Cancel RDV via service
                RdvService rdvSer = new RdvService();
                ServiceResult serRes = rdvSer.cancelRdv(rdv);

                if (serRes.isSuccess()) {
                    if (serRes.isSuccess()) {
                        session.setAttribute("success_message", "Rendez-vous annulé et notification envoyée.");
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                    }
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Confirm RDV by admin - GET handler
            case "confirm_rdv_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get RDV ID from request
                String rdvIdStr = request.getParameter("id");
                if (rdvIdStr == null || rdvIdStr.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                int rdvId;
                try {
                    rdvId = Integer.parseInt(rdvIdStr);
                } catch (NumberFormatException e) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Build RDV object
                Rdv rdv = new Rdv();
                rdv.setIdRdv(rdvId);

                // Confirm RDV via service
                RdvService rdvSer = new RdvService();
                ServiceResult serRes = rdvSer.confirmRdv(rdv);

                if (serRes.isSuccess()) {
                    if (serRes.isSuccess()) {
                        session.setAttribute("success_message", "Rendez-vous confirmé et notification envoyée.");
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                    }
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Notify RDV by admin - GET handler
            case "notify_rdv_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get RDV ID from request
                String rdvIdStr = request.getParameter("id");
                if (rdvIdStr == null || rdvIdStr.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                int rdvId;
                try {
                    rdvId = Integer.parseInt(rdvIdStr);
                } catch (NumberFormatException e) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Find RDV to check status
                RdvService rdvSer = new RdvService();
                ServiceResult serRes =rdvSer.findById(rdvId);
                Rdv rdvFind = (Rdv) serRes.getData();

                if (rdvFind == null) {
                    session.setAttribute("error_message", "Rendez-vous non trouvé.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Build RDV object
                Rdv rdv = new Rdv();
                rdv.setIdRdv(rdvId);

                // Choose notification method based on RDV status
                if (rdvFind.getEtatRdv().equals("annulé")) {
                    serRes = rdvSer.notifyCancelRdv(rdv);
                } else if (rdvFind.getEtatRdv().equals("confirmé")) {
                    serRes = rdvSer.notifyConfirmRdv(rdv);
                } else {
                    session.setAttribute("error_message", "Seul un rendez-vous annulé ou confirmé peut être notifié.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (serRes.isSuccess()) {
                    session.setAttribute("success_message", "Notification envoyée au patient avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Delete RDV by admin - GET handler
            case "delete_rdv_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get RDV ID from request
                String rdvIdStr = request.getParameter("id");
                if (rdvIdStr == null || rdvIdStr.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                int rdvId;
                try {
                    rdvId = Integer.parseInt(rdvIdStr);
                } catch (NumberFormatException e) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Build RDV object
                Rdv rdv = new Rdv();
                rdv.setIdRdv(rdvId);

                // Delete RDV via service
                RdvService rdvSer = new RdvService();
                ServiceResult serRes = rdvSer.deleteRdv(rdv);

                if (serRes.isSuccess()) {
                    session.setAttribute("success_message", "Rendez-vous supprimé avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            default: {
                request.setAttribute("message", "Page non trouvée");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                break;
            }
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

        if (action == null || action.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin?action=login");
            return;
        }

        switch (action) {
            // Login admin - POST handler
            case "login_submit": {
                // Check if already logged in
                if (session.getAttribute("admin") != null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Get form parameters
                String identifiant = request.getParameter("identifiant");
                String password = request.getParameter("password");

                // Hardcoded admin credentials
                if ("admin".equals(identifiant) && "000000".equals(password)) {
                    // Login successful
                    session.setAttribute("admin", identifiant);
                    session.setAttribute("page_admin", "medecin");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                } else {
                    // Login failed
                    request.setAttribute("error_message", "Identifiant ou mot de passe incorrect.");
                    request.getRequestDispatcher("/WEB-INF/jsp/admin/login.jsp").forward(request, response);
                }
                break;
            }
            // Filter medecins - POST handler
            case "filter_medecin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get form values from request
                String search = request.getParameter("filterSearch");
                String specialite = request.getParameter("filterSpecialite");
                String taux = request.getParameter("filterTaux");

                // Save filters to session
                session.setAttribute("filterSearchMedecin", search);
                session.setAttribute("filterSpecialiteMedecin", specialite);
                session.setAttribute("filterTauxMedecin", taux);

                // Redirect to dashboard
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            case "add_medecin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get form parameters
                String nomMed = request.getParameter("nomMed");
                String prenomMed = request.getParameter("prenomMed");
                String specialite = request.getParameter("specialite");
                String lieu = request.getParameter("lieu");
                String tauxHoraireStr = request.getParameter("tauxHoraire");
                String mdpMed = request.getParameter("mdpMed");

                // Save form values to session for pre-filling (in case of error)
                session.setAttribute("addMedNom", nomMed);
                session.setAttribute("addMedPrenom", prenomMed);
                session.setAttribute("addMedSpecialite", specialite);
                session.setAttribute("addMedLieu", lieu);
                session.setAttribute("addMedTaux", tauxHoraireStr);

                // Validate required fields
                if (nomMed == null || nomMed.isEmpty()) {
                    session.setAttribute("error_message", "Le nom est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (specialite == null || specialite.isEmpty()) {
                    session.setAttribute("error_message", "La spécialité est requise.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (lieu == null || lieu.isEmpty()) {
                    session.setAttribute("error_message", "Le lieu est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (tauxHoraireStr == null || tauxHoraireStr.isEmpty()) {
                    session.setAttribute("error_message", "Le taux horaire est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (mdpMed == null || mdpMed.isEmpty()) {
                    session.setAttribute("error_message", "Le mot de passe est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Parse taux horaire
                double tauxHoraire;
                try {
                    tauxHoraire = Double.parseDouble(tauxHoraireStr);
                } catch (Exception e) {
                    session.setAttribute("error_message", "Format du taux horaire invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Build Medecin object
                Medecin medecin = new Medecin();
                medecin.setNomMed(nomMed);
                medecin.setPrenomMed(prenomMed);
                medecin.setSpecialite(specialite);
                medecin.setLieu(lieu);
                medecin.setTauxHoraire(tauxHoraire);
                medecin.setMdpMed(mdpMed);

                // Call service to add medecin
                MedecinService medSer = new MedecinService();
                ServiceResult serRes = medSer.registerMedecin(medecin);

                if (serRes.isSuccess()) {
                    // Clear session attributes on success
                    session.removeAttribute("addMedNom");
                    session.removeAttribute("addMedPrenom");
                    session.removeAttribute("addMedSpecialite");
                    session.removeAttribute("addMedLieu");
                    session.removeAttribute("addMedTaux");
                    session.setAttribute("success_message", "Médecin ajouté avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Update medecin - POST handler (from admin)
            case "update_medecin_submit": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get form parameters
                String idMed = request.getParameter("idMed");
                String nomMed = request.getParameter("nomMed");
                String prenomMed = request.getParameter("prenomMed");
                String specialite = request.getParameter("specialite");
                String lieu = request.getParameter("lieu");
                String tauxHoraireStr = request.getParameter("tauxHoraire");
                String mdpMed = request.getParameter("mdpMed");

                // Validate required fields
                if (idMed == null || idMed.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du médecin manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (nomMed == null || nomMed.isEmpty()) {
                    session.setAttribute("error_message", "Le nom est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (specialite == null || specialite.isEmpty()) {
                    session.setAttribute("error_message", "La spécialité est requise.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (lieu == null || lieu.isEmpty()) {
                    session.setAttribute("error_message", "Le lieu est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (tauxHoraireStr == null || tauxHoraireStr.isEmpty()) {
                    session.setAttribute("error_message", "Le taux horaire est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Parse taux horaire
                double tauxHoraire;
                try {
                    tauxHoraire = Double.parseDouble(tauxHoraireStr);
                } catch (Exception e) {
                    session.setAttribute("error_message", "Format du taux horaire invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Build Medecin object
                Medecin medecin = new Medecin();
                medecin.setIdMed(idMed);
                medecin.setNomMed(nomMed);
                medecin.setPrenomMed(prenomMed);
                medecin.setSpecialite(specialite);
                medecin.setLieu(lieu);
                medecin.setTauxHoraire(tauxHoraire);
                medecin.setMdpMed(mdpMed); // Will be handled in service (only update if not empty)

                // Call service to update medecin
                MedecinService medSer = new MedecinService();
                ServiceResult serRes = medSer.updateMedecin(medecin);

                if (serRes.isSuccess()) {
                    session.setAttribute("success_message", "Médecin modifié avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Filter disponibilities for admin
            case "filter_dispo_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                String idMed = request.getParameter("idMed");
                String dateDebutStr = request.getParameter("filterDateDebut");
                String dateFinStr = request.getParameter("filterDateFin");
                String heureDebutStr = request.getParameter("filterHeureDebut");
                String heureFinStr = request.getParameter("filterHeureFin");

                // Save filters to session
                session.setAttribute("dispoAdminFilterDateDebut", dateDebutStr);
                session.setAttribute("dispoAdminFilterDateFin", dateFinStr);
                session.setAttribute("dispoAdminFilterHeureDebut", heureDebutStr);
                session.setAttribute("dispoAdminFilterHeureFin", heureFinStr);

                // Redirect to GET handler
                response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                break;
            }
            // Clear disponibility filters for admin
            case "clear_dispo_admin_filters": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                String idMed = request.getParameter("idMed");

                // Remove filters from session
                session.removeAttribute("dispoAdminFilterDateDebut");
                session.removeAttribute("dispoAdminFilterDateFin");
                session.removeAttribute("dispoAdminFilterHeureDebut");
                session.removeAttribute("dispoAdminFilterHeureFin");

                // Redirect to GET handler
                response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                break;
            }
            // Update disponibility - Admin POST handler
            case "update_dispo_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                String idDispoStr = request.getParameter("idDispo");
                String idMed = request.getParameter("idMed");
                String dateDispoStr = request.getParameter("dateDispo");
                String heureDebutStr = request.getParameter("heureDebut");
                String heureFinStr = request.getParameter("heureFin");

                if (idDispoStr == null || idDispoStr.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du créneau manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                    return;
                }

                int idDispo;
                LocalTime heureDebut;
                LocalTime heureFin;

                try {
                    idDispo = Integer.parseInt(idDispoStr);
                    heureDebut = LocalTime.parse(heureDebutStr);
                    heureFin = LocalTime.parse(heureFinStr);
                } catch (Exception e) {
                    session.setAttribute("error_message", "Format de date ou d'heure invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                    return;
                }

                Disponibilite disponibilite = new Disponibilite();
                disponibilite.setIdDispo(idDispo);
                disponibilite.setDispoIdMed(idMed);
                disponibilite.setDebutDispo(heureDebut);
                disponibilite.setFinDispo(heureFin);

                DisponibiliteService dispoSer = new DisponibiliteService();
                ServiceResult serRes = dispoSer.updateDispo(disponibilite);

                if (serRes.isSuccess()) {
                    session.setAttribute("success_message", "Créneau modifié avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=disponibilite_medecin&id=" + idMed);
                break;
            }
            // Filter patients - Admin POST handler
            case "filter_patient_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get form values from request
                String search = request.getParameter("filterSearch");
                String dateNaisDebut = request.getParameter("filterDateNaisDebut");
                String dateNaisFin = request.getParameter("filterDateNaisFin");

                // Save filters to session
                session.setAttribute("filterSearchPatientAdmin", search);
                session.setAttribute("filterDateNaisDebut", dateNaisDebut);
                session.setAttribute("filterDateNaisFin", dateNaisFin);

                // Redirect to dashboard to apply filters
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Update patient - Admin POST handler
            case "update_patient": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
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
                    session.setAttribute("error_message", "Identifiant patient manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (nomPat == null || nomPat.isEmpty()) {
                    session.setAttribute("error_message", "Le nom est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (emailPat == null || emailPat.isEmpty()) {
                    session.setAttribute("error_message", "L'email est requis.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Parse date of birth
                LocalDate dateNais;
                try {
                    dateNais = LocalDate.parse(dateNaisStr);
                } catch (Exception e) {
                    session.setAttribute("error_message", "Format de date de naissance invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
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
                    session.setAttribute("success_message", "Patient modifié avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Filter RDV for admin - POST handler
            case "filter_rdv_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get form values from request
                String search = request.getParameter("filterSearch");
                String dateDebutStr = request.getParameter("filterDateDebut");
                String dateFinStr = request.getParameter("filterDateFin");
                String heureDebutStr = request.getParameter("filterHeureDebut");
                String heureFinStr = request.getParameter("filterHeureFin");
                String statut = request.getParameter("filterStatut");

                // Save filters to session
                session.setAttribute("filterSearchAdminRdv", search);
                session.setAttribute("filterDateDebutAdmin", dateDebutStr);
                session.setAttribute("filterDateFinAdmin", dateFinStr);
                session.setAttribute("filterHeureDebutAdmin", heureDebutStr);
                session.setAttribute("filterHeureFinAdmin", heureFinStr);
                session.setAttribute("filterStatutAdmin", statut);

                // Redirect to dashboard to apply filters
                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            // Update RDV - Admin POST handler
            case "update_rdv_admin": {
                // Verify admin is logged in
                if (session.getAttribute("admin") == null) {
                    response.sendRedirect(request.getContextPath() + "/admin?action=login");
                    return;
                }

                // Get form parameters
                String idRdvStr = request.getParameter("idRdv");
                String dateRdvStr = request.getParameter("dateRdv");
                String heureDebutStr = request.getParameter("heureDebut");
                String heureFinStr = request.getParameter("heureFin");

                // Validate required fields
                if (idRdvStr == null || idRdvStr.isEmpty()) {
                    session.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (heureDebutStr == null || heureDebutStr.isEmpty()) {
                    session.setAttribute("error_message", "L'heure de début est requise.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                if (heureFinStr == null || heureFinStr.isEmpty()) {
                    session.setAttribute("error_message", "L'heure de fin est requise.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Parse values
                int idRdv;
                LocalTime heureDebut;
                LocalTime heureFin;

                try {
                    idRdv = Integer.parseInt(idRdvStr);
                    heureDebut = LocalTime.parse(heureDebutStr);
                    heureFin = LocalTime.parse(heureFinStr);
                } catch (Exception e) {
                    session.setAttribute("error_message", "Format de date ou d'heure invalide.");
                    response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                    return;
                }

                // Build RDV object
                Rdv rdv = new Rdv();
                rdv.setIdRdv(idRdv);
                rdv.setHeureDebut(heureDebut);
                rdv.setHeureFin(heureFin);

                // Call service to update RDV
                RdvService rdvSer = new RdvService();
                ServiceResult serRes = rdvSer.updateRdv(rdv);

                if (serRes.isSuccess()) {
                    session.setAttribute("success_message", "Rendez-vous modifié avec succès.");
                } else {
                    session.setAttribute("error_message", serRes.getErrorMessage());
                }

                response.sendRedirect(request.getContextPath() + "/admin?action=dashboard");
                break;
            }
            default: {
                request.setAttribute("message", "Action non trouvée");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                break;
            }
        }
    }

}
