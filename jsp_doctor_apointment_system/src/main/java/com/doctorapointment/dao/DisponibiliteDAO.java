package com.doctorapointment.dao;

import com.doctorapointment.model.Disponibilite;
import com.mysql.cj.xdevapi.PreparableStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public boolean chevaucheExistant(Disponibilite dispo) throws SQLException {
        String query = "SELECT COUNT(*) FROM disponibilite WHERE date_dispo = ? " +
                "AND dispo_id_med = ? AND debut_dispo < ? AND fin_dispo > ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setObject(1, dispo.getDateDispo());
            stmt.setString(2, dispo.getDispoIdMed());
            stmt.setObject(3, dispo.getFinDispo());
            stmt.setObject(4, dispo.getDebutDispo());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
