package com.doctorapointment.dao;

import com.doctorapointment.model.RdvPatMed;
import com.mysql.cj.protocol.Resultset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RdvPatMedDAO {
    private static final Logger log = LoggerFactory.getLogger(RdvPatMedDAO.class);

    // filter rdv patient
    public static List<RdvPatMed> filterRdvPatient(String search, String rdvIdPat,
                                                   LocalDate dateRdvDebut, LocalDate dateRdvFin, String etatRdv,
                                                   LocalTime heureDebut, LocalTime heureFin) throws SQLException {
        List<RdvPatMed> listRdv = new ArrayList<>();

        String query = "SELECT rdv.id_rdv, CONCAT(med.nom_med, ' ', med.prenom_med) AS nom_complet_med, " +
                "med.specialite, rdv.date_rdv, rdv.etat_rdv, " +
                "rdv.heure_debut, rdv.heure_fin, rdv.date_pris_rdv FROM rdv rdv JOIN medecin med ON " +
                "med.id_med = rdv.rdv_id_med JOIN patient pat ON pat.id_pat = rdv.rdv_id_pat WHERE " +
                "(rdv.id_rdv LIKE ? OR CONCAT(med.nom_med, ' ', med.prenom_med) LIKE ? ) AND pat.id_pat = ? ";

        // Filter: etat_rdv
        if (!etatRdv.equals("all")) {
            if (etatRdv.equals("dépassé")) {
                query += "AND (rdv.date_rdv < CURDATE() OR (rdv.date_rdv = CURDATE() AND rdv.heure_fin < NOW())) ";
            } else {
                query += "AND rdv.etat_rdv = ? ";
            }
        }

        // FIlter: date_rdv
        if (dateRdvDebut != null && dateRdvFin != null) {
            query += "AND rdv.date_rdv BETWEEN ? AND ? ";
        } else if (dateRdvDebut != null) {
            query += "AND rdv.date_rdv >= ? ";
        } else if (dateRdvFin != null) {
            query += "AND rdv.date_rdv <= ? ";
        }

        // Filter: heure_rdv
        if (heureDebut != null && heureFin != null) {
            query += "AND rdv.heure_debut >= ? AND rdv.heure_fin <= ? ";
        } else if (heureDebut != null) {
            query += "AND rdv.heure_debut >= ? ";
        } else if (heureFin != null) {
            query += "AND rdv.heure_fin <= ? ";
        }

        query += " ORDER BY rdv.date_rdv, rdv.heure_debut DESC ";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            search = "%" + search + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, rdvIdPat);
            int index = 4;
            if (!etatRdv.equals("all") && !etatRdv.equals("dépassé")) stmt.setString(index++, etatRdv);
            if (dateRdvDebut != null) stmt.setObject(index++, dateRdvDebut);
            if (dateRdvFin != null) stmt.setObject(index++, dateRdvFin);
            if (heureDebut != null) stmt.setObject(index++, heureDebut);
            if (heureFin != null) stmt.setObject(index++, heureFin);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RdvPatMed rdvPatMed = new RdvPatMed();
                rdvPatMed.setIdRdv(rs.getInt("id_rdv"));
                rdvPatMed.setDatePrisRdv(rs.getObject("date_pris_rdv", LocalDateTime.class));
                rdvPatMed.setRdvNomMed(rs.getString("nom_complet_med"));
                rdvPatMed.setSpecialite(rs.getString("specialite"));
                rdvPatMed.setEtatRdv(
                        (LocalDateTime.of(rs.getObject("date_rdv", LocalDate.class),
                                        rs.getObject("heure_fin", LocalTime.class))
                                .isBefore(
                                        LocalDateTime.now())) && !rs.getString("etat_rdv").equals("annulé")
                                ? "dépassé" : rs.getString("etat_rdv"));
                rdvPatMed.setDateRdv(rs.getObject("date_rdv", LocalDate.class));
                rdvPatMed.setHeureDebut(rs.getObject("heure_debut", LocalTime.class));
                rdvPatMed.setHeureFin(rs.getObject("heure_fin", LocalTime.class));

                listRdv.add(rdvPatMed);
            }
        }

        return listRdv;
    }

    // filter rdv medecin
    public static List<RdvPatMed> filterRdvMedecin(String search, String rdvIdMed,
                                                   LocalDate dateRdvDebut, LocalDate dateRdvFin, String etatRdv,
                                                   LocalTime heureDebut, LocalTime heureFin) throws SQLException {
        List<RdvPatMed> listRdv = new ArrayList<>();

        String query = "SELECT rdv.id_rdv, CONCAT(pat.nom_pat, ' ', pat.prenom_pat) AS nom_complet_pat, " +
                "pat.date_nais, pat.email_pat, rdv.date_rdv, rdv.etat_rdv, " +
                "rdv.heure_debut, rdv.heure_fin, rdv.date_pris_rdv FROM rdv rdv JOIN patient pat ON " +
                "pat.id_pat = rdv.rdv_id_pat JOIN medecin med ON med.id_med = rdv.rdv_id_med WHERE " +
                "(rdv.id_rdv LIKE ? OR CONCAT(pat.nom_pat, ' ', pat.prenom_pat) LIKE ? ) AND med.id_med = ? ";

        // Filter: etat_rdv
        if (!etatRdv.equals("all")) {
            if (etatRdv.equals("dépassé")) {
                query += "AND (rdv.date_rdv < CURDATE() OR (rdv.date_rdv = CURDATE() AND rdv.heure_fin < NOW())) ";
            } else {
                query += "AND rdv.etat_rdv = ? ";
            }
        }

        // FIlter: date_rdv
        if (dateRdvDebut != null && dateRdvFin != null) {
            query += "AND rdv.date_rdv BETWEEN ? AND ? ";
        } else if (dateRdvDebut != null) {
            query += "AND rdv.date_rdv >= ? ";
        } else if (dateRdvFin != null) {
            query += "AND rdv.date_rdv <= ? ";
        }

        // Filter: heure_rdv
        if (heureDebut != null && heureFin != null) {
            query += "AND rdv.heure_debut >= ? AND rdv.heure_fin <= ? ";
        } else if (heureDebut != null) {
            query += "AND rdv.heure_debut >= ? ";
        } else if (heureFin != null) {
            query += "AND rdv.heure_fin <= ? ";
        }

        query += " ORDER BY rdv.date_rdv, rdv.heure_debut DESC ";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            search = "%" + search + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, rdvIdMed);
            int index = 4;
            if (!etatRdv.equals("all") && !etatRdv.equals("dépassé")) stmt.setString(index++, etatRdv);
            if (dateRdvDebut != null) stmt.setObject(index++, dateRdvDebut);
            if (dateRdvFin != null) stmt.setObject(index++, dateRdvFin);
            if (heureDebut != null) stmt.setObject(index++, heureDebut);
            if (heureFin != null) stmt.setObject(index++, heureFin);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                RdvPatMed rdvPatMed = new RdvPatMed();
                rdvPatMed.setIdRdv(rs.getInt("id_rdv"));
                rdvPatMed.setDatePrisRdv(rs.getObject("date_pris_rdv", LocalDateTime.class));
                rdvPatMed.setRdvNomPat(rs.getString("nom_complet_pat"));
                rdvPatMed.setDateNais(rs.getObject("date_nais", LocalDate.class));
                rdvPatMed.setEmailPat(rs.getString("email_pat"));
                rdvPatMed.setEtatRdv(
                        (LocalDateTime.of(rs.getObject("date_rdv", LocalDate.class),
                                        rs.getObject("heure_fin", LocalTime.class))
                                .isBefore(
                                        LocalDateTime.now())) && !rs.getString("etat_rdv").equals("annulé")
                                ? "dépassé" : rs.getString("etat_rdv"));
                rdvPatMed.setDateRdv(rs.getObject("date_rdv", LocalDate.class));
                rdvPatMed.setHeureDebut(rs.getObject("heure_debut", LocalTime.class));
                rdvPatMed.setHeureFin(rs.getObject("heure_fin", LocalTime.class));

                listRdv.add(rdvPatMed);
            }
        }

        return listRdv;
    }

}
