/**
 * 
 */
package nc.mairie.siale.viewmodel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.domain.MissionAction;
import nc.mairie.siale.domain.MissionDocument;
import nc.mairie.siale.domain.Param;
import nc.mairie.siale.technique.Action;
import nc.mairie.siale.technique.ControleSaisie;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;


/**
 * @author boulu72
 *
 */
public class SaisirMissionsModel extends SelectorComposer<Component> {

	private static final long serialVersionUID = -9220239729082886020L;

	AnnotateDataBinder binder;
	
	@Wire
	Window saisirMission;
	
	Mission missionCourant;
	
	List<Param> listeSuiteDonnee;
	
	List<Param> listeAction;
	
	List<Param> listeDocument;
	
	MissionDocument missionDocumentCourant;
	
	Action actionDocument;
	
	@Wire
	Include includeSaisieDocument;
	@Wire("#includeSaisieDocument #zoneSaisieDocument")
	Window zoneSaisieDocument;
	
	@Wire
	Listbox missionDocumentsListBox;
	@Wire
	Listbox missionActionsListBox;
	
	public Action getActionDocument() {
		return actionDocument;
	}

	public void setActionDocument(Action actionDocument) {
		this.actionDocument = actionDocument;
	}

	public MissionDocument getMissionDocumentCourant() {
		return missionDocumentCourant;
	}

	public void setMissionDocumentCourant(MissionDocument missionDocumentCourant) {
		this.missionDocumentCourant = missionDocumentCourant;
	}

	public List<Param> getListeAction() {
		return listeAction;
	}

	public void setListeAction(List<Param> listeAction) {
		this.listeAction = listeAction;
	}

	public List<Param> getListeDocument() {
		return listeDocument;
	}

	public void setListeDocument(List<Param> listeDocument) {
		this.listeDocument = listeDocument;
	}

	public List<Param> getListeSuiteDonnee() {
		return listeSuiteDonnee;
	}

	public void setListeSuiteDonnee(List<Param> listeSuiteDonnee) {
		this.listeSuiteDonnee = listeSuiteDonnee;
	}

	public Mission getMissionCourant() {
		return missionCourant;
	}
	
	public void setMissionCourant(Mission missionCourant) {
		this.missionCourant = missionCourant;
	}

	protected void initialiseAllListes() {
		Comparator<Param> comparator = new Comparator<Param>() {
			@Override
			public int compare(Param o1, Param o2) {
				return o1.getNom().compareTo(o2.getNom());
			}
		};
		
		listeSuiteDonnee= Param.findParamsActifsByNomDuTypeParam("SUITE_DONNEE").getResultList();
		Collections.sort(listeSuiteDonnee,comparator);
		
		listeAction = Param.findParamsActifsByNomDuTypeParam("ACTION").getResultList();
		Collections.sort(listeAction,comparator);
		
		listeDocument = Param.findParamsActifsByNomDuTypeParam("DOCUMENT").getResultList();
		Collections.sort(listeDocument,comparator);
		
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
	
		comp.setAttribute(comp.getId(), this, true);
	
		final Execution execution = Executions.getCurrent();
		Long id = (Long)execution.getArg().get("idMission");
		
		setMissionCourant(Mission.findMission(id));
		if (getMissionCourant().getMissionAction() == null) {
			MissionAction missionAction = new MissionAction();
			getMissionCourant().setMissionAction(missionAction);
		}
		
		//Initialisetion des listes 
		initialiseAllListes();
		
		binder = new AnnotateDataBinder(comp);
		binder.loadAll();

	}

	public void rappelGestionMission() {
		Component parent = saisirMission.getParent();
		parent.getChildren().clear();
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("idMission", getMissionCourant().getId());
		
		//Window window= (Window) Executions.createComponents("/_gestion_missions/GestionMissions.zul", parent , args);
		Executions.createComponents("/_gestion_missions/GestionMissions.zul", parent , args);
	}
	
	
	@Listen("onClick = #annulerMission;" +
			"onCancel= #saisirMission")
	public void onClick$annulerMission() {
	
		rappelGestionMission();
	
	}
	
	@Listen("onClick = #validerMission")
	public void onClick$validerMission() {
	
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(saisirMission);
		
		//Si nombre n'est pas saisi, alors les autres ne doivent pas l'être
		if (getMissionCourant().getPrelevement_nb() == 0) {
			if (getMissionCourant().getPrelevement_satisfaisant() != 0) {
				controleSaisie.ajouteErreur(saisirMission.getFellow("prelevement_satisfaisant", true), "Ne peut être saisi si le nombre de prélèvements n'est pas saisi");
			}
			if (getMissionCourant().getPrelevement_mediocre() != 0) {
				controleSaisie.ajouteErreur(saisirMission.getFellow("prelevement_mediocre", true), "Ne peut être saisi si le nombre de prélèvements n'est pas saisi");
			}
			if (getMissionCourant().getPrelevement_mediocre() != 0) {
				controleSaisie.ajouteErreur(saisirMission.getFellow("prelevement_non_satisfaisant", true), "Ne peut être saisi si le nombre de prélèvements n'est pas saisi");
			}
		} else if (	getMissionCourant().getPrelevement_satisfaisant()+
					getMissionCourant().getPrelevement_mediocre()+
					getMissionCourant().getPrelevement_non_satisfaisant() != getMissionCourant().getPrelevement_nb()) {
			controleSaisie.ajouteErreur(saisirMission.getFellow("prelevement_nb", true), "Satisfaisant + Médiocre + Non Satisfaisant doiut être égal à Nombre");
		}
		
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
		
		//missionCourant.setMissionAction(null);
		
		
		setMissionCourant(missionCourant.merge());
		
		rappelGestionMission();
			
	}

	@Listen("onClick = #ajouterMissionDocument")
	public void ajouterMissionDocument() {
		setActionDocument(Action.AJOUT);
		MissionDocument nouv = new MissionDocument();
		nouv.setTheMission(getMissionCourant());
		nouv.setDateDocument(new Date());
		setMissionDocumentCourant(nouv);
		binder.loadComponent(zoneSaisieDocument);
	}

	@Listen("onClick = #modifierMissionDocument; onDoubleClick =  #missionDocumentsListItem")
	public void onClick$modifierMissionDocument() {
		setActionDocument(Action.MODIFICATION);
		binder.loadComponent(zoneSaisieDocument);
	}

	
	@Listen("onClick = #supprimerMissionDocument")
	public void onClick$supprimerMissionDocument() {
		setActionDocument(Action.SUPPRESSION);
		binder.loadComponent(zoneSaisieDocument);
	}

	@Listen("onClick = #includeSaisieDocument #zoneSaisieDocument #annulerMissionDocument;" +
			"onCancel= #includeSaisieDocument #zoneSaisieDocument")
	public void onClick$annulerMissionDocument() {

		setActionDocument(Action.AUCUNE);
		
		//Pour forcer à supprimer les messages d'erreur
		zoneSaisieDocument.invalidate();
		
		binder.loadComponent(saisirMission);
	}

	
	@Listen("onClick = #includeSaisieDocument #zoneSaisieDocument #validerMissionDocument")
	public void onClick$validerMissionDocument() {
		//On vérifie l'arborescence des zones de saisie
		ControleSaisie controleSaisie = new ControleSaisie(zoneSaisieDocument);
		
		//Si erreurs, on les met et on ne va pas plus loin
		controleSaisie.afficheErreursSilYEnA();
	
		
		switch (actionDocument) {
		case AJOUT:
			
			//verif qu'il n'existe pas déjà
			for (MissionDocument missionDocument : getMissionCourant().getMissionDocuments()) {
				
				if (missionDocument.getTheDocument().getId().equals(getMissionDocumentCourant().getTheDocument().getId())) {
					Messagebox.show("Ce document est déjà affectée");
					return;
				}
			}
			
			//mise à jour de la liste
			getMissionCourant().getMissionDocuments().add(getMissionDocumentCourant());
			
			break;
		
		case MODIFICATION:
			
			//rien à faire
			
			break;
		case SUPPRESSION:
			
			//mise à jour de la liste
			getMissionCourant().getMissionDocuments().remove(getMissionDocumentCourant());
			
			break;
		default:
			break;
		}
		setActionDocument(Action.AUCUNE);
		binder.loadComponent(saisirMission);
	}
	
}
