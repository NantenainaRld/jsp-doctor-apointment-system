package com.doctorapointment.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RdvPatMed {
    private int idRdv;
    private String rdvNomMed, rdvNomPat, etatRdv, specialite;
    private LocalDate dateRdv;
    private LocalTime heureDebut, heureFin;
    private LocalDateTime datePrisRdv;

    // getters

    public int getIdRdv() {
        return idRdv;
    }

    public String getRdvNomMed() {
        return rdvNomMed;
    }

    public String getRdvNomPat() {
        return rdvNomPat;
    }

    public LocalDateTime getDatePrisRdv() {
        return datePrisRdv;
    }

    public String getEtatRdv() {
        return etatRdv;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public LocalDate getDateRdv() {
        return dateRdv;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public String getSpecialite() {
        return specialite;
    }
    // setters

    public void setIdRdv(int idRdv) {
        this.idRdv = idRdv;
    }

    public void setRdvNomMed(String rdvNomMed) {
        this.rdvNomMed = rdvNomMed;
    }

    public void setDatePrisRdv(LocalDateTime datePrisRdv) {
        this.datePrisRdv = datePrisRdv;
    }

    public void setRdvNomPat(String rdvNomPat) {
        this.rdvNomPat = rdvNomPat;
    }

    public void setEtatRdv(String etatRdv) {
        this.etatRdv = etatRdv;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setDateRdv(LocalDate dateRdv) {
        this.dateRdv = dateRdv;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}
