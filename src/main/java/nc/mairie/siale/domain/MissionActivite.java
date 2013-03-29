package nc.mairie.siale.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class MissionActivite implements Cloneable{

    private Boolean principale;

    @NotNull
    @ManyToOne
    private Param theActivite;

    @NotNull
    @ManyToOne
    private Mission theMission;
    
    @Override
    public MissionActivite clone() throws CloneNotSupportedException {
    	MissionActivite res = (MissionActivite)super.clone();
    	res.setPrincipale(new Boolean(principale));
    	//res.setTheActivite(theActivite == null ? null : theActivite.clone());
    	return res;
    }
}
