package com.doctorapointment.dao;

import com.doctorapointment.model.Disponibilite;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.PreparableStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DisponibiliteDAO {
    private static final Logger log = LoggerFactory.getLogger(DisponibiliteDAO.class);

    // find by date , idmed, debut and fin
    public static boolean findByDateIdMedCreneau(Disponibilite disponibilite) {
        String query = "SELECT COUNT(*) FROM disponibilite WHERE date_dispo = ? " +
                "AND dispo_id_med = ? AND debut_dispo <= ? AND fin_dispo >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, disponibilite.getDateDispo());
            stmt.setString(2, disponibilite.getDispoIdMed());
            stmt.setObject(3, disponibilite.getDebutDispo());
            stmt.setObject(4, disponibilite.getFinDispo());

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error("Error finding dispo by date and idmed and creneau", e);
            return false;
        }
    }

    // add disponibilte
    public boolean addDisponibilte(Disponibilite disponibilite) throws SQLException {
        String query = "INSERT INTO disponibilite (dispo_id_med, date_dispo, debut_dispo, fin_dispo) " +
                "VALUES (?, ?, ?, ?) ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, disponibilite.getDispoIdMed());
            stmt.setObject(2, disponibilite.getDateDispo());
            stmt.setObject(3, disponibilite.getDebutDispo());
            stmt.setObject(4, disponibilite.getFinDispo());

            return stmt.executeUpdate() > 0;
        }
    }

    // chevauchement
    public boolean existeChevauche(Disponibilite dispo) throws SQLException {
        String query = "SELECT COUNT(*) FROM disponibilite WHERE date_dispo = ? " +
                "AND dispo_id_med = ? AND ( (debut_dispo < ? AND fin_dispo > ?) " +
                "OR (debut_dispo = ? AND fin_dispo = ?) )";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, dispo.getDateDispo());
            stmt.setString(2, dispo.getDispoIdMed());
            stmt.setObject(3, dispo.getFinDispo());
            stmt.setObject(4, dispo.getDebutDispo());
            stmt.setObject(5, dispo.getDebutDispo());
            stmt.setObject(6, dispo.getFinDispo());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // filter disponibilite
    public List<Disponibilite> filterDisponibilite(String idMed,
                                                   LocalDate datetDebut, LocalDate dateFin,
                                                   LocalTime heureDebut, LocalTime heureFin) throws SQLException {
        List<Disponibilite> listDispo = new ArrayList<>();

        String query = "SELECT id_dispo, date_dispo, debut_dispo, fin_dispo " +
                "FROM disponibilite WHERE dispo_id_med = ? " +
                "AND (date_dispo > CURDATE() OR (date_dispo = CURDATE() AND fin_dispo >= NOW())) ";

        // Verification: date_dispo
        if (datetDebut != null && dateFin != null) {
            query += "AND date_dispo BETWEEN ? AND ? "; // dateDebut , dateFin
        } else if (datetDebut != null) {
            query += "AND date_dispo >= ?  "; // dateDebut
        } else if (dateFin != null) {
            query += "AND date_dispo <= ? "; // dateFin
        }

        // Verification: heure_dispo
        if (heureDebut != null && heureFin != null) {
            query += "AND debut_dispo <= ? AND fin_dispo >= ? "; // heureDebut, heureFin
        } else if (heureDebut != null) {
            query += "AND debut_dispo >= ? "; // heureDebut
        } else if (heureFin != null) {
            query += "AND debut_dispo <= ? "; // heureFin
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, idMed);
            int index = 2;
            if (datetDebut != null && dateFin != null) {
                stmt.setObject(index++, datetDebut);
                stmt.setObject(index++, dateFin);
            } else if (datetDebut != null) {
                stmt.setObject(index++, datetDebut);
            } else if (dateFin != null) {
                stmt.setObject(index++, dateFin);
            }
            if (heureDebut != null && heureFin != null) {
                stmt.setObject(index++, heureDebut);
                stmt.setObject(index++, heureFin);
            } else if (heureDebut != null) {
                stmt.setObject(index++, heureDebut);
            } else if (heureFin != null) {
                stmt.setObject(index++, heureFin);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Disponibilite disponibilite = new Disponibilite();
                disponibilite.setIdDispo(rs.getInt("id_dispo"));
                disponibilite.setDateDispo(rs.getObject("date_dispo", LocalDate.class));
                disponibilite.setDebutDispo(rs.getObject("debut_dispo", LocalTime.class));
                disponibilite.setFinDispo(rs.getObject("fin_dispo", LocalTime.class));

                listDispo.add(disponibilite);
            }
        }

        return listDispo;
    }

    // update disponibilite
    public boolean updatDisponibilite(Disponibilite disponibilite) throws SQLException {
        String query = "UPDATE disponibilite SET debut_dispo = ? , fin_dispo = ? WHERE id_dispo = ? ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, disponibilite.getDebutDispo());
            stmt.setObject(2, disponibilite.getFinDispo());
            stmt.setInt(3, disponibilite.getIdDispo());

            return stmt.executeUpdate() >= 0;
        }
    }

    // chevauchement (exclu)
    public boolean chevaucheExcluExistant(Disponibilite dispo) throws SQLException {
        String query = "SELECT COUNT(*) FROM disponibilite WHERE date_dispo = ? " +
                "AND dispo_id_med = ? AND ((debut_dispo < ? AND fin_dispo > ?) " +
                "OR (debut_dispo = ? AND fin_dispo = ?)) AND id_dispo != ? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, dispo.getDateDispo());
            stmt.setString(2, dispo.getDispoIdMed());
            stmt.setObject(3, dispo.getFinDispo());
            stmt.setObject(4, dispo.getDebutDispo());
            stmt.setObject(5, dispo.getDebutDispo());
            stmt.setObject(6, dispo.getFinDispo());
            stmt.setInt(7, dispo.getIdDispo());

            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    // find by id
    public Disponibilite findById(int idDispo) throws SQLException {
        String query = "SELECT * FROM disponibilite WHERE id_dispo = ? ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idDispo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Disponibilite dispo = new Disponibilite();
                dispo.setIdDispo(rs.getInt("id_dispo"));
                dispo.setDispoIdMed(rs.getString("dispo_id_med"));
                dispo.setDateDispo(rs.getObject("date_dispo", LocalDate.class));
                dispo.setDebutDispo(rs.getObject("debut_dispo", LocalTime.class));
                dispo.setFinDispo(rs.getObject("fin_dispo", LocalTime.class));

                return dispo;
            }

            return null;
        }
    }

    // delete dispo
    public boolean deleteDispo(int idDispo) throws SQLException {
        String query = "DELETE FROM disponibilite WHERE id_dispo = ? ";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idDispo);

            return stmt.executeUpdate() >= 0;
        }
    }
}
