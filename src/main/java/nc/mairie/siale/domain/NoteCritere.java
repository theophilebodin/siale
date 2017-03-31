package nc.mairie.siale.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class NoteCritere implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6293833914338897655L;

	@NotNull
    private String nom;

    private double ponderation;

    @NotNull
    @ManyToOne
    private NoteGroupe noteGroupe;
    
    @Override
    public NoteCritere clone() throws CloneNotSupportedException {
    	return (NoteCritere)super.clone();
    }
 

	public String getNom() {
        return this.nom;
    }

	public void setNom(String nom) {
        this.nom = nom;
    }

	public double getPonderation() {
        return this.ponderation;
    }

	public void setPonderation(double ponderation) {
        this.ponderation = ponderation;
    }

	public NoteGroupe getNoteGroupe() {
        return this.noteGroupe;
    }

	public void setNoteGroupe(NoteGroupe noteGroupe) {
        this.noteGroupe = noteGroupe;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @SequenceGenerator(name = "noteCritereGen", sequenceName = "notecritere_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "noteCritereGen")
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "nom", "ponderation", "noteGroupe");

	public static final EntityManager entityManager() {
        EntityManager em = new NoteCritere().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countNoteCriteres() {
        return entityManager().createQuery("SELECT COUNT(o) FROM NoteCritere o", Long.class).getSingleResult();
    }

	public static List<NoteCritere> findAllNoteCriteres() {
        return entityManager().createQuery("SELECT o FROM NoteCritere o", NoteCritere.class).getResultList();
    }

	public static List<NoteCritere> findAllNoteCriteres(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM NoteCritere o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, NoteCritere.class).getResultList();
    }

	public static NoteCritere findNoteCritere(Long id) {
        if (id == null) return null;
        return entityManager().find(NoteCritere.class, id);
    }

	public static List<NoteCritere> findNoteCritereEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM NoteCritere o", NoteCritere.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<NoteCritere> findNoteCritereEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM NoteCritere o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, NoteCritere.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            NoteCritere attached = NoteCritere.findNoteCritere(this.id);
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
    public NoteCritere merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        NoteCritere merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
