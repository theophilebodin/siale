package nc.mairie.siale.domain;

import java.io.Serializable;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "notecritere_sequence")
public class NoteCritere implements Cloneable, Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6293833914338897655L;

	@NotNull
    private String nom;

    private double ponderation;

    @NotNull
    @ManyToOne
    private NoteGroupe noteGroupe;
    
    @Override
    public NoteCritere clone() throws CloneNotSupportedException {
    	return (NoteCritere)super.clone();
    }
 
}
