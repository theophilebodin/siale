package nc.mairie.siale.domain;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class MissionDocument implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2346195251744519517L;

	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateDocument;

    @ManyToOne
    private Param theDocument;

    @ManyToOne
    private Mission theMission;

	@Id
    @SequenceGenerator(name = "missionDocumentGen", sequenceName = "missiondocument_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "missionDocumentGen")
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Date getDateDocument() {
        return this.dateDocument;
    }

	public void setDateDocument(Date dateDocument) {
        this.dateDocument = dateDocument;
    }

	public Param getTheDocument() {
        return this.theDocument;
    }

	public void setTheDocument(Param theDocument) {
        this.theDocument = theDocument;
    }

	public Mission getTheMission() {
        return this.theMission;
    }

	public void setTheMission(Mission theMission) {
        this.theMission = theMission;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "dateDocument", "theDocument", "theMission");

	public static final EntityManager entityManager() {
        EntityManager em = new MissionDocument().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMissionDocuments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MissionDocument o", Long.class).getSingleResult();
    }

	public static List<MissionDocument> findAllMissionDocuments() {
        return entityManager().createQuery("SELECT o FROM MissionDocument o", MissionDocument.class).getResultList();
    }

	public static List<MissionDocument> findAllMissionDocuments(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MissionDocument o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MissionDocument.class).getResultList();
    }

	public static MissionDocument findMissionDocument(Long id) {
        if (id == null) return null;
        return entityManager().find(MissionDocument.class, id);
    }

	public static List<MissionDocument> findMissionDocumentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MissionDocument o", MissionDocument.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<MissionDocument> findMissionDocumentEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MissionDocument o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MissionDocument.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MissionDocument attached = MissionDocument.findMissionDocument(this.id);
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
    public MissionDocument merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MissionDocument merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
