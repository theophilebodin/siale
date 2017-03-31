package nc.mairie.siale.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Droit implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8797993967678893505L;
	@NotNull
    private String nom;
    
	//Maven site find bugs ne trouve pas findDroit (aspect)
	//J'ai donc du créer cette méthode à la con pour que findbugs "trouve" FindDroit.... :(
	public static Droit findDroitACauseDeMavenSiteFindBugs(Long l) {
		return findDroit(l);
	}
    

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "nom");

	public static final EntityManager entityManager() {
        EntityManager em = new Droit().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countDroits() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Droit o", Long.class).getSingleResult();
    }

	public static List<Droit> findAllDroits() {
        return entityManager().createQuery("SELECT o FROM Droit o", Droit.class).getResultList();
    }

	public static List<Droit> findAllDroits(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Droit o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Droit.class).getResultList();
    }

	public static Droit findDroit(Long id) {
        if (id == null) return null;
        return entityManager().find(Droit.class, id);
    }

	public static List<Droit> findDroitEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Droit o", Droit.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Droit> findDroitEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Droit o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Droit.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Droit attached = Droit.findDroit(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Droit merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Droit merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getNom() {
        return this.nom;
    }

	public void setNom(String nom) {
        this.nom = nom;
    }

	@Id
    @SequenceGenerator(name = "droitGen", sequenceName = "droit_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "droitGen")
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }
}
