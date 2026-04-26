package com.doctorapointment.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Disponibilite {
    private int idDispo;
    private String dispoIdMed;
    private LocalDate dateDispo;
    private LocalTime debutDispo;
    private LocalTime finDispo;

    // getters
    public int getIdDispo() {
        return idDispo;
    }
    public String getDispoIdMed(){
        return  dispoIdMed;
    }
    public LocalDate getDateDispo(){
        return dateDispo;
    }
    public   LocalTime getDebutDispo(){
        return debutDispo;
    }
    public LocalTime getFinDispo(){
        return finDispo;
    }

    // setters

    public void setDispoIdMed(String dispoIdMed) {
        this.dispoIdMed = dispoIdMed.toUpperCase();
    }

    public void setDateDispo(LocalDate dateDispo) {
        this.dateDispo = dateDispo;
    }

    public void setIdDispo(int idDispo) {
        this.idDispo = idDispo;
    }

    public void setDebutDispo(LocalTime debutDebut) {
        this.debutDispo = debutDebut;
    }

    public void setFinDispo(LocalTime finDispo) {
        this.finDispo = finDispo;
    }
}
