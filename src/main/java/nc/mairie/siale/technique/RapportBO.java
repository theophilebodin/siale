package nc.mairie.siale.technique;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;

public class RapportBO {

	
	final public static String regexDossierBO =  "(\\[[^\\[]*\\]){1}(\\p{Blank}*,\\p{Blank}*(\\[[^\\[]*\\]))*";
	
	/**
	 * 
	 * @return Url du rapport BO pour la Iframe
	 */
	public static String getURLRapportBO(String dossierBO, String rapport) throws WrongValueException {
		
		String urlOpenDocument = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_OPENDOCUMENT");
		if (urlOpenDocument == null) {
			throw new WrongValueException("Le paramètre BO_OPENDOCUMENT n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		
		//Recup du dossier
		if (dossierBO == null) {
			throw new WrongValueException ("Le paramètre BO_FOLDER n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}
		//format du dossier [dossier] ou [dossier],[sous doss1],[sous dossier2]
		if (!dossierBO.matches(regexDossierBO)) {
			throw new WrongValueException ("Le paramètre BO_FOLDER doit être de la forme [dossier] ou [dossier],[sous doss1],[sous dossier2].");
		}
		
		//Recup du rapport
		if (rapport == null) {
			throw new WrongValueException ("Le paramètre rapport ne doit pas être null");
		}
		
		
		return Executions.encodeURL(urlOpenDocument+"?sPath="+dossierBO+"&sDocName="+rapport);
	}
	
	/**
	 * 
	 * @return Url du rapport BO pour la Iframe en récupérant le dossier dans le contexte
	 */
	public static String getURLRapportBO(String rapport) throws WrongValueException {
		
		//Recup du dossier
		String dossierBO = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_FOLDER");
		if (dossierBO == null) {
			throw new WrongValueException ("Le paramètre BO_FOLDER n'est pas défini dans le context.xml. Contacter l'administrateur.");
		}

		//format du dossier [dossier] ou [dossier],[sous doss1],[sous dossier2]
		if (!dossierBO.matches(regexDossierBO)) {
			throw new WrongValueException ("Le paramètre BO_FOLDER est mal défini dans le context.xml. Il doit être de la forme [dossier] ou [dossier],[sous doss1],[sous dossier2]. Contacter l'administrateur.");
		}
	
		
		return getURLRapportBO(dossierBO, rapport);
	}
	
}
