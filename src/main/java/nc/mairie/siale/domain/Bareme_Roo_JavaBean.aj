// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import java.util.Date;
import java.util.Set;
import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.NoteGroupe;

privileged aspect Bareme_Roo_JavaBean {
    
    public String Bareme.getNom() {
        return this.nom;
    }
    
    public void Bareme.setNom(String nom) {
        this.nom = nom;
    }
    
    public Date Bareme.getDateCreation() {
        return this.dateCreation;
    }
    
    public void Bareme.setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public Set<NoteGroupe> Bareme.getNoteGroupes() {
        return this.noteGroupes;
    }
    
    public void Bareme.setNoteGroupes(Set<NoteGroupe> noteGroupes) {
        this.noteGroupes = noteGroupes;
    }
    
}
