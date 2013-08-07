package nc.mairie.siale.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "droit_sequence")
public class Droit implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8797993967678893505L;
	@NotNull
    private String nom;
    
	//Maven site find bugs ne trouve pas findDroit (aspect)
	//J'ai donc du créer cette méthode à la con pour que findbugs "trouve" FindDroit.... :(
	public static Droit findDroitACauseDeMavenSiteFindBugs(Long l) {
		return findDroit(l);
	}
    
}
