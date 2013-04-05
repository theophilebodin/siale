/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.technique.Action;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Include;
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
	
	@Wire("#includeSaisieControleurSIALE #zoneSaisieControleurSIALE #controleurCheckbox")
	Checkbox controleurCheckbox;
	
	@Wire("#includeSaisieControleurSIALE #zoneSaisieControleurSIALE #controleurCheckbox")
	Checkbox administrateurCheckbox;
	
	
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
		
//		if (getTypeParamCourant() == null) {
//			alert("Un type de paramètre doit être sélectionné");
//			return;
//		}
//		
//		actionControleurSIALE = Action.AJOUT;
//		
//		Param param = new Param();
//		param.setTypeParam(getTypeParamCourant());
//		param.setActif(true);
//		setParamCourant(param);
//		binder.loadComponent(zoneSaisieControleurSIALE);
	}
	
	@Listen("onClick = #modifierControleurSIALE; onDoubleClick = #controleurSIALEListItem")
	public void onClick$modifierControleurSIALE() {
		System.out.println("onClick$modifierControleurSIALE");
//		//Si on a doublecliqué et pas éditable
//		if (!isControleurSIALEEditable()) return;
//		try {
//			paramCourantSAV = paramCourant.clone();
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		
//		actionControleurSIALE=  Action.MODIFICATION;
//		binder.loadComponent(zoneSaisieControleurSIALE);
	}

	@Listen("onClick =  #supprimerControleurSIALE")
	public void onClick$supprimerControleurSIALE() {
		System.out.println("onClick$supprimerControleurSIALE");
//		Messagebox.show("Confirmez-vous la suppression ?",
//			    "Question", Messagebox.OK | Messagebox.CANCEL,
//			    Messagebox.QUESTION,
//			        new EventListener<Event>() {
//						
//						@Override
//						public void onEvent(Event e) throws Exception {
//			                if(Messagebox.ON_OK.equals(e.getName())){
//			        			//SGBD
//			                	try {
//			        				paramCourant.remove();
//			        			} catch (Exception excp) {
//			        				alert("Suppresson impossible car le paramètre est utilisé par des missions");
//			        				return;
//			        			}
//			                	//VIEW
//			        			listeParam.remove(paramCourant);
//			        			actionControleurSIALE = Action.AUCUNE;
//			        			binder.loadAll();
//			                }else if(Messagebox.ON_CANCEL.equals(e.getName())){
//			                    //Cancel is clicked
//			                	return;
//			                }
//			            }
//			
//			        }
//			    );

	}
	
	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #rechercherControleurSIALE;" +
			"onCancel= #includeSaisieControleurSIALE #zoneSaisieControleurSIALE")
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
		System.out.println("onClick$annulerControleurSIALE");
//		if (actionControleurSIALE != Action.AJOUT) {
//			listeParam.remove(paramCourant);
//			listeParam.add(paramCourantSAV);
//			setParamCourant(paramCourantSAV);
//		}
//		
//		actionControleurSIALE = Action.AUCUNE;
//		binder.loadComponent(zoneSaisieControleurSIALE);
	}
	
	@Listen("onClick = #includeSaisieControleurSIALE #zoneSaisieControleurSIALE #validerControleurSIALE")
	public void onClick$validerControleurSIALE() {
		System.out.println("onClick$validerControleurSIALE");
//		//On vérifie l'arborescence des zones de saisie
//		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieControleurSIALE);
//		
//		
//		//On vérifie qu'il n'existe pas déjà
//		for (Param param : listeParam) {
//			if (param != getParamCourant() && param.getNom().equals(getParamCourant().getNom())) {
//				controleSaisie.ajouteErreur(zoneSaisieControleurSIALE.getFellow("paramtb"), 
//						"Ce nom existe déjà pour ce type de paramêtre");
//				break;
//			}
//		}
//		
//		//Si erreurs, on les met et on ne va pas plus loin
//		controleSaisie.afficheErreursSilYEnA();
//		
//		switch (actionControleurSIALE) {
//		case AJOUT:
//			
//			
//		
//			setParamCourant(paramCourant.merge());
//			
//			listeParam.add(paramCourant);
////			
////			binder.loadAll();
//			break;
//		case MODIFICATION:
//			setParamCourant(paramCourant.merge());
//			break;
//		case SUPPRESSION:
//			//Fait directement après validation
//			break;
//		default:
//			break;
//		}
//		
//		//initialiseAllListes();
//		actionControleurSIALE = Action.AUCUNE;
//		binder.loadAll();
	}
	
}
