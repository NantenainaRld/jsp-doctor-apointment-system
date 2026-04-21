package com.doctorapointment.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
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

        // ========== filter medecin =============
        MedecinService medSer = new MedecinService();
        response.getWriter().print(((List<Medecin>) (medSer.filterMedecin("",
                "anestesiste", "desc").getData())).get(0).getIdMed());
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
