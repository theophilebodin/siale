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
public class MissionActivite implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -398815132975829803L;

	private Boolean principale;

    @NotNull
    @ManyToOne
    private Param theActivite;

    @NotNull
    @ManyToOne
    private Mission theMission;
    
    @Override
    public MissionActivite clone() throws CloneNotSupportedException {
    	MissionActivite res = (MissionActivite)super.clone();
    	boolean b =  principale.booleanValue();
    	res.setPrincipale(b);
    	//res.setTheActivite(theActivite == null ? null : theActivite.clone());
    	return res;
    }

	@Id
    @SequenceGenerator(name = "missionActiviteGen", sequenceName = "missionactivite_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "missionActiviteGen")
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

	public Boolean getPrincipale() {
        return this.principale;
    }

	public void setPrincipale(Boolean principale) {
        this.principale = principale;
    }

	public Param getTheActivite() {
        return this.theActivite;
    }

	public void setTheActivite(Param theActivite) {
        this.theActivite = theActivite;
    }

	public Mission getTheMission() {
        return this.theMission;
    }

	public void setTheMission(Mission theMission) {
        this.theMission = theMission;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "principale", "theActivite", "theMission");

	public static final EntityManager entityManager() {
        EntityManager em = new MissionActivite().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countMissionActivites() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MissionActivite o", Long.class).getSingleResult();
    }

	public static List<MissionActivite> findAllMissionActivites() {
        return entityManager().createQuery("SELECT o FROM MissionActivite o", MissionActivite.class).getResultList();
    }

	public static List<MissionActivite> findAllMissionActivites(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MissionActivite o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MissionActivite.class).getResultList();
    }

	public static MissionActivite findMissionActivite(Long id) {
        if (id == null) return null;
        return entityManager().find(MissionActivite.class, id);
    }

	public static List<MissionActivite> findMissionActiviteEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MissionActivite o", MissionActivite.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<MissionActivite> findMissionActiviteEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MissionActivite o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MissionActivite.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MissionActivite attached = MissionActivite.findMissionActivite(this.id);
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
    public MissionActivite merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MissionActivite merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
