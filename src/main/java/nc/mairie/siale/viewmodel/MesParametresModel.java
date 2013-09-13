/**
 * 
 */
package nc.mairie.siale.viewmodel;

import nc.mairie.siale.domain.ParametreControleurSiale;

import nc.mairie.siale.technique.Constantes;
import nc.mairie.siale.technique.ControleSaisie;
import nc.mairie.siale.technique.CurrentUser;
import nc.mairie.siale.technique.GradientBanniere;

import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Window;


/**
 * @author boulu72
 *
 */
public class MesParametresModel extends SelectorComposer<Component> {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2617321906240279396L;
	
	AnnotateDataBinder binder;
	
	@Wire
	Window mesParametres;
	
	ParametreControleurSiale parametreControleurSialeCourant = null;
	
	public ParametreControleurSiale getParametreControleurSialeCourant() {
		return parametreControleurSialeCourant;
	}

	public void setParametreControleurSialeCourant(ParametreControleurSiale parametreControleurSialeCourant) {
		this.parametreControleurSialeCourant = parametreControleurSialeCourant;
	}

	public void initialiseParametreControleurSialeCourant () {
		try {
			parametreControleurSialeCourant = ParametreControleurSiale.findParametreControleurSialesByControleurSIALE(CurrentUser.getCurrentUser()).getSingleResult();
		} catch (Exception e) {
			parametreControleurSialeCourant = ParametreControleurSiale.getNewDefaultParametreControleurSiale();
			parametreControleurSialeCourant.setControleurSIALE(CurrentUser.getCurrentUser());
		}
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		initialiseParametreControleurSialeCourant();
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

	}
	

	public void raffraichirBanniere () {
		//Pour raffraichir la banniere
		Borderlayout banniere = (Borderlayout) mesParametres.getParent().getParent().getFellow("banniere");
		banniere.setStyle(GradientBanniere.getStyleCSS());
		banniere.invalidate();
	}

	@Listen("onClick = #annulerParametre;")
	public void onClick$annulerParametre() {

		binder.loadComponent(mesParametres);
	}
	
	@Listen("onClick = #validerParametre")
	public void onClick$validerParametre() {
		
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(mesParametres);
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		setParametreControleurSialeCourant(parametreControleurSialeCourant.merge());
		
		//Pour raffraichir la banniere
		raffraichirBanniere();
			
		alert("Vos paramètres ont bien été enregistrés");
		
		binder.loadAll();
	}
	
	@Listen("onClick = #defautParametre")
	public void onClick$DefautParametre() {
		
		//Si version, alors on supprime
		if (getParametreControleurSialeCourant().getVersion() != null) {
			parametreControleurSialeCourant.remove();
		}
		
		initialiseParametreControleurSialeCourant();
			
		//Pour raffraichir la banniere
		raffraichirBanniere();
		
		alert("Vous avez à présent les paramètres par défaut");
		
		binder.loadAll();
	}
	
	public int getDEFAULT_PARAM_MOIS_VISU () {
		return Constantes.PARAM_MOIS_VISU;
	}
	
	public String getDEFAULT_PARAM_BANNIERE_COULEUR () {
		return Constantes.PARAM_BANNIERE_COULEUR;
	}
	
}
