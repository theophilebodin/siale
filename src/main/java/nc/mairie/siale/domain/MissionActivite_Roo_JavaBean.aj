// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionActivite;
import nc.mairie.siale.domain.Param;

privileged aspect MissionActivite_Roo_JavaBean {
    
    public Boolean MissionActivite.getPrincipale() {
        return this.principale;
    }
    
    public void MissionActivite.setPrincipale(Boolean principale) {
        this.principale = principale;
    }
    
    public Param MissionActivite.getTheActivite() {
        return this.theActivite;
    }
    
    public void MissionActivite.setTheActivite(Param theActivite) {
        this.theActivite = theActivite;
    }
    
    public Mission MissionActivite.getTheMission() {
        return this.theMission;
    }
    
    public void MissionActivite.setTheMission(Mission theMission) {
        this.theMission = theMission;
    }
    
}
