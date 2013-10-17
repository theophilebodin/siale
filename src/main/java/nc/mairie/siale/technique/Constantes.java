package nc.mairie.siale.technique;

import nc.mairie.siale.domain.Droit;

public class Constantes {

	//Ceci génère une anomalie pour findBugs de maven site :(
	//final static public Droit DROIT_CONTROLEUR=Droit.findDroit(Long.valueOf(1));
	//J'ai donc du créer cette méthode findDroitACauseDeMavenSiteFindBugs à la con pour que findbugs "trouve" FindDroit.... :(
	//public static final Droit DROIT_CONTROLEUR=Droit.findDroit(Long.valueOf(1));
	//public static final Droit DROIT_ADMIN=Droit.findDroit(Long.valueOf(2));
	
	final static public Droit DROIT_CONTROLEUR=Droit.findDroitACauseDeMavenSiteFindBugs(Long.valueOf(1));
	
	final static public Droit DROIT_ADMIN=Droit.findDroitACauseDeMavenSiteFindBugs(Long.valueOf(2));

	final static public int PARAM_MOIS_VISU=6;
	
	final static public String PARAM_BANNIERE_COULEUR = "#4CCAEB";
	
	final static public double BAREME_SEUIL_FAIBLE = 1.51;
	
	final static public double BAREME_SEUIL_MODERE = 2.51;
	
	final static public double BAREME_SEUIL_ELEVE = 3.51;
	

}
