package nc.mairie.siale.technique;

import nc.mairie.siale.domain.Droit;

public class Constantes {

	transient static public Droit droitControleur=Droit.findDroit(new Long(1));
	
	transient static public Droit droitAdmin=Droit.findDroit(new Long(2));


}
