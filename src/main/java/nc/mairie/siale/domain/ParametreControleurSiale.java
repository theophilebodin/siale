package nc.mairie.siale.domain;
import nc.mairie.siale.technique.Constantes;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.OneToOne;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findParametreControleurSialesByControleurSIALE" })
public class ParametreControleurSiale {

    /**
     */
    private int moisVisuMission;

    /**
     */
    private String banniereCouleur;
    
    /**
     */
    @OneToOne
    private ControleurSIALE controleurSIALE;

    public static ParametreControleurSiale getNewDefaultParametreControleurSiale() {
        ParametreControleurSiale param = new ParametreControleurSiale();
        param.setMoisVisuMission(Constantes.PARAM_MOIS_VISU);
        param.setBanniereCouleur(Constantes.PARAM_BANNIERE_COULEUR);
        return param;
    }

   
}
