package com.doctorapointment.model;

public class Medecin {
	private String idMed, nomMed, prenomMed, specialite, lieu, mdpMed;
	private double tauxHoraire;
	
	// getters
	public String getIdMed() {
		return this.idMed;
	}
	public String getNomMed() {
		return this.nomMed;
	}
	public String getPrenomMed() {
		return this.prenomMed;
	}
	public String getSpecialite() {
		return this.specialite;
	}
	public String getLieu() {
		return this.lieu;
	}
	public String getMdpMed() {
		return this.mdpMed;
	}
	public double getTauxHoraire() {
		return this.tauxHoraire;
	}
	
	// setters 
	public void setIdMed(String idMed) {
		this.idMed =idMed.toUpperCase();
	}
	public void setNomMed(String nomMed) {
		this.nomMed = nomMed.toUpperCase();
	}
	public void setPrenomMed(String prenomMed) {
		this.prenomMed = prenomMed;
	}
	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
	public void setMdpMed(String mdpMed) {
		this.mdpMed = mdpMed;
	}
	public void setTauxHoraire(double tauxHoraire) {
		this.tauxHoraire = tauxHoraire;
	}
}
