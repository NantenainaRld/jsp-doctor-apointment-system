package com.doctorapointment.servelet;

import java.io.IOException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doctorapointment.model.Patient;
import com.doctorapointment.service.PatientService;
import com.doctorapointment.service.ServiceResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.annotation.WebServlet;

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
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		// validate action
		if (action == null) {
			request.setAttribute("message", "Aucune action spécifiée");
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
			return;
		}

		try {
			switch (action) {
			// patient register
			case "register":
				
				// handle login
				HttpSession session = request.getSession(false);
				if(session != null && session.getAttribute("user") != null) {
					response.sendRedirect("dashboard");
					return;
				}
				
				Patient patient = new Patient();
				patient.setNomPat(request.getParameter("nom_pat"));
				patient.setPrenomPat(request.getParameter("prenom_pat"));
				patient.setMdpPat(request.getParameter("mdp_pat"));
				patient.setEmailPat(request.getParameter("email_pat"));
				
				LocalDate dateNais = LocalDate.parse(request.getParameter("date_nais"));
				patient.setDateNais(dateNais);
				
				// call service
				ServiceResult serRes = patSer.registerPatient(patient);
				
				// handle result
				if(serRes.isSuccess()) {
					response.sendRedirect("login");
					return;
				}
				request.setAttribute("error_message",serRes.getErrorMessage());
				request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
				return;
			default:
				request.setAttribute("message", "L'action demandée n'est pas trouvée");
				request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
				return;
			}
		} catch (Exception e) {
			request.setAttribute("message", "Une erreur technique est survenue.");
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
			log.error("Error at patient post route : '{}' ", e);
			return;
		}
	}

}
