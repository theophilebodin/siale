// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import java.util.Set;
import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.NoteCritere;
import nc.mairie.siale.domain.NoteGroupe;

privileged aspect NoteGroupe_Roo_JavaBean {
    
    public String NoteGroupe.getNom() {
        return this.nom;
    }
    
    public void NoteGroupe.setNom(String nom) {
        this.nom = nom;
    }
    
    public double NoteGroupe.getPonderation() {
        return this.ponderation;
    }
    
    public void NoteGroupe.setPonderation(double ponderation) {
        this.ponderation = ponderation;
    }
    
    public Set<NoteCritere> NoteGroupe.getNoteCriteres() {
        return this.noteCriteres;
    }
    
    public void NoteGroupe.setNoteCriteres(Set<NoteCritere> noteCriteres) {
        this.noteCriteres = noteCriteres;
    }
    
    public Bareme NoteGroupe.getBareme() {
        return this.bareme;
    }
    
    public void NoteGroupe.setBareme(Bareme bareme) {
        this.bareme = bareme;
    }
    
}
