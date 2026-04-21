package com.doctorapointment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doctorapointment.model.Medecin;

public class MedecinDAO {
	private static final Logger log = LoggerFactory.getLogger(PatientDAO.class);
	
	// Register medecin
	public boolean registerMedecin(Medecin medecin) {
		String query = "INSERT INTO medecin (id_med, nom_med, prenom_med, specialite,"
				+ " lieu, mdp_med, taux_horaire) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)){
			
			stmt.setString(1,medecin.getIdMed());
			stmt.setString(2,medecin.getNomMed());
			stmt.setString(3,medecin.getPrenomMed());
			stmt.setString(4,medecin.getSpecialite());
			stmt.setString(5,medecin.getLieu());
			stmt.setString(6,medecin.getMdpMed());
			stmt.setDouble(7,medecin.getTauxHoraire());
			
			return stmt.executeUpdate() > 0;
		}
		catch(SQLException e) {
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
}
