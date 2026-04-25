package com.doctorapointment.service;

import com.doctorapointment.dao.*;
import com.doctorapointment.model.Disponibilite;
import com.doctorapointment.model.Patient;
import com.doctorapointment.model.Rdv;
import com.doctorapointment.model.RdvPatMed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class RdvService {
    private static final Logger log = LoggerFactory.getLogger(RdvService.class);
    RdvDAO rdvDAO = new RdvDAO();

    // add rdv
    public ServiceResult addRdv(Rdv rdv) {
        try {
            String idMed = rdv.getRdvIdMed() == null ? "" : rdv.getRdvIdMed().trim();
            String idPat = rdv.getRdvIdPat() == null ? "" : rdv.getRdvIdPat().trim();
            LocalDate dateRdv = rdv.getDateRdv();
            LocalTime heureDebut = rdv.getHeureDebut();
            LocalTime heureFin = rdv.getHeureFin();

            // Validation: id_med
            if (idMed.isEmpty()) return new ServiceResult(false,
                    "Veuiller choisir un médecin pour le rendez-vous.");

            // Validation: date_rdv
            if (dateRdv == null) return new ServiceResult(false, "La date du rendez-vous est requise.");
            if (dateRdv.isBefore(LocalDate.now())) return new ServiceResult(false,
                    "La date du rendez-vous ne doit pas être dans le passé.");

            // Validation: heure_debut et heure_fin
            if (heureDebut == null) return new ServiceResult(false, "L'heure de début est requise.");
            if (heureFin == null) return new ServiceResult(false, "L'heure de fin est requise.");
            if (heureDebut.isAfter(heureFin))
                return new ServiceResult(false, "L'heure de début ne peut pas être après l'heure de fin.");

            // Validation: durée min/max
            long duree = ChronoUnit.MINUTES.between(heureDebut, heureFin);
            if (duree < 10) return new ServiceResult(false, "La durée du rendez-vous doit être au moins 10 minutes.");
            if (duree > 180) return new ServiceResult(false, "La durée du rendez-vous ne doit pas dépasser 3 heures.");

            // Validation: date + heure dans le passé
            LocalDateTime dateHeureDebut = LocalDateTime.of(dateRdv, heureDebut);
            LocalDateTime dateHeureFin = LocalDateTime.of(dateRdv, heureFin);
            LocalDateTime maintenant = LocalDateTime.now();
            if (dateHeureDebut.isBefore(maintenant)) {
                return new ServiceResult(false, "La date et l'heure de début ne doivent pas être dans le passé.");
            }
            if (dateHeureFin.isBefore(maintenant)) {
                return new ServiceResult(false, "La date et l'heure de fin ne doivent pas être dans le passé.");
            }

            // Verification: patient
            PatientDAO patDao = new PatientDAO();
            if (patDao.findById(rdv.getRdvIdPat()) == null) return new ServiceResult(false,
                    "Le patient avec l'ID <b>" + rdv.getRdvIdPat() + "</b> n'existe pas.");

            // Verification: medecin
            MedecinDAO medDAO = new MedecinDAO();
            if (medDAO.findById(rdv.getRdvIdMed()) == null) return new ServiceResult(false,
                    "Le médecin avec l'ID <b>" + rdv.getRdvIdMed() + "</b> n'existe pas.");

            // Verification: disponibilite
            Disponibilite disponibilite = new Disponibilite();
            disponibilite.setDispoIdMed(rdv.getRdvIdMed());
            disponibilite.setDateDispo(rdv.getDateRdv());
            disponibilite.setDebutDispo(rdv.getHeureDebut());
            disponibilite.setFinDispo(rdv.getHeureFin());
            if (!DisponibiliteDAO.findByDateIdMedCreneau(disponibilite)) {
                return new ServiceResult(false, "Ce médecin n'est pas disponible dans la date et créneau choisi.");
            }

            // Verification: chevauchement
            if (rdvDAO.existConflit(rdv)) {
                return new ServiceResult(false, "Ce créneau est déjà occupé, veuillez modifier.");
            }

            // Add rdv
            if (rdvDAO.addRdv(rdv)) return new ServiceResult(true, null);
            return new ServiceResult(false, "Une erreur technique, veuillez réessayer.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Urreur technique, veuillez résssayer.");
        } catch (Exception e) {
            log.error("Error adding rdv", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // filter rdv patient
    public ServiceResult filterRdvPatient(String search, String rdvIdPat,
                                          LocalDate dateRdvDebut, LocalDate dateRdvFin, String etatRdv,
                                          LocalTime heureDebut, LocalTime heureFin) {
        try {
            // Validation
            search = search == null ? "" : search.trim();
            rdvIdPat = rdvIdPat == null ? "" : rdvIdPat.trim();
            if (dateRdvDebut != null && dateRdvFin != null && dateRdvFin.isBefore(dateRdvDebut)) {
                return new ServiceResult(false, "La date de fin ne doit pas être avant la date de début");
            }
            etatRdv = etatRdv == null || etatRdv.isEmpty() ? "all" : etatRdv.trim().toLowerCase();

            // Filter: rdv patient
            return new ServiceResult(true,
                    null, RdvPatMedDAO.filterRdvPatient(search,
                    rdvIdPat, dateRdvDebut, dateRdvFin, etatRdv, heureDebut, heureFin));
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer");
        } catch (Exception e) {
            log.error("Error filtering rdv patient", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite");
        }
    }

    // filter rdv medecin
    public ServiceResult filterRdvMedecin(String search, String rdvIdMed,
                                          LocalDate dateRdvDebut, LocalDate dateRdvFin, String etatRdv,
                                          LocalTime heureDebut, LocalTime heureFin) {
        try {
            // Validation
            search = search == null ? "" : search.trim();
            rdvIdMed = rdvIdMed == null ? "" : rdvIdMed.trim();
            if (dateRdvDebut != null && dateRdvFin != null && dateRdvFin.isBefore(dateRdvDebut)) {
                return new ServiceResult(false, "La date de fin ne doit pas être avant la date de début");
            }
            etatRdv = etatRdv == null || etatRdv.isEmpty() ? "all" : etatRdv.trim().toLowerCase();

            // Filter: rdv medecin
            return new ServiceResult(true,
                    null, RdvPatMedDAO.filterRdvMedecin(search,
                    rdvIdMed, dateRdvDebut, dateRdvFin, etatRdv, heureDebut, heureFin));
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer");
        } catch (Exception e) {
            log.error("Error filtering rdv medecin", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite");
        }
    }

    // filter rdv dispo
    public ServiceResult filterRdvDispo(Rdv rdv, LocalTime heureDebut, LocalTime heureFin) {
        try {
            if (heureDebut != null && heureFin != null && heureFin.isBefore(heureDebut)) {
                return new ServiceResult(false, "L'heure de fin ne doit pas être avant l'heure de début");
            }

            return new ServiceResult(true, null, rdvDAO.filterRdvDispo(rdv, heureDebut, heureFin
            ));
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réesayer.");
        } catch (Exception e) {
            log.error("Error filtering rdv for disponibilite", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // update rdv
    public ServiceResult updateRdv(Rdv rdv, String idPat) {
        try {
            // Validation: heure_rdv
            if (rdv.getHeureDebut() == null) return new ServiceResult(false, "L'heure de début est requise.");
            if (rdv.getHeureFin() == null) return new ServiceResult(false, "L'heure de fin est requise.");
            if (rdv.getHeureDebut().isAfter(rdv.getHeureFin())) {
                return new ServiceResult(false, "L'heure de fin ne doit pas être avant l'heure de début.");
            }

            // Validation: rdv
            Rdv rdvFind = rdvDAO.findById(rdv.getIdRdv());
            if (rdvFind == null) return new ServiceResult(false, "Le rendez-vous choisi n'est pas trouvé.");
            if (!rdvFind.getRdvIdPat().equals(idPat.toUpperCase()))
                return new ServiceResult(false, "Cet rendez-vous n'appartient pas à ce patient.");
            if (!rdvFind.getEtatRdv().equals("en attente")) {
                return new ServiceResult(false, "Seul un rendez-vous en attente peut être modifié.");
            }
            if (LocalDateTime.of(rdvFind.getDateRdv(), rdv.getHeureDebut()).isBefore(LocalDateTime.now())) {
                return new ServiceResult(false, "L'heure de début est déjà dépassée.");
            }
            // conflit
            Rdv rdvConflit = new Rdv();
            rdvConflit.setIdRdv(rdv.getIdRdv());
            rdvConflit.setDateRdv(rdvFind.getDateRdv());
            rdvConflit.setHeureDebut(rdv.getHeureDebut());
            rdvConflit.setHeureFin(rdv.getHeureFin());
            rdvConflit.setRdvIdMed(rdvFind.getRdvIdMed());
            if (rdvDAO.existConflitUpdate(rdvConflit)) {
                return new ServiceResult(false, "Ce créneau est déjà occupé, veuillez modifier.");
            }

            // update rdv
            if (rdvDAO.updateRdv(rdvConflit)) return new ServiceResult(true, null);
            return new ServiceResult(false, "Une erreur est survenue lors de la modification du rendez-vous.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error updating rdv", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // delete rdv patient
    public ServiceResult deleteRdvPatient(Rdv rdv) {
        try {

            // Validation: rdv
            Rdv rdvFind = rdvDAO.findById(rdv.getIdRdv());
            if (rdvFind == null) return new ServiceResult(false, "Le rendez-vous choisi n'est pas trouvé.");
            if (!rdvFind.getRdvIdPat().equals(rdv.getRdvIdPat())) {
                return new ServiceResult(false, "Cet rendez-vous ne vous appartient pas.");
            }

            // delete rdv
            if (rdvDAO.deleteRdv(rdv.getIdRdv())) return new ServiceResult(true, null);
            return new ServiceResult(false, "Une erreur est survenue lors de la suppression du rendez-vous.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error deleting rdv patient", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // delete rdv medecin
    public ServiceResult deleteRdvMedecin(Rdv rdv) {
        try {

            // Validation: rdv
            Rdv rdvFind = rdvDAO.findById(rdv.getIdRdv());
            if (rdvFind == null) return new ServiceResult(false, "Le rendez-vous choisi n'est pas trouvé.");
            if (!rdvFind.getRdvIdMed().equals(rdv.getRdvIdMed())) {
                return new ServiceResult(false, "Cet rendez-vous ne vous appartient pas.");
            }

            // delete rdv
            if (rdvDAO.deleteRdv(rdv.getIdRdv())) return new ServiceResult(true, null);
            return new ServiceResult(false, "Une erreur est survenue lors de la suppression du rendez-vous.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error deleting rdv medecin", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // delete rdv
    public ServiceResult deleteRdv(Rdv rdv) {
        try {

            // Validation: rdv
            Rdv rdvFind = rdvDAO.findById(rdv.getIdRdv());
            if (rdvFind == null) return new ServiceResult(false, "Le rendez-vous choisi n'est pas trouvé.");

            // delete rdv
            if (rdvDAO.deleteRdv(rdv.getIdRdv())) return new ServiceResult(true, null);
            return new ServiceResult(false, "Une erreur est survenue lors de la suppression du rendez-vous.");
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réessayer.");
        } catch (Exception e) {
            log.error("Error deleting rdv", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // cancel rdv patient
    public ServiceResult cancelRdvPatient(Rdv rdv) {
        try {
            // Validation: rdv
            Rdv rdvFind = rdvDAO.findById(rdv.getIdRdv());
            if (rdvFind == null) return new ServiceResult(false, "Le rendez-vous choisi n'est pas trouvé.");
            if (!rdvFind.getRdvIdPat().equals(rdv.getRdvIdPat()))
                return new ServiceResult(false, "Cet rendez-vous ne vous appartient pas.");

            // find email patient
            PatientDAO patientDao = new PatientDAO();
            Patient patient = patientDao.findById(rdv.getRdvIdPat());
            if (patient == null) return new ServiceResult(false, "Compte introuvable, veuillez réconnecter.");
21
            // cancel rdv patient
            if (!rdvDAO.cancelRdv(rdv.getIdRdv())) return new ServiceResult(false,21
                    "Une erreur est survenue lors de l'annulation du rendez-vous.");

            // send mail
            EmailService emailSer = new EmailService();
            String contenu = "Bonjour, votre rendez-vous numéro <b style='color:blue'>" + rdv.getIdRdv() + "</b> a été annulé avec succès.";
            if (!emailSer.sendMail(patient.getEmailPat(), "Annulation rendez-vous", contenu)) {
                return new ServiceResult(false,
                        "Rendez-vous annulé avec succès mais il y a une erreur survenue pour l'envoie d'email");
            }

            return new ServiceResult(true, null);
        } catch (SQLException e) {
            log.error("Error SQL", e);
            return new ServiceResult(false, "Erreur technique, veuillez réssayer");
        } catch (Exception e) {
            log.error("Error canceling rdv patient", e);
            return new ServiceResult(false, "Une erreur innatendue s'est produite.");
        }
    }

    // TODO: confirm rdv + email (verify if dépassé)

}
