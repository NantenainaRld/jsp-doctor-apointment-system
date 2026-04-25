package com.doctorapointment.dao;

import com.doctorapointment.model.Rdv;
import com.mysql.cj.protocol.Resultset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RdvDAO {
    private static final Logger log = LoggerFactory.getLogger(RdvDAO.class);

    // add rdv
    public boolean addRdv(Rdv rdv) throws SQLException {
        String query = "INSERT INTO rdv (rdv_id_med, rdv_id_pat, date_pris_rdv, " +
                "date_rdv, heure_debut, heure_fin) VALUES (?, ?, NOW(), ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, rdv.getRdvIdMed());
            stmt.setString(2, rdv.getRdvIdPat());
            stmt.setObject(3, rdv.getDateRdv());
            stmt.setObject(4, rdv.getHeureDebut());
            stmt.setObject(5, rdv.getHeureFin());

            return stmt.executeUpdate() > 0;
        }
    }

    // find conflit
    public boolean existConflit(Rdv rdv) throws SQLException {
        String query = "SELECT COUNT(*) FROM rdv WHERE date_rdv = ? AND" +
                " heure_debut < ? AND heure_fin > ? AND rdv_id_med = ? AND etat_rdv != 'annulé'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, rdv.getDateRdv());
            stmt.setObject(2, rdv.getHeureFin());
            stmt.setObject(3, rdv.getHeureDebut());
            stmt.setString(4, rdv.getRdvIdMed());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // filter rdv dispo
    public List<Rdv> filterRdvDispo(Rdv rdv, LocalTime heureDebut, LocalTime heureFin) throws SQLException {
        List<Rdv> listRdv = new ArrayList<>();
        String query = "SELECT id_rdv, heure_debut, heure_fin FROM rdv WHERE date_rdv = ? AND rdv_id_med = ? ";
        if (heureDebut != null && heureFin != null) {
            query += "AND heure_debut >= ? AND heure_fin <= ?";
        } else if (heureDebut != null) {
            query += "AND heure_debut >= ? ";
        } else if (heureFin != null) {
            query += "AND heure_fin <= ? ";
        }
        query += "ORDER BY heure_debut, id_rdv";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, rdv.getDateRdv());
            stmt.setString(2, rdv.getRdvIdMed());
            int index = 3;
            if (heureDebut != null) stmt.setObject(index++, heureDebut);
            if (heureFin != null) stmt.setObject(index++, heureFin);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Rdv rdvv = new Rdv();
                rdvv.setIdRdv(rs.getInt("id_rdv"));
                rdvv.setHeureDebut(rs.getObject("heure_debut", LocalTime.class));
                rdvv.setHeureFin(rs.getObject("heure_fin", LocalTime.class));

                listRdv.add(rdvv);
            }
        }

        return listRdv;
    }

    // update rdv
    public boolean updateRdv(Rdv rdv) throws SQLException {
        String query = "UPDATE rdv SET heure_debut = ?, heure_fin = ? WHERE id_rdv = ? ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, rdv.getHeureDebut());
            stmt.setObject(2, rdv.getHeureFin());
            stmt.setInt(3, rdv.getIdRdv());

            return stmt.executeUpdate() > 0;
        }
    }

    // find rdv by id
    public Rdv findById(int idRdv) throws SQLException {
        String query = "SELECT id_rdv, rdv_id_med, rdv_id_pat," +
                " date_rdv, etat_rdv, heure_debut, heure_fin, date_pris_rdv FROM rdv WHERE id_rdv = ? ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idRdv);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Rdv rdvFind = new Rdv();
                rdvFind.setIdRdv(rs.getInt("id_rdv"));
                rdvFind.setRdvIdMed(rs.getString("rdv_id_med"));
                rdvFind.setRdvIdPat(rs.getString("rdv_id_pat"));
                rdvFind.setDateRdv(rs.getObject("date_rdv", LocalDate.class));
                rdvFind.setEtatRdv(rs.getString("etat_rdv"));
                rdvFind.setHeureDebut(rs.getObject("heure_debut", LocalTime.class));
                rdvFind.setHeureFin(rs.getObject("heure_fin", LocalTime.class));

                return rdvFind;
            }

            return null;
        }
    }

    // find conflit update rdv
    public boolean existConflitUpdate(Rdv rdv) throws SQLException {
        String query = "SELECT COUNT(*) FROM rdv WHERE date_rdv = ? AND" +
                " heure_debut < ? AND heure_fin > ? AND rdv_id_med = ? AND etat_rdv != 'annulé' AND id_rdv != ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, rdv.getDateRdv());
            stmt.setObject(2, rdv.getHeureFin());
            stmt.setObject(3, rdv.getHeureDebut());
            stmt.setString(4, rdv.getRdvIdMed());
            stmt.setInt(5, rdv.getIdRdv());

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
