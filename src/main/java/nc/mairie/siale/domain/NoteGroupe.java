package nc.mairie.siale.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import nc.mairie.siale.technique.Nombre;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "notegroupe_sequence")
public class NoteGroupe implements Cloneable {

    @NotNull
    private String nom;

    private double ponderation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "noteGroupe", orphanRemoval = true, fetch=FetchType.EAGER)
    private Set<NoteCritere> noteCriteres = new HashSet<NoteCritere>();

    @NotNull
    @ManyToOne
    private Bareme bareme;

    public int getNombreCriteres() {
        return getNoteCriteres().size();
    }
    
    public double getSommePonderationArrondie () {
    	return Nombre.arrondir(getSommePonderation(),2);
    }
    
    public double getSommePonderation () {
    	double total =0;
		for (NoteCritere noteCritere : getNoteCriteres()) {
			total+=noteCritere.getPonderation();
		}
		return total;
    }

    @Override
    public nc.mairie.siale.domain.NoteGroupe clone() throws CloneNotSupportedException {
        NoteGroupe res = (NoteGroupe) super.clone();
        res.noteCriteres = new HashSet<NoteCritere>();
        for (NoteCritere noteCritere : this.getNoteCriteres()) {
            res.getNoteCriteres().add(noteCritere.clone());
        }
        return res;
    }
}
