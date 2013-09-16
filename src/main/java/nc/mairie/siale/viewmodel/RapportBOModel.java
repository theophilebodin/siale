/**
 * 
 */
package nc.mairie.siale.viewmodel;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zkplus.databind.AnnotateDataBinder;

/**
 * @author boulu72
 *
 */
public class RapportBOModel extends SelectorComposer<Component> {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2617321906240279396L;
	
	AnnotateDataBinder binder;
	
	/**
	 * 
	 * @return Url du rapport BO pour la Iframe
	 */
	public String getURLRapportBO() {
		
		//Recup du rapport
		String rapport = (String) Executions.getCurrent().getArg().get("RAPPORT");
		if (rapport == null) {
			alert("Le paramètre rapport est introuvable dans les arguments ZUL");
			return null;
		}
		
		String urlOpenDocument = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_OPENDOCUMENT");
		if (urlOpenDocument == null) {
			alert("Le paramètre BO_OPENDOCUMENT n'est pas défini dans le context.xml. Contacter l'administrateur.");
			return null;
		}
		
		String dossierBO = Executions.getCurrent().getDesktop().getWebApp().getInitParameter("BO_FOLDER");
		if (dossierBO == null) {
			alert("Le paramètre BO_FOLDER n'est pas défini dans le context.xml. Contacter l'administrateur.");
			return null;
		}
		
		return Executions.encodeURL(urlOpenDocument+"?sPath="+dossierBO+"&sDocName="+rapport);
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

	}
	
}
