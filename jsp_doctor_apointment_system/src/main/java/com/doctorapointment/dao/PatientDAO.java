package com.doctorapointment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.doctorapointment.model.Patient;

public class PatientDAO {
	private static final Logger log = LoggerFactory.getLogger(PatientDAO.class);

	// register patient
	public boolean registerPatient(Patient patient) {
		String query = "INSERT INTO patient (id_pat, nom_pat, prenom_pat, date_nais, mdp_pat, email_pat)"
				+ " VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {

			stmt.setString(1, patient.getIdPat());
			stmt.setString(2, patient.getNomPat());
			stmt.setString(3, patient.getPrenomPat());
			stmt.setObject(4, patient.getDateNais());
			stmt.setString(5, patient.getMdpPat());
			stmt.setString(6, patient.getEmailPat());

			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			log.error("Error creating patient : '{}' ", e);
			return false;
		}
	}

	// check email
	public Boolean emailExists(String email) {
		String query = "SELECT COUNT(*) FROM patient WHERE email_pat = ? ";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

			return false;
		} catch (SQLException e) {
			log.error("Error verifying email : '{}' ", e);
			return null;
		}
	}

	// get next patient ID
	public String getNextPatientId() {
	    String query = "SELECT MAX(id_pat) FROM patient";

	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(query)) {

	        if (rs.next()) {
	            String maxId = rs.getString(1);
	            if (maxId != null && maxId.startsWith("P")) {
	                int num = Integer.parseInt(maxId.substring(1)) + 1;
	                return String.format("P%03d", num);
	            }
	        }
	    } catch (SQLException e) {
	        log.error("Error getting next patient ID : '{}' ", e);
	    }
	    return "P001";
	}
}
