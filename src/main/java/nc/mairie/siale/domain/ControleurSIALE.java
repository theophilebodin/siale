package nc.mairie.siale.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import nc.mairie.siale.technique.Constantes;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class ControleurSIALE implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5951494916201157715L;

	@NotNull
    private String displayname;

    @NotNull
    private String username;

    private boolean actif;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Droit> droits = new HashSet<Droit>();

    public String getNomAffichage() {
        return displayname;
    }

    public boolean isAdmin() {
        for (Droit droit : getDroits()) {
            if (droit.getId().equals(Constantes.DROIT_ADMIN.getId())) return true;
        }
        return false;
    }

    public boolean isControleur() {
        for (Droit droit : getDroits()) {
            if (droit.getId().equals(Constantes.DROIT_CONTROLEUR.getId())) return true;
        }
        return false;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "displayname", "username", "actif", "droits");

	public static final EntityManager entityManager() {
        EntityManager em = new ControleurSIALE().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countControleurSIALEs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ControleurSIALE o", Long.class).getSingleResult();
    }

	public static List<ControleurSIALE> findAllControleurSIALEs() {
        return entityManager().createQuery("SELECT o FROM ControleurSIALE o", ControleurSIALE.class).getResultList();
    }

	public static List<ControleurSIALE> findAllControleurSIALEs(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ControleurSIALE o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ControleurSIALE.class).getResultList();
    }

	public static ControleurSIALE findControleurSIALE(Long id) {
        if (id == null) return null;
        return entityManager().find(ControleurSIALE.class, id);
    }

	public static List<ControleurSIALE> findControleurSIALEEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ControleurSIALE o", ControleurSIALE.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ControleurSIALE> findControleurSIALEEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ControleurSIALE o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ControleurSIALE.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ControleurSIALE attached = ControleurSIALE.findControleurSIALE(this.id);
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
    public ControleurSIALE merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ControleurSIALE merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @SequenceGenerator(name = "controleurSIALEGen", sequenceName = "controleursiale_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "controleurSIALEGen")
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

	public String getDisplayname() {
        return this.displayname;
    }

	public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

	public String getUsername() {
        return this.username;
    }

	public void setUsername(String username) {
        this.username = username;
    }

	public boolean isActif() {
        return this.actif;
    }

	public void setActif(boolean actif) {
        this.actif = actif;
    }

	public Set<Droit> getDroits() {
        return this.droits;
    }

	public void setDroits(Set<Droit> droits) {
        this.droits = droits;
    }

	public static Long countFindControleurSIALEsByActifNotAndDroits(boolean actif, Set<Droit> droits) {
        if (droits == null) throw new IllegalArgumentException("The droits argument is required");
        EntityManager em = ControleurSIALE.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(o) FROM ControleurSIALE AS o WHERE o.actif IS NOT :actif  AND");
        for (int i = 0; i < droits.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :droits_item").append(i).append(" MEMBER OF o.droits");
        }
        TypedQuery q = em.createQuery(queryBuilder.toString(), Long.class);
        q.setParameter("actif", actif);
        int droitsIndex = 0;
        for (Droit _droit: droits) {
            q.setParameter("droits_item" + droitsIndex++, _droit);
        }
        return ((Long) q.getSingleResult());
    }

	public static Long countFindControleurSIALEsByUsernameLikeAndActifNot(String username, boolean actif) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        username = username.replace('*', '%');
        if (username.charAt(0) != '%') {
            username = "%" + username;
        }
        if (username.charAt(username.length() - 1) != '%') {
            username = username + "%";
        }
        EntityManager em = ControleurSIALE.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ControleurSIALE AS o WHERE LOWER(o.username) LIKE LOWER(:username)  AND o.actif IS NOT :actif", Long.class);
        q.setParameter("username", username);
        q.setParameter("actif", actif);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ControleurSIALE> findControleurSIALEsByActifNotAndDroits(boolean actif, Set<Droit> droits) {
        if (droits == null) throw new IllegalArgumentException("The droits argument is required");
        EntityManager em = ControleurSIALE.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ControleurSIALE AS o WHERE o.actif IS NOT :actif  AND");
        for (int i = 0; i < droits.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :droits_item").append(i).append(" MEMBER OF o.droits");
        }
        TypedQuery<ControleurSIALE> q = em.createQuery(queryBuilder.toString(), ControleurSIALE.class);
        q.setParameter("actif", actif);
        int droitsIndex = 0;
        for (Droit _droit: droits) {
            q.setParameter("droits_item" + droitsIndex++, _droit);
        }
        return q;
    }

	public static TypedQuery<ControleurSIALE> findControleurSIALEsByActifNotAndDroits(boolean actif, Set<Droit> droits, String sortFieldName, String sortOrder) {
        if (droits == null) throw new IllegalArgumentException("The droits argument is required");
        EntityManager em = ControleurSIALE.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ControleurSIALE AS o WHERE o.actif IS NOT :actif  AND");
        for (int i = 0; i < droits.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :droits_item").append(i).append(" MEMBER OF o.droits");
        }
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" " + sortOrder);
            }
        }
        TypedQuery<ControleurSIALE> q = em.createQuery(queryBuilder.toString(), ControleurSIALE.class);
        q.setParameter("actif", actif);
        int droitsIndex = 0;
        for (Droit _droit: droits) {
            q.setParameter("droits_item" + droitsIndex++, _droit);
        }
        return q;
    }

	public static TypedQuery<ControleurSIALE> findControleurSIALEsByUsernameLikeAndActifNot(String username, boolean actif) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        username = username.replace('*', '%');
        if (username.charAt(0) != '%') {
            username = "%" + username;
        }
        if (username.charAt(username.length() - 1) != '%') {
            username = username + "%";
        }
        EntityManager em = ControleurSIALE.entityManager();
        TypedQuery<ControleurSIALE> q = em.createQuery("SELECT o FROM ControleurSIALE AS o WHERE LOWER(o.username) LIKE LOWER(:username)  AND o.actif IS NOT :actif", ControleurSIALE.class);
        q.setParameter("username", username);
        q.setParameter("actif", actif);
        return q;
    }

	public static TypedQuery<ControleurSIALE> findControleurSIALEsByUsernameLikeAndActifNot(String username, boolean actif, String sortFieldName, String sortOrder) {
        if (username == null || username.length() == 0) throw new IllegalArgumentException("The username argument is required");
        username = username.replace('*', '%');
        if (username.charAt(0) != '%') {
            username = "%" + username;
        }
        if (username.charAt(username.length() - 1) != '%') {
            username = username + "%";
        }
        EntityManager em = ControleurSIALE.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ControleurSIALE AS o WHERE LOWER(o.username) LIKE LOWER(:username)  AND o.actif IS NOT :actif");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ControleurSIALE> q = em.createQuery(queryBuilder.toString(), ControleurSIALE.class);
        q.setParameter("username", username);
        q.setParameter("actif", actif);
        return q;
    }
}
