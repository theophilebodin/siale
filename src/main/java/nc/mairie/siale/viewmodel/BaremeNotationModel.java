/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.mairie.siale.domain.Bareme;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.NoteCritere;
import nc.mairie.siale.domain.NoteGroupe;
import nc.mairie.siale.technique.Action;
import nc.mairie.siale.technique.ControleSaisie;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 * @author boulu72
 *
 */
@SuppressWarnings("deprecation")
public class BaremeNotationModel extends SelectorComposer<Component> implements Serializable{

	private static final long serialVersionUID = -9112439256492985008L;

	AnnotateDataBinder binder;
	
	@Wire
	Window baremeNotation;
	
	Action actionNoteGroupe=Action.AUCUNE;
	
	Action actionBareme=Action.AUCUNE;
	
	@Wire
	Listbox baremeListBox;
	
	List<Bareme> listeBareme;
	Bareme baremeCourant;
	
	NoteGroupe noteGroupeCourant;
	NoteGroupe noteGroupeCourantSAV;
	
	
	@Wire
	Div zoneListe;
	@Wire
	Div zoneSaisie;
	@Wire("#includeSaisieBareme #zoneSaisieBareme")
	Window zoneSaisieBareme;
	@Wire("#includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe")
	Window zoneSaisieGroupe;
	@Wire("#includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #includeSaisieCritere #zoneSaisieCritere #zoneSaisieCritere")
	Window zoneSaisieCritere;
	
	NoteCritere noteCritereCourant;
	NoteCritere noteCritereSAV;
	
	Action actionNoteCritere=Action.AUCUNE;
	
	boolean saisieNonSauvergardee = false;
	
	boolean baremeEditable = false;
	
	public List<NoteGroupe> getListeNoteGroupe() {
		return baremeCourant == null ? null : new ArrayList<NoteGroupe>(baremeCourant.getNoteGroupes());
	}
	
	public List<NoteCritere> getListeNoteCritere() {
		return noteGroupeCourant == null ? null : new ArrayList<NoteCritere>(noteGroupeCourant.getNoteCriteres());
	}

	public Action getActionBareme() {
		return actionBareme;
	}

	public void setActionBareme(Action actionBareme) {
		this.actionBareme = actionBareme;
	}


	
	public Bareme getBaremeCourant() {
		return baremeCourant;
	}

	public void setBaremeCourant(Bareme baremeCourant) {
		this.baremeCourant = baremeCourant;
		baremeEditable = actionBareme == Action.AJOUT ? true : ! Mission.existMissionsByBareme(getBaremeCourant());
	}
	
	public List<Bareme> getListeBareme() {
		return listeBareme;
	}

	public void setListeBareme(List<Bareme> listeBareme) {
		this.listeBareme = listeBareme;
	}
	
	public boolean isSaisieNonSauvergardee() {
		return saisieNonSauvergardee;
	}


	public void setSaisieNonSauvergardee(boolean saisieNonSauvergardee) {
		this.saisieNonSauvergardee = saisieNonSauvergardee;
	}


	public Action getActionNoteCritere() {
		return actionNoteCritere;
	}


	public void setActionNoteCritere(Action actionNoteCritere) {
		this.actionNoteCritere = actionNoteCritere;
	}


	public NoteCritere getNoteCritereCourant() {
		return noteCritereCourant;
	}


	public void setNoteCritereCourant(NoteCritere noteCritereCourant) {
		this.noteCritereCourant = noteCritereCourant;
	}


	public NoteGroupe getNoteGroupeCourant() {
		return noteGroupeCourant;
	}


	public void setNoteGroupeCourant(NoteGroupe noteGroupeCourant) {
		this.noteGroupeCourant = noteGroupeCourant;
	}

	public Action getActionNoteGroupe() {
		return actionNoteGroupe;
	}


	public void setActionNoteGroupe(Action actionNoteGroupe) {
		this.actionNoteGroupe = actionNoteGroupe;
	}


	protected void initialiseAllListes() {
		
		listeBareme = Bareme.findAllBaremes();
		
	}
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
		
		//Initialisetion des listes 
		initialiseAllListes();
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

		if (listeBareme.size() == 0) {
			//On rajoute un nouveau bareme
			prepareAjouterBareme(false);
		}

		
	}
	
	public void prepareAjouterBareme(boolean clone) throws Exception{
		actionBareme=Action.AJOUT;
		
		Bareme bareme = new Bareme();
		
		if (clone) {
			Bareme lastBareme = null;
        	//recherche du plus récent bareme
        	for (Bareme b : listeBareme) {
				if (lastBareme == null || b.getDateCreation().compareTo(lastBareme.getDateCreation()) > 0) {
					lastBareme = b;
				}
			}
        	
        	bareme.setNom("Bareme cloné de "+lastBareme.getNom());
        	bareme.setSeuilFaible(lastBareme.getSeuilFaible().doubleValue());
        	bareme.setSeuilModere(lastBareme.getSeuilModere().doubleValue());
        	bareme.setSeuilEleve(lastBareme.getSeuilEleve().doubleValue());
        	
        	//parcours des notesgroupes
        	for (NoteGroupe noteGroupe : lastBareme.getNoteGroupes()) {
        		NoteGroupe newNoteGroupe = new NoteGroupe();
        		newNoteGroupe.setBareme(bareme);
				newNoteGroupe.setNom(noteGroupe.getNom());
				newNoteGroupe.setPonderation(noteGroupe.getPonderation());
				
				//parcours des criteres
				for (NoteCritere noteCritere : noteGroupe.getNoteCriteres()) {
					NoteCritere newNoteCritere = new NoteCritere();
					newNoteCritere.setNoteGroupe(newNoteGroupe);
					newNoteCritere.setNom(noteCritere.getNom());
					newNoteCritere.setPonderation(noteCritere.getPonderation());
					newNoteGroupe.getNoteCriteres().add(newNoteCritere);
				}
				
				bareme.getNoteGroupes().add(newNoteGroupe);
				
			}
		} else {
			bareme.setNom("Nouveau Bareme");
		}
		bareme.setDateCreation(new Date());
		setSaisieNonSauvergardee(true);
		setBaremeCourant(bareme);
		listeBareme.add(bareme);
		binder.loadAll();
	}
	
	
	@Listen("onClick = #ajouterBareme")
	public void onClick$ajouterBareme () throws Exception {
		
		//Si liste des barèmes non vide, on propose de 
		if (listeBareme.size() != 0) {
			Messagebox.show("Voulez-vous cloner le précédent barème ?",
				    "Question", Messagebox.YES | Messagebox.NO,
				    Messagebox.QUESTION,
				        new EventListener<Event>() {
							
							@Override
							public void onEvent(Event e) throws Exception {
				                if(Messagebox.ON_YES.equals(e.getName())){
				                	//OK est sélectionné donc on clone le dernier bareme
				                	prepareAjouterBareme(true);		
				                	return;
				                   
				                }else if(Messagebox.ON_NO.equals(e.getName())){
				                    //Non is clicked
				                	//On prépare un barème vide
				                	prepareAjouterBareme(false);
				                	return;
				                }
				            }

					
				        }
				    );
		} else {
			prepareAjouterBareme(false);
		}
		
	}
	
	@Listen("onClick = #modifierBareme; onDoubleClick = #baremeListItem")
	public void onClick$modifierBareme () {
		
		if (! getBaremeEditable()) {
			
			Messagebox.show("Des notations utilisent ce barème.\n Il est consultable mais n'est pas modifiable.",
				    "Information", Messagebox.OK,
				    Messagebox.INFORMATION,
				        new EventListener<Event>() {
							
							@Override
							public void onEvent(Event e) throws Exception {
				                if(Messagebox.ON_OK.equals(e.getName())){
				                	actionBareme = Action.CONSULTATION;
				            		binder.loadAll();
				                }
				            }
			        }
			    );
			
		} else {
			actionBareme=Action.MODIFICATION;
			binder.loadAll();
		}
	}

	@Listen("onClick = #supprimerBareme")
	public void onClick$supprimerBareme () {
		//si des notes concernées alors suppression impossible :
		if (! getBaremeEditable()) {
			Messagebox.show("Des notations utilisent ce barème. Il n'est que consultable");
			return;
		}
		
		Messagebox.show("La supression de "+getBaremeCourant().getNom()+" entraine la suppression des groupes et de ses critères.\n Confirmez la suppression ?",
			    "Question", Messagebox.OK | Messagebox.CANCEL,
			    Messagebox.QUESTION,
			        new EventListener<Event>() {
						
						@Override
						public void onEvent(Event e) throws Exception {
			                if(Messagebox.ON_OK.equals(e.getName())){
			                	//OK est sélectionné donc on supprime
			                	getBaremeCourant().remove();
			                	
			                	initialiseAllListes();
			            		actionBareme = Action.AUCUNE;
			                	
			                	//Pour afficher le message pour sauvegarder
			            		setSaisieNonSauvergardee(false);
			            		
			            		binder.loadAll();

			                }else if(Messagebox.ON_CANCEL.equals(e.getName())){
			                    //Cancel is clicked
			                	return;
			                }
			            }

				
			        }
			    );
	}
	

	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #annulerBareme;" +
			"onCancel= #includeSaisieBareme #zoneSaisieBareme")
	public void onClick$annulerBareme() {
		if (getBaremeCourant() != null && getBaremeCourant().getId() != null ){
			setBaremeCourant(Bareme.findBareme(getBaremeCourant().getId()));
		}
		
		actionBareme = Action.AUCUNE;
		initialiseAllListes();
		setSaisieNonSauvergardee(false);
		binder.loadAll();
	}
	
	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #validerBareme")
	public void onClick$validerBareme() {

		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie();
		
		//conrtrôle des seuils saisis
		if (getBaremeCourant().getSeuilFaible() >= getBaremeCourant().getSeuilModere()) {
			controleSaisie.ajouteErreur(zoneSaisieBareme.getFellow("seuilFaible"), 
					"Le seuil faible doit être inférieur au seuil modéré");
		}
		if (getBaremeCourant().getSeuilModere() >= getBaremeCourant().getSeuilEleve()) {
			controleSaisie.ajouteErreur(zoneSaisieBareme.getFellow("seuilModere"), 
					"Le seuil modere doit être inférieur au seuil élevé");
		}
		
		//La somme des pondérations doit être égale à 1 sinon erreur
		double total=getBaremeCourant().getSommePonderationArrondie();
		if (total != 1) {
			controleSaisie.ajouteErreur(zoneSaisieBareme.getFellow("sommePonderationGroupe"), 
					"La somme des pondérations vaut "+Math.round(total*100.0)/100.0+" et devrait valoir 1");
		}
		
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
	
		baremeCourant=getBaremeCourant().merge();
		
		initialiseAllListes();
		actionBareme = Action.AUCUNE;
		
		alert("Le barême de notation a bien été mis à jour");
		setSaisieNonSauvergardee(false);
		binder.loadAll();
		

	}
	
	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #ajouterNoteGroupe")
	public void onClick$ajouterNoteGroupe() {
		actionNoteGroupe = Action.AJOUT;
		
		NoteGroupe noteGroupe = new NoteGroupe();
		noteGroupe.setBareme(getBaremeCourant());
		setNoteGroupeCourant(noteGroupe);
		binder.loadAll();
	}
	
	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #modifierNoteGroupe; onDoubleClick = #includeSaisieBareme #zoneSaisieBareme #noteGroupeListItem")
	public void onClick$modifierNoteGroupe() {

		try {
			noteGroupeCourantSAV = noteGroupeCourant.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		actionNoteGroupe=actionBareme == Action.CONSULTATION ? Action.CONSULTATION : Action.MODIFICATION;
		binder.loadAll();
	}

	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #supprimerNoteGroupe")
	public void onClick$supprimerNoteGroupe() {
		
		Messagebox.show("La supression de "+getNoteGroupeCourant().getNom()+" entraine la suppression de ses critères. Confirmez la suppression ?",
			    "Question", Messagebox.OK | Messagebox.CANCEL,
			    Messagebox.QUESTION,
			        new EventListener<Event>() {
						
						@Override
						public void onEvent(Event e) throws Exception {
			                if(Messagebox.ON_OK.equals(e.getName())){
			                	//OK est sélectionné donc on supprime
			                	getBaremeCourant().getNoteGroupes().remove(noteGroupeCourant);
			                	
			                	//Pour afficher le message pour sauvegarder
			            		setSaisieNonSauvergardee(true);
			            		
			            		binder.loadAll();

			                }else if(Messagebox.ON_CANCEL.equals(e.getName())){
			                    //Cancel is clicked
			                	return;
			                }
			            }

				
			        }
			    );
		
	}

	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #annulerNoteGroupe;" +
			"onCancel= #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe")
	public void onClick$annulerNoteGroupe() throws Exception {
		
		if (actionNoteGroupe != Action.AJOUT) {
			getBaremeCourant().getNoteGroupes().remove(noteGroupeCourant);
			getBaremeCourant().getNoteGroupes().add(noteGroupeCourantSAV);
			setNoteGroupeCourant(noteGroupeCourantSAV);
		}
		
		actionNoteGroupe = Action.AUCUNE;
		//surtout pas sinon on perd tout 
		//initialiseAllListes();
		binder.loadAll();
	}
	
	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #validerNoteGroupe")
	public void onClick$validerNoteGroupe() {
	
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieGroupe);
		
		//Le nom doit être unique !!
		int nb = actionNoteGroupe == Action.AJOUT ? 1 :0;;
		for (NoteGroupe noteGroupe : getBaremeCourant().getNoteGroupes()) {
			if (noteGroupe.getNom().equalsIgnoreCase(noteGroupeCourant.getNom())) {
				nb++;
				if (nb>1) {
					controleSaisie.ajouteErreur(zoneSaisieGroupe.getFellow("noteGroupeNom"), "Un groupe possède déjà ce nom");
					break;
				}
			}
		}
		
		
		//La somme des pondérations doit être égale à 1 sinon erreur
		double total=getNoteGroupeCourant().getSommePonderationArrondie();
		if (total != 1) {
			controleSaisie.ajouteErreur(zoneSaisieGroupe.getFellow("sommePonderationCritere"), 
					"La somme des ponérations vaut "+Math.round(total*100.0)/100.0+" et devrait valoir 1");
		}
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		switch (actionNoteGroupe) {
		case AJOUT:
			getBaremeCourant().getNoteGroupes().add(noteGroupeCourant);
			break;
		case MODIFICATION:
			
			break;
		case SUPPRESSION:
			//C'est supprimé avec le messagebox !!
			//getBaremeCourant().getNoteGroupes().remove(noteGroupeCourant);
			
			break;

		default:
			break;
		}

		//Pour afficher le message pour sauvegarder
		setSaisieNonSauvergardee(true);
		
		//initialiseAllListes();
		actionNoteGroupe = Action.AUCUNE;
		binder.loadAll();
	}
					   
	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #ajouterNoteCritere")
	public void onClick$ajouterNoteCritere() {
		actionNoteCritere = Action.AJOUT;
		
		NoteCritere noteCritere = new NoteCritere();
		noteCritere.setNoteGroupe(getNoteGroupeCourant());
		setNoteCritereCourant(noteCritere);
		binder.loadComponent(zoneSaisieCritere);
	}
	
	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #modifierNoteCritere; onDoubleClick = #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #includeSaisieGroupe #zoneSaisieGroupe #noteCritereListItem")
	public void onClick$modifierNoteCritere() {
		
		try {
			noteCritereSAV = noteCritereCourant.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		actionNoteCritere=actionBareme == Action.CONSULTATION ? Action.CONSULTATION : Action.MODIFICATION;
		binder.loadComponent(zoneSaisieCritere);
	}

	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #supprimerNoteCritere")
	public void onClick$supprimerNoteCritere() {
		
		actionNoteCritere=Action.SUPPRESSION;
		binder.loadComponent(zoneSaisieCritere);
	}

	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #includeSaisieCritere #zoneSaisieCritere #annulerNoteCritere;" +
			"onCancel= #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #includeSaisieCritere #zoneSaisieCritere")
	public void onClick$annulerNoteCritere() {
	
		if (actionNoteCritere != Action.AJOUT) {
			getNoteGroupeCourant().getNoteCriteres().remove(noteCritereCourant);
			getNoteGroupeCourant().getNoteCriteres().add(noteCritereSAV);
			setNoteCritereCourant(noteCritereSAV);
		}
		
		actionNoteCritere = Action.AUCUNE;
		//surtout pas sinon on perd tout 
		//initialiseAllListes();
		binder.loadComponent(zoneSaisieCritere);
		binder.loadComponent(zoneSaisieGroupe.getFellow("noteCritereListBox"));
	}
	
	@Listen("onClick = #includeSaisieBareme #zoneSaisieBareme #includeSaisieGroupe #zoneSaisieGroupe #includeSaisieCritere #zoneSaisieCritere #validerNoteCritere")
	public void onClick$validerNoteCritere() {
	
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieCritere);
		
		//Le nom doit être unique !!
		int nb = actionNoteCritere == Action.AJOUT ? 1 :0;
		for (NoteCritere noteCritere : noteGroupeCourant.getNoteCriteres()) {
			if (noteCritere.getNom().equalsIgnoreCase(noteCritereCourant.getNom())) {
				nb++;
				if (nb>1) {
					controleSaisie.ajouteErreur(zoneSaisieCritere.getFellow("noteCritereNom"), "Un critere possède déjà ce nom");
					break;
				}
			}
		}
		
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		switch (actionNoteCritere) {
		case AJOUT:
			noteGroupeCourant.getNoteCriteres().add(noteCritereCourant);
			break;
		case MODIFICATION:
			break;
		case SUPPRESSION:
			noteGroupeCourant.getNoteCriteres().remove(noteCritereCourant);
			break;
		default:
			break;
		}
		
		
		//initialiseAllListes();
		actionNoteCritere = Action.AUCUNE;
		binder.loadComponent(zoneSaisieCritere);
		binder.loadComponent(zoneSaisieGroupe.getFellow("noteCritereListBox"));
	}

	@Listen("onAfterRender = #baremeListBox")
	public void onAfterRender$menuListboxMission() {
//		baremeListBox.setSelectedIndex(0);
//		Events.sendEvent(baremeListBox, new Event(Events.ON_SELECT,baremeListBox));
	}
	
	/* 
	 * Si des missions utilisent le barème, il n'est pas modifiavle
	 */
	public boolean getBaremeEditable() {
		return baremeEditable;
	}
	
	public boolean getSaisieDisabled() {
		return ! getBaremeEditable();
	}
	
	public boolean isCritereSaisieDisabled() {
		return actionBareme == Action.CONSULTATION || actionNoteCritere == Action.SUPPRESSION;
		
	}
	
}
