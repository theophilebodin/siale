// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.mairie.siale.domain.TypeParam;
import org.springframework.transaction.annotation.Transactional;

privileged aspect TypeParam_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager TypeParam.entityManager;
    
    public static final List<String> TypeParam.fieldNames4OrderClauseFilter = java.util.Arrays.asList("serialVersionUID", "nom");
    
    public static final EntityManager TypeParam.entityManager() {
        EntityManager em = new TypeParam().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long TypeParam.countTypeParams() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TypeParam o", Long.class).getSingleResult();
    }
    
    public static List<TypeParam> TypeParam.findAllTypeParams() {
        return entityManager().createQuery("SELECT o FROM TypeParam o", TypeParam.class).getResultList();
    }
    
    public static List<TypeParam> TypeParam.findAllTypeParams(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TypeParam o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TypeParam.class).getResultList();
    }
    
    public static TypeParam TypeParam.findTypeParam(Long id) {
        if (id == null) return null;
        return entityManager().find(TypeParam.class, id);
    }
    
    public static List<TypeParam> TypeParam.findTypeParamEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TypeParam o", TypeParam.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<TypeParam> TypeParam.findTypeParamEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
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
    public void TypeParam.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void TypeParam.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            TypeParam attached = TypeParam.findTypeParam(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void TypeParam.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void TypeParam.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public TypeParam TypeParam.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TypeParam merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
