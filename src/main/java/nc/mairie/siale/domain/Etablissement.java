package nc.mairie.siale.domain;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "etablissement_sequence")
public class Etablissement {

    @NotNull
    private String code;

    @NotNull
    private String libelle;

    @NotNull
    private String contact;

    @NotNull
    private String adresse;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "etablissement")
    @OneToMany(mappedBy = "etablissement")
    private Set<Mission> missions = new HashSet<Mission>();

    public String getNomAffichage() {
        return getLibelle() + "-" + getCode();
    }
}
