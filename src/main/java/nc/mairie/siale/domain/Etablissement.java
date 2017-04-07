package nc.mairie.siale.domain;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Etablissement implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5457593271943426896L;

	@NotNull
    private String code;

    @NotNull
    private String libelle;

    @NotNull
    private String contact;

    @NotNull
    private String adresse;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "etablissement")
    @OneToMany(mappedBy = "etablissement")
    private Set<Mission> missions = new HashSet<Mission>();

    public String getNomAffichage() {
        return getLibelle() + "-" + getCode();
    }

	@Id
    @SequenceGenerator(name = "etablissementGen", sequenceName = "etablissement_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "etablissementGen")
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "code", "libelle", "contact", "adresse", "missions");

	public static final EntityManager entityManager() {
        EntityManager em = new Etablissement().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countEtablissements() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Etablissement o", Long.class).getSingleResult();
    }

	public static List<Etablissement> findAllEtablissements() {
        return entityManager().createQuery("SELECT o FROM Etablissement o", Etablissement.class).getResultList();
    }

	public static List<Etablissement> findAllEtablissements(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Etablissement o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Etablissement.class).getResultList();
    }

	public static Etablissement findEtablissement(Long id) {
        if (id == null) return null;
        return entityManager().find(Etablissement.class, id);
    }

	public static List<Etablissement> findEtablissementEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Etablissement o", Etablissement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Etablissement> findEtablissementEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Etablissement o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Etablissement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Etablissement attached = Etablissement.findEtablissement(this.id);
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
    public Etablissement merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Etablissement merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getCode() {
        return this.code;
    }

	public void setCode(String code) {
        this.code = code;
    }

	public String getLibelle() {
        return this.libelle;
    }

	public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

	public String getContact() {
        return this.contact;
    }

	public void setContact(String contact) {
        this.contact = contact;
    }

	public String getAdresse() {
        return this.adresse;
    }

	public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

	public Set<Mission> getMissions() {
        return this.missions;
    }

	public void setMissions(Set<Mission> missions) {
        this.missions = missions;
    }
}
