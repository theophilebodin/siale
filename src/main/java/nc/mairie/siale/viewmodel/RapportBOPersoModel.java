/**
 * 
 */
package nc.mairie.siale.viewmodel;


import nc.mairie.siale.domain.ParametreControleurSiale;
import nc.mairie.siale.technique.ControleSaisie;
import nc.mairie.siale.technique.CurrentUser;
import nc.mairie.siale.technique.RapportBO;

import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

/**
 * @author boulu72
 *
 */
public class RapportBOPersoModel extends SelectorComposer<Component> {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2617321906240279396L;
	
	AnnotateDataBinder binder;
	
	@Wire
	Window rapportBOPerso;
	
	@Wire
	Iframe iframeBO;
	
	private String folderBOCourant;
	private String rapportBOCourant;
	
	public String getFolderBOCourant() {
		return folderBOCourant;
	}

	public void setFolderBOCourant(String folderBOCourant) {
		this.folderBOCourant = folderBOCourant;
	}

	public String getRapportBOCourant() {
		return rapportBOCourant;
	}

	public void setRapportBOCourant(String rapportBOCourant) {
		this.rapportBOCourant = rapportBOCourant;
	}

	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		//Recup du nombre mois à afficher
		ParametreControleurSiale parametreControleurSiale;
		try {
			parametreControleurSiale = ParametreControleurSiale.findParametreControleurSialesByControleurSIALE(CurrentUser.getCurrentUser()).getSingleResult();
		} catch (Exception e) {
			parametreControleurSiale = ParametreControleurSiale.getNewDefaultParametreControleurSiale();
		}
		
		setFolderBOCourant(parametreControleurSiale.getDossierBOPrefere());
		setRapportBOCourant(parametreControleurSiale.getRapportBOPrefere());

		comp.setAttribute(comp.getId(), this, true);
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

	}
	
	@Listen("onClick = #openBO; onOK = #rapportBOPerso;")
	public void onClick$openBO() {

		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(rapportBOPerso);
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		iframeBO.setSrc(RapportBO.getURLRapportBO(getFolderBOCourant(), getRapportBOCourant()));
		binder.loadAll();
		
	}
	
}
