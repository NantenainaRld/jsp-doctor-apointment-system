package com.doctorapointment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.doctorapointment.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doctorapointment.model.Medecin;

import javax.xml.crypto.Data;
import javax.xml.transform.Result;

public class MedecinDAO {
    private static final Logger log = LoggerFactory.getLogger(MedecinDAO.class);

    // register medecin
    public boolean registerMedecin(Medecin medecin) {
        String query = "INSERT INTO medecin (id_med, nom_med, prenom_med, specialite,"
                + " lieu, mdp_med, taux_horaire) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, medecin.getIdMed());
            stmt.setString(2, medecin.getNomMed());
            stmt.setString(3, medecin.getPrenomMed());
            stmt.setString(4, medecin.getSpecialite());
            stmt.setString(5, medecin.getLieu());
            stmt.setString(6, medecin.getMdpMed());
            stmt.setDouble(7, medecin.getTauxHoraire());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error adding medecin", e);
            return false;
        }
    }

    // get next medecin id
    public String getNextMedecinId() {
        String query = "SELECT MAX(id_med) FROM medecin";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId != null && maxId.startsWith("M")) {
                    int num = Integer.parseInt(maxId.substring(1)) + 1;
                    return String.format("M%03d", num);
                }
            }
        } catch (SQLException e) {
            log.error("Error getting next medecin ID", e);
        }
        return "M001";
    }

    // find by id
    public Medecin findById(String idMed) {
        String query = "SELECT id_med, nom_med, prenom_med," +
                " specialite, lieu, mdp_med, taux_horaire FROM medecin WHERE id_med = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, idMed);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Medecin medecin = new Medecin();
                medecin.setIdMed(rs.getString("id_med"));
                medecin.setNomMed(rs.getString("nom_med"));
                medecin.setPrenomMed(rs.getString("prenom_med"));
                medecin.setSpecialite(rs.getString("specialite"));
                medecin.setLieu(rs.getString("lieu"));
                medecin.setMdpMed(rs.getString("mdp_med"));
                medecin.setTauxHoraire(rs.getDouble("taux_horaire"));

                return medecin;
            }
        } catch (SQLException e) {
            log.error("Error finding medecin by id", e);
        }

        return null;
    }

    // filter medecin
    public static List<Medecin> filterMedecin(String search, String specialite, String taux) {
        String query = "SELECT id_med, nom_med, prenom_med, specialite, lieu, taux_horaire " +
                "FROM medecin WHERE (id_med LIKE ? OR CONCAT(nom_med, ' ' , prenom_med) LIKE ? " +
                "OR lieu LIKE ?) ";

        // search : specialite
        if (!specialite.isEmpty()) query += "AND specialite = ? ";
        // search : order by taux_horaire
        if (taux.equals("desc")) {
            query += "ORDER BY taux_horaire DESC ";
        } else if (taux.equals("asc")) {
            query += "ORDER BY taux_horaire ASC ";
        } else {
            query += "ORDER BY id_med ASC ";
        }

        List<Medecin> listMed = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            search = "%" + search + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);
            if (!specialite.isEmpty()) stmt.setString(4, specialite);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medecin medecin = new Medecin();
                medecin.setIdMed(rs.getString("id_med"));
                medecin.setNomMed(rs.getString("nom_med"));
                medecin.setPrenomMed(rs.getString("prenom_med"));
                medecin.setSpecialite(rs.getString("specialite"));
                medecin.setLieu(rs.getString("lieu"));
                medecin.setTauxHoraire(rs.getDouble("taux_horaire"));

                listMed.add(medecin);
            }
        } catch (SQLException e) {
            log.error("Error filtering medecin", e);
        }

        return listMed;
    }

    // update medecin
    public boolean updateMedecin(Medecin medecin) {
        String query = "UPDATE medecin SET nom_med = ?, prenom_med = ?, specialite = ?, lieu = ?," +
                "taux_horaire = ? ";
        if (!medecin.getMdpMed().isEmpty()) query += ", mdp_med = ? ";
        query += "WHERE id_med = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, medecin.getNomMed());
            stmt.setString(2, medecin.getPrenomMed());
            stmt.setString(3, medecin.getSpecialite());
            stmt.setString(4, medecin.getLieu());
            stmt.setDouble(5, medecin.getTauxHoraire());
            int index = 6;
            if (!medecin.getMdpMed().isEmpty()) stmt.setString(index++, medecin.getMdpMed());
            stmt.setString(index, medecin.getIdMed());

            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            log.error("Error updating medecin", e);
            return false;
        }
    }

    // delete medecin
    public static boolean deleteMedecin(String idMed) {
        String query = "DELETE FROM medecin WHERE id_med = ? ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, idMed);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error deleting patient");
            return false;
        }
    }

    // top medecin
    public List<Medecin> topMedecin() throws SQLException {
        List<Medecin> lisMed = new ArrayList<>();

        String query = "SELECT med.id_med, med.nom_med, med.prenom_med, med.specialite, " +
                " med.lieu, med.taux_horaire, COUNT(rdv.id_rdv) AS nb_rdv " +
                "FROM medecin med " +
                "JOIN rdv rdv ON rdv.rdv_id_med = med.id_med " +
                "GROUP BY med.id_med, med.nom_med, med.prenom_med, med.specialite, med.lieu, med.taux_horaire " +
                "ORDER BY nb_rdv DESC " +
                "LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(query)) {
            while (rs.next()) {
                Medecin medecin = new Medecin();
                medecin.setIdMed(rs.getString("id_med"));
                medecin.setNomMed(rs.getString("nom_med"));
                medecin.setPrenomMed(rs.getString("prenom_med"));
                medecin.setSpecialite(rs.getString("specialite"));
                medecin.setLieu(rs.getString("lieu"));
                medecin.setTauxHoraire(rs.getDouble("taux_horaire"));

                lisMed.add(medecin);
            }
        }

        return lisMed;
    }
}
