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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Notation implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -1689595868969946396L;

	private double note;

    @ManyToOne
    private NoteCritere noteCritere;

    @ManyToOne
    private Mission mission;
    
	public static boolean existNotationByMission(Mission mission) {
        if (mission == null) throw new IllegalArgumentException("The mission argument is required");
        EntityManager em = Notation.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(*) FROM Notation AS o WHERE o.mission = :mission", Long.class);
        q.setParameter("mission", mission);
        return q.getResultList().get(0)!=0;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "note", "noteCritere", "mission");

	public static final EntityManager entityManager() {
        EntityManager em = new Notation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countNotations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Notation o", Long.class).getSingleResult();
    }

	public static List<Notation> findAllNotations() {
        return entityManager().createQuery("SELECT o FROM Notation o", Notation.class).getResultList();
    }

	public static List<Notation> findAllNotations(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Notation o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Notation.class).getResultList();
    }

	public static Notation findNotation(Long id) {
        if (id == null) return null;
        return entityManager().find(Notation.class, id);
    }

	public static List<Notation> findNotationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Notation o", Notation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Notation> findNotationEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Notation o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Notation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Notation attached = Notation.findNotation(this.id);
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
    public Notation merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Notation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @SequenceGenerator(name = "notationGen", sequenceName = "notation_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "notationGen")
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

	public double getNote() {
        return this.note;
    }

	public void setNote(double note) {
        this.note = note;
    }

	public NoteCritere getNoteCritere() {
        return this.noteCritere;
    }

	public void setNoteCritere(NoteCritere noteCritere) {
        this.noteCritere = noteCritere;
    }

	public Mission getMission() {
        return this.mission;
    }

	public void setMission(Mission mission) {
        this.mission = mission;
    }
}
