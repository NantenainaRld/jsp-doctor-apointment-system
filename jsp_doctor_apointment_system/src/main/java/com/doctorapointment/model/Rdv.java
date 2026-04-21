package com.doctorapointment.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Rdv {
    private int idRdv;
    private String rdvIdMed, rdvIdPat;
    private LocalDate dateRdv;
    private LocalTime heureDebut, heureFin;

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
    
}
