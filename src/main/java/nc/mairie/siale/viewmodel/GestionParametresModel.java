/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nc.mairie.siale.domain.Param;
import nc.mairie.siale.domain.TypeParam;
import nc.mairie.siale.technique.Action;
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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;



/**
 * @author boulu72
 *
 */
public class GestionParametresModel extends SelectorComposer<Component> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6002244966600701488L;
	
	AnnotateDataBinder binder;
	
	List<TypeParam> listeTypeParam;
	TypeParam typeParamCourant;
	
	List<Param> listeParam;
	Param paramCourant;
	Param paramCourantSAV;
	
	Action actionParametre;
	
	@Wire
	Window gestionParametres;
	
	@Wire
	Include includeSaisieParametre;
	
	@Wire("#includeSaisieParametre #zoneSaisieParametre")
	Window zoneSaisieParametre;
	
	boolean parametreEditable = false;
	
	
	public boolean isParametreEditable() {
		return parametreEditable;
	}


	public void setParametreEditable(boolean parametreEditable) {
		this.parametreEditable = parametreEditable;
	}


	public Action getActionParametre() {
		return actionParametre;
	}


	public void setActionParametre(Action actionParametre) {
		this.actionParametre = actionParametre;
	}


	public Param getParamCourant() {
		return paramCourant;
	}


	public void setParamCourant(Param paramCourant) {
		this.paramCourant = paramCourant;
	}


	public List<Param> getListeParam() {
		if (getTypeParamCourant() != null) {
			listeParam = Param.findParamsByTypeParam(getTypeParamCourant()).getResultList();
		} else listeParam = null;
		return listeParam;
	}


	public void setListeParam(List<Param> listeParam) {
		this.listeParam = listeParam;
	}


	public TypeParam getTypeParamCourant() {
		return typeParamCourant;
	}

	public void setTypeParamCourant(TypeParam typeParamCourant) {
		this.typeParamCourant = typeParamCourant;
		//on remet le paging au début
		((Listbox)gestionParametres.getFellow("paramListBox")).setActivePage(0);
		
//		List<Param> list = Param.findParamsByTypeParam(getTypeParamCourant()).getResultList();
//		setListeParam(list);
		setParamCourant(null);
		binder.loadAll();

	}


	public List<TypeParam> getListeTypeParam() {
		return listeTypeParam;
	}

	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		listeTypeParam = TypeParam.findAllTypeParams();
		Collections.sort(listeTypeParam, new Comparator<TypeParam>() {

			@Override
			public int compare(TypeParam o1, TypeParam o2) {
				return o1.getNom().compareTo(o2.getNom());
			}
		});

		//Si admin alord parameditable
		setParametreEditable(CurrentUser.getCurrentUser().isAdmin());
		
		if (!isParametreEditable()) {
			alert("Vous n'êtes pas administrateur.\nVous ne pouvez que consulter les paramètres");
		}
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();
		


	}
	

	@Listen("onClick = #ajouterParametre")
	public void onClick$ajouterParametre() {
		
		if (getTypeParamCourant() == null) {
			alert("Un type de paramètre doit être sélectionné");
			return;
		}
		
		actionParametre = Action.AJOUT;
		
		Param param = new Param();
		param.setTypeParam(getTypeParamCourant());
		param.setActif(true);
		setParamCourant(param);
		binder.loadComponent(zoneSaisieParametre);
	}
	
	@Listen("onClick = #modifierParametre; onDoubleClick = #parametreListItem")
	public void onClick$modifierParametre() {
		//Si on a doublecliqué et pas éditable
		if (!isParametreEditable()) return;
		try {
			paramCourantSAV = paramCourant.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		actionParametre=  isParametreEditable() ?  Action.MODIFICATION : Action.CONSULTATION ;
		binder.loadComponent(zoneSaisieParametre);
	}

	@Listen("onClick =  #supprimerParametre")
	public void onClick$supprimerParametre() {
		
		Messagebox.show("Confirmez-vous la suppression ?",
			    "Question", Messagebox.OK | Messagebox.CANCEL,
			    Messagebox.QUESTION,
			        new EventListener<Event>() {
						
						@Override
						public void onEvent(Event e) throws Exception {
			                if(Messagebox.ON_OK.equals(e.getName())){
			        			//SGBD
			                	try {
			        				paramCourant.remove();
			        			} catch (Exception excp) {
			        				alert("Suppresson impossible car le paramètre est utilisé par des missions");
			        				return;
			        			}
			                	//VIEW
			        			listeParam.remove(paramCourant);
			        			actionParametre = Action.AUCUNE;
			        			binder.loadAll();
			                }else if(Messagebox.ON_CANCEL.equals(e.getName())){
			                    //Cancel is clicked
			                	return;
			                }
			            }
			
			        }
			    );

	}

	@Listen("onClick = #includeSaisieParametre #zoneSaisieParametre #annulerParametre;" +
			"onCancel= #includeSaisieParametre #zoneSaisieParametre")
	public void onClick$annulerParametre() {

		if (actionParametre != Action.AJOUT) {
			listeParam.remove(paramCourant);
			listeParam.add(paramCourantSAV);
			setParamCourant(paramCourantSAV);
		}
		
		actionParametre = Action.AUCUNE;
		binder.loadComponent(zoneSaisieParametre);
	}
	
	@Listen("onClick = #includeSaisieParametre #zoneSaisieParametre #validerParametre")
	public void onClick$validerParametre() {
		
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieParametre);
		
		
		//On vérifie qu'il n'existe pas déjà
		for (Param param : listeParam) {
			if (param != getParamCourant() && param.getNom().equals(getParamCourant().getNom())) {
				controleSaisie.ajouteErreur(zoneSaisieParametre.getFellow("paramtb"), 
						"Ce nom existe déjà pour ce type de paramêtre");
				break;
			}
		}
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		switch (actionParametre) {
		case AJOUT:
			
			
		
			setParamCourant(paramCourant.merge());
			
			listeParam.add(paramCourant);
//			
//			binder.loadAll();
			break;
		case MODIFICATION:
			setParamCourant(paramCourant.merge());
			break;
		case SUPPRESSION:
			//Fait directement après validation
			break;
		default:
			break;
		}
		
		//initialiseAllListes();
		actionParametre = Action.AUCUNE;
		binder.loadAll();
	}
	
}
