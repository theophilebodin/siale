package nc.mairie.siale.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "missionaction_sequence")
public class MissionAction implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 7903447031476154232L;

	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateAction;

//
//    @NotNull
//    @OneToOne
//    private Mission theMission;
    
    @NotNull
    @ManyToOne
    private Param theAction;

}
