package com.doctorapointment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
			log.error("Error adding patient", e);
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
			log.error("Error verifying email_pat", e);
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
			log.error("Error getting next patient ID", e);
		}
		return "P001";
	}

	// find by email
	public Patient findByEmail(String email) {
		String query = "SELECT id_pat, nom_pat, prenom_pat, date_nais, mdp_pat, email_pat FROM patient WHERE email_pat = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Patient patient = new Patient();
				patient.setIdPat(rs.getString("id_pat"));
				patient.setNomPat(rs.getString("nom_pat"));
				patient.setPrenomPat(rs.getString("prenom_pat"));
				patient.setDateNais(rs.getObject("date_nais", LocalDate.class));
				patient.setEmailPat(rs.getString("email_pat"));
				patient.setMdpPat(rs.getString("mdp_pat"));

				return patient;
			}
		} catch (SQLException e) {
			log.error("Error finding patient by email : {}", email, e);
		}

		return null;
	}

	// Filter patient
	public static List<Patient> filterPatient(String search, LocalDate dateNaisDebut, LocalDate dateNaisFin) {
		String query = "SELECT id_pat, nom_pat, prenom_pat, date_nais, email_pat FROM patient WHERE "
				+ "(id_pat LIKE ? OR CONCAT(nom_pat, ' ', prenom_pat) LIKE ? OR email_pat LIKE ?) ";

		// search: date_nais
		if (dateNaisDebut == null) {
			if (dateNaisFin != null) {
				query += "AND date_nais <= ? ";
			}
		} else {
			if (dateNaisFin == null) {
				query += "AND date_nais >= ? ";
			} else {
				query += "AND date_nais BETWEEN ? AND ? ";
			}
		}

		// patient object list
		List<Patient> listPat = new ArrayList<>();

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			search = "%" + search + "%";
			stmt.setString(1, search);
			stmt.setString(2, search);
			stmt.setString(3, search);
			// search: date_nais
			int index = 4;
			if (dateNaisDebut != null) {
				stmt.setObject(index++, dateNaisDebut);
			}
			if (dateNaisFin != null) {
				stmt.setObject(index++, dateNaisFin);
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Patient patient = new Patient();
				patient.setIdPat(rs.getString("id_pat"));
				patient.setNomPat(rs.getString("nom_pat"));
				patient.setPrenomPat(rs.getString("prenom_pat"));
				patient.setEmailPat(rs.getString("email_pat"));
				patient.setDateNais(rs.getObject("date_nais", LocalDate.class));

				listPat.add(patient);
			}
		} catch (SQLException e) {
			log.error("Error filtering patient", e);
		}

		return listPat;
	}

	// Update patient
	public boolean updatePatient(Patient patient) {
		String query = "UPDATE patient SET nom_pat = ?, prenom_pat = ?, date_nais = ?, email_pat = ? ";
		if (patient.getMdpPat() != null && !patient.getMdpPat().isEmpty()) {
			query += ", mdp_pat = ? ";
		}
		query += "WHERE id_pat = ? ";
		
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)){
			stmt.setString(1,patient.getNomPat());
			stmt.setString(2,patient.getPrenomPat());
			stmt.setObject(3,patient.getDateNais());
			stmt.setString(4,patient.getEmailPat());
			int index = 5;
			if(patient.getMdpPat() != null && !patient.getMdpPat().isEmpty()) {
				stmt.setString(index++, patient.getMdpPat());
			}
			stmt.setString(index, patient.getIdPat());
			
			return stmt.executeUpdate() >= 0;
		}
		catch(SQLException e) {
			log.error("Error updating patient", e);
			return false;
		}
	}
	
	// Find by id_pat
	public Patient findById(String idPat) {
		String query = "SELECT id_pat, nom_pat, prenom_pat, date_nais, mdp_pat, email_pat FROM patient WHERE id_pat = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, idPat);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Patient patient = new Patient();
				patient.setIdPat(rs.getString("id_pat"));
				patient.setNomPat(rs.getString("nom_pat"));
				patient.setPrenomPat(rs.getString("prenom_pat"));
				patient.setDateNais(rs.getObject("date_nais", LocalDate.class));
				patient.setEmailPat(rs.getString("email_pat"));
				patient.setMdpPat(rs.getString("mdp_pat"));

				return patient;
			}
		} catch (SQLException e) {
			log.error("Error finding patient by id : {}", idPat, e);
		}

		return null;
	}
}
