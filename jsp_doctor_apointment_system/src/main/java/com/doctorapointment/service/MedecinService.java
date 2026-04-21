package com.doctorapointment.service;


import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doctorapointment.dao.MedecinDAO;
import com.doctorapointment.model.Medecin;

public class MedecinService {
	private MedecinDAO medDAO = new MedecinDAO();
	private static final Logger log = LoggerFactory.getLogger(MedecinService.class);

	public ServiceResult registerMedecin(Medecin medecin) {
		String nomMed = medecin.getNomMed() != null ? medecin.getNomMed().trim() : "";
		String prenomMed = medecin.getPrenomMed() != null ? medecin.getPrenomMed().trim() : "";
		String specialite = medecin.getSpecialite() != null ? medecin.getSpecialite().trim() : "";
		String lieu = medecin.getLieu() != null ? medecin.getLieu().trim() : "";
		String mdpMed = medecin.getMdpMed() != null ? medecin.getMdpMed() : "";

		// Validation: nom_med
		if (nomMed.isEmpty()) {
			return new ServiceResult(false, "Veuillez remplir le nom.");
		}
		if (nomMed.matches(".*\\d.*")) {
			return new ServiceResult(false, "Le nom ne doit pas contenir des chiffres.");
		}
		medecin.setNomMed(nomMed);

		// Validation: prenom_med
		if (!prenomMed.isEmpty() && prenomMed.matches(".*\\d.*")) {
			return new ServiceResult(false, "Le prénom ne doit pas contenir des chiffres.");
		}
		medecin.setPrenomMed(prenomMed);

		// Validation: specialite
		if (specialite.isEmpty()) {
			return new ServiceResult(false, "La spécialité est requise.");
		}
		medecin.setSpecialite(specialite);

		// Validation: lieu
		if (lieu.isEmpty()) {
			return new ServiceResult(false, "Le lieu est requis.");
		}
		medecin.setLieu(lieu);

		// Validation: mdp_med
		if (mdpMed.isEmpty()) {
			return new ServiceResult(false, "Le mot de passe est réquis.");
		}
		if (mdpMed.length() < 6) {
			return new ServiceResult(false, "Le mot de passe doit être au moins 6 caractères.");
		}

		// Validation: taux_horaire
		if (medecin.getTauxHoraire() < 0) {
			return new ServiceResult(false, "Le taux horaire ne doit pas être de valeur négative.");
		}

		// mdp hash
		try {
			String hashedPass = BCrypt.hashpw(mdpMed, BCrypt.gensalt());
			medecin.setMdpMed(hashedPass);
		} catch (IllegalArgumentException e) {
			log.error("Error during hashing mdp_med", e);
			return new ServiceResult(false, "Une erreur est survenue, veuillez réssayer.");
		}

		// set id_med
		medecin.setIdMed(medDAO.getNextMedecinId());

		// Register medecin
		boolean addMedResult = medDAO.registerMedecin(medecin);
		if (addMedResult) {
			return new ServiceResult(true, null);
		}
		return new ServiceResult(false, "Erreur lors de l'ajout de medecin");
	}
}
