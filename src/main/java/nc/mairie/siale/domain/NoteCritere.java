package nc.mairie.siale.domain;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class NoteCritere implements Cloneable{

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
