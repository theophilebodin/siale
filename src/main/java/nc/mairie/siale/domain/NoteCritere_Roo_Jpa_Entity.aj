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
import nc.mairie.siale.domain.NoteCritere;

privileged aspect NoteCritere_Roo_Jpa_Entity {
    
    declare @type: NoteCritere: @Entity;
    
    @Id
    @SequenceGenerator(name = "noteCritereGen", sequenceName = "notecritere_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "noteCritereGen")
    @Column(name = "id")
    private Long NoteCritere.id;
    
    @Version
    @Column(name = "version")
    private Integer NoteCritere.version;
    
    public Long NoteCritere.getId() {
        return this.id;
    }
    
    public void NoteCritere.setId(Long id) {
        this.id = id;
    }
    
    public Integer NoteCritere.getVersion() {
        return this.version;
    }
    
    public void NoteCritere.setVersion(Integer version) {
        this.version = version;
    }
    
}