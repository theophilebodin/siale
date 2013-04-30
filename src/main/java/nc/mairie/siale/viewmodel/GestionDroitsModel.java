/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Droit;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.technique.Action;
import nc.mairie.siale.technique.Constantes;
import nc.mairie.siale.technique.ControleSaisie;
import nc.mairie.siale.technique.CurrentUser;
import nc.mairie.siale.technique.LDAP;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
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
	ControleurSIALE controleurSIALESelected;
	

	Action actionControleurSIALE;
	
	@Wire
	Window gestionDroits;
	
	@Wire
	Include includeSaisieControleurSIALE;
	@Wire("#includeSaisieControleurSIALE #zoneSaisieControleurSIALE")
	Window zoneSaisieControleurSIALE;
	
	@Wire("#includeSaisieControleurSIALE #zoneSaisieControleurSIALE #controleurSIALEListBox")
	Combobox controleurSIALEListBox;
	
	ArrayList<String> attributs;
	
	public ControleurSIALE getControleurSIALESelected() {
		return controleurSIALESelected;
	}


	public void setControleurSIALESelected(ControleurSIALE controleurSIALESelected) {
		this.controleurSIALESelected = controleurSIALESelected;
	}

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
				return (o1.getNomAffichage()).compareTo(o2.getNomAffichage());
			}
		});
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		initialiseAllListes();
		
		attributs = new ArrayList<String>();
		attributs.add("displayname");//nom
		attributs.add("givenName");//prenom
		attributs.add("samaccountname");//username
			
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
	}
	

	@Listen("onClick = #ajouterControleurSIALE")
	public void onClick$ajouterControleurSIALE() {
		
		actionControleurSIALE = Action.AJOUT;

		ControleurSIALE c = new ControleurSIALE();
		c.setActif(true);
		c.getDroits().add(Constantes.droitControleur);
		setControleurSIALECourant(c);
		
		initialisteControleurSIALEListBox("samaccountname", getControleurSIALECourant().getUsername());
		
		binder.loadComponent(zoneSaisieControleurSIALE);
	}
	
	@Listen("onClick = #modifierControleurSIALE; onDoubleClick = #controleurSIALEListItem")
	public void onClick$modifierControleurSIALE() {
		
		initialisteControleurSIALEListBox("samaccountname", getControleurSIALECourant().getUsername());
		
		if (controleurSIALEListBox.getModel().getSize() > 0) {
			setControleurSIALESelected((ControleurSIALE)controleurSIALEListBox.getModel().getElementAt(0));
		}
 		
		actionControleurSIALE=  Action.MODIFICATION;
		binder.loadComponent(zoneSaisieControleurSIALE);
	}

	@Listen("onClick =  #supprimerControleurSIALE")
	public void onClick$supprimerControleurSIALE() {
		
		//Si rattaché à une mission pas possible
		if (Mission.existMissionsByControleurSIALE(getControleurSIALECourant())) {
			Messagebox.show("Suppresson impossible.\n Ce contrôleur est rattaché à une mission.", "Erreur", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
		
		Messagebox.show("Confirmez-vous la suppression de "+getControleurSIALECourant().getNomAffichage()+" ?",
				"Confirmation",
			    Messagebox.OK | Messagebox.CANCEL,
			    Messagebox.QUESTION,
			        new EventListener<Event>() {
						
						@Override
						public void onEvent(Event e) throws Exception {
			                if(Messagebox.ON_OK.equals(e.getName())){
			        			//SGBD
			                	try {
			        				controleurSIALECourant.remove();
			        			} catch (Exception excp) {
			        				Messagebox.show("Suppresson impossible.\n Ce contrôleur est rattaché à une mission.", "Erreur", Messagebox.OK, Messagebox.EXCLAMATION);
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
	
	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #annulerControleurSIALE;" +
			"onCancel= #includeSaisieControleurSIALE #zoneSaisieControleurSIALE")
	public void onClick$annulerControleurSIALE() {
		
		//si on était sur un controleur, on le mémorise
		if (getControleurSIALECourant() != null && getControleurSIALECourant().getId() != null ){
			setControleurSIALECourant(ControleurSIALE.findControleurSIALE(getControleurSIALECourant().getId()));
		}
		
		setControleurSIALESelected(null);
		
		initialiseAllListes();

		//Pour forcer à supprimer les messages d'erreur
		zoneSaisieControleurSIALE.invalidate();
		
		actionControleurSIALE = Action.AUCUNE;
		binder.loadComponent(gestionDroits);
	}
	
	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #validerControleurSIALE")
	public void onClick$validerControleurSIALE() {
		
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieControleurSIALE);
		
		//Le controleur doit être sélectionne
		if (controleurSIALESelected == null || controleurSIALESelected.getUsername() == null) {
			controleSaisie.ajouteErreur(controleurSIALEListBox, "Vous devez selectionner un Controleur.");
		} else {
			controleurSIALECourant.setDisplayname(controleurSIALESelected.getDisplayname());
			controleurSIALECourant.setUsername(controleurSIALESelected.getUsername());
		}
		
		// doit être admin et/ou controleur
		if (getControleurSIALECourant().getDroits().size() == 0) {           
			controleSaisie.ajouteErreur(zoneSaisieControleurSIALE.getFellow("controleurCheckbox"),"Il faut cocher Controleur ET/OU Administrateur.");
		}
		
		//Le nom doit être unique !!
		int nb = actionControleurSIALE == Action.AJOUT ? 1 :0;;
		for (ControleurSIALE controleurSIALE : getListeControleurSIALE()) {
			if (controleurSIALE.getUsername().equalsIgnoreCase(controleurSIALECourant.getUsername())) {
				nb++;
				if (nb>1) {
					controleSaisie.ajouteErreur(controleurSIALEListBox, "Ce controleur esiste déjà");
					break;
				}
			}
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
	}
	
	//Renvoie faux  sur le user courant a até affecté à des missions
	public boolean isControleurModifiableDisabled () {
		if (isAdminModifiableDisabled()) return true;
		if (getControleurSIALECourant().getId() == null) return false;
		return Mission.existMissionsByControleurSIALE(getControleurSIALECourant());
	}
	
	
	//Renvoie faux  sur le user courant est le user selectionné
	public boolean isAdminModifiableDisabled () {
		return 	getControleurSIALECourant() == null || 
				CurrentUser.getCurrentUser().getId().equals(getControleurSIALECourant().getId());
	}

	private void initialisteControleurSIALEListBox(String critere, String value) {
		
		List<ControleurSIALE> newModel = new ArrayList<ControleurSIALE>();
 		
		ArrayList<Hashtable<String, Object>> arr = LDAP.chercherUserLDAPAttributs(attributs,critere,"*"+value+"*");
		
		for (Hashtable<String, Object> hash : arr) {
			
			//S'il a un accountname on le rajoute
			String samaccountname = (String)hash.get("samaccountname"); 
			if (samaccountname != null && !samaccountname.equals("null") ) {
				ControleurSIALE c = new ControleurSIALE();
				c.setDisplayname(hash.get("displayname").toString());
				c.setUsername(hash.get("samaccountname").toString());
				newModel.add(c);
			}
			
		}
 	
		Collections.sort(newModel, new Comparator<ControleurSIALE>() {

			@Override
			public int compare(ControleurSIALE o1, ControleurSIALE o2) {
				return o1.getDisplayname().compareTo(o2.getDisplayname());
			}
		});
		
 		//etablissementListBox.setModel(new ListModelList<Etablissement>(newModel));
 		BindingListModelList<ControleurSIALE>  bind= new BindingListModelList<ControleurSIALE>(newModel,true);
 		
 		controleurSIALEListBox.setModel(bind);
 		
	}
	
	public void onChangingDETOURNE$controleurSIALEListBox(String value) {
		
		//Si plus de 2 caractères saisis, on recherche dans LDAP
		if (value.length() > 2) {
			initialisteControleurSIALEListBox("displayname", value);
		} else {
			controleurSIALEListBox.setModel(null);
		}
	}
	
}
