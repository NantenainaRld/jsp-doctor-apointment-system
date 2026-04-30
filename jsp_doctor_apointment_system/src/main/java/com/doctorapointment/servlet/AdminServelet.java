package com.doctorapointment.servlet;

import com.doctorapointment.model.*;
import com.doctorapointment.service.*;
import com.mysql.cj.ServerVersion;
import com.mysql.cj.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Servlet implementation class AdminServelet
 */
@WebServlet("/admin")
public class AdminServelet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServelet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // =========== list patient ==========
//		PatientService patSer = new PatientService();
//		List<Patient> listPat =(List<Patient>) patSer.filterPatient("","2007-01-01", null).getData();
//
//		for(Patient pat: listPat) {
//			response.getWriter().print(pat.getEmailPat());
//		}

        // =============== update patient ============
//		PatientService patSer = new PatientService();
//		Patient patient = new Patient();
//		patient.setIdPat("p004");
//		patient.setNomPat("update_nom-");
//		patient.setDateNais(LocalDate.parse("2012-01-01"));
//		patient.setEmailPat("update@u.update");
//		patient.setMdpPat("00000000");
//
//		response.getWriter().print(patSer.updatePatient(patient).getErrorMessage());

        // =============== delete patient ===========
//		PatientService patSer = new PatientService();
//		Patient patient = new Patient();
//		patient.setIdPat("p009");
//
//		response.getWriter().print(patSer.deletePatient(patient).getErrorMessage());

        // ============ add medecin =============
//		MedecinService medSer = new MedecinService();
//		Medecin medecin = new Medecin();
//		medecin.setNomMed("roger");
//		medecin.setSpecialite("anestesiste");
//		medecin.setLieu("fianarantsoa");
//		medecin.setMdpMed("000000");
//
//		response.getWriter().print(medSer.registerMedecin(medecin).getErrorMessage());

        // ============= login medecin =============
//		MedecinService medSer = new MedecinService();
//		response.getWriter().print(medSer.loginMedecin("m002","000000").getErrorMessage());

        // ========== update medecin ==============
//        MedecinService medSer = new MedecinService();
//        Medecin medecin = new Medecin();
//        medecin.setIdMed("m003");
//		medecin.setNomMed("Lalaina");
//		medecin.setSpecialite("mpitsabo");
//		medecin.setPrenomMed("fanampiny");
//		medecin.setLieu("mahajanga");
//		medecin.setTauxHoraire(15000.50);
//		medecin.setMdpMed("12345678");
//
//        response.getWriter().print(medSer.updateMedecin(medecin).getErrorMessage());

//		// ========== delete medecin ===========
//		MedecinService medSer = new MedecinService();
//		response.getWriter().print(medSer.deleteMed("m002").getErrorMessage());

        // ============== add rdv ==========
//		RdvService rdvSer = new RdvService();
//		Rdv rdv = new Rdv();
//		rdv.setRdvIdMed("m004");
//		rdv.setDateRdv(LocalDate.of(2026,5,2));
//		rdv.setHeureDebut(LocalTime.of(13,50));
//		rdv.setHeureFin(LocalTime.of(14,20));
//		rdv.setRdvIdPat("P003");
//		response.getWriter().print(rdvSer.addRdv(rdv).getErrorMessage());

        // ============ filter patient rdv============
//		RdvService rdvSer = new RdvService();
//		ServiceResult serRes = rdvSer.filterRdvPatient("","p003",
//				null,null,"", null, null);
//		if(serRes.isSuccess()){
//			for(RdvPatMed rdv : (List<RdvPatMed>) serRes.getData()){
//				response.getWriter().print(rdv.getRdvNomMed() + "<br>");
//			}
//
//			response.getWriter().println("end");
//		}
//		else{
//			response.getWriter().println(serRes.getErrorMessage());
//		}

        // ========== filter rdv medecin ================
//		RdvService rdvSer = new RdvService();
//		ServiceResult serRes = rdvSer.filterRdvMedecin("","m004",
//				null,null,"", null, null);
//		if(serRes.isSuccess()){
//			for(RdvPatMed rdv : (List<RdvPatMed>) serRes.getData()){
//				response.getWriter().print(rdv.getEtatRdv() + "<br>");
//			}
//
//			response.getWriter().println("end");
//		}
//		else{
//			response.getWriter().println(serRes.getErrorMessage());
//		}

        // =========== filter rdv dispo ===============
//		Rdv rdv = new Rdv();
//		rdv.setRdvIdMed("m004");
//		rdv.setDateRdv(LocalDate.of(2026,05,02));
//		RdvService rdvSer = new RdvService();
//		ServiceResult serRes = rdvSer.filterRdvDispo(rdv, LocalTime.of(12, 50), LocalTime.of(15,20));
//
//		if(serRes.isSuccess()){
//			for (Rdv r: (List<Rdv>) serRes.getData()){
//				response.getWriter().println(r.getHeureDebut());
//			}
//			response.getWriter().println("end");
//		}
//		else{
//			response.getWriter().println(serRes.getErrorMessage());
//		}

        // =========== update rdv ============
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(3);
//        rdv.setHeureDebut(LocalTime.of(14, 12));
//        rdv.setHeureFin(LocalTime.of(21, 52));
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.updateRdv(rdv, "p003");
//        if (serRes.isSuccess()) {
//            response.getWriter().println("Update successfully.");
//        } else {
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // =============== delete rdv patient ==========
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(2);
//        rdv.setRdvIdPat("p003");
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.deleteRdvPatient(rdv);
//
//        if (serRes.isSuccess()) {
//            response.getWriter().println("Deleted successfully");
//        } else {
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // ========== delete rdv medecin ==========
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(1);
//        rdv.setRdvIdMed("m004");
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.deleteRdvMedecin(rdv);
//
//        if (serRes.isSuccess()) {
//            response.getWriter().println("Deleted successfully");
//        } else {
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // ============== delete rdv =========
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(3);
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.deleteRdv(rdv);
//
//        if (serRes.isSuccess()) {
//            response.getWriter().println("Deleted successfully");
//        } else {
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // ============ cancel rdv patient ===========
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(4);
//        rdv.setRdvIdPat("p003");
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.cancelRdvPatient(rdv);
//        if(serRes.isSuccess()){
//            response.getWriter().println("Canceled successfully.");
//        }
//        else{
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // =========== cancel rdv medecin =========
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(4);
//        rdv.setRdvIdMed("m004");
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.cancelRdvMedecin(rdv);
//        if(serRes.isSuccess()){
//            response.getWriter().println("Canceled successfully.");
//        }
//        else{
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // ============= cancel rdv ================
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(4);
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.cancelRdv(rdv);
//        if(serRes.isSuccess()){
//            response.getWriter().println("Canceled successfully.");
//        }
//        else{
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // =========== confirm rdv medecin =============
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(4);
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.confirmRdv(rdv);
//        if(serRes.isSuccess()){
//            response.getWriter().println("Confirmed successfully.");
//        }
//        else{
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // ============ notify cancel rdv medecin =======
//        Rdv rdv = new Rdv();
//        rdv.setIdRdv(4);
//        rdv.setRdvIdMed("m004");
//        RdvService rdvSer = new RdvService();
//        ServiceResult serRes = rdvSer.notifyCancelRdvMedecin(rdv);
//        if(serRes.isSuccess()){
//            response.getWriter().println("notify successfully.");
//        }
//        else{
//            response.getWriter().println(serRes.getErrorMessage());


        // =========== add disponibilite ============
//        Disponibilite disponibilite = new Disponibilite();
//        disponibilite.setDispoIdMed("m004");
//        disponibilite.setDateDispo(LocalDate.of(2026,5,2));
//        disponibilite.setDebutDispo(LocalTime.of(16,20));
//        disponibilite.setFinDispo(LocalTime.of(17,25));
//        DisponibiliteService dipoSer = new DisponibiliteService();
//        ServiceResult serRes =dipoSer.addDispo(disponibilite);
//        if(serRes.isSuccess()){
//            response.getWriter().println("added");
//        }
//        else{
//            response.getWriter().println(serRes.getErrorMessage());
//        }

        // ======== filter disponibilite =======
//        DisponibiliteService dispoSer = new DisponibiliteService();
//        ServiceResult serRes = dispoSer.filterDispo("m004",
//                LocalDate.of(2026, 5, 2), null, null, null);
//        if(serRes.isSuccess()){
//            for(Disponibilite dispo : (List<Disponibilite>) serRes.getData()){
//                response.getWriter().print(dispo.getDateDispo());
//            }
//            response.getWriter().print("end");
//        }
//        else{
//            response.getWriter().print(serRes.getErrorMessage());
//        }

        // ============== update disponibilite medecin =============
//        Disponibilite disponibilite = new Disponibilite();
//        disponibilite.setIdDispo(2);
//        disponibilite.setDebutDispo(LocalTime.of(12,13));
//        disponibilite.setFinDispo(LocalTime.of(16,50));
//        disponibilite.setDispoIdMed("m004");
//        DisponibiliteService dispoSer = new DisponibiliteService();
//        ServiceResult serRes = dispoSer.updateDispoMedecin(disponibilite);
//
//        if(serRes.isSuccess()){
//            response.getWriter().print("updated");
//        }
//        else{
//            response.getWriter().print(serRes.getErrorMessage());
//        }

        // =============== update disponibilite ============
//        Disponibilite disponibilite = new Disponibilite();
//        disponibilite.setIdDispo(2);
//        disponibilite.setDebutDispo(LocalTime.of(12,13));
//        disponibilite.setFinDispo(LocalTime.of(16,50));
//        DisponibiliteService dispoSer = new DisponibiliteService();
//        ServiceResult serRes = dispoSer.updateDispo(disponibilite);
//
//        if(serRes.isSuccess()){
//            response.getWriter().print("updated");
//        }
//        else{
//            response.getWriter().print(serRes.getErrorMessage());
//        }

        // =========== top medecin ========
//        MedecinService medSer = new MedecinService();
//        ServiceResult serRes = medSer.topMedecin();
//        if(serRes.isSuccess()){
//            for(Medecin med: (List<Medecin>) serRes.getData()){
//                response.getWriter().print(med.getNomMed());
//            }
//            response.getWriter().print("end");
//        }
//        else{
//            response.getWriter().print(serRes.getErrorMessage());
//        }


        HttpSession session = request.getSession();
        Patient patient = new Patient();
        patient.setIdPat("P002");
        patient.setNomPat("Jean");
        patient.setPrenomPat("Dupoint");
        session.setAttribute("patient",patient);
        session.setAttribute("page_patient", "rdv");

        RdvService rdvSer = new RdvService();
        ServiceResult serRes = rdvSer.filterRdvPatient("","P003",null,null,null,null,null);
        request.setAttribute("listRdv", serRes.getData());
        request.getRequestDispatcher("WEB-INF/jsp/patient/index.jsp").forward(request,response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
