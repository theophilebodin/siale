package nc.mairie.siale.domain;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import nc.mairie.siale.technique.Constantes;
import nc.mairie.siale.technique.Nombre;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "bareme_sequence")
public class Bareme implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 827767898335738016L;

    @NotNull
    private String nom;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateCreation;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bareme", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<NoteGroupe> noteGroupes = new HashSet<NoteGroupe>();

    private Double seuilFaible = Constantes.BAREME_SEUIL_FAIBLE;
    
	private Double seuilModere = Constantes.BAREME_SEUIL_MODERE;
    
	private Double seuilEleve = Constantes.BAREME_SEUIL_ELEVE;
	
	public double getSommePonderationArrondie() {
        return Nombre.arrondir(getSommePonderation(), 2);
    }

    public double getSommePonderation() {
        double total = 0;
        for (NoteGroupe noteGroupe : getNoteGroupes()) {
            total += noteGroupe.getPonderation();
        }
        return total;
    }

}
