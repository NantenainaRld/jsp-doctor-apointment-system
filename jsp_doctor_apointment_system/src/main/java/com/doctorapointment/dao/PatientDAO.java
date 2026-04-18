package com.doctorapointment.dao;

import org.slf4j.LoggerFactory;

import com.doctorapointment.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;

public class PatientDAO {
	private static final Logger log = LoggerFactory.getLogger(PatientDAO.class);

	// add patient
	public boolean addPatient(Patient patient) {
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
}
