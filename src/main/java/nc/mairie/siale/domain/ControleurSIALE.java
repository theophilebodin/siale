package nc.mairie.siale.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import nc.mairie.siale.technique.Constantes;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "controleursiale_sequence", finders = { "findControleurSIALEsByActifNotAndDroits", "findControleurSIALEsByUsernameLikeAndActifNot" })
public class ControleurSIALE implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5951494916201157715L;

	@NotNull
    private String displayname;

    @NotNull
    private String username;

    private boolean actif;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Droit> droits = new HashSet<Droit>();

    public String getNomAffichage() {
        return displayname;
    }

    public boolean isAdmin() {
        for (Droit droit : getDroits()) {
            if (droit.getId().equals(Constantes.DROIT_ADMIN.getId())) return true;
        }
        return false;
    }

    public boolean isControleur() {
        for (Droit droit : getDroits()) {
            if (droit.getId().equals(Constantes.DROIT_CONTROLEUR.getId())) return true;
        }
        return false;
    }
}
