package nc.mairie.siale.technique;

import nc.mairie.siale.domain.ParametreControleurSiale;

public class GradientBanniere {

	
	/**
	 * 
	 * @return un style CSS gradient
	 */
	public static String getStyleCSS (String color) {
		
		String res = 	
			"background: #ffffff; /* Old browsers */"+
			"background: -moz-linear-gradient(left, #ffffff 5%, "+color+" 50%, "+color+" 50%, "+color+" 50%, #ffffff 95%); /* FF3.6+ */"+
			"background: -webkit-gradient(linear, left top, right top, color-stop(5%,#ffffff), color-stop(50%,"+color+"), color-stop(50%,"+color+"), color-stop(50%,"+color+"), color-stop(95%,#ffffff)); /* Chrome,Safari4+ */"+
			"background: -webkit-linear-gradient(left, #ffffff 5%,"+color+" 50%,"+color+" 50%,"+color+" 50%,#ffffff 95%); /* Chrome10+,Safari5.1+ */"+
			"background: -o-linear-gradient(left, #ffffff 5%,"+color+" 50%,"+color+" 50%,"+color+" 50%,#ffffff 95%); /* Opera 11.10+ */"+
			"background: -ms-linear-gradient(left, #ffffff 5%,"+color+" 50%,"+color+" 50%,"+color+" 50%,#ffffff 95%); /* IE10+ */"+
			"background: linear-gradient(to right, #ffffff 5%,"+color+" 50%,"+color+" 50%,"+color+" 50%,#ffffff 95%); /* W3C */"+
			"filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#ffffff', endColorstr='#ffffff',GradientType=1 ); /* IE6-9 */";
		
		return res;
	}

	
	/**
	 * 
	 * @return un style CSS gradient en fonction de l'envoronnement d'exécution
	 */
	public static String getStyleCSS () {
		String color;
		
		try {
			color = ((ParametreControleurSiale)ParametreControleurSiale.findParametreControleurSialesByControleurSIALE(CurrentUser.getCurrentUser()).getSingleResult()).getBanniereCouleur();
		} catch (Exception e) {
			color = Constantes.PARAM_BANNIERE_COULEUR;
		}
		
		return getStyleCSS(color);
	}
	
}
