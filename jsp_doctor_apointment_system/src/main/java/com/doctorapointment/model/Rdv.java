package com.doctorapointment.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Rdv {
    private int idRdv;
    private String rdvIdMed, rdvIdPat, etatRdv;
    private LocalDate dateRdv;
    private LocalTime heureDebut, heureFin;
    private LocalDateTime datePrisRdv;

    // getters
    public int getIdRdv() {return this.idRdv;}

    public String getRdvIdMed() {
        return rdvIdMed;
    }

    public String getRdvIdPat() {
        return rdvIdPat;
    }

    public LocalDate getDateRdv() {
        return dateRdv;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public String getEtatRdv() {
        return etatRdv;
    }

    public LocalDateTime getDatePrisRdv() {
        return datePrisRdv;
    }

    // setters

    public void setIdRdv(int idRdv) {
        this.idRdv = idRdv;
    }

    public void setDateRdv(LocalDate dateRdv) {
        this.dateRdv = dateRdv;
    }

    public void setRdvIdMed(String rdvIdMed) {
        this.rdvIdMed = rdvIdMed;
    }

    public void setRdvIdPat(String rdvIdPat) {
        this.rdvIdPat = rdvIdPat;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public void setEtatRdv(String etatRdv) {
        this.etatRdv = etatRdv;
    }

    public void setDatePrisRdv(LocalDateTime datePrisRdv) {
        this.datePrisRdv = datePrisRdv;
    }
}
