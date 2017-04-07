package nc.mairie.siale.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class TypeParam implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5461220011752406279L;
	@NotNull
    @Size(min = 2, max = 20)
    private String nom;
    
    public TypeParam clone() throws CloneNotSupportedException {
    	return (TypeParam) super.clone();
    };

	public String getNom() {
        return this.nom;
    }

	public void setNom(String nom) {
        this.nom = nom;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "nom");

	public static final EntityManager entityManager() {
        EntityManager em = new TypeParam().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countTypeParams() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TypeParam o", Long.class).getSingleResult();
    }

	public static List<TypeParam> findAllTypeParams() {
        return entityManager().createQuery("SELECT o FROM TypeParam o", TypeParam.class).getResultList();
    }

	public static List<TypeParam> findAllTypeParams(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TypeParam o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TypeParam.class).getResultList();
    }

	public static TypeParam findTypeParam(Long id) {
        if (id == null) return null;
        return entityManager().find(TypeParam.class, id);
    }

	public static List<TypeParam> findTypeParamEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TypeParam o", TypeParam.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<TypeParam> findTypeParamEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TypeParam o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TypeParam.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            TypeParam attached = TypeParam.findTypeParam(this.id);
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
    public TypeParam merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TypeParam merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @SequenceGenerator(name = "typeParamGen", sequenceName = "typeparam_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "typeParamGen")
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
