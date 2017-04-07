package nc.mairie.siale.domain;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import nc.mairie.siale.technique.Nombre;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class NoteGroupe implements Cloneable, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8942375038117004895L;

	@NotNull
    private String nom;

    private double ponderation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "noteGroupe", orphanRemoval = true, fetch=FetchType.EAGER)
    private Set<NoteCritere> noteCriteres = new HashSet<NoteCritere>();

    @NotNull
    @ManyToOne
    private Bareme bareme;

    public int getNombreCriteres() {
        return getNoteCriteres().size();
    }
    
    public double getSommePonderationArrondie () {
    	return Nombre.arrondir(getSommePonderation(),2);
    }
    
    public double getSommePonderation () {
    	double total =0;
		for (NoteCritere noteCritere : getNoteCriteres()) {
			total+=noteCritere.getPonderation();
		}
		return total;
    }

    @Override
    public nc.mairie.siale.domain.NoteGroupe clone() throws CloneNotSupportedException {
        NoteGroupe res = (NoteGroupe) super.clone();
        res.noteCriteres = new HashSet<NoteCritere>();
        for (NoteCritere noteCritere : this.getNoteCriteres()) {
        	NoteCritere nc = noteCritere.clone();
        	nc.setNoteGroupe(res);
            res.getNoteCriteres().add(nc);
        }
        return res;
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

	public Set<NoteCritere> getNoteCriteres() {
        return this.noteCriteres;
    }

	public void setNoteCriteres(Set<NoteCritere> noteCriteres) {
        this.noteCriteres = noteCriteres;
    }

	public Bareme getBareme() {
        return this.bareme;
    }

	public void setBareme(Bareme bareme) {
        this.bareme = bareme;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @SequenceGenerator(name = "noteGroupeGen", sequenceName = "notegroupe_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "noteGroupeGen")
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "nom", "ponderation", "noteCriteres", "bareme");

	public static final EntityManager entityManager() {
        EntityManager em = new NoteGroupe().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countNoteGroupes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM NoteGroupe o", Long.class).getSingleResult();
    }

	public static List<NoteGroupe> findAllNoteGroupes() {
        return entityManager().createQuery("SELECT o FROM NoteGroupe o", NoteGroupe.class).getResultList();
    }

	public static List<NoteGroupe> findAllNoteGroupes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM NoteGroupe o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, NoteGroupe.class).getResultList();
    }

	public static NoteGroupe findNoteGroupe(Long id) {
        if (id == null) return null;
        return entityManager().find(NoteGroupe.class, id);
    }

	public static List<NoteGroupe> findNoteGroupeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM NoteGroupe o", NoteGroupe.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<NoteGroupe> findNoteGroupeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM NoteGroupe o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, NoteGroupe.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            NoteGroupe attached = NoteGroupe.findNoteGroupe(this.id);
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
    public NoteGroupe merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        NoteGroupe merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
