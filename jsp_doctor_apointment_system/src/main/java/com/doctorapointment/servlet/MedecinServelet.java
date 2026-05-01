package com.doctorapointment.servlet;

import com.doctorapointment.dao.MedecinDAO;
import com.doctorapointment.model.Disponibilite;
import com.doctorapointment.model.Medecin;
import com.doctorapointment.model.Rdv;
import com.doctorapointment.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/medecin")
public class MedecinServelet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(Medecin.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // Validate action
        if (action == null || action.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/medecin?action=login");
            return;
        }

//        // Test data - TODO: Remove before production
//        Medecin medecin1 = new Medecin();
//        medecin1.setIdMed("m004");
//        medecin1.setNomMed("FULGENCE");
//        medecin1.setPrenomMed("Honoré");
//        medecin1.setTauxHoraire(5000);
//        medecin1.setLieu("Tanambao");
//        medecin1.setSpecialite("dentiste");
//        session.setAttribute("medecin", medecin1);

        try {
            switch (action) {
                // Page: dashboard
                case "dashboard": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get current page from session or default to "rdv"
                    String currentPage = (String) session.getAttribute("page_medecin");
                    if (currentPage == null) {
                        currentPage = "rdv";
                        session.setAttribute("page_medecin", "rdv");
                    }

                    // Get medecin from session
                    Medecin medecin = (Medecin) session.getAttribute("medecin");
                    String rdvIdMed = medecin.getIdMed();

                    // Load data based on current page
                    RdvService rdvSer = new RdvService();
                    DisponibiliteService dispoSer = new DisponibiliteService();
                    PatientService patSer = new PatientService();

                    if (currentPage.equals("rdv")) {
                        // Retrieve RDV filters from session
                        String search = (String) session.getAttribute("filterSearchMedRdv");
                        String dateDebutStr = (String) session.getAttribute("filterDateDebutMed");
                        String dateFinStr = (String) session.getAttribute("filterDateFinMed");
                        String statut = (String) session.getAttribute("filterStatutMed");
                        String heureDebutStr = (String) session.getAttribute("filterHeureDebutMed");
                        String heureFinStr = (String) session.getAttribute("filterHeureFinMed");

                        // Set default values
                        if (search == null) search = "";
                        if (statut == null) statut = "all";

                        // Convert values
                        LocalDate dateDebut = (dateDebutStr != null && !dateDebutStr.isEmpty()) ? LocalDate.parse(dateDebutStr) : null;
                        LocalDate dateFin = (dateFinStr != null && !dateFinStr.isEmpty()) ? LocalDate.parse(dateFinStr) : null;
                        LocalTime heureDebut = (heureDebutStr != null && !heureDebutStr.isEmpty()) ? LocalTime.parse(heureDebutStr) : null;
                        LocalTime heureFin = (heureFinStr != null && !heureFinStr.isEmpty()) ? LocalTime.parse(heureFinStr) : null;

                        // Apply filters
                        ServiceResult serRes = rdvSer.filterRdvMedecin(search, rdvIdMed, dateDebut, dateFin, statut, heureDebut, heureFin);

                        if (serRes.isSuccess()) {
                            request.setAttribute("listRdv", serRes.getData());
                        } else {
                            request.setAttribute("error_message", serRes.getErrorMessage());
                            request.setAttribute("listRdv", new ArrayList<>());
                        }

                    } else if (currentPage.equals("patient")) {
                        // Retrieve patient filters from session
                        String search = (String) session.getAttribute("filterSearchPatient");
                        String dateDebutStr = (String) session.getAttribute("filterDateDebutPatient");
                        String dateFinStr = (String) session.getAttribute("filterDateFinPatient");

                        // Set default values
                        if (search == null) search = "";

                        // Convert values
                        LocalDate dateDebut = (dateDebutStr != null && !dateDebutStr.isEmpty()) ? LocalDate.parse(dateDebutStr) : null;
                        LocalDate dateFin = (dateFinStr != null && !dateFinStr.isEmpty()) ? LocalDate.parse(dateFinStr) : null;

                        // Filter patients
                        ServiceResult serRes = patSer.filterPatMed(rdvIdMed, search, dateDebut, dateFin);

                        if (serRes.isSuccess()) {
                            request.setAttribute("listPatients", serRes.getData());
                        } else {
                            request.setAttribute("error_message", serRes.getErrorMessage());
                            request.setAttribute("listPatients", new ArrayList<>());
                        }

                    } else if (currentPage.equals("horaire")) {
                        // Retrieve disponibility filters from session
                        String dateDebutStr = (String) session.getAttribute("filterDateDebutDispo");
                        String dateFinStr = (String) session.getAttribute("filterDateFinDispo");
                        String heureDebutStr = (String) session.getAttribute("filterHeureDebutDispo");
                        String heureFinStr = (String) session.getAttribute("filterHeureFinDispo");

                        // Convert values
                        LocalDate dateDebut = (dateDebutStr != null && !dateDebutStr.isEmpty()) ? LocalDate.parse(dateDebutStr) : null;
                        LocalDate dateFin = (dateFinStr != null && !dateFinStr.isEmpty()) ? LocalDate.parse(dateFinStr) : null;
                        LocalTime heureDebut = (heureDebutStr != null && !heureDebutStr.isEmpty()) ? LocalTime.parse(heureDebutStr) : null;
                        LocalTime heureFin = (heureFinStr != null && !heureFinStr.isEmpty()) ? LocalTime.parse(heureFinStr) : null;

                        // Filter disponibilities
                        ServiceResult dispoRes = dispoSer.filterDispo(rdvIdMed, dateDebut, dateFin, heureDebut, heureFin);

                        if (dispoRes.isSuccess()) {
                            request.setAttribute("listDispo", dispoRes.getData());
                        } else {
                            request.setAttribute("error_message", dispoRes.getErrorMessage());
                            request.setAttribute("listDispo", new ArrayList<>());
                        }
                    }

                    // Forward to index page
                    request.getRequestDispatcher("/WEB-INF/jsp/medecin/index.jsp").forward(request, response);
                    break;
                }
                // Page: top medecin - show top 5 doctors
                case "top_medecin": {
                    if (session.getAttribute("patient") == null || session.getAttribute("admin") == null) {
                        response.sendRedirect(request.getContextPath() + "/");
                        return;
                    }

                    // Get top 5 medecins from DAO
                    MedecinService medSer = new MedecinService();
                    ServiceResult serRes = medSer.topMedecin();
                    if (!serRes.isSuccess()) {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                    }
                    List<Medecin> topMedecins = (List<Medecin>) serRes.getData();
                    request.setAttribute("topMedecins", topMedecins);

                    // Forward to top medecin page
                    request.getRequestDispatcher("/WEB-INF/jsp/medecin/top_medecin.jsp").forward(request, response);
                    break;
                }
                // Clear RDV filters - GET handler
                case "clear_rdv_filters": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Remove all RDV filter attributes from session
                    session.removeAttribute("filterSearchMedRdv");
                    session.removeAttribute("filterDateDebutMed");
                    session.removeAttribute("filterDateFinMed");
                    session.removeAttribute("filterHeureDebutMed");
                    session.removeAttribute("filterHeureFinMed");
                    session.removeAttribute("filterStatutMed");

                    // Redirect to dashboard with empty filters
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Cancel RDV medecin - GET handler
                case "cancel_rdv": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get RDV ID from request
                    String rdvIdStr = request.getParameter("id");
                    if (rdvIdStr == null || rdvIdStr.isEmpty()) {
                        session.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    int rdvId;
                    try {
                        rdvId = Integer.parseInt(rdvIdStr);
                    } catch (NumberFormatException e) {
                        session.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Get medecin from session
                    Medecin medecin = (Medecin) session.getAttribute("medecin");
                    String rdvIdMed = medecin.getIdMed();

                    // Build RDV object
                    Rdv rdv = new Rdv();
                    rdv.setIdRdv(rdvId);
                    rdv.setRdvIdMed(rdvIdMed);

                    // Cancel RDV via service
                    RdvService rdvSer = new RdvService();
                    ServiceResult serRes = rdvSer.cancelRdvMedecin(rdv);

                    if (serRes.isSuccess()) {
                        session.setAttribute("success_message",
                                "Rendez-vous annulé avec succès et le patient a été notifié par email.");
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                    }

                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Confirm RDV medecin - GET handler
                case "confirm_rdv": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get RDV ID from request
                    String rdvIdStr = request.getParameter("id");
                    if (rdvIdStr == null || rdvIdStr.isEmpty()) {
                        session.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    int rdvId;
                    try {
                        rdvId = Integer.parseInt(rdvIdStr);
                    } catch (NumberFormatException e) {
                        session.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Get medecin from session
                    Medecin medecin = (Medecin) session.getAttribute("medecin");
                    String rdvIdMed = medecin.getIdMed();

                    // Build RDV object
                    Rdv rdv = new Rdv();
                    rdv.setIdRdv(rdvId);
                    rdv.setRdvIdMed(rdvIdMed);

                    // Confirm RDV via service
                    RdvService rdvSer = new RdvService();
                    ServiceResult serRes = rdvSer.confirmRdvMedecin(rdv);

                    if (serRes.isSuccess()) {
                        session.setAttribute("success_message",
                                "Rendez-vous confirmé avec succèset le patient sera notifié avec un email de confirmation.");
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                    }

                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Notify RDV medecin - GET handler
                case "notifier_rdv": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get RDV ID from request
                    String rdvIdStr = request.getParameter("id");
                    if (rdvIdStr == null || rdvIdStr.isEmpty()) {
                        session.setAttribute("error_message", "Identifiant du rendez-vous manquant.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    int rdvId;
                    try {
                        rdvId = Integer.parseInt(rdvIdStr);
                    } catch (NumberFormatException e) {
                        session.setAttribute("error_message", "Identifiant du rendez-vous invalide.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Get medecin from session
                    Medecin medecin = (Medecin) session.getAttribute("medecin");
                    String rdvIdMed = medecin.getIdMed();

                    // Find RDV to check its status
                    RdvService rdvSer = new RdvService();
                    ServiceResult serResFind = rdvSer.findById(rdvId);
                    if (!serResFind.isSuccess()) {
                        session.setAttribute("error_message", serResFind.getErrorMessage());
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }
                    Rdv rdvFind = (Rdv) serResFind.getData();

                    // Build RDV object
                    Rdv rdv = new Rdv();
                    rdv.setIdRdv(rdvId);
                    rdv.setRdvIdMed(rdvIdMed);

                    ServiceResult serRes;

                    // Choose notification method based on RDV status
                    if (rdvFind.getEtatRdv().equals("annulé")) {
                        serRes = rdvSer.notifyCancelRdvMedecin(rdv);
                    } else if (rdvFind.getEtatRdv().equals("confirmé")) {
                        serRes = rdvSer.notifyConfirmRdvMedecin(rdv);
                    } else {
                        session.setAttribute("error_message", "Seul un rendez-vous annulé ou confirmé peut être notifié.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    if (serRes.isSuccess()) {
                        session.setAttribute("success_message", "Notification envoyée au patient avec succès.");
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                    }

                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Page: rdv - Set session page to rdv and redirect to dashboard
                case "rdv": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Set current page to rdv in session
                    session.setAttribute("page_medecin", "rdv");

                    // Redirect to dashboard to load the rdv page
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Page: horaire - Set session page to horaire and redirect to dashboard
                case "horaire": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Set current page to horaire in session
                    session.setAttribute("page_medecin", "horaire");

                    // Redirect to dashboard to load the horaire page
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Page: patient - Set session page to patient and redirect to dashboard
                case "patient": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Set current page to patient in session
                    session.setAttribute("page_medecin", "patient");

                    // Redirect to dashboard to load the patient page
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Clear disponibility filters
                case "clear_dispo_filters": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Remove all filter attributes from session
                    session.removeAttribute("filterDateDebutDispo");
                    session.removeAttribute("filterDateFinDispo");
                    session.removeAttribute("filterHeureDebutDispo");
                    session.removeAttribute("filterHeureFinDispo");

                    // Redirect to dashboard with empty filters
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Delete disponibility - GET handler
                case "delete_dispo": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get disponibility ID from request
                    String idDispoStr = request.getParameter("id");
                    if (idDispoStr == null || idDispoStr.isEmpty()) {
                        session.setAttribute("error_message", "Identifiant du créneau manquant.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    int idDispo;
                    try {
                        idDispo = Integer.parseInt(idDispoStr);
                    } catch (NumberFormatException e) {
                        session.setAttribute("error_message", "Identifiant du créneau invalide.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Get medecin from session
                    Medecin medecin = (Medecin) session.getAttribute("medecin");
                    String idMed = medecin.getIdMed();

                    // Call service to delete disponibility
                    DisponibiliteService dispoSer = new DisponibiliteService();
                    ServiceResult serRes = dispoSer.deleteDispoMedecin(idMed, idDispo);

                    if (serRes.isSuccess()) {
                        session.setAttribute("success_message", "Un horaire a été supprimé avec succès.");
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                    }

                    // Redirect to dashboard to refresh the list
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Clear patient filters
                case "clear_patient_filters": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Remove all patient filter attributes from session
                    session.removeAttribute("filterSearchPatient");
                    session.removeAttribute("filterDateDebutPatient");
                    session.removeAttribute("filterDateFinPatient");

                    // Redirect to dashboard
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Page: update profile - GET handler
                case "profile": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get medecin from session
                    Medecin medecin = (Medecin) session.getAttribute("medecin");
                    String medecinId = medecin.getIdMed();

                    // Fetch full medecin details from database
                    MedecinService medSer = new MedecinService();
                    ServiceResult serRes = medSer.findById(medecinId);

                    if (serRes.isSuccess()) {
                        request.setAttribute("medecin", serRes.getData());
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/update_medecin.jsp").forward(request, response);
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    }
                    break;
                }
                // Page: login - GET handler
                case "login": {
                    // Check if medecin is already logged in
                    if (session.getAttribute("medecin") != null) {
                        // Already logged in, redirect to dashboard
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Forward to login page
                    request.getRequestDispatcher("/WEB-INF/jsp/medecin/login.jsp").forward(request, response);
                    break;
                }
                // Logout medecin - GET handler
                case "logout": {
                    // Invalidate the session (remove all attributes)
                    session.invalidate();

                    // Redirect to login page
                    response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                    break;
                }
                default: {
                    request.setAttribute("message", "Page non trouvée");
                    request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error in MedecinServlet GET", e);
            request.setAttribute("message", "Une erreur technique est survenue.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        // Validate action
        if (action == null || action.trim().isEmpty()) {
            request.setAttribute("message", "Aucune action spécifiée");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        try {
            switch (action) {
                // Filter RDV medecin - POST handler
                case "filter_rdv_medecin": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get form values from request
                    String search = request.getParameter("filterSearch");
                    String dateDebutStr = request.getParameter("filterDateDebut");
                    String dateFinStr = request.getParameter("filterDateFin");
                    String heureDebutStr = request.getParameter("filterHeureDebut");
                    String heureFinStr = request.getParameter("filterHeureFin");
                    String statut = request.getParameter("filterStatut");

                    // Save filters to session for persistence
                    session.setAttribute("filterSearchMedRdv", search);
                    session.setAttribute("filterDateDebutMed", dateDebutStr);
                    session.setAttribute("filterDateFinMed", dateFinStr);
                    session.setAttribute("filterHeureDebutMed", heureDebutStr);
                    session.setAttribute("filterHeureFinMed", heureFinStr);
                    session.setAttribute("filterStatutMed", statut);

                    // Redirect to dashboard to apply filters
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Filter disponibilities for medecin
                case "filter_dispo_medecin": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get form values from request
                    String dateDebutStr = request.getParameter("filterDateDebut");
                    String dateFinStr = request.getParameter("filterDateFin");
                    String heureDebutStr = request.getParameter("filterHeureDebut");
                    String heureFinStr = request.getParameter("filterHeureFin");

                    // Save filters to session
                    session.setAttribute("filterDateDebutDispo", dateDebutStr);
                    session.setAttribute("filterDateFinDispo", dateFinStr);
                    session.setAttribute("filterHeureDebutDispo", heureDebutStr);
                    session.setAttribute("filterHeureFinDispo", heureFinStr);

                    // Redirect to dashboard
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Add disponibility - POST handler
                case "add_dispo": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get form parameters
                    String dateDispoStr = request.getParameter("dateDispo");
                    String heureDebutStr = request.getParameter("heureDebut");
                    String heureFinStr = request.getParameter("heureFin");

                    // Save form values to session for pre-filling (in case of error)
                    session.setAttribute("addDispoDate", dateDispoStr);
                    session.setAttribute("addDispoHeureDebut", heureDebutStr);
                    session.setAttribute("addDispoHeureFin", heureFinStr);

                    // Validate required fields
                    if (dateDispoStr == null || dateDispoStr.isEmpty()) {
                        session.setAttribute("error_message", "La date est requise.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    if (heureDebutStr == null || heureDebutStr.isEmpty()) {
                        session.setAttribute("error_message", "L'heure de début est requise.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    if (heureFinStr == null || heureFinStr.isEmpty()) {
                        session.setAttribute("error_message", "L'heure de fin est requise.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Parse values
                    LocalDate dateDispo;
                    LocalTime heureDebut;
                    LocalTime heureFin;

                    try {
                        dateDispo = LocalDate.parse(dateDispoStr);
                        heureDebut = LocalTime.parse(heureDebutStr);
                        heureFin = LocalTime.parse(heureFinStr);
                    } catch (Exception e) {
                        session.setAttribute("error_message", "Format de date ou d'heure invalide.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Get medecin from session
                    Medecin medecin = (Medecin) session.getAttribute("medecin");
                    String idMed = medecin.getIdMed();

                    // Build Disponibilite object
                    Disponibilite disponibilite = new Disponibilite();
                    disponibilite.setDispoIdMed(idMed);
                    disponibilite.setDateDispo(dateDispo);
                    disponibilite.setDebutDispo(heureDebut);
                    disponibilite.setFinDispo(heureFin);

                    // Call service to add disponibility
                    DisponibiliteService dispoSer = new DisponibiliteService();
                    ServiceResult serRes = dispoSer.addDispo(disponibilite);

                    if (serRes.isSuccess()) {
                        // Clear session attributes on success
                        session.removeAttribute("addDispoDate");
                        session.removeAttribute("addDispoHeureDebut");
                        session.removeAttribute("addDispoHeureFin");
                        session.setAttribute("success_message", "Nouveau horaire ajouté avec succès.");
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                    }

                    // Redirect to dashboard to refresh the list
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Update disponibility - POST handler
                case "update_dispo": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get form parameters
                    String idDispoStr = request.getParameter("idDispo");
                    String dateDispoStr = request.getParameter("dateDispo");
                    String heureDebutStr = request.getParameter("heureDebut");
                    String heureFinStr = request.getParameter("heureFin");

                    // Validate required fields
                    if (idDispoStr == null || idDispoStr.isEmpty()) {
                        session.setAttribute("error_message", "Identifiant du créneau manquant.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    if (dateDispoStr == null || dateDispoStr.isEmpty()) {
                        session.setAttribute("error_message", "La date est requise.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    if (heureDebutStr == null || heureDebutStr.isEmpty()) {
                        session.setAttribute("error_message", "L'heure de début est requise.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    if (heureFinStr == null || heureFinStr.isEmpty()) {
                        session.setAttribute("error_message", "L'heure de fin est requise.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Parse values
                    int idDispo;
                    LocalDate dateDispo;
                    LocalTime heureDebut;
                    LocalTime heureFin;

                    try {
                        idDispo = Integer.parseInt(idDispoStr);
                        dateDispo = LocalDate.parse(dateDispoStr);
                        heureDebut = LocalTime.parse(heureDebutStr);
                        heureFin = LocalTime.parse(heureFinStr);
                    } catch (Exception e) {
                        session.setAttribute("error_message", "Format de date ou d'heure invalide.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Get medecin from session
                    Medecin medecin = (Medecin) session.getAttribute("medecin");
                    String idMed = medecin.getIdMed();

                    // Build Disponibilite object
                    Disponibilite disponibilite = new Disponibilite();
                    disponibilite.setIdDispo(idDispo);
                    disponibilite.setDispoIdMed(idMed);
                    disponibilite.setDateDispo(dateDispo);
                    disponibilite.setDebutDispo(heureDebut);
                    disponibilite.setFinDispo(heureFin);

                    // Call service to update disponibility
                    DisponibiliteService dispoSer = new DisponibiliteService();
                    ServiceResult serRes = dispoSer.updateDispoMedecin(disponibilite);

                    if (serRes.isSuccess()) {
                        session.setAttribute("success_message", "Horaire modifié avec succès.");
                    } else {
                        session.setAttribute("error_message", serRes.getErrorMessage());
                    }

                    // Redirect to dashboard to refresh the list
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Filter patients for medecin
                case "filter_patient_medecin": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
                        return;
                    }

                    // Get form values from request
                    String search = request.getParameter("filterSearch");
                    String dateDebutStr = request.getParameter("filterDateDebut");
                    String dateFinStr = request.getParameter("filterDateFin");

                    // Save filters to session
                    session.setAttribute("filterSearchPatient", search);
                    session.setAttribute("filterDateDebutPatient", dateDebutStr);
                    session.setAttribute("filterDateFinPatient", dateFinStr);

                    // Redirect to dashboard
                    response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    break;
                }
                // Update medecin profile - POST handler
                case "update_medecin_submit": {
                    // Verify medecin is logged in
                    if (session.getAttribute("medecin") == null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=login");
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
                        request.setAttribute("error_message", "Identifiant médecin manquant.");
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/update_medecin.jsp").forward(request, response);
                        return;
                    }

                    if (nomMed == null || nomMed.isEmpty()) {
                        request.setAttribute("error_message", "Le nom est requis.");
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/update_medecin.jsp").forward(request, response);
                        return;
                    }

                    if (specialite == null || specialite.isEmpty()) {
                        request.setAttribute("error_message", "La spécialité est requise.");
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/update_medecin.jsp").forward(request, response);
                        return;
                    }

                    if (lieu == null || lieu.isEmpty()) {
                        request.setAttribute("error_message", "Le lieu est requis.");
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/update_medecin.jsp").forward(request, response);
                        return;
                    }

                    // Parse hourly rate
                    double tauxHoraire;
                    try {
                        tauxHoraire = Double.parseDouble(tauxHoraireStr);
                    } catch (Exception e) {
                        request.setAttribute("error_message", "Format du taux horaire invalide.");
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/update_medecin.jsp").forward(request, response);
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
                    medecin.setMdpMed(mdpMed);

                    // Update medecin via service
                    MedecinService medSer = new MedecinService();
                    ServiceResult serRes = medSer.updateMedecin(medecin);

                    if (serRes.isSuccess()) {
                        // Refresh session medecin data
                        ServiceResult freshMedecin = medSer.findById(idMed);
                        if (freshMedecin.isSuccess()) {
                            session.setAttribute("medecin", freshMedecin.getData());
                        }
                        session.setAttribute("success_message", "Votre profil a été mis à jour avec succès.");
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    } else {
                        request.setAttribute("error_message", serRes.getErrorMessage());
                        // Fetch current medecin data to preserve form values
                        ServiceResult currentMedecin = medSer.findById(idMed);
                        if (currentMedecin.isSuccess()) {
                            request.setAttribute("medecin", currentMedecin.getData());
                        }
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/update_medecin.jsp").forward(request, response);
                    }
                    break;
                }
                // Login medecin - POST handler
                case "login_submit": {
                    // Check if already logged in
                    if (session.getAttribute("medecin") != null) {
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                        return;
                    }

                    // Get form parameters
                    String idMed = request.getParameter("idMed");
                    String mdpMed = request.getParameter("mdpMed");

                    // Save entered ID to session for pre-filling (in case of error)
                    session.setAttribute("loginIdMed", idMed);

                    // Validate input
                    if (idMed == null || idMed.isEmpty() || mdpMed == null || mdpMed.isEmpty()) {
                        request.setAttribute("error_message", "Veuillez remplir tous les champs.");
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/login.jsp").forward(request, response);
                        return;
                    }

                    // Authenticate medecin
                    MedecinService medSer = new MedecinService();
                    ServiceResult serRes = medSer.loginMedecin(idMed, mdpMed);

                    if (serRes.isSuccess()) {
                        // Login successful
                        Medecin medecin = (Medecin) serRes.getData();
                        session.setAttribute("medecin", medecin);
                        session.setAttribute("page_medecin", "rdv");

                        // Remove temporary login ID from session
                        session.removeAttribute("loginIdMed");

                        // Redirect to dashboard
                        response.sendRedirect(request.getContextPath() + "/medecin?action=dashboard");
                    } else {
                        // Login failed
                        session.setAttribute("error_message", serRes.getErrorMessage());
                        request.getRequestDispatcher("/WEB-INF/jsp/medecin/login.jsp").forward(request, response);
                    }
                    break;
                }
                default: {
                    request.setAttribute("message", "L'action demandée n'est pas trouvée pour la méthode POST");
                    request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error in MedecinServlet doPost", e);
            request.setAttribute("message", "Une erreur technique est survenue.");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }
}
