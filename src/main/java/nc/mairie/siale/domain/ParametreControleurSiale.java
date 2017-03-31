package nc.mairie.siale.domain;
import nc.mairie.siale.technique.Constantes;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;

@Entity
@Configurable
public class ParametreControleurSiale {

    /**
     */
    private int moisVisuMission;

    /**
     */
    private String banniereCouleur;
    
    /**
     */
    @OneToOne
    private ControleurSIALE controleurSIALE;

    public static ParametreControleurSiale getNewDefaultParametreControleurSiale() {
        ParametreControleurSiale param = new ParametreControleurSiale();
        param.setMoisVisuMission(Constantes.PARAM_MOIS_VISU);
        param.setBanniereCouleur(Constantes.PARAM_BANNIERE_COULEUR);
        return param;
    }

   

	public static Long countFindParametreControleurSialesByControleurSIALE(ControleurSIALE controleurSIALE) {
        if (controleurSIALE == null) throw new IllegalArgumentException("The controleurSIALE argument is required");
        EntityManager em = ParametreControleurSiale.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ParametreControleurSiale AS o WHERE o.controleurSIALE = :controleurSIALE", Long.class);
        q.setParameter("controleurSIALE", controleurSIALE);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ParametreControleurSiale> findParametreControleurSialesByControleurSIALE(ControleurSIALE controleurSIALE) {
        if (controleurSIALE == null) throw new IllegalArgumentException("The controleurSIALE argument is required");
        EntityManager em = ParametreControleurSiale.entityManager();
        TypedQuery<ParametreControleurSiale> q = em.createQuery("SELECT o FROM ParametreControleurSiale AS o WHERE o.controleurSIALE = :controleurSIALE", ParametreControleurSiale.class);
        q.setParameter("controleurSIALE", controleurSIALE);
        return q;
    }

	public static TypedQuery<ParametreControleurSiale> findParametreControleurSialesByControleurSIALE(ControleurSIALE controleurSIALE, String sortFieldName, String sortOrder) {
        if (controleurSIALE == null) throw new IllegalArgumentException("The controleurSIALE argument is required");
        EntityManager em = ParametreControleurSiale.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ParametreControleurSiale AS o WHERE o.controleurSIALE = :controleurSIALE");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ParametreControleurSiale> q = em.createQuery(queryBuilder.toString(), ParametreControleurSiale.class);
        q.setParameter("controleurSIALE", controleurSIALE);
        return q;
    }

	public int getMoisVisuMission() {
        return this.moisVisuMission;
    }

	public void setMoisVisuMission(int moisVisuMission) {
        this.moisVisuMission = moisVisuMission;
    }

	public String getBanniereCouleur() {
        return this.banniereCouleur;
    }

	public void setBanniereCouleur(String banniereCouleur) {
        this.banniereCouleur = banniereCouleur;
    }

	public ControleurSIALE getControleurSIALE() {
        return this.controleurSIALE;
    }

	public void setControleurSIALE(ControleurSIALE controleurSIALE) {
        this.controleurSIALE = controleurSIALE;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("moisVisuMission", "banniereCouleur", "controleurSIALE");

	public static final EntityManager entityManager() {
        EntityManager em = new ParametreControleurSiale().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countParametreControleurSiales() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ParametreControleurSiale o", Long.class).getSingleResult();
    }

	public static List<ParametreControleurSiale> findAllParametreControleurSiales() {
        return entityManager().createQuery("SELECT o FROM ParametreControleurSiale o", ParametreControleurSiale.class).getResultList();
    }

	public static List<ParametreControleurSiale> findAllParametreControleurSiales(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ParametreControleurSiale o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ParametreControleurSiale.class).getResultList();
    }

	public static ParametreControleurSiale findParametreControleurSiale(Long id) {
        if (id == null) return null;
        return entityManager().find(ParametreControleurSiale.class, id);
    }

	public static List<ParametreControleurSiale> findParametreControleurSialeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ParametreControleurSiale o", ParametreControleurSiale.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ParametreControleurSiale> findParametreControleurSialeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ParametreControleurSiale o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ParametreControleurSiale.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ParametreControleurSiale attached = ParametreControleurSiale.findParametreControleurSiale(this.id);
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
    public ParametreControleurSiale merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ParametreControleurSiale merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
