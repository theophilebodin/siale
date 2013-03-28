// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import java.util.Date;
import java.util.Set;
import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Etablissement;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionAction;
import nc.mairie.siale.domain.MissionActivite;
import nc.mairie.siale.domain.MissionDocument;
import nc.mairie.siale.domain.Notation;
import nc.mairie.siale.domain.Param;

privileged aspect Mission_Roo_JavaBean {
    
    public String Mission.getProjet() {
        return this.projet;
    }
    
    public void Mission.setProjet(String projet) {
        this.projet = projet;
    }
    
    public String Mission.getEtablissementNonDeclare() {
        return this.etablissementNonDeclare;
    }
    
    public void Mission.setEtablissementNonDeclare(String etablissementNonDeclare) {
        this.etablissementNonDeclare = etablissementNonDeclare;
    }
    
    public Etablissement Mission.getEtablissement() {
        return this.etablissement;
    }
    
    public void Mission.setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }
    
    public int Mission.getPrelevement_nb() {
        return this.prelevement_nb;
    }
    
    public void Mission.setPrelevement_nb(int prelevement_nb) {
        this.prelevement_nb = prelevement_nb;
    }
    
    public int Mission.getPrelevement_satisfaisant() {
        return this.prelevement_satisfaisant;
    }
    
    public void Mission.setPrelevement_satisfaisant(int prelevement_satisfaisant) {
        this.prelevement_satisfaisant = prelevement_satisfaisant;
    }
    
    public int Mission.getPrelevement_mediocre() {
        return this.prelevement_mediocre;
    }
    
    public void Mission.setPrelevement_mediocre(int prelevement_mediocre) {
        this.prelevement_mediocre = prelevement_mediocre;
    }
    
    public int Mission.getPrelevement_non_satisfaisant() {
        return this.prelevement_non_satisfaisant;
    }
    
    public void Mission.setPrelevement_non_satisfaisant(int prelevement_non_satisfaisant) {
        this.prelevement_non_satisfaisant = prelevement_non_satisfaisant;
    }
    
    public Set<MissionActivite> Mission.getMissionActivites() {
        return this.missionActivites;
    }
    
    public void Mission.setMissionActivites(Set<MissionActivite> missionActivites) {
        this.missionActivites = missionActivites;
    }
    
    public Set<Param> Mission.getControleurs() {
        return this.controleurs;
    }
    
    public void Mission.setControleurs(Set<Param> controleurs) {
        this.controleurs = controleurs;
    }
    
    public Set<MissionDocument> Mission.getMissionDocuments() {
        return this.missionDocuments;
    }
    
    public void Mission.setMissionDocuments(Set<MissionDocument> missionDocuments) {
        this.missionDocuments = missionDocuments;
    }
    
    public Param Mission.getSuiteDonnee() {
        return this.suiteDonnee;
    }
    
    public void Mission.setSuiteDonnee(Param suiteDonnee) {
        this.suiteDonnee = suiteDonnee;
    }
    
    public MissionAction Mission.getMissionAction() {
        return this.missionAction;
    }
    
    public void Mission.setMissionAction(MissionAction missionAction) {
        this.missionAction = missionAction;
    }
    
    public Date Mission.getDateIntervention() {
        return this.dateIntervention;
    }
    
    public void Mission.setDateIntervention(Date dateIntervention) {
        this.dateIntervention = dateIntervention;
    }
    
    public Date Mission.getDatePrevue() {
        return this.datePrevue;
    }
    
    public void Mission.setDatePrevue(Date datePrevue) {
        this.datePrevue = datePrevue;
    }
    
    public String Mission.getObservation() {
        return this.observation;
    }
    
    public void Mission.setObservation(String observation) {
        this.observation = observation;
    }
    
    public Boolean Mission.getCloturee() {
        return this.cloturee;
    }
    
    public void Mission.setCloturee(Boolean cloturee) {
        this.cloturee = cloturee;
    }
    
    public Set<ControleurSIALE> Mission.getControleursSIALE() {
        return this.controleursSIALE;
    }
    
    public void Mission.setControleursSIALE(Set<ControleurSIALE> controleursSIALE) {
        this.controleursSIALE = controleursSIALE;
    }
    
    public Date Mission.getDureePrevueRDV() {
        return this.dureePrevueRDV;
    }
    
    public void Mission.setDureePrevueRDV(Date dureePrevueRDV) {
        this.dureePrevueRDV = dureePrevueRDV;
    }
    
    public Set<Notation> Mission.getNotations() {
        return this.notations;
    }
    
    public void Mission.setNotations(Set<Notation> notations) {
        this.notations = notations;
    }
    
    public Bareme Mission.getBareme() {
        return this.bareme;
    }
    
    public void Mission.setBareme(Bareme bareme) {
        this.bareme = bareme;
    }
    
    public Date Mission.getDateNotation() {
        return this.dateNotation;
    }
    
    public void Mission.setDateNotation(Date dateNotation) {
        this.dateNotation = dateNotation;
    }
    
}
