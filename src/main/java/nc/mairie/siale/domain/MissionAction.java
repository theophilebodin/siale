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
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class MissionAction implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7903447031476154232L;

	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateAction;

//
//    @NotNull
//    @OneToOne
//    private Mission theMission;
    
    @NotNull
    @ManyToOne
    private Param theAction;


	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "dateAction", "theAction");

	public static final EntityManager entityManager() {
        EntityManager em = new MissionAction().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMissionActions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MissionAction o", Long.class).getSingleResult();
    }

	public static List<MissionAction> findAllMissionActions() {
        return entityManager().createQuery("SELECT o FROM MissionAction o", MissionAction.class).getResultList();
    }

	public static List<MissionAction> findAllMissionActions(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MissionAction o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MissionAction.class).getResultList();
    }

	public static MissionAction findMissionAction(Long id) {
        if (id == null) return null;
        return entityManager().find(MissionAction.class, id);
    }

	public static List<MissionAction> findMissionActionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MissionAction o", MissionAction.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<MissionAction> findMissionActionEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MissionAction o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MissionAction.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MissionAction attached = MissionAction.findMissionAction(this.id);
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
    public MissionAction merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MissionAction merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Date getDateAction() {
        return this.dateAction;
    }

	public void setDateAction(Date dateAction) {
        this.dateAction = dateAction;
    }

	public Param getTheAction() {
        return this.theAction;
    }

	public void setTheAction(Param theAction) {
        this.theAction = theAction;
    }

	@Id
    @SequenceGenerator(name = "missionActionGen", sequenceName = "missionaction_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "missionActionGen")
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
