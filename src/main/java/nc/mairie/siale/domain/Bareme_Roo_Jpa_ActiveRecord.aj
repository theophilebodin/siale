// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.mairie.siale.domain.Bareme;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Bareme_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Bareme.entityManager;
    
    public static final EntityManager Bareme.entityManager() {
        EntityManager em = new Bareme().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Bareme.countBaremes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Bareme o", Long.class).getSingleResult();
    }
    
    public static List<Bareme> Bareme.findAllBaremes() {
        return entityManager().createQuery("SELECT o FROM Bareme o", Bareme.class).getResultList();
    }
    
    public static Bareme Bareme.findBareme(Long id) {
        if (id == null) return null;
        return entityManager().find(Bareme.class, id);
    }
    
    public static List<Bareme> Bareme.findBaremeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Bareme o", Bareme.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Bareme.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Bareme.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Bareme attached = Bareme.findBareme(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Bareme.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Bareme.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Bareme Bareme.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Bareme merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}