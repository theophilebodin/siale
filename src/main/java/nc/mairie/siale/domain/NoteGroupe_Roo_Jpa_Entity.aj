// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import nc.mairie.siale.domain.NoteGroupe;

privileged aspect NoteGroupe_Roo_Jpa_Entity {
    
    declare @type: NoteGroupe: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long NoteGroupe.id;
    
    @Version
    @Column(name = "version")
    private Integer NoteGroupe.version;
    
    public Long NoteGroupe.getId() {
        return this.id;
    }
    
    public void NoteGroupe.setId(Long id) {
        this.id = id;
    }
    
    public Integer NoteGroupe.getVersion() {
        return this.version;
    }
    
    public void NoteGroupe.setVersion(Integer version) {
        this.version = version;
    }
    
}
