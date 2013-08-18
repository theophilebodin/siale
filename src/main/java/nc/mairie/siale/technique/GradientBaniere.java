package nc.mairie.siale.technique;

public class GradientBaniere {

	/**
	 * 
	 * @return un style CSS gradient en fonction de l'envoronnement d'exécution
	 */
	public static String getStyleCSS () {
		String env = LDAP.getHashParametres().get("ENVIRONNEMENT");
		
		String color = LDAP.getHashParametres().get("BANNER_COLOR_"+env);
		if (color == null) color="#000000";
		
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
	
}
