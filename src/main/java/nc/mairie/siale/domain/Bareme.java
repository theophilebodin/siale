package nc.mairie.siale.domain;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import nc.mairie.siale.technique.Constantes;
import nc.mairie.siale.technique.Nombre;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Bareme implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 827767898335738016L;

    @NotNull
    private String nom;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateCreation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bareme", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<NoteGroupe> noteGroupes = new HashSet<NoteGroupe>();

    private Double seuilFaible = Constantes.BAREME_SEUIL_FAIBLE;
    
	private Double seuilModere = Constantes.BAREME_SEUIL_MODERE;
    
	private Double seuilEleve = Constantes.BAREME_SEUIL_ELEVE;
	
	public double getSommePonderationArrondie() {
        return Nombre.arrondir(getSommePonderation(), 2);
    }

    public double getSommePonderation() {
        double total = 0;
        for (NoteGroupe noteGroupe : getNoteGroupes()) {
            total += noteGroupe.getPonderation();
        }
        return total;
    }


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @SequenceGenerator(name = "baremeGen", sequenceName = "bareme_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "baremeGen")
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

	public String getNom() {
        return this.nom;
    }

	public void setNom(String nom) {
        this.nom = nom;
    }

	public Date getDateCreation() {
        return this.dateCreation;
    }

	public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

	public Set<NoteGroupe> getNoteGroupes() {
        return this.noteGroupes;
    }

	public void setNoteGroupes(Set<NoteGroupe> noteGroupes) {
        this.noteGroupes = noteGroupes;
    }

	public Double getSeuilFaible() {
        return this.seuilFaible;
    }

	public void setSeuilFaible(Double seuilFaible) {
        this.seuilFaible = seuilFaible;
    }

	public Double getSeuilModere() {
        return this.seuilModere;
    }

	public void setSeuilModere(Double seuilModere) {
        this.seuilModere = seuilModere;
    }

	public Double getSeuilEleve() {
        return this.seuilEleve;
    }

	public void setSeuilEleve(Double seuilEleve) {
        this.seuilEleve = seuilEleve;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "nom", "dateCreation", "noteGroupes", "seuilFaible", "seuilModere", "seuilEleve");

	public static final EntityManager entityManager() {
        EntityManager em = new Bareme().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBaremes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Bareme o", Long.class).getSingleResult();
    }

	public static List<Bareme> findAllBaremes() {
        return entityManager().createQuery("SELECT o FROM Bareme o", Bareme.class).getResultList();
    }

	public static List<Bareme> findAllBaremes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Bareme o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Bareme.class).getResultList();
    }

	public static Bareme findBareme(Long id) {
        if (id == null) return null;
        return entityManager().find(Bareme.class, id);
    }

	public static List<Bareme> findBaremeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Bareme o", Bareme.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Bareme> findBaremeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Bareme o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Bareme.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Bareme attached = Bareme.findBareme(this.id);
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
    public Bareme merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Bareme merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
