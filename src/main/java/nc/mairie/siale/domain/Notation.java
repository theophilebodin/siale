package nc.mairie.siale.domain;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Notation {

    private double note;

    @ManyToOne
    private NoteCritere noteCritere;

    @ManyToOne
    private Mission mission;
    
	public static boolean existNotationByMission(Mission mission) {
        if (mission == null) throw new IllegalArgumentException("The mission argument is required");
        EntityManager em = Notation.entityManager();
        TypedQuery<Long> q = em.createQuery("SELECT count(*) FROM Notation AS o WHERE o.mission = :mission", Long.class);
        q.setParameter("mission", mission);
        return q.getResultList().get(0)!=0;
    }
}
