package nc.mairie.siale.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ControleurSIALE {

    @NotNull
    private String prenom;

    @NotNull
    private String nom;

    @NotNull
    private String username;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Droit> droits = new HashSet<Droit>();
    
    public String getNomAffichage () {
    	return nom+" "+prenom;
    }

}
