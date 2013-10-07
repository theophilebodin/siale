/**
 * 
 */
package nc.mairie.siale.viewmodel;


import nc.mairie.siale.technique.RapportBO;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;

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
	public String getURLRapportBO() throws Exception {
		
		//Recup du rapport
		//String rapport = (String) Executions.getCurrent().getArg().get("RAPPORT");
		String iDocID = (String) Executions.getCurrent().getArg().get("iDocID");
		if (iDocID == null) {
			throw new WrongValueException("Le paramètre iDocID est introuvable dans les arguments ZUL");
		}
		
		return RapportBO.getURLRapportBO(iDocID);
		
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

	}

}
