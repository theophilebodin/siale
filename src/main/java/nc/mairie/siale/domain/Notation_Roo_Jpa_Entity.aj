// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import nc.mairie.siale.domain.Notation;

privileged aspect Notation_Roo_Jpa_Entity {
    
    declare @type: Notation: @Entity;
    
    @Id
    @SequenceGenerator(name = "notationGen", sequenceName = "notation_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "notationGen")
    @Column(name = "id")
    private Long Notation.id;
    
    @Version
    @Column(name = "version")
    private Integer Notation.version;
    
    public Long Notation.getId() {
        return this.id;
    }
    
    public void Notation.setId(Long id) {
        this.id = id;
    }
    
    public Integer Notation.getVersion() {
        return this.version;
    }
    
    public void Notation.setVersion(Integer version) {
        this.version = version;
    }
    
}
