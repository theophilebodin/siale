package nc.mairie.siale.domain;

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
@RooJpaActiveRecord(finders = { "findControleurSIALEsByActifNotAndDroits" })
public class ControleurSIALE {

    @NotNull
    private String displayname;

    @NotNull
    private String username;

    private boolean actif;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Droit> droits = new HashSet<Droit>();

    //Utilisé par la ZUL de gestionMission
    public String getNomAffichage() {
        return displayname;
    }

    public boolean isAdmin() {
        for (Droit droit : getDroits()) {
            if (droit.getId().equals(Constantes.droitAdmin.getId())) return true;
        }
        return false;
    }

    public boolean isControleur() {
        for (Droit droit : getDroits()) {
            if (droit.getId().equals(Constantes.droitControleur.getId())) return true;
        }
        return false;
    }
}
