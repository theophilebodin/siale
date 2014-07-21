package nc.mairie.siale.domain;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "param_sequence", finders = { "findParamsByTypeParam", "findParamsByNomLike", "findParamsActifsByNomDuTypeParam" })
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
    
}
