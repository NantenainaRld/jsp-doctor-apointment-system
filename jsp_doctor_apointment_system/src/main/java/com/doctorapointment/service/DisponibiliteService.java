package com.doctorapointment.service;

import com.doctorapointment.dao.DisponibiliteDAO;
import com.doctorapointment.dao.MedecinDAO;
import com.doctorapointment.model.Disponibilite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DisponibiliteService {
    private static final Logger log = LoggerFactory.getLogger(DisponibiliteService.class);
    DisponibiliteDAO dispoDAO = new DisponibiliteDAO();

    // add disponibilite
    public ServiceResult addDispo(Disponibilite disponibilite) {
        try {
            // Validation: dispo_id_med
            if (disponibilite.getDispoIdMed() == null || disponibilite.getDispoIdMed().isEmpty()) {
                return new ServiceResult(false, "L'ID du médecin est réquis.");
            }

            // Validation: date_dispo
            if (disponibilite.getDateDispo() == null) {
                return new ServiceResult(false, "La date de la disponibilité est réquise.");
            }
            if (disponibilite.getDateDispo().isBefore(LocalDate.now())) {
                return new ServiceResult(false, "La date de la disponibilité ne doit pas être dans le passe.");
            }

            // Validation: debut and fin
            if (disponibilite.getDebutDispo() == null) {
                return new ServiceResult(false, "L'heure de début de la disponibilité est réquise.");
            }
            if (disponibilite.getFinDispo() == null) {
                return new ServiceResult(false, "L'heure de fin de la disponibilité est réquise.");
            }
            if (disponibilite.getDebutDispo().isAfter(disponibilite.getFinDispo())) {
                return new ServiceResult(false, "L'heure de début ne doit pas être après l'heure de fin");
            }
            long duree = ChronoUnit.MINUTES.between(disponibilite.getDebutDispo(), disponibilite.getFinDispo());
            if (duree < 10) {
                return new ServiceResult(false, "La durée de disponibilité ne doit pas être moins de 10 minutes");
            }

            // Verification: medecin exist
            MedecinDAO medecinDAO = new MedecinDAO();
            if (medecinDAO.findById(disponibilite.getDispoIdMed()) == null) {
                return new ServiceResult(false, "Le médecin avec l'ID" + disponibilite.getDispoIdMed() + " n'existe pas.");
            }

            // Verification: chevauchement disponibilite
            if (dispoDAO.chevaucheExistant(disponibilite)) {
                return new ServiceResult(false, "La date et heure de disponibilité existe déjà ou se chevauche.");
            }

            // add disponibilite
            if (dispoDAO.addDisponibilte(disponibilite)) {
                return new ServiceResult(true, null);
            }
            return new ServiceResult(false, "Une erreur est survenue lors de l'ajout de nouveau horaire.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error adding disponibilite", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite");
        }
    }
}
