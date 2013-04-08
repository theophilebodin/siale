/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Droit;
import nc.mairie.siale.technique.Action;
import nc.mairie.siale.technique.Constantes;
import nc.mairie.siale.technique.ControleSaisie;
import nc.mairie.siale.technique.CurrentUser;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;



/**
 * @author boulu72
 *
 */
public class GestionDroitsModel extends SelectorComposer<Component> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3479478963746622036L;
	/**
	 * 
	 */

	
	AnnotateDataBinder binder;
	
	List<ControleurSIALE> listeControleurSIALE;

	ControleurSIALE controleurSIALECourant;
	
	Action actionControleurSIALE;
	
	@Wire
	Window gestionDroits;
	
	@Wire
	Include includeSaisieControleurSIALE;
	@Wire("#includeSaisieControleurSIALE #zoneSaisieControleurSIALE")
	Window zoneSaisieControleurSIALE;
	
		
	public List<ControleurSIALE> getListeControleurSIALE() {
		return listeControleurSIALE;
	}


	public void setListeControleurSIALE(List<ControleurSIALE> listeControleurSIALE) {
		this.listeControleurSIALE = listeControleurSIALE;
	}


	public ControleurSIALE getControleurSIALECourant() {
		return controleurSIALECourant;
	}


	public void setControleurSIALECourant(ControleurSIALE controleurSIALECourant) {
		this.controleurSIALECourant = controleurSIALECourant;
	}


	public Action getActionControleurSIALE() {
		return actionControleurSIALE;
	}


	public void setActionControleurSIALE(Action actionControleurSIALE) {
		this.actionControleurSIALE = actionControleurSIALE;
	}


	public void initialiseAllListes () {
		listeControleurSIALE = ControleurSIALE.findAllControleurSIALEs();
		Collections.sort(listeControleurSIALE, new Comparator<ControleurSIALE>() {

			@Override
			public int compare(ControleurSIALE o1, ControleurSIALE o2) {
				return (o1.getNom()+o1.getPrenom()).compareTo(o2.getNom()+o2.getPrenom());
			}
		});
		
//		listeDroit = Droit.findAllDroits();
//		Collections.sort(listeDroit, new Comparator<Droit>() {
//
//			@Override
//			public int compare(Droit o1, Droit o2) {
//				// TODO Auto-generated method stub
//				return o1.getNom().compareTo(o2.getNom());
//			}
//		});
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		initialiseAllListes();
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	}
	

	@Listen("onClick = #ajouterControleurSIALE")
	public void onClick$ajouterControleurSIALE() {
		System.out.println("onClick$ajouterControleurSIALE");
		
		actionControleurSIALE = Action.AJOUT;

		ControleurSIALE c = new ControleurSIALE();
		//TODO avant recherche
		c.setNom("nompopol");
		c.setPrenom("prenompopol");
		c.setUsername("popol72");
		c.setActif(true);
		c.getDroits().add(Constantes.droitControleur);
		setControleurSIALECourant(c);
		binder.loadComponent(zoneSaisieControleurSIALE);
	}
	
	@Listen("onClick = #modifierControleurSIALE; onDoubleClick = #controleurSIALEListItem")
	public void onClick$modifierControleurSIALE() {
		
		actionControleurSIALE=  Action.MODIFICATION;
		binder.loadComponent(zoneSaisieControleurSIALE);
	}

	@Listen("onClick =  #supprimerControleurSIALE")
	public void onClick$supprimerControleurSIALE() {
		System.out.println("onClick$supprimerControleurSIALE");
		
		//TODO vérifier si pas affecté sur une mission
		
		Messagebox.show("Confirmez-vous la suppression ?",
			    "Question", Messagebox.OK | Messagebox.CANCEL,
			    Messagebox.QUESTION,
			        new EventListener<Event>() {
						
						@Override
						public void onEvent(Event e) throws Exception {
			                if(Messagebox.ON_OK.equals(e.getName())){
			        			//SGBD
			                	try {
			        				controleurSIALECourant.remove();
			        			} catch (Exception excp) {
			        				alert("Suppresson impossible.\n Ce contrôleur est rattaché à une mission.");
			        				return;
			        			}
			                	//VIEW
			        			initialiseAllListes();
			        			binder.loadAll();
			                }else if(Messagebox.ON_CANCEL.equals(e.getName())){
			                    //Cancel is clicked
			                	return;
			                }
			            }
			
			        }
			    );

	}
	
	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #rechercherControleurSIALE")
	public void onClick$rechercherControleurSIALE() {
		System.out.println("onClick$rechercherControleurSIALE");
//		//TODO recherche dans l'AD
//		if (actionControleurSIALE != Action.AJOUT) {
//			listeParam.remove(paramCourant);
//			listeParam.add(paramCourantSAV);
//			setParamCourant(paramCourantSAV);
//		}
//		
//		actionControleurSIALE = Action.AUCUNE;
//		binder.loadComponent(zoneSaisieControleurSIALE);
	}
	

	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #annulerControleurSIALE;" +
			"onCancel= #includeSaisieControleurSIALE #zoneSaisieControleurSIALE")
	public void onClick$annulerControleurSIALE() {
		
		//si on était sur un controleur, on le mémorise
		if (getControleurSIALECourant() != null && getControleurSIALECourant().getId() != null ){
			setControleurSIALECourant(ControleurSIALE.findControleurSIALE(getControleurSIALECourant().getId()));
		}
		
		initialiseAllListes();

		actionControleurSIALE = Action.AUCUNE;
		binder.loadComponent(gestionDroits);
	}
	
	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #validerControleurSIALE")
	public void onClick$validerControleurSIALE() {
		System.out.println("onClick$validerControleurSIALE");
		
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieControleurSIALE);
		
		
		// doit être admin et/ou controleur
		if (getControleurSIALECourant().getDroits().size() == 0) {           
			controleSaisie.ajouteErreur(zoneSaisieControleurSIALE.getFellow("controleurCheckbox"),"Il faut cocher Controleur ET/OU Administrateur.");
		}
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		switch (actionControleurSIALE) {
		case AJOUT:
			controleurSIALECourant.merge();
			
			break;
		case MODIFICATION:
			controleurSIALECourant.merge();
			break;
		case SUPPRESSION:
			//Fait directement après validation
			break;
		default:
			break;
		}
		
		initialiseAllListes();
		actionControleurSIALE = Action.AUCUNE;
		binder.loadAll();
	}

	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #controleurCheckbox")
	public void onCheck$controleurCheckbox (Event event) {
		Checkbox cb = (Checkbox)event.getTarget();
		
		//Si checké, on rajoute le droit
		if (cb.isChecked()) {
			getControleurSIALECourant().getDroits().add(Constantes.droitControleur);
		//sinon, on l'enleve
		} else {
			for (Droit droit : getControleurSIALECourant().getDroits()) {
				if (droit.getId().equals(Constantes.droitControleur.getId())) {
					getControleurSIALECourant().getDroits().remove(droit);
					break;
				}
			}
		}
		
		System.out.println(getControleurSIALECourant().getDroits());
		
	}
	
	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #administrateurCheckbox")
	public void onCheck$administrateurCheckbox (Event event) {
	Checkbox cb = (Checkbox)event.getTarget();
		
		//Si checké, on rajoute le droit
		if (cb.isChecked()) {
			getControleurSIALECourant().getDroits().add(Constantes.droitAdmin);
		//sinon, on l'enleve
		} else {
			for (Droit droit : getControleurSIALECourant().getDroits()) {
				if (droit.getId().equals(Constantes.droitAdmin.getId())) {
					getControleurSIALECourant().getDroits().remove(droit);
					break;
				}
			}
		}
		
		System.out.println(getControleurSIALECourant().getDroits());
	
	}
	
	//Renvoie fax  sur le user courant est le user selectionné
	public boolean isAdminModifiableDisabled () {
		return getControleurSIALECourant() == null || CurrentUser.getCurrentUser().getId().equals(getControleurSIALECourant().getId());
	}
	
}
