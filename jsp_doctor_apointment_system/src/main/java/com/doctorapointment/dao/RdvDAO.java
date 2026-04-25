package com.doctorapointment.dao;

import com.doctorapointment.model.Rdv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    // find by debut and fin
    public boolean existConflit(Rdv rdv) throws SQLException{
        String  query = "SELECT COUNT(*) FROM rdv WHERE date_rdv = ? AND" +
                " heure_debut < ? AND heure_fin > ? AND rdv_id_med = ? AND etat_rdv != 'annulé'";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setObject(1,rdv.getDateRdv());
            stmt.setObject(2, rdv.getHeureFin());
            stmt.setObject(3, rdv.getHeureDebut());
            stmt.setString(4, rdv.getRdvIdMed());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
