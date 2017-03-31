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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Param implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2494040797235333748L;

	@NotNull
    @Size(min = 2, max = 100)
    private String nom;

    @NotNull
    @ManyToOne
    private TypeParam typeParam;

    private Boolean actif;

    //Utilisé par la ZUL de gestionMission
    public String getNomAffichage () {
    	return nom;
    }

    /**
     * Pas possible à coder avec les finders de base
     * @param nom nom
     * @return un TypedQuery de Param
     */
    public static TypedQuery<Param> findParamsActifsByNomDuTypeParam(String nom) {
        if (nom == null) throw new IllegalArgumentException("The nom argument is required");
        EntityManager em = Param.entityManager();
        TypedQuery<Param> q = em.createQuery("SELECT o FROM Param AS o WHERE o.typeParam.nom = :nom and o.actif is true", Param.class);
        q.setParameter("nom", nom);
        return q;
    }
    
    @Override
    public Param clone() throws CloneNotSupportedException {
    	Param res = (Param)super.clone();
    	//res.setTypeParam(typeParam.clone());
    	return res;
    }
    

	public String getNom() {
        return this.nom;
    }

	public void setNom(String nom) {
        this.nom = nom;
    }

	public TypeParam getTypeParam() {
        return this.typeParam;
    }

	public void setTypeParam(TypeParam typeParam) {
        this.typeParam = typeParam;
    }

	public Boolean getActif() {
        return this.actif;
    }

	public void setActif(Boolean actif) {
        this.actif = actif;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "nom", "typeParam", "actif");

	public static final EntityManager entityManager() {
        EntityManager em = new Param().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countParams() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Param o", Long.class).getSingleResult();
    }

	public static List<Param> findAllParams() {
        return entityManager().createQuery("SELECT o FROM Param o", Param.class).getResultList();
    }

	public static List<Param> findAllParams(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Param o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Param.class).getResultList();
    }

	public static Param findParam(Long id) {
        if (id == null) return null;
        return entityManager().find(Param.class, id);
    }

	public static List<Param> findParamEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Param o", Param.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Param> findParamEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Param o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Param.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Param attached = Param.findParam(this.id);
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
    public Param merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Param merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @SequenceGenerator(name = "paramGen", sequenceName = "param_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "paramGen")
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

	public static Long countFindParamsByNomLike(String nom) {
        if (nom == null || nom.length() == 0) throw new IllegalArgumentException("The nom argument is required");
        nom = nom.replace('*', '%');
        if (nom.charAt(0) != '%') {
            nom = "%" + nom;
        }
        if (nom.charAt(nom.length() - 1) != '%') {
            nom = nom + "%";
        }
        EntityManager em = Param.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Param AS o WHERE LOWER(o.nom) LIKE LOWER(:nom)", Long.class);
        q.setParameter("nom", nom);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindParamsByTypeParam(TypeParam typeParam) {
        if (typeParam == null) throw new IllegalArgumentException("The typeParam argument is required");
        EntityManager em = Param.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Param AS o WHERE o.typeParam = :typeParam", Long.class);
        q.setParameter("typeParam", typeParam);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<Param> findParamsByNomLike(String nom) {
        if (nom == null || nom.length() == 0) throw new IllegalArgumentException("The nom argument is required");
        nom = nom.replace('*', '%');
        if (nom.charAt(0) != '%') {
            nom = "%" + nom;
        }
        if (nom.charAt(nom.length() - 1) != '%') {
            nom = nom + "%";
        }
        EntityManager em = Param.entityManager();
        TypedQuery<Param> q = em.createQuery("SELECT o FROM Param AS o WHERE LOWER(o.nom) LIKE LOWER(:nom)", Param.class);
        q.setParameter("nom", nom);
        return q;
    }

	public static TypedQuery<Param> findParamsByNomLike(String nom, String sortFieldName, String sortOrder) {
        if (nom == null || nom.length() == 0) throw new IllegalArgumentException("The nom argument is required");
        nom = nom.replace('*', '%');
        if (nom.charAt(0) != '%') {
            nom = "%" + nom;
        }
        if (nom.charAt(nom.length() - 1) != '%') {
            nom = nom + "%";
        }
        EntityManager em = Param.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Param AS o WHERE LOWER(o.nom) LIKE LOWER(:nom)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Param> q = em.createQuery(queryBuilder.toString(), Param.class);
        q.setParameter("nom", nom);
        return q;
    }

	public static TypedQuery<Param> findParamsByTypeParam(TypeParam typeParam) {
        if (typeParam == null) throw new IllegalArgumentException("The typeParam argument is required");
        EntityManager em = Param.entityManager();
        TypedQuery<Param> q = em.createQuery("SELECT o FROM Param AS o WHERE o.typeParam = :typeParam", Param.class);
        q.setParameter("typeParam", typeParam);
        return q;
    }

	public static TypedQuery<Param> findParamsByTypeParam(TypeParam typeParam, String sortFieldName, String sortOrder) {
        if (typeParam == null) throw new IllegalArgumentException("The typeParam argument is required");
        EntityManager em = Param.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Param AS o WHERE o.typeParam = :typeParam");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Param> q = em.createQuery(queryBuilder.toString(), Param.class);
        q.setParameter("typeParam", typeParam);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
