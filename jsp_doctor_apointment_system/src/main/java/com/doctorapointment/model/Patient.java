package com.doctorapointment.model;

import java.time.LocalDate;

public class Patient {
	private String idPat, nomPat, prenomPat, mdpPat, emailPat;
	private LocalDate dateNais;

	// getters
	public String getIdPat() {
		return this.idPat;
	}

	public String getNomPat() {
		return this.nomPat;
	}

	public String getPrenomPat() {
		return this.prenomPat;
	}

	public String getMdpPat() {
		return this.mdpPat;
	}

	public String getEmailPat() {
		return this.emailPat;
	}
	public LocalDate getDateNais() {
		return this.dateNais;
	}
	
	// setters
	public void setIdPat(String idPat) {
		this.idPat = idPat;
	}
	public void setNomPat(String nomPat) {
		this.nomPat = nomPat;
	}
	public void setPrenomPat(String prenomPat) {
		this.prenomPat = prenomPat;
	}
	public void setMdpPat(String mdpPat) {
		this.mdpPat = mdpPat;
	}
	public void setEmailPat(String emailPat) {
		this.emailPat = emailPat;
	}
	public void setDateNais(LocalDate dateNais) {
		this.dateNais = dateNais;
	}
}
