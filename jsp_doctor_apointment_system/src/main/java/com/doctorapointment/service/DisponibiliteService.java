package com.doctorapointment.service;

import com.doctorapointment.dao.DisponibiliteDAO;
import com.doctorapointment.dao.MedecinDAO;
import com.doctorapointment.model.Disponibilite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            if (dispoDAO.existeChevauche(disponibilite)) {
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

    // filter disponibilité
    public ServiceResult filterDispo(String idMed, LocalDate dateDebut, LocalDate dateFin, LocalTime heureDebut, LocalTime heureFin) {
        try {
            // Validation: id_med
            if (idMed == null || idMed.isEmpty()) {
                return new ServiceResult(false, "Veuillez choisir un médecin d'abord.");
            }

            // Validation: date_dispo
            if ((dateDebut != null && dateDebut.isBefore(LocalDate.now()))
                    || (dateFin != null && dateFin.isBefore(LocalDate.now()))) {
                return new ServiceResult(false, "Les dates ne doivent être dans le passé.");
            }
            if (dateDebut != null && dateFin != null && dateDebut.isAfter(dateFin)) {
                return new ServiceResult(false, "La date de début ne doit pas être après la date de fin.");
            }

            // Validation: heure_dispo
            if (heureDebut != null && heureFin != null && heureDebut.isAfter(heureFin)) {
                return new ServiceResult(false, "L'heure de début ne doit pas être après l'heure de fin.");
            }

            // Validation: id_med
            MedecinDAO medecinDAO = new MedecinDAO();
            if (medecinDAO.findById(idMed) == null) {
                return new ServiceResult(false, "Le médecin avec l'ID <b>" + idMed + "</b> n'existe pas.");
            }

            // Filter disponibilite
            return new ServiceResult(true,
                    null, dispoDAO.filterDisponibilite(idMed, dateDebut, dateFin, heureDebut, heureFin));

        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error filtering disponibilité", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // update disponibilite medecin
    public ServiceResult updateDispoMedecin(Disponibilite disponibilite) {
        try {
            // Validation: heure_dispo
            if (disponibilite.getDebutDispo() == null || disponibilite.getFinDispo() == null) {
                return new ServiceResult(false, "Veuillez compléter le créneau (heure début et fin).");
            }
            if (disponibilite.getDebutDispo().isAfter(disponibilite.getFinDispo())) {
                return new ServiceResult(false, "L'heure de début ne doit pas être après l'heure de fin.");
            }
            long duree = ChronoUnit.MINUTES.between(disponibilite.getDebutDispo(), disponibilite.getFinDispo());
            if (duree < 10) {
                return new ServiceResult(false, "La durée de disponibilité ne doit pas être moins de 10 minutes");
            }

            // Verification: disponibilite exist
            Disponibilite dispoFind = dispoDAO.findById(disponibilite.getIdDispo());
            if (dispoFind == null) {
                return new ServiceResult(false, "Cette disponibilité n'existe pas.");
            }
            if (!dispoFind.getDispoIdMed().equals(disponibilite.getDispoIdMed())) {
                return new ServiceResult(false, "Cette horaire ne vous appartient pas.");
            }

            // Verification: conflit exist
            Disponibilite dispoConf = new Disponibilite();
            dispoConf.setIdDispo(disponibilite.getIdDispo());
            dispoConf.setDispoIdMed(dispoFind.getDispoIdMed());
            dispoConf.setDateDispo(dispoFind.getDateDispo());
            dispoConf.setDebutDispo(disponibilite.getDebutDispo());
            dispoConf.setFinDispo(disponibilite.getFinDispo());
            if (dispoDAO.chevaucheExcluExistant(dispoConf)) {
                return new ServiceResult(false, "Cet horaire existe déjà ou se chevauche, veuillez modifier.");
            }

            // update disponibilite
            if (dispoDAO.updatDisponibilite(disponibilite)) {
                return new ServiceResult(true, null);
            }
            return new ServiceResult(false, "Une erreur est survenue lors de la modification de la disponibilité.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error updating disponibilite", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // update disponibilité
    public ServiceResult updateDispo(Disponibilite disponibilite) {
        try {
            // Validation: heure_dispo
            if (disponibilite.getDebutDispo() == null || disponibilite.getFinDispo() == null) {
                return new ServiceResult(false, "Veuillez compléter le créneau (heure début et fin).");
            }
            if (disponibilite.getDebutDispo().isAfter(disponibilite.getFinDispo())) {
                return new ServiceResult(false, "L'heure de début ne doit pas être après l'heure de fin.");
            }
            long duree = ChronoUnit.MINUTES.between(disponibilite.getDebutDispo(), disponibilite.getFinDispo());
            if (duree < 10) {
                return new ServiceResult(false, "La durée de disponibilité ne doit pas être moins de 10 minutes");
            }

            // Verification: disponibilite exist
            Disponibilite dispoFind = dispoDAO.findById(disponibilite.getIdDispo());
            if (dispoFind == null) {
                return new ServiceResult(false, "Cette disponibilité n'existe pas.");
            }

            // Verification: conflit exist
            Disponibilite dispoConf = new Disponibilite();
            dispoConf.setIdDispo(disponibilite.getIdDispo());
            dispoConf.setDispoIdMed(dispoFind.getDispoIdMed());
            dispoConf.setDateDispo(dispoFind.getDateDispo());
            dispoConf.setDebutDispo(disponibilite.getDebutDispo());
            dispoConf.setFinDispo(disponibilite.getFinDispo());
            if (dispoDAO.chevaucheExcluExistant(dispoConf)) {
                return new ServiceResult(false, "Cet horaire existe déjà ou se chevauche, veuillez modifier.");
            }

            // update disponibilite
            if (dispoDAO.updatDisponibilite(disponibilite)) {
                return new ServiceResult(true, null);
            }
            return new ServiceResult(false, "Une erreur est survenue lors de la modification de la disponibilité.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error updating disponibilite", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // delete disponibilite medecin
    public ServiceResult deleteDispoMedecin(Disponibilite disponibilite) {
        try {
            // Verification: disponibilite exist and medecin = medecin
            Disponibilite dispoFind = dispoDAO.findById(disponibilite.getIdDispo());
            if (dispoFind == null) {
                return new ServiceResult(false, "Cette disponibilité n'existe pas.");
            }
            if (!dispoFind.getDispoIdMed().equals(disponibilite.getDispoIdMed()))
                return new ServiceResult(false, "Cette disponibilité ne vous appartient pas.");

            // Delete disponibilite
            if (dispoDAO.deleteDisponibilite(disponibilite.getIdDispo())) {
                return new ServiceResult(true, null);
            }
            return new ServiceResult(false, "Une erreur est survenue lors de la suppression de la disponibilité.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error deleting disponibilite", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // delete disponibilite
    public ServiceResult deleteDispo(Disponibilite disponibilite) {
        try {
            // Verification: disponibilite exist and medecin = medecin
            Disponibilite dispoFind = dispoDAO.findById(disponibilite.getIdDispo());
            if (dispoFind == null) {
                return new ServiceResult(false, "Cette disponibilité n'existe pas.");
            }

            // Delete disponibilite
            if (dispoDAO.deleteDisponibilite(disponibilite.getIdDispo())) {
                return new ServiceResult(true, null);
            }
            return new ServiceResult(false, "Une erreur est survenue lors de la suppression de la disponibilité.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error deleting disponibilite", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }
}
