package nc.mairie.siale.domain;

import java.io.Serializable;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "missionactivite_sequence")
public class MissionActivite implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -398815132975829803L;

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
    	boolean b =  principale.booleanValue();
    	res.setPrincipale(b);
    	//res.setTheActivite(theActivite == null ? null : theActivite.clone());
    	return res;
    }
}
