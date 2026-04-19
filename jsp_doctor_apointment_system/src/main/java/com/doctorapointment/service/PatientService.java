package com.doctorapointment.service;

import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doctorapointment.dao.PatientDAO;
import com.doctorapointment.model.Patient;

public class PatientService {
	private PatientDAO patDAO = new PatientDAO();
	private static final Logger log = LoggerFactory.getLogger(PatientService.class);

	// register patient
	public ServiceResult registerPatient(Patient patient) {
		String nomPat = patient.getNomPat() != null ? patient.getNomPat().trim() : "";
		String prenomPat = patient.getPrenomPat() != null ? patient.getPrenomPat().trim() : "";
		LocalDate dateNais = patient.getDateNais();
		String mdpPat = patient.getMdpPat() != null ? patient.getMdpPat() : "";
		String emailPat = patient.getEmailPat() != null ? patient.getEmailPat().trim() : "";

		// Validation: nom_pat
		if (nomPat.isEmpty()) {
			return new ServiceResult(false, "Veuillez remplir le nom.");
		}
		if (nomPat.matches(".*\\d.*")) {
			return new ServiceResult(false, "Le nom ne doit pas contenir des chiffres.");
		}
		patient.setNomPat(nomPat);

		// Validation: prenom_pat
		if (!prenomPat.isEmpty() && prenomPat.matches(".*\\d.*")) {
			return new ServiceResult(false, "Le prénom ne doit pas contenir des chiffres.");
		}
		patient.setPrenomPat(prenomPat);

		// Validation: date_nais
		if (dateNais == null) {
			return new ServiceResult(false, "La date de naissance est requise.");
		}
		if (dateNais.isAfter(LocalDate.now())) {
			return new ServiceResult(false, "La date de naissance ne peut pas être dans le futur.");
		}
		int age = LocalDate.now().getYear() - dateNais.getYear();
		if (age < 10 || age > 150) {
			return new ServiceResult(false,
					"Âge invalide (doit être entre 10 et 120 ans), veuillez changer la date de naissance.");
		}
		patient.setDateNais(dateNais);

		// Validation: mdp_pat
		if (mdpPat.isEmpty()) {
			return new ServiceResult(false, "Le mot de passe est réquis.");
		}
		if (mdpPat.length() < 6) {
			return new ServiceResult(false, "Le mot de passe doit être au moins 6 caractères.");
		}

		// Validation: email_pat
		if (emailPat.isEmpty()) {
			return new ServiceResult(false, "L'adresse email est requise.");
		}
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		if (!emailPat.matches(emailRegex)) {
			return new ServiceResult(false, "Format d'email invalide (exemple: nom@domaine.com)");
		}
		Boolean emailExists = patDAO.emailExists(emailPat);
		if (emailExists == null) {
			return new ServiceResult(false, "Une erreur est survenue, veuillez réssayer.");
		}
		if (emailExists) {
			return new ServiceResult(false, "L'adresse email <b>" + emailPat + "</b> existe déja.");
		}
		patient.setEmailPat(emailPat);

		// email hash
		try {
			String hashedPass = BCrypt.hashpw(mdpPat, BCrypt.gensalt());
			patient.setMdpPat(hashedPass);
		}
		catch(IllegalArgumentException e) {
			log.error("Error during hashing mdp_pat", e);
			return new ServiceResult(false,"Une erreur est survenue, veuillez réssayer.");
		}

		// set id_pat
		patient.setIdPat(patDAO.getNextPatientId());

		// Register patient
		boolean addPatResult = patDAO.registerPatient(patient);
		if(addPatResult) {
			return new ServiceResult(true, null);
		}
		return new ServiceResult(false, "Erreur lors de l'ajout de patient.");
	}
	
	// login patient
	public ServiceResult loginPatient(String email, String mdpPat) {
		Patient patient = patDAO.findByEmail(email);
		
		// Validation: login
		if(patient == null) {
			return new ServiceResult(false, "Email ou mot de passe incorrect.");
		}
		if(!BCrypt.checkpw(mdpPat, patient.getMdpPat())) {
			return new ServiceResult(false, "Email ou mot de passe incorrect.");
		}
		
		patient.setMdpPat(null);
		
		return new ServiceResult(true,null, patient);
	}
}
