package com.doctorapointment.servlet;

import com.doctorapointment.model.Rdv;
import com.doctorapointment.model.RdvPatMed;
import com.doctorapointment.service.RdvService;
import com.doctorapointment.service.ServiceResult;
import com.mysql.cj.ServerVersion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.doctorapointment.model.Medecin;
import com.doctorapointment.model.Patient;
import com.doctorapointment.service.MedecinService;
import com.doctorapointment.service.PatientService;

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
        Rdv rdv = new Rdv();
        rdv.setIdRdv(4);
        rdv.setRdvIdPat("p003");
        RdvService rdvSer = new RdvService();
        ServiceResult serRes = rdvSer.cancelRdvPatient(rdv);
        if(serRes.isSuccess()){
            response.getWriter().println("Canceled successfully.");
        }
        else{
            response.getWriter().println(serRes.getErrorMessage());
        }
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
